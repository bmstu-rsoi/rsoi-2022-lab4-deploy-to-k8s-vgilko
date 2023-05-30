import React, {useState} from 'react';
import {Button, Card, Image, ListGroup} from "react-bootstrap";
import carPicture from "../../assets/car.png";
import RentFormModal from "../../controls/RentFormModal";

const CarCard = ({carUid, brand, model, registrationNumber, power, type, price, available, reload}) => {
    const [showRentModal, setShowRentModal] = useState(false);
    const [selectedCar, setSelectedCar] = useState(null);

    function handleShowRentModal(event) {
        const newVar = {
            carUid: carUid,
            carBrand: brand,
            carModel: model,
            carPrice: price,
            carRegistrationNumber: registrationNumber,
        };
        setSelectedCar(newVar);
        setShowRentModal(true);
    }

    function handleShow(event) {
        setShowRentModal(!showRentModal);
    }

    return (
        <div>
            <Card style={{
                padding: 0,
                margin: "0 16px 24px 0"
            }}>
                <Card.Body style={{padding: 0}}>
                    <Card.Header style={{
                        borderStyle: "none",
                        textAlign: "center",
                        padding: "16px 0 16px 0"
                    }}>{brand} {model}</Card.Header>
                    <ListGroup style={{borderStyle: "none"}}>
                        <ListGroup.Item style={{
                            borderStyle: "none",
                            textAlign: "center",
                            padding: "16px 0 16px 0"
                        }}>
                            <Card.Title>{registrationNumber}</Card.Title>
                        </ListGroup.Item>

                        <ListGroup.Item style={{
                            borderStyle: "none",
                            padding: "16px 0 16px 0"
                        }}>
                            <Image fluid={true}
                                   src={carPicture}
                            />

                        </ListGroup.Item>
                    </ListGroup>
                    <Card.Body style={{
                        alignContent: "center",
                        textAlign: "center",
                        fontSize: "1.5rem",
                        padding: "16px 0 16px 0"
                    }}>
                        <Card.Text>{price} ₽/сутки</Card.Text>
                        {available
                            ? <Button id={carUid} variant="primary"
                                      onClick={handleShowRentModal}>Арендовать</Button>
                            : <Button id={carUid} disabled variant="primary">Арендовать</Button>}

                    </Card.Body>
                </Card.Body>
            </Card>

            <RentFormModal
                handleShow={handleShow}
                show={showRentModal}
                reload={reload}
                {...selectedCar}/>
        </div>
    );
};

export default CarCard;
