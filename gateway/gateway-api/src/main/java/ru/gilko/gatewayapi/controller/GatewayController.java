package ru.gilko.gatewayapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gilko.gatewayapi.constants.ControllerUrls;
import ru.gilko.gatewayapi.dto.CarBookDto;
import ru.gilko.gatewayapi.dto.car.CarDto;
import ru.gilko.gatewayapi.dto.rental.RentalCreationOutDto;
import ru.gilko.gatewayapi.dto.rental.RentalDto;
import ru.gilko.gatewayapi.dto.wrapper.PageableCollectionOutDto;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static ru.gilko.gatewayapi.constants.ProjectConstants.USERNAME_HEADER;

@CrossOrigin
public interface GatewayController {
    @Operation(summary = "Получить список автомобилей")
    @GetMapping(ControllerUrls.CARS_URL)
    PageableCollectionOutDto<CarDto> getAllCars(@Parameter(description = "Показывать забронированные автомобили или нет.")
                                                @RequestParam boolean showAll,
                                                @Parameter(description = "Номер страницы. Начинается с 0.")
                                                @RequestParam int page,
                                                @Parameter(description = "Размер страницы")
                                                @RequestParam int size);


    @Operation(summary = "Получить список совершённых аренд")
    @GetMapping(ControllerUrls.RENTAL_URL)
    ResponseEntity<List<RentalDto>> getRental(@Parameter(description = "Логин пользователя")
                                              @RequestHeader(USERNAME_HEADER) String username);

    @Operation(summary = "Получить информацию о конретной аренде пользователя")
    @GetMapping(ControllerUrls.RENTAL_URL_WITH_ID)
    ResponseEntity<RentalDto> getRental(@Parameter(description = "Логин пользователя")
                                        @RequestHeader(USERNAME_HEADER) String username,
                                        @Parameter(description = "ID аренды")
                                        @PathVariable UUID rentalUid);

    @Operation(summary = "Арендовать автомобиль")
    @PostMapping(ControllerUrls.RENTAL_URL)
    ResponseEntity<RentalCreationOutDto> bookCar(@Parameter(description = "Логин пользователя")
                                                 @RequestHeader(USERNAME_HEADER) String userName,
                                                 @RequestBody @Valid CarBookDto carBookDto);

    @Operation(summary = "Завершить аренду автомобиля")
    @PostMapping(ControllerUrls.RENTAL_URL_FINISH)
    ResponseEntity<?> finishRental(@Parameter(description = "Логин пользователя")
                                   @RequestHeader(USERNAME_HEADER) String username,
                                   @Parameter(description = "ID аренды")
                                   @PathVariable UUID rentalUid);

    @Operation(summary = "Отменить аренду автомобиля")
    @DeleteMapping(ControllerUrls.RENTAL_URL_WITH_ID)
    ResponseEntity<?> cancelRental(@Parameter(description = "Логин пользователя")
                                   @RequestHeader(USERNAME_HEADER) String username,
                                   @Parameter(description = "ID аренды")
                                   @PathVariable UUID rentalUid);

}
