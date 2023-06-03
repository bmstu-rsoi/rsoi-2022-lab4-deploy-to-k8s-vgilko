package ru.gilko.statisticapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfitableModelDto {
    private String model;
    private int proceeds;
    private int totalProceeds;
}
