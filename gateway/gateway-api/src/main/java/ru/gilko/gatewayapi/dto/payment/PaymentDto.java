package ru.gilko.gatewayapi.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.gilko.paymentapi.constants.enums.PaymentStatus;

import java.util.UUID;

@Data
public class PaymentDto {
    @Schema(description = "ID счёта на оплату")
    private UUID paymentUid;

    @Schema(description = "Статус оплаты. Оплачено/отменено.")
    private PaymentStatus status;

    @Schema(description = "Сумма к оплате")
    private int price;
}
