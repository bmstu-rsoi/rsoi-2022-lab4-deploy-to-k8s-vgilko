package ru.gilko.gatewayapi.dto.rental;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.gilko.gatewayapi.dto.car.CarBaseDto;
import ru.gilko.gatewayapi.dto.payment.PaymentDto;
import ru.gilko.rentalapi.constants.enums.RentalStatus;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class RentalDto {
    @Schema(description = "ID аренды")
    private UUID rentalUid;

    @Schema(description = "Статус аренды. В процессе/завершена/отменена.")
    private RentalStatus status;

    @Schema(description = "Дата начала аренды")
    private LocalDate dateFrom;

    @Schema(description = "Дата завершения аренды")
    private LocalDate dateTo;

    @Schema(description = "Арендованный автомобиль")
    private CarBaseDto car;

    @Schema(description = "Информация по оплате")
    private PaymentDto payment;
}
