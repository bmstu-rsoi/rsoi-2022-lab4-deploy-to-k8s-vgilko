import React, {useEffect, useState} from 'react';
import axios from "axios";
import {CARS_URL} from "../../constants/ControllerUrls";
import {Form, Row} from "react-bootstrap";
import CarCard from "./CarCard";
import LoadingSpinner from "../LoadingSpinner";
import {useSearchParams} from "react-router-dom";


function CarCardList() {
    const CARD_PER_ROW = 3;
    const DEFAULT_PAGE_SIZE = 10;

    const [cardsData, setCardsData] = useState({});
    const [queryParams, setQueryParams] = useSearchParams();
    const [isNeedReload, setIsNeedReload] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [filledRows, setFilledRows] = useState([]);

    useEffect(() => {
        const searchParams = new URLSearchParams(location.search);
        const showAll = searchParams.get("showAll") === "true";
        const page = Number(searchParams.get("page")) || 0;
        const size = Number(searchParams.get("size")) || DEFAULT_PAGE_SIZE;

        setQueryParams({showAll, page, size});
    }, [location.search]);


    useEffect(() => {
        setIsNeedReload(false);
        getCars();
    }, [queryParams, isNeedReload]);

    function needReload() {
        setIsNeedReload(true);
    }

    function getCars() {
        setIsLoading(true);
        return (
            axios.get(CARS_URL, {
                params: queryParams
            })
                .then(response => {
                    setIsLoading(false);

                    setCardsData(response.data);
                })
                .catch(error => {
                    console.log(error);
                }));
    }

    function onChangeShowAll(event) {
        setQueryParams({...queryParams, showAll: event.target.checked});
    }

    function buildCard(car) {
        return <CarCard key={car.carUid}
                        reload={needReload}
                        {...car} />
    }

    function buildRow(cards) {
        return (
            <Row xs={1} md={CARD_PER_ROW} style={{justifyContent: "center", alignItems: "center"}}>
                {cards.map(car => buildCard(car))}
            </Row>
        );
    }

    function getFilledRows() {
        if (!cardsData.items) {
            return [];
        }

        let result = [];
        const rowsAmount = Math.ceil(cardsData.items.length / CARD_PER_ROW);
        for (let index = 0; index < rowsAmount; index++) {
            let sliceStartIndex = index * CARD_PER_ROW;

            const isLastRow = sliceStartIndex + CARD_PER_ROW >= cardsData.items.length;
            let sliceEndIndex = isLastRow
                ? cardsData.items.length
                : sliceStartIndex + CARD_PER_ROW;

            let cardSlice = cardsData.items.slice(sliceStartIndex, sliceEndIndex);

            result.push(buildRow(cardSlice));
        }

        setFilledRows(result);
    }

    useEffect(() => {
        getFilledRows();
    }, [cardsData]);

    return (
        <div>
            <Form style={{margin: "16px"}}>
                <Form.Check
                    type="switch"
                    id="showAll"
                    label="Показывать забронированные"
                    checked={queryParams.get("showAll") === "true"}
                    onChange={onChangeShowAll}
                />
            </Form>

            {isLoading ? <LoadingSpinner/> : ""}

            {filledRows.length === 0 && !isLoading
                ? <h1 align="center">Ни одного свободного автомобиля :(</h1>
                : filledRows}
        </div>
    );
}

// TODO добавить пагинацию
export default CarCardList;
