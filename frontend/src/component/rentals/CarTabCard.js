import CarRentalDto from "../../dtos/CarRentalDto";
import {Card} from "react-bootstrap";

function CarTabCard(car: CarRentalDto) {
    if (!car.carUid) {
        return (<Card id={car.carUid}>
            <Card.Body>
                <Card.Header>Автомобиль</Card.Header>
                <Card.Title>Информация временно недоступна</Card.Title>
            </Card.Body>
        </Card>);
    }

    return (
        <Card id={car.carUid}>
            <Card.Body>
                <Card.Header>Автомобиль</Card.Header>
                <Card.Title>{car.brand} | {car.model}</Card.Title>
                <Card.Text>{car.registrationNumber}</Card.Text>
            </Card.Body>
        </Card>
    );
}

export default CarTabCard;