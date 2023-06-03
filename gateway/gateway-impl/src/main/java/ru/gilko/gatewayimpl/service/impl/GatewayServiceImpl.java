package ru.gilko.gatewayimpl.service.impl;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.gilko.carsapi.dto.CarOutDto;
import ru.gilko.carsapi.feign.CarFeign;
import ru.gilko.gatewayapi.dto.CarBookDto;
import ru.gilko.gatewayapi.dto.car.CarBaseDto;
import ru.gilko.gatewayapi.dto.car.CarDto;
import ru.gilko.gatewayapi.dto.payment.PaymentDto;
import ru.gilko.gatewayapi.dto.rental.RentalCreationOutDto;
import ru.gilko.gatewayapi.dto.rental.RentalDto;
import ru.gilko.gatewayapi.dto.rental.StatisticDto;
import ru.gilko.gatewayapi.dto.statistic.CancelingStatisticDto;
import ru.gilko.gatewayapi.dto.statistic.ProfitableModelDto;
import ru.gilko.gatewayapi.dto.wrapper.PageableCollectionOutDto;
import ru.gilko.gatewayapi.exception.InvalidOperationException;
import ru.gilko.gatewayapi.exception.NoSuchEntityException;
import ru.gilko.gatewayimpl.service.api.ExternalServiceCaller;
import ru.gilko.gatewayimpl.service.api.GatewayService;
import ru.gilko.gatewayimpl.utils.MappingUtils;
import ru.gilko.paymentapi.dto.PaymentOutDto;
import ru.gilko.paymentapi.feign.PaymentFeign;
import ru.gilko.rentalapi.dto.RentalInDto;
import ru.gilko.rentalapi.dto.RentalOutDto;
import ru.gilko.rentalapi.feign.RentalFeign;
import ru.gilko.statisticapi.feign.StatisticFeign;

import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Slf4j
public class GatewayServiceImpl implements GatewayService {
    private final CarFeign carFeign;
    private final RentalFeign rentalFeign;
    private final PaymentFeign paymentFeign;
    private final StatisticFeign statisticFeign;
    private final ExternalServiceCaller externalServiceCaller;

    private final KafkaTemplate<String, StatisticDto> statisticBroker;

    private final MappingUtils mappingUtils;

    private final ModelMapper modelMapper;

    public GatewayServiceImpl(CarFeign carFeign,
                              RentalFeign rentalFeign,
                              PaymentFeign paymentFeign,
                              StatisticFeign statisticFeign,
                              ExternalServiceCaller externalServiceCaller,
                              KafkaTemplate<String, StatisticDto> statisticBroker,
                              MappingUtils mappingUtils,
                              ModelMapper modelMapper) {
        this.carFeign = carFeign;
        this.rentalFeign = rentalFeign;
        this.paymentFeign = paymentFeign;
        this.statisticFeign = statisticFeign;
        this.externalServiceCaller = externalServiceCaller;
        this.statisticBroker = statisticBroker;
        this.mappingUtils = mappingUtils;
        this.modelMapper = modelMapper;
    }

    @Override
    public PageableCollectionOutDto<CarDto> getAllCars(boolean showAll, int page, int size) {
        Page<CarOutDto> carOutDtos;
        try {
            carOutDtos = carFeign.getCars(showAll, page, size);
        } catch (FeignException e) {
            log.error("Enable to get cars. Exception: {}", e.getMessage());
            carOutDtos = Page.empty();
        }

        log.info("Received {} entities from car service", carOutDtos.getTotalElements());

        return mappingUtils.mapToPageCollectionOutDto(carOutDtos, CarDto.class);
    }

    @Override
    public List<RentalDto> getRental(String username) {
        List<RentalOutDto> rentals = getRentals(username);

        List<UUID> paymentsUids = new LinkedList<>();
        List<UUID> carUids = new LinkedList<>();
        rentals.forEach(rentalOutDto -> {
            paymentsUids.add(rentalOutDto.getPaymentUid());
            carUids.add(rentalOutDto.getCarUid());
        });

        Map<UUID, PaymentOutDto> payments = getPayments(paymentsUids);
        Map<UUID, CarOutDto> cars = getCars(carUids);

        return rentals.stream()
                .map(rentalOutDto -> buildOutDto(rentalOutDto, payments, cars))
                .toList();
    }

    private List<RentalOutDto> getRentals(String username) {
        try {
            List<RentalOutDto> rentals = rentalFeign.getRentals(username);
            log.info("Get {} rentals for user {}", rentals.size(), username);

            return rentals;
        } catch (FeignException e) {
            log.error("Unable to get rentals for user {}", username);
            return Collections.emptyList();
        }
    }

    private Map<UUID, CarOutDto> getCars(List<UUID> carIds) {
        return externalServiceCaller.getOrEmpty(carIds, carFeign::getCars, CarOutDto::getCarUid);
    }

    private Map<UUID, PaymentOutDto> getPayments(List<UUID> paymentUids) {
        return externalServiceCaller.getOrEmpty(paymentUids, paymentFeign::getPayments, PaymentOutDto::getPaymentUid);
    }


    @Override
    public RentalDto getRental(String username, UUID rentalUid) {
        RentalOutDto rental = rentalFeign.getRental(rentalUid, username);
        log.info("Get rental form rental service: {}", rental);

        CarOutDto car = carFeign.getCar(rental.getCarUid());
        PaymentOutDto payment = paymentFeign.getPayment(rental.getPaymentUid());

        RentalDto mappedRental = modelMapper.map(rental, RentalDto.class);
        mappedRental.setCar(modelMapper.map(car, CarBaseDto.class));
        mappedRental.setPayment(modelMapper.map(payment, PaymentDto.class));

        return mappedRental;
    }

