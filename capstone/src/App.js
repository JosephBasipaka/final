/**
 * The App component is the main component of the application that handles routing and renders
 * different components based on the current route.
 * @returns The App component is being returned, which contains the routes for different pages of the
 * application.
 */
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
import Failure from "./Payment/Failure";
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
          {/* The `<Route path="/register" element={<Register />} />` is defining a route for the
          "/register" path. When the user navigates to the "/register" path, the `<Register />`
          component will be rendered. */}
          <Route path="/register" element={<Register />} />
          {/* The `<Route path="/" element={<Login />} />` is defining a route for the root path ("/").
          When the user navigates to the root path, the `<Login />` component will be rendered. */}
          <Route path="/" element={<Login />} />
          {/* The `<Route>` component is defining a route for the "/customer" path. When the user
          navigates to the "/customer" path, the components inside the `<AdminElement>` component
          will be rendered. */}
          <Route
            path="/customer"
            element={
              <AdminElement>
                <NavBar />
                <Customer />
              </AdminElement>
            }
          />

          {/* The `<Route>` component is defining a route for the "/add" path. When the user navigates
          to the "/add" path, the components inside the `<UserElement>` component will be rendered.
          This includes the `<NavBar />` component and the `<Add />` component. The `<UserElement>`
          component is a wrapper component that checks the current user type and only renders the
          children components if the user type is either "ADMIN" or "USER". If the user type is not
          one of these, a message stating "You donot have access to this page" will be rendered
          instead. */}
          <Route
            path="/add"
            element={
              <UserElement>
                <NavBar />
                <Add />
              </UserElement>
            }
          />
          {/* The `<Route>` component is defining a route for the "/dunning" path. When the user
          navigates to the "/dunning" path, the components inside the `<AdminElement>` component
          will be rendered. This includes the `<NavBar />` component and the `<Dunning />`
          component. The `<AdminElement>` component is a wrapper component that checks the current
          user type and only renders the children components if the user type is "ADMIN". If the
          user type is not "ADMIN", a message stating "You donot have access to this page" will be
          rendered instead. */}
          <Route
            path="/dunning"
            element={
              <AdminElement>
                <NavBar />
                <Dunning />
              </AdminElement>
            }
          />
          {/* The `<Route>` component is defining a route for the "/payments" path. When the user
          navigates to the "/payments" path, the components inside the `<UserElement>` component
          will be rendered. This includes the `<NavBar />` component and the `<Payment />`
          component. The `<UserElement>` component is a wrapper component that checks the current
          user type and only renders the children components if the user type is either "ADMIN" or
          "USER". If the user type is not one of these, a message stating "You donot have access to
          this page" will be rendered instead. */}
          <Route
            path="/payments"
            element={
              <UserElement>
                <NavBar />
                <Payment />
              </UserElement>
            }
          />
          {/* The `<Route>` component with the `path="/stats"` attribute is defining a route for the
          "/stats" path. When the user navigates to the "/stats" path, the components inside the
          `<AdminElement>` component will be rendered. This includes the `<NavBar />` component and
          the `<Stats />` component. */}
          <Route
            path="/stats"
            element={
              <AdminElement>
                <NavBar />
                <Stats />
              </AdminElement>
            }
          />
          {/* The `<Route path="*" ... />` is a catch-all route that matches any path that hasn't been
          defined in the previous routes. It is typically used as a fallback route to handle 404
          errors or display a "Page not found" message when the user navigates to an invalid URL. In
          this case, when the user navigates to a path that doesn't match any of the defined routes,
          the `<div>` element with the "Sorry! Page not Found" message will be rendered along with
          the `<NavBar />` component. */}
          <Route
            path="*"
            element={
              <div className="max-w-lg mx-auto mt-36 font-sans font-semibold text-sm text-black ">
                <NavBar />
                Sorry! Page not Found
              </div>
            }
          />
          {/* The `<Route>` component with the `path="/success"` attribute is defining a route for the
          "/success" path. When the user navigates to the "/success" path, the components inside the
          `<NavBar />` component and the `<Success />` component will be rendered. This route is
          used to display the success page after a successful payment. */}
          <Route
            path="/success"
            element={
              <>
                <NavBar />
                <Success />
              </>
            }
          />
          {/* The `<Route>` component with the `path="/failure"` attribute is defining a route for the
          "/failure" path. When the user navigates to the "/failure" path, the components inside the
          `<NavBar />` component and the `<Failure />` component will be rendered. This route is
          used to display the failure page after an unsuccessful payment. */}
          <Route
            path="/failure"
            element={
              <>
                <NavBar />
                <Failure />
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
