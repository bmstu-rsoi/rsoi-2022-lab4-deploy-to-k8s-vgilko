package ru.gilko.statisticimpl.service;

import ru.gilko.gatewayapi.dto.rental.StatisticDto;
import ru.gilko.statisticapi.dto.CancelingStatisticDto;
import ru.gilko.statisticapi.dto.ProfitableModelDto;

import java.util.List;

public interface StatisticService {
    void save(StatisticDto statisticDto);
    List<ProfitableModelDto> getMostProfitable();

    List<CancelingStatisticDto> getCancelingStatistics();
}
