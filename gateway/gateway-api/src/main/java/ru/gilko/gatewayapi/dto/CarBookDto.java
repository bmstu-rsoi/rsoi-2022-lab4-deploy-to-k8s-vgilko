package ru.gilko.gatewayapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class CarBookDto {
    @Schema(description = "ID автомобиля")
    @NotNull(message = "CarUid should not be null")
    private UUID carUid;

    @Schema(description = "Дата начала аренды")
    @NotNull(message = "DateFrom should not be null")
    private LocalDate dateTo;

    @Schema(description = "Дата окончания аренды")
    @NotNull(message = "DateTo should not be null")
    private LocalDate dateFrom;
}
