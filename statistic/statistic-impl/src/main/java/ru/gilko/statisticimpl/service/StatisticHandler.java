package ru.gilko.statisticimpl.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.gilko.gatewayapi.dto.rental.StatisticDto;

@Component
@Slf4j
@AllArgsConstructor
public class StatisticHandler {
    private final StatisticService statisticService;

    @KafkaListener(
            topics = "statistic",
            groupId = "statistic_service",
            containerFactory = "statisticContainerFactory"
    )
    void handle(StatisticDto statisticDto) {
        log.info("Reading message from kafka: {}", statisticDto);

        statisticService.save(statisticDto);
    }
}
