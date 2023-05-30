import React from 'react';
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import CarCardList from "./component/cars/CarCardList";
import 'bootstrap/dist/css/bootstrap.min.css';
import {Container, ThemeProvider} from "react-bootstrap";
import RentalCardList from "./component/rentals/RentalCardList";
import NavBar from "./controls/NavBar";


function App() {
    return (
        <BrowserRouter>
            <div className="App">
                <ThemeProvider breakpoints={['md']}>
                    <Container>
                        <NavBar/>
                        <Routes>
                            <Route path="/" element={<Navigate to={"/cars"}/>}/>
                            <Route path="/cars" element={<CarCardList/>}/>
                            <Route path="/rentals" element={<RentalCardList/>}/>
                        </Routes>
                    </Container>
                </ThemeProvider>
            </div>
        </BrowserRouter>
    );
}

export default App;
