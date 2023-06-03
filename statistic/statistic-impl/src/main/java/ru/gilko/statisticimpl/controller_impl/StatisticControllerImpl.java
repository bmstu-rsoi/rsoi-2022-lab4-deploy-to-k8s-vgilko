package ru.gilko.statisticimpl.controller_impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.gilko.statisticapi.controller.StatisticController;
import ru.gilko.statisticimpl.service.StatisticService;

import static org.springframework.http.HttpStatus.OK;

@RestController
@Slf4j
@AllArgsConstructor
public class StatisticControllerImpl implements StatisticController {
    private final StatisticService statisticService;

    @Override
    public ResponseEntity<?> getCancelled() {
        log.info("Request for reading statistic of cancelled rentals");
        return ResponseEntity.ok(statisticService.getCancelingStatistics());
    }


    @Override
    public ResponseEntity<?> getProfitable() {
        log.info("Request for reading statistic by most profitable models in last month");

        return new ResponseEntity<>(statisticService.getMostProfitable(), OK);
    }
}
