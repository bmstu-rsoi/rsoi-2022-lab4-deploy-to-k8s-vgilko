class CarRentalDto {
    carUid: string;
    brand: string;
    model: string;
    registrationNumber: string;

    constructor(carUid, brand, model, registrationNumber) {
        this.carUid = carUid;
        this.brand = brand;
        this.model = model;
        this.registrationNumber = registrationNumber;
    }
}

export default CarRentalDto;