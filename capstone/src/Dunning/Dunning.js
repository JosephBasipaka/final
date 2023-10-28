import React, { useEffect, useState, useRef } from "react";
import { fetchCustomers, fetchServices } from "../api";
import axios from "axios";
import PieChart from "./PieChart";
import PopupMessage from "./PopUpMessage";

const Dunning = () => {
  const [customers, setCustomers] = useState([]);
  const [serviceSpecificRemainders, setRemainders] = useState([]);
  const [showReminderDetails, setShowReminderDetails] = useState(false);
  const [showTerminationDetails, setShowTerminationDetails] = useState(false);
  const [showReminderButtons, setShowReminderButtons] = useState(false);
  const [loading, setLoading] = useState(true);
  const [showChart, setShowChart] = useState(false);
  const [showSuccessMessage, setShowSuccessMessage] = useState(false);
  const [popupMessage, setPopupMessage] = useState("");

  const activeCount = 10;
  const terminatedCount = 5;
  const activeColor = "blue";
  const terminatedColor = "red";

  const chartData = {
    labels: ["Active", "Terminated"],
    datasets: [
      {
        data: [activeCount, terminatedCount],
        backgroundColor: [activeColor, terminatedColor],
      },
    ],
  };
  const handleShowChart = () => {
    setShowChart(!showChart);
  };

  useEffect(() => {
    fetchCustomers().then(async (response) => {
      setCustomers(response.data);
      setLoading(false);

      const fetchPromises = response.data.map(async (customer) => {
        if (customer) {
          const serviceId = customer.services[0]?.id ?? 0;
          const customerId = customer.id;
          return fetch(
            `http://localhost:8080/api/dunning/dunningDetails?customerId=${customerId}&serviceId=${serviceId}`
          );
        }
        return {
          initialReminder: false,
          followUpReminderCount: 0,
          finalReminder: false,
        };
      });
      const temp = [];

      Promise.all(fetchPromises)
        .then((responses) => {
          responses.forEach((response) => {
            response.json().then((data) => {
              temp.push(data[0]);
              console.log(temp);
              setRemainders(temp);
            });
          });
        })
        .catch((error) => {
          console.error("Error fetching data:", error);
        });
    });
  }, []);

  async function sendReminders(step) {
    console.log("Working Remainder function");
    const response = await fetch(
      `http://localhost:8080/api/dunning/intReminder?reminderType=${step}`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({}),
      }
    );
    if (response.status === 200) {
      setShowSuccessMessage(true);
      console.log("Reminder Mails Sent to all customers");
    } else {
      console.error("Failed to send reminders");
    }
  }

  async function sendReminderForCustomer(customer, service) {
    console.log("Working Remainder function for customer");
    const response =
      (`http://localhost:8080/api/dunning/reminderTo?customerId=${customer.id}&${service.id}`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({}),
      });
    if (response.status === 200) {
      console.log(
        "Reminder Mail Sent to " +
          customer.name +
          "for " +
          service.serviceName +
          "service with" +
          service.serviceCost
      );
    } else {
      console.error("Failed to send reminder");
    }
  }
  async function sendTerminationForCustomer(customer, service) {
    console.log("Working Termination function");
    const response = await fetch(
      `http://localhost:8080/api/dunning/terminationTo?customerId=${customer.id}&${service.id}`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({}),
      }
    );
    if (response.status === 200) {
      console.log(
        "Termination Mail Sent to " +
          customer.name +
          "for " +
          service.serviceName +
          "service with" +
          service.serviceCost
      );
    } else {
      console.error("Failed to send remainder");
    }
  }
  async function sendTermination() {
    console.log("Working Termination function");
    const response = await fetch(
      "http://localhost:8080/api/dunning/termination",
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({}),
      }
    );
    if (response.status === 200) {
      setShowSuccessMessage(true);
      console.log("Termination Mail Sent to all customers");
    } else {
      console.error("Failed to send remainder");
    }
  }

  const handleReminderButtons = () => {
    setShowReminderButtons(!showReminderButtons);
    if (showReminderDetails) {
      setShowReminderDetails(false);
    }
    if (showTerminationDetails) {
      setShowTerminationDetails(false);
    }
  };

  return (
    <div className="h-screen diagonal-background">
      <button
        className={`ml-4 mt-6 w-60 p-4 rounded-l-md rounded-r-3xl transition duration-300 font-sans font-semibold text-base ${
          showReminderButtons
            ? "bg-indigo-500 text-white hover:bg-indigo-600 focus:bg-indigo-400 focus:ring focus:ring-indigo-600"
            : "bg-white text-black hover:bg-gray-200 focus:bg-gray-300 focus:ring focus:ring-blue-600"
        }`}
        onClick={handleReminderButtons}
      >
        Reminder Buttons
      </button>
      <button
        className={`ml-4 mt-6 w-60 p-4 rounded-l-md rounded-r-3xl transition duration-300 font-sans font-semibold text-base ${
          showReminderDetails
            ? "bg-indigo-500 text-white hover:bg-indigo-600 focus:bg-indigo-400 focus:ring focus:ring-indigo-600"
            : "bg-white text-black hover:bg-gray-200 focus:bg-gray-300 focus:ring focus:ring-blue-600"
        }`}
        onClick={() => {
          setShowReminderDetails(!showReminderDetails);
          if (showTerminationDetails || showReminderButtons) {
            setShowTerminationDetails(false);
            setShowReminderButtons(false);
          }
        }}
      >
        Reminder Details
      </button>
      <button
        className={`ml-4 mt-6 p-4 w-60 rounded-l-md rounded-r-3xl transition duration-300 font-sans font-semibold text-base ${
          showTerminationDetails
            ? "bg-indigo-500 text-white hover:bg-indigo-600 focus:bg-indigo-400 focus:ring focus:ring-indigo-600"
            : "bg-white text-black hover:bg-gray-200 focus:bg-gray-300 focus:ring focus:ring-blue-600"
        }`}
        onClick={() => {
          setShowTerminationDetails(!showTerminationDetails);
          if (showReminderDetails || showReminderButtons) {
            setShowReminderDetails(false);
            setShowReminderButtons(false);
          }
        }}
      >
        Terminated Details
      </button>
      {showReminderButtons && (
        <div className="flex flex-col space-y-4 mt-4">
          <h1 className="font-bold text-lg text-black font-sans ml-8">
            Reminder emails are automatically scheduled for delivery. If manual
            sending is preferred, please use the following buttons.
          </h1>
          <div className="flex flex-col space-y-4 ml-8">
            <h1 className="font-medium text-black font-sans">
              Send Payment Reminders to all customers with Outstanding Balances.
            </h1>
            <div className="flex flex-row items-center space-x-2 ">
              <button
                className="border border-1 rounded-lg p-3 bg-indigo-700 hover:bg-indigo-500 text-white font-sans font-semibold "
                onClick={() => {
                  setPopupMessage("Initial Reminder sent successfully");
                  sendReminders("initial");
                }}
              >
                Send Initial Reminder
              </button>
              <PopupMessage
                show={showSuccessMessage}
                message={popupMessage}
                onClose={() => setShowSuccessMessage(false)}
              />
              <button
                className="border border-1 rounded-lg p-3 bg-indigo-700 hover:bg-indigo-500 text-white font-sans font-semibold "
                onClick={() => {
                  setPopupMessage("Follow Up Remainder sent successfully");
                  sendReminders("followup");
                }}
              >
                Send FollowUp Reminders
              </button>
              <PopupMessage
                show={showSuccessMessage}
                message={popupMessage}
                onClose={() => setShowSuccessMessage(false)}
              />
              <button
                className="border border-1 rounded-lg p-3 bg-indigo-700 hover:bg-indigo-500 text-white font-sans font-semibold "
                onClick={() => {
                  setPopupMessage("Final Notice sent successfully");
                  sendReminders("finalnotice");
                }}
              >
                Send Final Notice
              </button>
              <PopupMessage
                show={showSuccessMessage}
                message={popupMessage}
                onClose={() => setShowSuccessMessage(false)}
              />
            </div>
          </div>
          <div className="flex flex-row items-center space-x-2 ml-8">
            <h1 className="font-medium text-black font-sans">
              Send Termination Notices to customers who haven't responded to
              reminder Emails.
            </h1>
            <button
              className="border border-1 rounded-lg p-3 bg-red-700 hover:bg-red-500 text-white font-sans font-semibold "
              onClick={() => {
                setPopupMessage("Service Terminated Successfully");
                sendTermination();
              }}
            >
              Send Termination
            </button>
            <PopupMessage
              show={showSuccessMessage}
              message={popupMessage}
              onClose={() => setShowSuccessMessage(false)}
            />
          </div>
        </div>
      )}
      {showReminderDetails && (
        <div className=" h-screen m-4">
          <h1 className="text-2xl text-blue-800 font-bold ">
            Reminder Details
          </h1>
          {loading ? (
            <p>Loading...</p>
          ) : customers.length === 0 ? (
            <p>No customers found.</p>
          ) : (
            <table className="table w-full mt-4 rounded-lg">
              <thead>
                <tr className=" bg-blue-800 text-white">
                  <th className="border border-gray-400 px-4 py-2">
                    Name and Email
                  </th>
                  <th className="border border-gray-400 px-4 py-2">
                    Service Details
                  </th>
                  <th className="border border-gray-400 px-4 py-2">
                    Remainder Status
                  </th>
                  <th className="border border-gray-400 px-4 py-2">
                    Send Remainder
                  </th>
                </tr>
              </thead>
              <tbody className="bg-gray-200">
                {customers.map((customer, _id) => (
                  <tr key={customer.id} className="font-semibold">
                    {customer?.services[0]?.status === "Active" ? (
                      <>
                        <td className="border border-gray-400 px-4 py-2">
                          {customer?.name} - {customer?.email}
                        </td>
                        <td className="border border-gray-400 px-4 py-2">
                          <div>
                            {customer.services.map((service, serviceIndex) => (
                              <div key={serviceIndex}>
                                <p>Name: {service.serviceName}</p>
                                <p>Cost: {service.serviceCost}</p>
                                <p>Status: {service.status}</p>
                                {customer.invoices.map(
                                  (invoice, invoiceIndex) => (
                                    <div key={invoiceIndex}>
                                      <p>Due Date: {invoice.dueDate}</p>
                                    </div>
                                  )
                                )}
                                {serviceIndex <
                                  customer.services.length - 1 && (
                                  <div className="border-b border-gray-400 my-4" />
                                )}
                              </div>
                            ))}
                          </div>
                        </td>
                        <td className="border border-gray-400 px-4 py-2">
                          <div>
                            <div>
                              <p>
                                Initial Reminder :{" "}
                                {serviceSpecificRemainders[_id]?.initialReminder
                                  ? "YES"
                                  : "NO"}
                              </p>
                              <p>
                                Follow-Up Reminder Count :{" "}
                                {
                                  serviceSpecificRemainders[_id]
                                    ?.followUpReminderCount
                                }
                              </p>
                              <p>
                                Final Reminder :{" "}
                                {serviceSpecificRemainders[_id]?.finalReminder
                                  ? "YES"
                                  : "NO"}
                              </p>
                            </div>
                          </div>
                        </td>
                        <td className="border border-gray-400 px-4 py-2">
                          <div>
                            {customer.services.map((service, serviceIndex) => (
                              <div
                                key={serviceIndex}
                                className="flex flex-col "
                              >
                                <button
                                  className="border border-1 h-10 w-auto rounded-lg bg-indigo-700 hover:bg-indigo-500 text-white font-sans font-semibold "
                                  onClick={() =>
                                    sendReminderForCustomer(customer, service)
                                  }
                                >
                                  Send Reminder
                                </button>
                                <button
                                  className="border border-1 h-10 w-auto rounded-lg bg-red-700 hover:bg-red-500 text-white font-sans font-semibold "
                                  onClick={() =>
                                    sendTerminationForCustomer(
                                      customer,
                                      service
                                    )
                                  }
                                >
                                  Send Termination
                                </button>
                                {serviceIndex <
                                  customer.services.length - 1 && (
                                  <div className="border-b border-gray-400 my-4" />
                                )}
                              </div>
                            ))}
                          </div>
                        </td>
                      </>
                    ) : null}
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}

      {showTerminationDetails && (
        <div className=" h-screen m-4">
          <h1 className="text-2xl text-blue-800 font-bold ">
            Terminated Details
          </h1>
          {loading ? (
            <p>Loading...</p>
          ) : customers.length === 0 ? (
            <p>No customers found.</p>
          ) : (
            <table className="table w-full mt-4 rounded-lg">
              <thead>
                <tr className=" bg-blue-800 text-white">
                  <th className="border border-gray-400 px-4 py-2">
                    Name and Email
                  </th>
                  <th className="border border-gray-400 px-4 py-2">
                    Service Details
                  </th>
                </tr>
              </thead>
              <tbody className="bg-gray-200">
                {customers.map((customer) => (
                  <tr key={customer.id} className="font-semibold">
                    {customer?.services[0]?.status === "Terminated" ? (
                      <>
                        <td className="border border-gray-400 px-4 py-2">
                          {customer?.name} - {customer?.email}
                        </td>
                        <td className="border border-gray-400 px-4 py-2">
                          <div>
                            {customer.services.map((service, serviceIndex) => (
                              <div key={serviceIndex}>
                                <p>Name: {service.serviceName}</p>
                                <p>Cost: {service.serviceCost}</p>
                                <p>Status: {service.status}</p>
                                {customer.invoices.map(
                                  (invoice, invoiceIndex) => (
                                    <div key={invoiceIndex}>
                                      <p>Due Date: {invoice.dueDate}</p>
                                    </div>
                                  )
                                )}
                                {serviceIndex <
                                  customer.services.length - 1 && (
                                  <div className="border-b border-gray-400 my-4" />
                                )}
                              </div>
                            ))}
                          </div>
                        </td>
                      </>
                    ) : null}
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}
    </div>
  );
};

export default Dunning;
