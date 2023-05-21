import {Route, Routes} from "react-router";
import {Home} from "./pages/home/Home.tsx";
import {Payment} from "./pages/payment/Payment.tsx";

function App() {


    return (
        <>
            <Routes>
                <Route path="/" element={<Home/>}/>
                <Route path="/payment" element={<Payment />}/>
            </Routes>

        </>
    )
}

export default App
