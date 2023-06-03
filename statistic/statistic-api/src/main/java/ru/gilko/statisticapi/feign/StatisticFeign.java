package ru.gilko.statisticapi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.gilko.statisticapi.dto.CancelingStatisticDto;
import ru.gilko.statisticapi.dto.ProfitableModelDto;

import java.util.List;

import static ru.gilko.statisticapi.constants.ControllerUrls.CANCELLED;
import static ru.gilko.statisticapi.constants.ControllerUrls.PROFITABLE;

@FeignClient(name = "statistic", path = "/statistic", url = "${feign.statistic.url}")
public interface StatisticFeign {
    @GetMapping(path = CANCELLED)
    List<CancelingStatisticDto> getCancelled();

    @GetMapping(path = PROFITABLE)
    List<ProfitableModelDto> getProfitable();
}
