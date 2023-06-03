class ProfitableModelDto {
    model: string;
    proceeds: number;
    totalProceeds: number;

    constructor(model: string,
                proceeds: number,
                totalProceeds: number) {
        this.model = model;
        this.proceeds = proceeds;
        this.totalProceeds = totalProceeds;
    }
}

export default ProfitableModelDto;