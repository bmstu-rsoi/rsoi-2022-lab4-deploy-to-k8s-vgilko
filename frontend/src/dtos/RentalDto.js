import CarRentalDto from "./CarRentalDto";
import PaymentRentalDto from "./PaymentRentalDto";
import RentStatusEnum from "../enums/RentStatusEnum";

class RentalDto {
    rentalUid: String;
    status: RentStatusEnum;
    dateFrom: Date;
    dateTo: Date;

    car: CarRentalDto;
    payment;

    constructor(rentalUid: String,
                status: RentStatusEnum,
                dateFrom: Date,
                dateTo: Date,
                car: CarRentalDto,
                payment: PaymentRentalDto) {
        this.rentalUid = rentalUid;
        this.status = status;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.car = car;
        this.payment = payment;
    }
}

export default RentalDto;