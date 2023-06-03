package ru.gilko.statisticimpl.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.gilko.gatewayapi.dto.rental.StatisticDto;
import ru.gilko.statisticapi.dto.CancelingStatisticDto;
import ru.gilko.statisticapi.dto.ProfitableModelDto;
import ru.gilko.statisticimpl.domain.Statistic;
import ru.gilko.statisticimpl.repository.StatisticRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final ModelMapper modelMapper;
    private final StatisticRepository statisticRepository;

    @Override
    public void save(StatisticDto statisticDto) {
        Statistic mapped = modelMapper.map(statisticDto, Statistic.class);

        statisticRepository.save(mapped);
    }

    @Override
    public List<ProfitableModelDto> getMostProfitable() {
        LocalDate monthBeforeDate = LocalDate.now().minusMonths(1);
        List<Statistic> profitableModels = statisticRepository.findAllByEventDateIsAfter(monthBeforeDate);

        Map<String, Integer> modelTotalSumMap = profitableModels.stream()
                .collect(Collectors.groupingBy(Statistic::getModel, Collectors.summingInt(Statistic::getPrice)));

        final int totalProceeds = modelTotalSumMap.values()
                .stream()
                .reduce(Integer::sum)
                .orElse(0);

        List<String> topModels = modelTotalSumMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .toList();

        return topModels.stream()
                .map(model -> new ProfitableModelDto(model, modelTotalSumMap.get(model), totalProceeds))
                .toList();
    }

    @Override
    public List<CancelingStatisticDto> getCancelingStatistics() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1);

        List<Statistic> cancellations = statisticRepository.findAllByEventDateIsAfter(startDate);

        Map<String, Integer> modelTotalSumMap = cancellations.stream()
                .collect(Collectors.groupingBy(Statistic::getModel, Collectors.summingInt(Statistic::getPrice)));

        final int totalProceeds = modelTotalSumMap.values()
                .stream()
                .reduce(Integer::sum)
                .orElse(0);

        List<String> topModels = modelTotalSumMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .toList();

        List<CancelingStatisticDto> cancelingStatisticDtos = new ArrayList<>();

        for (String model : topModels) {
            List<Statistic> modelCancellations = cancellations.stream()
                    .filter(statistic -> statistic.getModel().equals(model))
                    .toList();

            for (Statistic statistic : modelCancellations) {
                LocalDate cancellationDate = statistic.getEventDate();
                int daysToEnd = calculateDaysToEndPeriod(statistic.getEventDate(), statistic.getDateTo());

                ProfitableModelDto profitableModelDto = new ProfitableModelDto(model, modelTotalSumMap.get(model), totalProceeds);
                CancelingStatisticDto cancelingStatisticDto =
                        new CancelingStatisticDto(profitableModelDto, cancellationDate.getDayOfWeek().getValue(), daysToEnd);

                cancelingStatisticDtos.add(cancelingStatisticDto);
            }
        }

        return cancelingStatisticDtos;
    }

    private int calculateDaysToEndPeriod(LocalDate cancellationDate, LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(cancellationDate, endDate);
    }
}

