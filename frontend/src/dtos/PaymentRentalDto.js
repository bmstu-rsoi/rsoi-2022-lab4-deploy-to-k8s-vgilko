import PaymentStatusEnum from "../enums/PaymentStatusEnum";

class PaymentRentalDto {
    paymentUid: string;
    status: PaymentStatusEnum;
    price: number;

    constructor(paymentUid: string,
                status: PaymentStatusEnum,
                price: number) {
        this.paymentUid = paymentUid;
        this.status = status;
        this.price = price;
    }
}

export default PaymentRentalDto;