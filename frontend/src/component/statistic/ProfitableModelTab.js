import React, {useState} from "react";
import {Chart} from "react-google-charts";
import axios from "axios";
import {PROFITABLE} from "../../constants/ControllerUrls";

function ProfitableModelTab() {
    const [statisticData, setData] = useState(getData());

    function getData() {
        const config = {
            headers: {
                "X-User-Name": "vgilko"
            }
        };

        const proceeds = [];

        axios.get(PROFITABLE, config)
            .then(r =>
                r.data.forEach(profitableDto => proceeds.push([profitableDto.model, profitableDto.proceeds])
                ));

        return proceeds;
    }

    const data = [
        ["Модель авто", "Прибыль"],
        ...statisticData
    ];

    const options = {
        title: "Рентабельность моделей автомобилей",
    };

    return (
        <Chart
            chartType="PieChart"
            data={data}
            options={options}
            width={"100%"}
            height={"400px"}
        />
    )
}

export default ProfitableModelTab;