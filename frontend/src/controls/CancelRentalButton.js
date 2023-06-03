import {Button} from "react-bootstrap";
import axios from "axios";
import {RENTAL_URL_WITH_ID} from "../constants/ControllerUrls";

const   CancelRentalButton = ({rentalUid, reload}) => {
    function cancelRent() {
        const config = {
            headers: {
                "X-User-Name": "vgilko"
            }
        };

        axios.delete(RENTAL_URL_WITH_ID(rentalUid), config)
            .then(r => {
                console.log("Cancel rental " + rentalUid);
                reload();
            })
    }

    return (<Button variant={"primary"} onClick={cancelRent}>Отменить</Button>);
}

export default CancelRentalButton;