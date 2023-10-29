/**
 * The code defines a React functional component called NavBar that renders a navigation bar with
 * different links based on the current user type.
 * @returns The NavBar component is returning a navigation bar with a list of links. The links are
 * conditionally rendered based on the value of the CURRENT_USER_TYPE variable. The CURRENT_USER_TYPE
 * variable is compared to the USER_TYPES.ADMIN and USER_TYPES.USER constants to determine which links
 * should be displayed. The navigation bar also displays the value of the CURRENT_USER_TYPE variable.
 */
import React from "react";
import { Link } from "react-router-dom";
import { CURRENT_USER_TYPE, USER_TYPES } from "../App";

const NavBar = () => {
  return (
    <nav className="bg-blue-500 p-8">
      <ul className="flex justify-between">
        {CURRENT_USER_TYPE === USER_TYPES.ADMIN ? (
          <li>
            <Link to="/customer" className="text-white hover:text-gray-200">
              Home
            </Link>
          </li>
        ) : null}
        {CURRENT_USER_TYPE === USER_TYPES.ADMIN ||
        CURRENT_USER_TYPE === USER_TYPES.USER ? (
          <li>
            <Link to="/add" className="text-white hover:text-gray-200">
              Add
            </Link>
          </li>
        ) : null}
        {CURRENT_USER_TYPE === USER_TYPES.ADMIN ? (
          <li>
            <Link to="/dunning" className="text-white hover:text-gray-200">
              Dunning
            </Link>
          </li>
        ) : null}
        <li>
          <Link to="/payments" className="text-white hover:text-gray-200">
            Payments
          </Link>
        </li>
        <li>
          <Link to="/stats" className="text-white hover:text-gray-200">
            Stats
          </Link>
        </li>
        <div>{CURRENT_USER_TYPE}</div>
      </ul>
    </nav>
  );
};

export default NavBar;
