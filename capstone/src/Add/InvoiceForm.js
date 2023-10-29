/**
 * The `InvoiceForm` component is a form that allows users to submit an invoice with additional details
 * such as the amount to pay and the due date.
 * @returns The InvoiceForm component is being returned.
 */
import React, { useState } from "react";
function InvoiceForm({ onSubmitInvoice, customerData, serviceData }) {
  const [invoiceAmount, setInvoiceAmount] = useState(0);
  const [dueDate, setDueDate] = useState(null);
  const [selectedValue, setSelectedValue] = useState("");
  const [paid, setPaid] = useState(false);

  /**
   * The function handleSubmitInvoice is an asynchronous function that handles the submission of an
   * invoice by making API calls to fetch customer and service data, and then adds the invoice to the
   * database.
   * @param e - The parameter `e` is an event object that represents the event that triggered the
   * function. In this case, it is likely an event object from a form submission.
   */
  const handleSubmitInvoice = async (e) => {
    e.preventDefault();
    let resCustomer;
    const responseCustomer = await fetch(
      `http://localhost:8080/api/customer/id?name=${customerData.name}&email=${customerData.email}`
    );
    if (responseCustomer.status === 200) {
      resCustomer = await responseCustomer.json();
    }
    let resService;
    console.log(
      "In invoice " +
        "customerId" +
        resCustomer.id +
        " " +
        resCustomer +
        "Customer_name" +
        customerData.name +
        "Customer_email" +
        customerData.email +
        "Service" +
        serviceData.selectedPlan +
        " And " +
        serviceData.amount
    );
    const responseService = await fetch(
      `http://localhost:8080/api/service/id?customerId=${resCustomer.id}&serviceName=${serviceData.selectedPlan}`
    );
    if (responseService.status === 200) {
      resService = await responseService.json();
    }
    let service = resService;
    let customer = resCustomer;
    let amount = invoiceAmount;
    const response = await fetch("http://localhost:8080/api/addInvoice", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ amount, dueDate, paid, customer, service }),
    });
    if (response.status === 201) {
      console.log("Invoice added successfully");
    } else {
      console.error("Failed to add invoice");
    }
    const invoiceData = { invoiceAmount, dueDate, paid, customer, service };
    onSubmitInvoice(invoiceData, customerData, serviceData);
  };
  /**
   * The function `handleDueDateChange` updates the due date based on the selected value from an event.
   * @param event - The `event` parameter is an object that represents the event that triggered the
   * function. It contains information about the event, such as the target element that triggered the
   * event and the value of the target element. In this case, it is used to get the new value of the
   * target element.
   */
  const handleDueDateChange = (event) => {
    const newValue = event.target.value;
    setSelectedValue(newValue);

    if (newValue === "15" || newValue === "30") {
      const currentDate = new Date();
      currentDate.setDate(currentDate.getDate() + parseInt(newValue, 10));
      const year = currentDate.getFullYear();
      const month = String(currentDate.getMonth() + 1).padStart(2, "0");
      const day = String(currentDate.getDate()).padStart(2, "0");
      const formattedDate = `${year}-${month}-${day}`;

      setDueDate(formattedDate);
    } else {
      setDueDate(null);
    }
  };
  return (
    <div className="flex items-center w-full mx-auto h-screen diagonal-background">
      <form
        onSubmit={handleSubmitInvoice}
        className="grid place-items-center lg:w-5/12 sm:w-8/12 w-11/12 mx-auto bg-white text-[#4f7cff] shadow-2xl rounded-3xl"
      >
        <label className="pt-8 pb-4 text-3xl font-bold capitalize">
          Additional Details
        </label>
        <div className="w-full flex flex-col px-14 py-4">
          <label className="font-bold text-lg">
            Amount Planning to Pay now:
          </label>
          <input
            className="w-full border border-gray-300 rounded-lg px-3 py-3 mt-1 text-lg outline-none"
            type="number"
            value={invoiceAmount}
            onChange={(e) => setInvoiceAmount(e.target.value)}
            max={serviceData.amount}
            min={1}
            required
          />
        </div>
        <div className="w-full flex flex-col px-14 py-4">
          <label className="font-bold text-lg">Select a due date to pay:</label>
          <select value={selectedValue} onChange={handleDueDateChange} required>
            <option value="15">15 days</option>
            <option value="30">30 days</option>
          </select>
        </div>
        <div className="mx-auto flex justify-center items-center pb-8">
          <button
            className="bg-[#3d5fc4] text-white rounded-md text-base uppercase w-fit p-2 hover:bg-blue-800"
            type="submit"
          >
            Submit Invoice
          </button>
        </div>
      </form>
    </div>
  );
}

export default InvoiceForm;
