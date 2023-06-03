package ru.gilko.statisticapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CancelingStatisticDto {
    private ProfitableModelDto profitableModel;
    private int day;
    private int daysToEnd;
}
