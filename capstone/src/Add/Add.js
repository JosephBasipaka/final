import React, { useState } from "react";
import CustomerForm from "./CustomerForm";
import ServiceForm from "./ServiceForm";
import InvoiceForm from "./InvoiceForm";
import { useNavigate } from "react-router-dom";

// function HandleInvoiceSubmit({ data, setInvoiceData, serviceData}) {
//   // const [invoiceData, setInvoiceData] = useState({});
//   const navigate = useNavigate();

//   const handleSubmit = () => {
//     setInvoiceData(data);
//     navigate('/success');
//   };

//   return <InvoiceForm onSubmitInvoice={handleSubmit} customerData={data} serviceData={serviceData}/>;
// }

function Add() {
  const [showCustomerForm, setShowCustomerForm] = useState(true);
  const [showServiceForm, setShowServiceForm] = useState(false);
  const [showInvoiceForm, setShowInvoiceForm] = useState(false);
  const [customerData, setCustomerData] = useState({});
  const [serviceData, setServiceData] = useState({});
  const [invoiceData, setInvoiceData] = useState({});
  const navigate = useNavigate();

  const handleCustomerSubmit = (data) => {
    setCustomerData(data);
    setShowCustomerForm(false);
    setShowServiceForm(true);
  };

  const handleServiceSubmit = (data) => {
    setServiceData(data);
    setShowServiceForm(false);
    setShowInvoiceForm(true);
  };

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
        //  <HandleInvoiceSubmit customerData={customerData} serviceData = {serviceData} setInvoiceData={setInvoiceData} />
      )}
    </div>
  );
}
export default Add;
