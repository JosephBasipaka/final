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
        <div>{CURRENT_USER_TYPE}</div>
      </ul>
    </nav>
  );
};

export default NavBar;
