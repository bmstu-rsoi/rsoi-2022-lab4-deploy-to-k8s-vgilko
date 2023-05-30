
class PaymentRentalDto {
    paymentUid: String;
    status: String;
    price: Number;

    constructor(paymentUid: String,
                status: PaymentStatusEnum,
                price: Number) {
        this.paymentUid = paymentUid;
        this.status = status;
        this.price = price;
    }
}

export default PaymentRentalDto;