const PROTOCOL = "http://"
const SERVER_URL = PROTOCOL + "127.0.0.1:8080"

const API_URL = SERVER_URL + "/api/v1"

export const CARS_URL = API_URL + "/cars";
export const RENTAL_URL = API_URL + "/rental";
export const RENTAL_URL_WITH_ID = (rentalUid) => RENTAL_URL + `/${rentalUid}`;
export const RENTAL_URL_FINISH = (rentalUid) => RENTAL_URL_WITH_ID(rentalUid) + "/finish";

export const STATISTIC = API_URL + "/statistic";
export const CANCELLING = STATISTIC + "/cancelled";
export const PROFITABLE = STATISTIC + "/profitable";