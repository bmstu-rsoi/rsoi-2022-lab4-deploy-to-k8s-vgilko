package ru.gilko.gatewayapi.constants;

public class ControllerUrls {
    private ControllerUrls() {
    }



    public static final String CARS_URL = "/cars";
    public static final String RENTAL_URL = "/rental";
    public static final String RENTAL_URL_WITH_ID = RENTAL_URL + "/{rentalUid}";
    public static final String RENTAL_URL_FINISH = RENTAL_URL_WITH_ID + "/finish";
    public static final String STATISTIC = "/statistic";
    public static final String CANCELLED = STATISTIC + "/cancelled";
    public static final String PROFITABLE = STATISTIC + "/profitable";
}
