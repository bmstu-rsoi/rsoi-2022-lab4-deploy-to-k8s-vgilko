package ru.gilko.gatewayapi.dto.car;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
public class CarBaseDto {
    @Schema(description = "ID автомобиля")
    private UUID carUid;

    @Schema(description = "Марка производителя")
    private String brand;

    @Schema(description = "Модель автомобиля")
    private String model;

    @Schema(description = "Регистрационный номер автомобиля")
    private String registrationNumber;
}
