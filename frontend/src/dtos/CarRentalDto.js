class CarRentalDto {
    carUid: String;
    brand: String;
    model: String;
    registrationNumber: String;

    constructor(carUid, brand, model, registrationNumber) {
        this.carUid = carUid;
        this.brand = brand;
        this.model = model;
        this.registrationNumber = registrationNumber;
    }
}

export default CarRentalDto;