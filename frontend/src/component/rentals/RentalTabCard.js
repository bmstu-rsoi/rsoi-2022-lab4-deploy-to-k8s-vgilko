import {Card} from "react-bootstrap";
import RentStatusEnum from "../../enums/RentStatusEnum";
import FinishRentalButton from "../../controls/FinishRentalButton";
import RentalDto from "../../dtos/RentalDto";
import CancelRentalButton from "../../controls/CancelRentalButton";

interface RentalTabCardProps {
    rental: RentalDto;
    doReload: () => void;
}

function RentalTabCard({rental, doReload}: RentalTabCardProps) {
    function normalizeDate(date: []) {
        const [year, month, day] = date;
        return new Date(year, month - 1, day);
    }

    const normalizedDateFrom = normalizeDate(rental.dateFrom);

    function getControls() {
        const rentalStatus = RentStatusEnum[rental.status];

        if (rentalStatus === RentStatusEnum.IN_PROGRESS) {
            const shouldShowCancelButton = Date.now() < normalizedDateFrom;

            return (
                <div style={{
                    display: "flex",
                    justifyContent: "space-between",
                    textAlign: "center"
                }}>
                    <FinishRentalButton rentalUid={rental.rentalUid}
                                        reload={doReload}/>
                    {shouldShowCancelButton
                        ? <CancelRentalButton rentalUid={rental.rentalUid}
                                              reload={doReload}/>
                        : ""}
                </div>
            );
        }
    }

    const dateFrom = normalizedDateFrom.toLocaleDateString();
    const dateTo = normalizeDate(rental.dateTo).toLocaleDateString();

    return (
        <Card>
            <Card.Body>
                <Card.Header>Аренда</Card.Header>
                <Card.Title style={{padding: "6px 0 6px 0"}}>#{rental.rentalUid}</Card.Title>
                <Card.Text>Арендована
                    c <b>{dateFrom}</b> по <b>{dateTo}</b></Card.Text>
                <Card.Text>Статус аренды: {RentStatusEnum[rental.status]}</Card.Text>
                {getControls()}
            </Card.Body>
        </Card>
    );
}

export default RentalTabCard;