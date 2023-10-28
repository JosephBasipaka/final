import React, { useEffect, useState } from "react";
import { fetchCustomers } from "../api";
import PieChartComp from "./PieChartComp";

export default function Stats() {
  const [customers, setCustomers] = useState([]);
  const [basicData, setBasicData] = useState(0);
  const [standardData, setStandardData] = useState(0);
  const [premiumData, setPremiumData] = useState(0);
  const [paidCount, setPaidCount] = useState(0);
  const [unpaidCount, setUnpaidCount] = useState(0);
  const [activeCount, setActiveCount] = useState(0);
  const [terminatedCount, setTerminatedCount] = useState(0);

  const [selectedService, setSelectedService] = useState(null);
  const [customersWithSelectedService, setCustomersWithSelectedService] =
    useState([]);
  const [selectedInvoice, setSelectedInvoice] = useState(null);
  const [customersWithSelectedInvoice, setCustomersWithSelectedInvoice] =
    useState([]);
  const [selectedStatus, setSelectedStatus] = useState(null);
  const [customersWithSelectedStatus, setCustomersWithSelectedStatus] =
    useState([]);

  const handleChartClick = (index) => {
    setSelectedInvoice("");
    setSelectedStatus("");
    const services = ["Basic", "Standard", "Premium"];
    const selected = services[index];
    setSelectedService(selected);
    const filteredCustomers = customers.filter(
      (customer) => customer.services[0]?.serviceName === selected
    );
    console.log(filteredCustomers);
    setCustomersWithSelectedService(filteredCustomers);
  };
  const handlePaidClick = (index) => {
    setSelectedService("");
    setSelectedStatus("");
    const invoices = ["Paid", "Unpaid"];
    const selected = invoices[index];
    setSelectedInvoice(selected);
    const filteredCustomers = customers.filter((customer) => {
      if (selected === "Paid") {
        return customer.invoices[0]?.paid === true;
      } else if (selected === "Unpaid") {
        return customer.invoices[0]?.paid === false;
      }
      return false;
    });
    console.log(filteredCustomers);
    setCustomersWithSelectedInvoice(filteredCustomers);
  };
  const handleStatusClick = (index) => {
    setSelectedService("");
    setSelectedInvoice("");
    const status = ["Active", "Terminated"];
    const selected = status[index];
    setSelectedStatus(selected);
    const filteredCustomers = customers.filter(
      (customer) => customer.services[0]?.status === selected
    );
    console.log(filteredCustomers);
    setCustomersWithSelectedStatus(filteredCustomers);
  };

  useEffect(() => {
    fetchCustomers().then((response) => {
      setCustomers(response.data);
      console.log(response.data);
      for (const customer of response.data) {
        if (customer.services[0]?.serviceName === "Basic") {
          setBasicData((prevBasicData) => prevBasicData + 1);
        }
        if (customer.services[0]?.serviceName === "Standard") {
          setStandardData((prevStandardData) => prevStandardData + 1);
        }
        if (customer.services[0]?.serviceName === "Premium") {
          setPremiumData((prevPremiumData) => prevPremiumData + 1);
        }
        if (customer.invoices[0]?.paid) {
          setPaidCount((prevPaidCount) => prevPaidCount + 1);
        }
        if (!customer.invoices[0]?.paid) {
          setUnpaidCount((prevUnpaidCount) => prevUnpaidCount + 1);
        }
        if (customer.services[0]?.status === "Active") {
          setActiveCount((prevActiveCount) => prevActiveCount + 1);
        }
        if (customer.services[0]?.status === "Terminated") {
          setTerminatedCount((prevTerminatedCount) => prevTerminatedCount + 1);
        }
      }
    });
  }, []);

  return (
    <div className="h-screen diagonal-background">
      <h1 className="text-2xl font-bold">
        Metrics for Service Types by Customers
      </h1>
      <div className="flex flex-row items-center">
        <PieChartComp
          data={[basicData, standardData, premiumData]}
          labels={["Basic", "Standard", "Premium"]}
          backgroundColor={["powderblue", "pink", "cyan"]}
          onClick={handleChartClick}
        />
        {selectedService && (
          <div className="mt-4 p-4 rounded-lg bg-blue-900 bg-opacity-70">
            <h2 className="text-xl font-semibold mb-2 text-white">
              Selected Service: {selectedService}
            </h2>
            <table className="table-auto border-collapse">
              <thead>
                <tr className="bg-blue-600 text-white">
                  <th className="border border-gray-400 px-4 py-2">Name</th>
                  <th className="border border-gray-400 px-4 py-2">Email</th>
                </tr>
              </thead>
              <tbody className="bg-gray-200 text-black">
                {customersWithSelectedService.map((customer, index) => (
                  <tr key={index}>
                    <td className="border border-gray-400 px-4 py-2">
                      {customer.name}
                    </td>
                    <td className="border border-gray-400 px-4 py-2">
                      {customer.email}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
        <PieChartComp
          data={[paidCount, unpaidCount]}
          labels={["Paid", "Unpaid"]}
          backgroundColor={["green", "rgba(125,0,0,1)"]}
          onClick={handlePaidClick}
        />
        {selectedInvoice && (
          <div className="mt-4 p-4 rounded-lg bg-blue-900 bg-opacity-70">
            <h2 className="text-xl font-semibold mb-2 text-white">
              Selected Service Status: {selectedInvoice}
            </h2>
            <table className="table-auto border-collapse">
              <thead>
                <tr className="bg-blue-600 text-white">
                  <th className="border border-gray-400 px-4 py-2">
                    Customer Name
                  </th>
                  <th className="border border-gray-400 px-4 py-2">
                    Service Name
                  </th>
                  <th className="border border-gray-400 px-4 py-2">
                    Service Cost
                  </th>
                </tr>
              </thead>
              <tbody className="bg-gray-200 text-black">
                {customersWithSelectedInvoice.map((customer, index) => (
                  <tr key={index}>
                    <td className="border border-gray-400 px-4 py-2">
                      {customer.name}
                    </td>
                    <td className="border border-gray-400 px-4 py-2">
                      {customer.services[0]?.serviceName}
                    </td>
                    <td className="border border-gray-400 px-4 py-2">
                      {customer.services[0]?.serviceCost}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
        <PieChartComp
          data={[activeCount, terminatedCount]}
          labels={["Active", "Terminated"]}
          backgroundColor={["indigo", "rose"]}
          onClick={handleStatusClick}
        />
        {selectedStatus && (
          <div className="mt-4 p-4 rounded-lg bg-blue-900 bg-opacity-70">
            <h2 className="text-xl font-semibold mb-2 text-white">
              Selected Service Status: {selectedStatus}
            </h2>
            <table className="table-auto border-collapse">
              <thead>
                <tr className="bg-blue-600 text-white">
                  <th className="border border-gray-400 px-4 py-2">
                    Customer Name
                  </th>
                  <th className="border border-gray-400 px-4 py-2">
                    Service Name
                  </th>
                  <th className="border border-gray-400 px-4 py-2">
                    Service Due Date
                  </th>
                </tr>
              </thead>
              <tbody className="bg-gray-200 text-black">
                {customersWithSelectedStatus.map((customer, index) => (
                  <tr key={index}>
                    <td className="border border-gray-400 px-4 py-2">
                      {customer.name}
                    </td>
                    <td className="border border-gray-400 px-4 py-2">
                      {customer.services[0]?.serviceName}
                    </td>
                    <td className="border border-gray-400 px-4 py-2">
                      {customer.invoices[0]?.dueDate}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}
