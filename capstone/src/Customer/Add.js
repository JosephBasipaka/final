import React, { useState } from 'react';
// import { unstable_HistoryRouter } from 'react-router-dom';

function Add() {
  const [showCustomerForm, setShowCustomerForm] = useState(true);
  const [showServiceForm, setShowServiceForm] = useState(false);
  const [showInvoiceForm, setShowInvoiceForm] = useState(false);
  const [customerData, setCustomerData] = useState({});
  const [serviceData, setServiceData] = useState({});
  const [invoiceData, setInvoiceData] = useState({});
//   const history = unstable_HistoryRouter();

  const handleCustomerSubmit = (data) => {
    console.log("came here");
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
    // history.push('/success');
  };

  return (
    <div>
      {showCustomerForm ? (
        <CustomerForm onSubmit={handleCustomerSubmit} />
      ) : showServiceForm ? (
        <ServiceForm onSubmitService={handleServiceSubmit} customerData={customerData}  />
      ) : showInvoiceForm ? (
        <InvoiceForm onSubmitInvoice={handleInvoiceSubmit} customerData={customerData}  />
        ): null}
    </div>
  );
}

function CustomerForm({onSubmit}) {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');

  const handleSubmitCustomer = async (e) => {
    e.preventDefault();
    const response = await fetch('http://localhost:8080/api/addCustomer', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
    },
    body: JSON.stringify({ name, email }),
    });
    if (response.status === 201) {
        console.log('Customer added successfully');
      } else {
        console.error('Failed to add customer');
      }
      const customerData = { name, email };
      onSubmit(customerData);
    };

  return (
    <form onSubmit={handleSubmitCustomer}>
      <label>Name:</label>
      <input type="text" value={name} onChange={(e) => setName(e.target.value)} />
      <label>Email:</label>
      <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
      <button type="submit">Submit Customer</button>
    </form>
  );
}

function ServiceForm({onSubmitService, customerData }) {
    const [selectedPlan, setSelectedPlan] = useState('');
    const [amount, setAmount] = useState(0);

  const planOptions = [
    { name: 'Basic', amount: 1000 },
    { name: 'Standard', amount: 2000 },
    { name: 'Premium', amount: 5000 },
  ];

  const handlePlanChange = (e) => {
    const selectedOption = e.target.value;
    setSelectedPlan(selectedOption);
    const selectedPlanData = planOptions.find((plan) => plan.name === selectedOption);
    if (selectedPlanData) {
      setAmount(selectedPlanData.amount);
    }
  };

  const handleSubmitService = async (e) => {
    e.preventDefault();
    let customer_id = 0;
    const responseCustomerId = await fetch(`http://localhost:8080/api/customer/id?name=${customerData.name}&email=${customerData.email}`);
    if (responseCustomerId.status === 200) {
        customer_id = await responseCustomerId.json();
    }
    let serviceName = selectedPlan;
    let serviceCost = amount;
    let status = "Active";
    console.log("In service " + "amount" + serviceCost + "and" + amount + "serviceName" + serviceName + "and" + selectedPlan + "customerId" + customer_id + "Customer_name" + customerData.name + "Customer_email" + customerData.email)
    const response = await fetch('http://localhost:8080/api/addService', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
    },
    body: JSON.stringify({ serviceName , serviceCost ,status, customer_id}),
    });
    if (response.status === 201) {
        console.log('Service added successfully');
      } else {
        console.error('Failed to add service');
    }
    const serviceData = { selectedPlan, amount ,customer_id};
    onSubmitService(serviceData, customerData );
    };

  return (
    <form onSubmit={handleSubmitService}>
      <label>Service Plan:</label>
      <select value={selectedPlan} onChange={handlePlanChange}>
        <option value="">Select a plan</option>
        {planOptions.map((planOption) => (
          <option key={planOption.name} value={planOption.name}>
            {planOption.name}
          </option>
        ))}
      </select>
      <label>Amount:</label>
      <input type="number" value={amount} readOnly />
      <button type="submit">Submit Service</button>
    </form>
  );
}

function InvoiceForm({onSubmitInvoice, customerData }) {
  const [invoiceAmount, setInvoiceAmount] = useState(0);
  const [dueDate, setDueDate] = useState('');
  const [paid, setPaid] = useState(false);

  const handleSubmitInvoice = async (e) => {
    e.preventDefault();
    let customer_id = 0;
    const responseCustomerId = await fetch(`http://localhost:8080/api/customer/id?name=${customerData.name}&email=${customerData.email}`);
    if (responseCustomerId.status === 200) {
        customer_id = await responseCustomerId.json();
    }
    let amount = invoiceAmount;
    console.log("In invoice " + "amount" + amount + "dueDate" + dueDate + "customerId" + customer_id + "Customer_name" + customerData.name + "Customer_email" + customerData.email)
    const response = await fetch('http://localhost:8080/api/addInvoice', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
    },
    body: JSON.stringify({ amount, dueDate, paid ,customer_id}),
    });
    if (response.status === 201) {
        console.log('Invoice added successfully');
      } else {
        console.error('Failed to add invoice');
      }
    const invoiceData = { invoiceAmount, dueDate, paid ,customer_id};
    onSubmitInvoice(invoiceData, customerData );
    };

  return (
    <form onSubmit={handleSubmitInvoice}>
      <label>Amount:</label>
      <input type="number" value={invoiceAmount} onChange={(e) => setInvoiceAmount(e.target.value)} />
      <label>Due Date:</label>
      <input type="date" value={dueDate} onChange={(e) => setDueDate(e.target.value)} />
      <button type="submit">Submit Invoice</button>
    </form>
  );
}

export default Add;
