import axios from "axios";
import {RENTAL_URL} from "../../constants/ControllerUrls";
import {useEffect, useState} from "react";
import RentalCard from "./RentalCard";
import {ListGroup} from "react-bootstrap";

function RentalCardList() {
    const [rentals, setRentals] = useState([]);
    const [rentalCards, setRentalCards] = useState([]);

    useEffect(() => {
        getRentals();
    }, []);

    function getRentals() {
        const config = {
            headers: {
                "X-User-Name": "vgilko"
            }
        };

        axios.get(RENTAL_URL, config)
            .then(r => setRentals(r.data))
    }

    useEffect(() => {
        const mappedRentals = rentals.map(rental => (<RentalCard rental={rental}/>));

        setRentalCards(mappedRentals);
    }, [rentals])


    return (<ListGroup style={{alignContent:"center", textAlign:"center", marginTop: "24px"}}>
        {rentalCards.length === 0
            ? <h1>Нет данных об арендах</h1>
            : rentalCards}
    </ListGroup>)
}

export default RentalCardList;