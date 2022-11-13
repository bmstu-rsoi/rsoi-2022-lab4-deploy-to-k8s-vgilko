package ru.gilko.rental.controller.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.gilko.rental.controller.api.RentalController;
import ru.gilko.rental.dto.RentalInDto;
import ru.gilko.rental.dto.RentalOutDto;
import ru.gilko.rental.service.api.RentalService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ru.gilko.rental.constants.ControllerUrls.RENTAL_BASE_URL;
import static ru.gilko.rental.constants.ProjectConstants.USERNAME_HEADER;

@RestController
@Slf4j
public class RentalControllerImpl implements RentalController {
    private final RentalService rentalService;

    public RentalControllerImpl(RentalService rentalService) {
        this.rentalService = rentalService;
    }


    @Override
    public ResponseEntity<RentalOutDto> createRental(String username, RentalInDto rentalInDto) {
        log.info("Request for creating rental for user {}", username);

        return ResponseEntity.ok(rentalService.createRental(username, rentalInDto));
    }

    @GetMapping(path = RENTAL_BASE_URL)
    public ResponseEntity<List<RentalOutDto>> getRentals(@RequestHeader(USERNAME_HEADER) String username) {
        log.info("Request for reading all rental of user {}", username);

        return ResponseEntity.ok(rentalService.getRentals(username));
    }

    @Override
    public ResponseEntity<RentalOutDto> getRental(UUID rentalUid, String username) {
        log.info("Request for reading rental {} of user {}", rentalUid, username);

        Optional<RentalOutDto> rental = rentalService.getRental(rentalUid, username);

        return rental.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<?> cancelRental(UUID rentalUid, String username) {
        log.info("Request for cancelling rental {} for user {}", rentalUid, username);

        rentalService.cancelRental(rentalUid, username);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> finishRental(UUID rentalUid, String username) {
        log.info("Request for finish rental {} for user {}", rentalUid, username);

        rentalService.finishRental(rentalUid, username);

        return ResponseEntity.noContent().build();
    }

}