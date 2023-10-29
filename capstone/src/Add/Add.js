/**
 * The `Add` component is a React component that renders a multi-step form for adding customer,
 * service, and invoice data, and navigates to the "/customer" page after submitting the invoice data.
 * @returns The `Add` component is returning a JSX element that contains conditional rendering of three
 * forms: `CustomerForm`, `ServiceForm`, and `InvoiceForm`. The forms are rendered based on the values
 * of the `showCustomerForm`, `showServiceForm`, and `showInvoiceForm` state variables. The
 * `CustomerForm` is shown when `showCustomerForm` is true
 */
import React, { useState } from "react";
import CustomerForm from "./CustomerForm";
import ServiceForm from "./ServiceForm";
import InvoiceForm from "./InvoiceForm";
import { useNavigate } from "react-router-dom";

function Add() {
  const [showCustomerForm, setShowCustomerForm] = useState(true);
  const [showServiceForm, setShowServiceForm] = useState(false);
  const [showInvoiceForm, setShowInvoiceForm] = useState(false);
  const [customerData, setCustomerData] = useState({});
  const [serviceData, setServiceData] = useState({});
  const [invoiceData, setInvoiceData] = useState({});
  const navigate = useNavigate();

  /**
   * The function `handleCustomerSubmit` sets the customer data, hides the customer form, and shows the
   * service form.
   * @param data - The "data" parameter is the information submitted by the customer in the customer
   * form. It could include details such as the customer's name, address, contact information, and any
   * other relevant information.
   */
  const handleCustomerSubmit = (data) => {
    setCustomerData(data);
    setShowCustomerForm(false);
    setShowServiceForm(true);
  };

  /**
   * The function `handleServiceSubmit` sets the service data, hides the service form, and shows the
   * invoice form.
   * @param data - The `data` parameter is an object that contains the information submitted in the
   * service form.
   */
  const handleServiceSubmit = (data) => {
    setServiceData(data);
    setShowServiceForm(false);
    setShowInvoiceForm(true);
  };

  /**
   * The function `handleInvoiceSubmit` sets the invoice data and navigates to the "/customer" page.
   * @param data - The `data` parameter is an object that contains the invoice data.
   */
  const handleInvoiceSubmit = (data) => {
    setInvoiceData(data);
    navigate("/customer");
  };

  return (
    <div>
      {showCustomerForm && <CustomerForm onSubmit={handleCustomerSubmit} />}
      {showServiceForm && (
        <ServiceForm
          onSubmitService={handleServiceSubmit}
          customerData={customerData}
        />
      )}
      {showInvoiceForm && (
        <InvoiceForm
          onSubmitInvoice={handleInvoiceSubmit}
          customerData={customerData}
          serviceData={serviceData}
        />
      )}
    </div>
  );
}
export default Add;
