import {Nav} from "react-bootstrap";

function NavBar() {
    return (
        <Nav justify variant="tabs" defaultActiveKey="/home">
            <Nav.Item>
                <Nav.Link href="/cars">Автомобили</Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link href="/rentals">История поездок</Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link href="/statistic">Статистика</Nav.Link>
            </Nav.Item>
        </Nav>
    );
}

export default NavBar;