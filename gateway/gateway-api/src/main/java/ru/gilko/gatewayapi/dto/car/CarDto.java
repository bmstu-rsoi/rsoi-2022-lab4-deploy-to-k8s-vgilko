package ru.gilko.gatewayapi.dto.car;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CarDto extends CarBaseDto {
    @Schema(description = "Количество лошадиных сил")
    private int power;

    @Schema(description = "Тип кузова автомобиля")
    private String type;

    @Schema(description = "Цена аренды за период")
    private int price;

    @Schema(description = "Доступна к аренде")
    boolean available;
}
