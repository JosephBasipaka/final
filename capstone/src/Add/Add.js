import React, { useState } from 'react';
import CustomerForm from './CustomerForm';
import ServiceForm from './ServiceForm';
import InvoiceForm from './InvoiceForm';



function Add() {
  const [showCustomerForm, setShowCustomerForm] = useState(true);
  const [showServiceForm, setShowServiceForm] = useState(false);
  const [showInvoiceForm, setShowInvoiceForm] = useState(false);
  const [customerData, setCustomerData] = useState({});
  const [serviceData, setServiceData] = useState({});
  const [invoiceData, setInvoiceData] = useState({});

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
  };


  return (
    <div>
      {showCustomerForm && <CustomerForm onSubmit={handleCustomerSubmit} />}
      {showServiceForm && (
        <ServiceForm 
        onSubmitService={handleServiceSubmit}
        customerData={customerData} />
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
