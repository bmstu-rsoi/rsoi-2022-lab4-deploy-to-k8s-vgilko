import axios from "axios";
import {RENTAL_URL_FINISH} from "../constants/ControllerUrls";
import {Button} from "react-bootstrap";

function FinishRentalButton({rentalUid, reload}) {
    function finishRental() {
        const config = {
            headers: {
                "X-User-Name": "vgilko"
            }
        };

        axios.post(RENTAL_URL_FINISH(rentalUid), rentalUid, config)
            .then(r => {
                console.log(`Rental ${rentalUid} was finished`);
                reload();
            })

    }

    return (<Button variant={"primary"} onClick={finishRental}>Завершить</Button>);
}

export default FinishRentalButton;