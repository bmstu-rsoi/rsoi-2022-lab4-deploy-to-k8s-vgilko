import {Tab, Tabs} from "react-bootstrap";
import CancelledRentalsTab from "./CancelledRentalsTab";
import ProfitableModelTab from "./ProfitableModelTab";
import React, {useState} from "react";

function StatisticComponent() {
    const [key, setKey] = useState('cancelled');

    return (
        <Tabs id="uncontrolled-tab-example"
              activeKey={key}
              onSelect={(k) => setKey(k)}
              className="mb-3"
              style={{marginTop: "16px"}}>
            <Tab eventKey="cancelled"
                 title="Статистика отмен">
                <CancelledRentalsTab/>
            </Tab>
            <Tab eventKey="profitableModelDto"
                 title="Рентабельность">
                <ProfitableModelTab/>
            </Tab>
        </Tabs>
    );
}

export default StatisticComponent;