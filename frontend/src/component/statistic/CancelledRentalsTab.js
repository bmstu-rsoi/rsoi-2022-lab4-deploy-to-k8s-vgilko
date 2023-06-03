import React, {useState} from "react";
import axios from "axios";
import {Chart} from "react-google-charts";
import CancelingStatisticDto from "../../dtos/CancelingStatisticDto";
import {CANCELLING} from "../../constants/ControllerUrls";

function getDayCancelingInfo(cancelledStatistic) {
    let dayCancelings = []
    if (cancelledStatistic.length > 0) {
        dayCancelings = new Map([
            [0, 0],
            [1, 0],
            [2, 0],
            [3, 0],
            [4, 0],
            [5, 0],
            [6, 0],
        ]);

        for (const statistic of cancelledStatistic.values()) {
            dayCancelings.set(statistic.day, dayCancelings.get(statistic.day) + 1);
        }

        const dayMappings = new Map([
            [0, "Понедельник"],
            [1, "Вторник"],
            [2, "Среда"],
            [3, "Четверг"],
            [4, "Пятница"],
            [5, "Суббота"],
            [6, "Воскресенье"],
        ]);


        const result =
            Array.from(dayCancelings.entries())
                .map(dayToAmount => [dayMappings.get(dayToAmount[0]), dayToAmount[1]]);

        dayCancelings = [
            ["День недели", "Количество отмен"],
            ...result
        ];

    }
    const dayCancelingsOptions = {
        title: "Отмена аренды по дням недели",
    };
    return {dayCancelings, dayCancelingsOptions};
}

function getDaysToEndData(cancelingData: [CancelingStatisticDto]) {
    const modelToDays = new Map();

    for (const statistic of cancelingData) {
        let carModel = statistic.profitableModel.model;
        if (modelToDays.has(carModel)) {
            let tempStat = modelToDays.get(carModel);

            tempStat[0] += statistic.daysToEnd;
            tempStat[1] += 1;

            modelToDays.set(carModel, tempStat);
        } else {
            modelToDays.set(carModel, [statistic.daysToEnd, 1]);
        }
    }

    console.log(modelToDays);

    let modelToAvgDaysToEnd = Array.from(modelToDays.entries())
        .map(entry => [entry[0], entry[1][0] / entry[1][1]]);

    const modelToAvgDaysToEndOptions = {
        title: "Среднее количество дней до конца аренды",
    };

    modelToAvgDaysToEnd = [
        ["Модель", "В среднем дней до конца аренды"],
        ...modelToAvgDaysToEnd
    ];

    return {modelToAvgDaysToEnd, modelToAvgDaysToEndOptions};
}


function CancelledRentalsTab() {
    const [cancelledStatistic, setStatistic] = useState([]);
    const [hasData, setHasData] = useState(false);

    function getData() {
        if (hasData)
            return [];

        setHasData(true);
        const config = {
            headers: {
                "X-User-Name": "vgilko"
            }
        };

        axios.get(CANCELLING, config)
            .then(r => setStatistic(r.data));

        return [];
    }

    getData();
    let {dayCancelings, dayCancelingsOptions} = getDayCancelingInfo(cancelledStatistic);
    let {modelToAvgDaysToEnd, modelToAvgDaysToEndOptions} = getDaysToEndData(cancelledStatistic);

    console.log(modelToAvgDaysToEnd, modelToAvgDaysToEndOptions);

    return (
        <div>
            {/*<Chart*/}
            {/*    chartType="PieChart"*/}
            {/*    data={data}*/}
            {/*    options={options}*/}
            {/*    width={"100%"}*/}
            {/*    height={"400px"}*/}
            {/*/>*/}
            <Chart
                chartType="ColumnChart"
                data={dayCancelings}
                options={dayCancelingsOptions}
                width={"100%"}
                height={"400px"}
            />
            <Chart
                chartType="ColumnChart"
                data={modelToAvgDaysToEnd}
                options={modelToAvgDaysToEndOptions}
                width={"100%"}
                height={"400px"}
            />
        </div>
    );
}

export default CancelledRentalsTab;