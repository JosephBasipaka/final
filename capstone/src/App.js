import "./App.css";
import { Route, Routes } from "react-router-dom";
import Customer from "./Customer/Customer";
import NavBar from "./Navbar/Navbar";
import Dunning from "./Dunning/Dunning";
import Payment from "./Payment/Payment";
import Add from "./Add/Add";
import Register from "./Login/Register";
import Login from "./Login/Login";
import Success from "./Payment/Success";
import PieChart from "./Dunning/PieChart";
import PieChartComp from "./Dunning/PieChartComp";
import Dummy from "./Dunning/Stats";
import Stats from "./Dunning/Stats";

export const USER_TYPES = {
  ADMIN: "ADMIN",
  USER: "USER",
};

export const CURRENT_USER_TYPE = USER_TYPES.ADMIN;
function App() {
  return (
    <div>
      <header>
        <Routes>
          <Route path="/register" element={<Register />} />
          <Route path="/" element={<Login />} />
          <Route
            path="/customer"
            element={
              <AdminElement>
                <NavBar />
                <Customer />
              </AdminElement>
            }
          />
          <Route
            path="/add"
            element={
              <UserElement>
                <NavBar />
                <Add />
              </UserElement>
            }
          />
          <Route
            path="/dunning"
            element={
              <AdminElement>
                <NavBar />
                <Dunning />
              </AdminElement>
            }
          />
          <Route
            path="/payments"
            element={
              <UserElement>
                <NavBar />
                <Payment />
              </UserElement>
            }
          />
          <Route
            path="/stats"
            element={
              <AdminElement>
                <NavBar />
                <Stats />
              </AdminElement>
            }
          />
          <Route
            path="*"
            element={
              <div className="max-w-lg mx-auto mt-36 font-sans font-semibold text-sm text-black ">
                <NavBar />
                Sorry! Page not Found
              </div>
            }
          />
          <Route
            path="/success"
            element={
              <>
                <NavBar />
                <Success />
              </>
            }
          />
        </Routes>
      </header>
    </div>
  );
}

function UserElement({ children }) {
  if (
    CURRENT_USER_TYPE === USER_TYPES.ADMIN ||
    CURRENT_USER_TYPE === USER_TYPES.USER
  )
    return children;
  else return <div>You donot have access to this page</div>;
}
function AdminElement({ children }) {
  if (CURRENT_USER_TYPE === USER_TYPES.ADMIN) return children;
  else return <div>You donot have access to this page</div>;
}

export default App;
