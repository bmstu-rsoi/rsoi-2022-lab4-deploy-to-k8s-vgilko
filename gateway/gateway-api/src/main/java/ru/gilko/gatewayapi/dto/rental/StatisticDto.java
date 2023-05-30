package ru.gilko.gatewayapi.dto.rental;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StatisticDto {
    @Schema(description = "Дата начала аренды")
    private LocalDate dateFrom;

    @Schema(description = "Дата завершения аренды")
    private LocalDate dateTo;

    @Schema(description = "Дата события")
    private LocalDate eventDate;

    @Schema(description = "Марка производителя")
    private String brand;

    @Schema(description = "Модель автомобиля")
    private String model;
    @Schema(description = "Сумма к оплате")
    private int price;
}
