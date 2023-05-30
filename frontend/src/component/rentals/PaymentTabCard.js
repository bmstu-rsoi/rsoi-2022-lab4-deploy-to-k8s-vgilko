import PaymentRentalDto from "../../dtos/PaymentRentalDto";
import {Card} from "react-bootstrap";
import PaymentStatusEnum from "../../enums/PaymentStatusEnum";

function PaymentTabCard(payment: PaymentRentalDto) {
    if (!payment.paymentUid) {
        return (
            <Card>
                <Card.Body>
                    <Card.Header>Счёт</Card.Header>
                    <Card.Title>Информация временно недоступна</Card.Title>
                </Card.Body>
            </Card>
        )
    }

    return (
        <Card>
            <Card.Body>
                <Card.Header>Счёт</Card.Header>
                <Card.Title>#{payment.paymentUid}</Card.Title>
                <Card.Text>{payment.price} ₽</Card.Text>
                <Card.Text>Статус оплаты: {PaymentStatusEnum[payment.status]}</Card.Text>
            </Card.Body>
        </Card>
    )
}

export default PaymentTabCard;