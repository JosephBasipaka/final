import React from 'react';
import { Link } from 'react-router-dom';


const NavBar = () => {
  return (
    <nav className="bg-blue-500 p-8">
  <ul className="flex space-x-64">
    <li>
      <Link to="/" className="text-white hover:text-gray-200">Home</Link>
    </li>
    <li>
      <Link to="/add" className="text-white hover:text-gray-200">Add</Link>
    </li>
    <li>
      <Link to="/dunning" className="text-white hover:text-gray-200">Dunning</Link>
    </li>
    <li>
      <Link to="/payments" className="text-white hover:text-gray-200">Payments</Link>
    </li>
  </ul>
</nav>

  );
};

export default NavBar;
