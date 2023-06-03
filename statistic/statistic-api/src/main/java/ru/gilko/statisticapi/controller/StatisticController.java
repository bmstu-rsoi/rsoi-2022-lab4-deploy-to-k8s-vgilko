package ru.gilko.statisticapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import static ru.gilko.statisticapi.constants.ControllerUrls.CANCELLED;
import static ru.gilko.statisticapi.constants.ControllerUrls.PROFITABLE;

public interface StatisticController {
    @GetMapping(path = CANCELLED)
    ResponseEntity<?> getCancelled();

    @GetMapping(path = PROFITABLE)
    ResponseEntity<?> getProfitable();
}
