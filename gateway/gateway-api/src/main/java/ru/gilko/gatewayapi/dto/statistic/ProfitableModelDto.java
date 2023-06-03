package ru.gilko.gatewayapi.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfitableModelDto {
    private String model;
    private int proceeds;
    private int totalProceeds;
}
