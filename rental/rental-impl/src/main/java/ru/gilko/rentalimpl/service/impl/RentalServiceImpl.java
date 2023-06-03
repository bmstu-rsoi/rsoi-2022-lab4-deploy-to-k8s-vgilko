package ru.gilko.rentalimpl.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.gilko.rentalapi.constants.enums.RentalStatus;
import ru.gilko.rentalapi.dto.RentalInDto;
import ru.gilko.rentalapi.dto.RentalOutDto;
import ru.gilko.rentalapi.exceptions.InvalidOperationException;
import ru.gilko.rentalapi.exceptions.NoSuchEntityException;
import ru.gilko.rentalimpl.domain.Rental;
import ru.gilko.rentalimpl.repository.RentalRepository;
import ru.gilko.rentalimpl.service.api.RentalService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final ModelMapper modelMapper;

    public RentalServiceImpl(RentalRepository rentalRepository, ModelMapper modelMapper) {
        this.rentalRepository = rentalRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public RentalOutDto createRental(String username, RentalInDto rentalInDto) {
        Rental rental = buildRentalForCreation(username, rentalInDto);

        Rental savedRental = rentalRepository.save(rental);
        log.info("Rental was created {}", savedRental);

        return modelMapper.map(savedRental, RentalOutDto.class);
    }

    private Rental buildRentalForCreation(String username, RentalInDto rentalInDto) {
        Rental rental = modelMapper.map(rentalInDto, Rental.class);
        rental.setRentalUid(UUID.randomUUID());
        rental.setUsername(username);
        rental.setStatus(RentalStatus.IN_PROGRESS);
        return rental;
    }

    public List<RentalOutDto> getRentals(String username) {
        Sort sort = Sort.by("status").descending().and(Sort.by("dateTo").ascending());
        List<Rental> rentals = rentalRepository.findAllByUsername(username, sort);
        log.info("Get {} rentals for username {}", rentals.size(), username);

        return rentals.stream()
                .map(rental -> modelMapper.map(rental, RentalOutDto.class))
                .toList();
    }

    @Override
    public RentalOutDto getRental(UUID rentalUid, String username) {
        Rental rental = getRentalDomain(rentalUid, username);

        return modelMapper.map(rental, RentalOutDto.class);
    }

    @Override
    public void cancelRental(UUID rentalUid, String username) {
        Rental rental = getRentalDomain(rentalUid, username);

        if (rental.getDateFrom().isBefore(LocalDate.now())) {
            throw new InvalidOperationException("Невозможно отменить начатую аренду.");
        }

        changeRentalStatus(rental, RentalStatus.CANCELED);
    }

    @Override
    public void finishRental(UUID rentalUid, String username) {
        Rental rental = getRentalDomain(rentalUid, username);

        changeRentalStatus(rental, RentalStatus.FINISHED);
    }

    private void changeRentalStatus(Rental rental, RentalStatus status) {
        rental.setStatus(status);

        Rental updatedRental = rentalRepository.save(rental);
        log.info("Rental {} status was changed. New status: {}", rental.getId(), updatedRental.getStatus());
    }

    public Rental getRentalDomain(UUID rentalUid, String username) {
        Optional<Rental> rental = rentalRepository.findByUsernameAndRentalUid(username, rentalUid);

        if (rental.isEmpty()) {
            log.error("Requesting rental {} for user {} doesn't exist", rentalUid, username);
            throw new NoSuchEntityException("There is no rental %s for user %s".formatted(rentalUid, username));
        }
        return rental.get();
    }
}
