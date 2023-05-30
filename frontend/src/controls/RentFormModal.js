import React, {useEffect, useState} from 'react';

import {Button, Form, Modal} from "react-bootstrap";
import moment from "moment";
import axios from "axios";
import {RENTAL_URL} from "../constants/ControllerUrls";

function RentFormModal({carUid, carBrand, carModel, carPrice, carRegistrationNumber, handleShow, show, reload}) {
    const [rentDays, setRentDays] = useState(1);
    const [rentPrice, setRentPrice] = useState(carPrice);

    const [startDate, setStartDate] = useState(moment().format('YYYY-MM-DD'));
    const [endDate, setEndDate] = useState(moment().add(1, 'day').format('YYYY-MM-DD'));

    // todo: переделать, когда введу авторизацию
    function handleRent() {
        const config = {
            headers: {
                "X-User-Name": "vgilko"
            }
        };

        const data = {
            carUid: carUid,
            dateFrom: startDate,
            dateTo: endDate
        };

        axios.post(RENTAL_URL, data, config)
            .then(response => {
                reload();
                console.log(response.data);
            })
            .catch(error => {
                console.log(error);
            });

        onHide();
    }

    useEffect(() => {
        calculateRentalDays();
        calculateRentPrice();
    }, [startDate, endDate, rentDays, rentPrice]);


    function setDefaultDates() {
        setStartDate(moment().format('YYYY-MM-DD'));
        setEndDate(moment().add(1, 'day').format('YYYY-MM-DD'));
    }

    function onHide(event) {
        handleShow(event);
        setRentDays(1);
        setRentPrice(carPrice);

        setDefaultDates();
        calculateRentalDays();
    }

    function calculateRentPrice() {
        setRentPrice(rentDays * carPrice);
    }

    function calculateRentalDays() {
        const date1 = new Date(startDate);
        const date2 = new Date(endDate);

        const diffInMs = date2.getTime() - date1.getTime();
        const diffInDays = Math.ceil(diffInMs / 86400000);

        if (diffInDays < 1) {
            // setDefaultDates();
            return;
        }

        setRentDays(diffInDays);
        calculateRentPrice();
    }

    function changeStartDate(event) {
        setStartDate(event.target.value);

        calculateRentalDays();
    }

    function changeEndDate(event) {
        setEndDate(event.target.value);

        calculateRentalDays();
    }

    // TODO добавить обработку ошибки от сервиса
    return (
        <Modal key={"rent" + carUid} show={show} onHide={onHide}>
            <Modal.Header closeButton>
                <Modal.Title>{carBrand} {carModel} | {carRegistrationNumber}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p>Сутки аренды: {carPrice}₽</p>
                <Form style={{alignContent: "center"}}>
                    <Form.Control style={{width: "45%", display: "inline-block"}}
                                  type="date"
                                  value={startDate}
                                  min={moment().format('YYYY-MM-DD')}
                                  onChange={changeStartDate}/>
                    <Form.Text> --- </Form.Text>
                    <Form.Control style={{width: "45%", display: "inline-block"}}
                                  type="date"
                                  min={moment(startDate).add(1, 'day').format('YYYY-MM-DD')}
                                  value={endDate}
                                  onChange={changeEndDate}/>
                </Form>
            </Modal.Body>
            <Modal.Footer style={{justifyContent: "space-between"}}>
                <p style={{fontSize: "2rem"}}>{carPrice * rentDays}₽ / {rentDays} сут.</p>
                <Button variant="primary" onClick={handleRent}>Арендовать</Button>
            </Modal.Footer>
        </Modal>
    );
}

export default RentFormModal;