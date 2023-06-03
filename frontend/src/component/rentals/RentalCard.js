import {CardGroup} from "react-bootstrap";
import RentalTabCard from "./RentalTabCard";
import CarTabCard from "./CarTabCard";
import PaymentTabCard from "./PaymentTabCard";
import {useEffect, useState} from "react";
import axios from "axios";
import {RENTAL_URL_WITH_ID} from "../../constants/ControllerUrls";

const RentalCard = ({rental}) => {
    const [reload, setReload] = useState(false);
    const [rentalData, setRentalData] = useState(rental);

    function fetchData() {
        const config = {
            headers: {
                "X-User-Name": "vgilko"
            }
        };

        axios.get(RENTAL_URL_WITH_ID(rental.rentalUid), config)
            .then(response => {
                setRentalData(response.data);
            })
    }

    useEffect(() => {
        fetchData();
        setReload(false);
    }, [reload]);

    function doReload() {
        setReload(true);
    }

    return (
        <CardGroup style={{padding:"12px 0 12px 0"}}>
            <RentalTabCard rental={rentalData}
                           doReload={doReload}/>
            <CarTabCard {...rentalData.car}/>
            <PaymentTabCard {...rentalData.payment}/>
        </CardGroup>
    );
};

export default RentalCard;