/**
 * The `Customer` component fetches customer data from an API and displays it in a table format,
 * including invoice and service details.
 * @returns The `Customer` component is returning a JSX element. It renders a table that displays
 * customer details, including their name, email, invoice details, and service details. The component
 * also handles loading state and displays a loading message while the data is being fetched.
 */
import React, { useEffect, useState } from "react";
import { fetchCustomers } from "../api";

function Customer() {
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(true);
  /* The `useEffect` hook is used to perform side effects in functional components. In this case, it is
  used to fetch customer data from an API and update the state variables `customers`, `loading`, and
  `setLoading`. */
  useEffect(() => {
    fetchCustomers()
      .then((response) => {
        console.log(response.data);
        setCustomers(response.data);
        setLoading(false);
      })
      .catch((error) => {
        console.error("Error fetching data:", error);
        setLoading(false);
      });
  }, []);

  return (
    <div className=" h-screen">
      <h1 className="text-2xl text-blue-800 font-bold ">Customer Details</h1>
      {loading ? (
        <p>Loading...</p>
      ) : customers.length === 0 ? (
        <p>No customers found.</p>
      ) : (
        <table className="table-auto border-collapse w-full mt-4 ">
          <thead>
            <tr className=" bg-blue-800 text-white">
              <th className="border border-gray-400 px-4 py-2">Name</th>
              <th className="border border-gray-400 px-4 py-2">Email</th>
              <th className="border border-gray-400 px-4 py-2">
                Invoice Details
              </th>
              <th className="border border-gray-400 px-4 py-2">
                Service Details
              </th>
            </tr>
          </thead>
          <tbody className="bg-gray-200">
            {customers.map((customer) => (
              <tr key={customer.id} className="font-semibold">
                <td className="border border-gray-400 px-4 py-2">
                  {customer?.name}
                </td>
                <td className="border border-gray-400 px-4 py-2">
                  {customer?.email}
                </td>
                <td className="border border-gray-400 px-4 py-2">
                  <div>
                    <p>Amount: {customer?.invoices[0]?.amount}</p>
                    <p>Due Date: {customer?.invoices[0]?.dueDate}</p>
                    <p>
                      Status: {customer?.invoices[0]?.paid ? "Paid" : "Unpaid"}
                    </p>
                  </div>
                </td>
                <td className="border border-gray-400 px-4 py-2">
                  <div>
                    <p>Name: {customer?.services[0]?.serviceName}</p>
                    <p>Cost: {customer?.services[0]?.serviceCost}</p>
                    <p>Status: {customer?.services[0]?.status}</p>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default Customer;
