import type ProfitableModelDto from "./ProfitableModelDto";

class CancelingStatisticDto {
    profitableModel: ProfitableModelDto;
    day: number;
    daysToEnd: number;

    constructor(profitableModel: ProfitableModelDto,
                day: number,
                daysToEnd: number) {
        this.profitableModel = profitableModel;
        this.day = day;
        this.daysToEnd = daysToEnd;
    }
}

export default CancelingStatisticDto;