package ru.gilko.gatewayapi.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelingStatisticDto {
    private ProfitableModelDto profitableModel;
    private int day;
    private int daysToEnd;
}