    @Override
    public RentalCreationOutDto bookCar(String userName, CarBookDto carBookDto) {
        CarOutDto car = carFeign.getCar(carBookDto.getCarUid());

        if (!car.isAvailable()) {
            log.error("Trying to book not available car {}", car.getCarUid());
            throw new InvalidOperationException("Car %s is not available".formatted(car.getCarUid()));
        }

        changeCarAvailability(carBookDto.getCarUid());
        PaymentOutDto payment = createPayment(carBookDto, car.getPrice());
        RentalOutDto rental = createRental(userName, carBookDto, payment);

        RentalCreationOutDto rentalCreationOutDto = modelMapper.map(rental, RentalCreationOutDto.class);
        rentalCreationOutDto.setPayment(modelMapper.map(payment, PaymentDto.class));
        rentalCreationOutDto.setCarUid(carBookDto.getCarUid());

        return rentalCreationOutDto;
    }

    @Override
    public void finishRental(String username, UUID rentalUid) {
        try {
            rentalFeign.finishRental(rentalUid, username);

            RentalDto rental = getRental(username, rentalUid);

            statisticBroker.send("statistic", buildStatisticDto(rental));

            changeCarAvailability(rental.getCar().getCarUid());
        } catch (FeignException.NotFound e) {
            log.info("Trying to finish non-existing rental: username = {}, rentalUid = {}", username, rentalUid);

            throw new NoSuchEntityException(e.getMessage());
        }
    }

    @Override
    public void cancelRental(String username, UUID rentalUid) {
        try {
            RentalDto rental = getRental(username, rentalUid);

            if (rental.getDateFrom().isBefore(LocalDate.now())) {
                log.error("Trying to cancel started rental {} for user {}", rental, username);
                throw new InvalidOperationException("Невозможно отменить начатую аренду.");
            }

            statisticBroker.send("statistic", buildStatisticDto(rental));

            rentalFeign.cancelRental(rentalUid, username);
            paymentFeign.cancelPayment(rental.getPayment().getPaymentUid());
            changeCarAvailability(rental.getCar().getCarUid());
        } catch (FeignException.NotFound e) {
            log.info("Trying to cancel non-existing rental: username = {}, rentalUid = {}", username, rentalUid);

            throw new NoSuchEntityException(e.getMessage());
        }
    }

    @Override
    public List<CancelingStatisticDto> getCancellingStatistic() {
        try {
            return statisticFeign.getCancelled().stream()
                    .map(profitableDto -> modelMapper.map(profitableDto, CancelingStatisticDto.class))
                    .toList();
        } catch (FeignException e) {
            log.error("Unable to get cancelling statistic from statistic service");
            return Collections.emptyList();
        }
    }

    @Override
    public List<ProfitableModelDto> getProfitableStatistic() {
        try {
            return statisticFeign.getProfitable().stream()
                    .map(profitableDto -> modelMapper.map(profitableDto, ProfitableModelDto.class))
                    .toList();
        } catch (FeignException e) {
            log.error("Unable to get profitable statistic from statistic service");
            return Collections.emptyList();
        }
    }

    private void changeCarAvailability(UUID carId) {
        try {
            carFeign.changeAvailability(carId);
        } catch (FeignException.NotFound e) {
            log.info("Trying to change availability for non existing car {}", carId);
            throw new NoSuchEntityException("There is no car with id = %s".formatted(carId));
        }
    }

    private PaymentOutDto createPayment(CarBookDto carBookDto, int carRentalPrice) {
        int amountRentalDays = (int) calculateAmountRentalDays(carBookDto);
        int totalPrice = amountRentalDays * carRentalPrice;

        return paymentFeign.createPayment(totalPrice);
    }

    private long calculateAmountRentalDays(CarBookDto carBookDto) {
        long totalRentalDays = DAYS.between(carBookDto.getDateFrom(), carBookDto.getDateTo());

        if (totalRentalDays < 0) {
            log.error("Trying to create rental with invalid dates DateFrom {}, DateTo {}",
                    carBookDto.getDateFrom(),
                    carBookDto.getDateTo());
            throw new InvalidOperationException("Invalid car rental dates. DateTo should be after DateFrom");
        }

        return totalRentalDays;
    }

    private RentalOutDto createRental(String username, CarBookDto carBookDto, PaymentOutDto payment) {
        RentalInDto rentalInDto = new RentalInDto(
                carBookDto.getCarUid(), payment.getPaymentUid(), carBookDto.getDateFrom(), carBookDto.getDateTo());

        return rentalFeign.createRental(username, rentalInDto);
    }

    private RentalDto buildOutDto(RentalOutDto rentalOutDto,
                                  Map<UUID, PaymentOutDto> payments,
                                  Map<UUID, CarOutDto> cars) {
        RentalDto rental = modelMapper.map(rentalOutDto, RentalDto.class);

        PaymentOutDto payment = payments.get(rentalOutDto.getPaymentUid());
        if (payment != null) {
            PaymentDto paymentDto = modelMapper.map(payment, PaymentDto.class);
            rental.setPayment(paymentDto);
        }

        CarOutDto car = cars.get(rentalOutDto.getCarUid());
        if (car != null) {
            CarDto carDto = modelMapper.map(car, CarDto.class);
            rental.setCar(carDto);
        }

        return rental;
    }

    private StatisticDto buildStatisticDto(RentalDto rentalDto) {
        StatisticDto statisticDto = modelMapper.map(rentalDto, StatisticDto.class);
        statisticDto.setEventDate(LocalDate.now());

        return statisticDto;
    }
}
