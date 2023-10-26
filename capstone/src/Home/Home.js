import "./App.css";
import { Route, Routes } from "react-router-dom";
import Customer from "./Customer/Customer";
import NavBar from "./Navbar/Navbar";
import Dunning from "./Dunning/Dunning";
import Payment from "./Payment/Payment";
import Add from "./Add/Add";

export const USER_TYPES = {
  ADMIN: "ADMIN",
  USER: "USER",
};

export const CURRENT_USER_TYPE = USER_TYPES.USER;
function App() {
  return (
    <div>
      <header>
        <NavBar />
        <Routes>
          <Route
            path="/customer"
            element={
              <AdminElement>
                <Customer />{" "}
              </AdminElement>
            }
          />
          <Route
            path="/add"
            element={
              <UserElement>
                <Add />
              </UserElement>
            }
          />
          <Route
            path="/dunning"
            element={
              <AdminElement>
                <Dunning />
              </AdminElement>
            }
          />
          <Route
            path="/payments"
            element={
              <UserElement>
                <Payment />
              </UserElement>
            }
          />
          <Route
            path="*"
            element={
              <div className="max-w-lg mx-auto mt-36 font-sans font-semibold text-sm text-black ">
                {" "}
                Sorry! Page not Found
              </div>
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
