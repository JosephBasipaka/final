import React, { useState } from 'react';
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
    let resCustomer;
    const responseCustomer = await fetch(`http://localhost:8080/api/customer/id?name=${customerData.name}&email=${customerData.email}`);
    if (responseCustomer.status === 200) {
        resCustomer = await responseCustomer.json();
    }
    let customer = resCustomer;
    let serviceName = selectedPlan;
    let serviceCost = amount;
    let status = "Active";
    console.log("In service " + "amount" + serviceCost + "and" + amount + "serviceName" + serviceName + "and" + selectedPlan + "customerId" + customer + "Customer_name" + customerData.name + "Customer_email" + customerData.email)
    const response = await fetch('http://localhost:8080/api/addService', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
    },
    body: JSON.stringify({ serviceName , serviceCost ,status, customer}),
    });
    if (response.status === 201) {
        console.log('Service added successfully');
      } else {
        console.error('Failed to add service');
    }
    const serviceData = { selectedPlan, amount ,customer};
    onSubmitService(serviceData, customerData );
    };

  return (
    <form onSubmit={handleSubmitService} className='flex flex-col rounded-md items-center max-w-md mx-auto mt-20 pt-4 h-60 w-80 space-y-4 bg-gray-400'>
      <label className='font-bold text-lg'>Add Service Details</label>
      <div className='flex space-x-4'>
      <label className='font-bold font-sans text-lg'>Service Plan:</label>
      <select className='rounded-md pl-2' value={selectedPlan} onChange={handlePlanChange} required>
        <option value="">Select a plan</option>
        {planOptions.map((planOption) => (
          <option key={planOption.name} value={planOption.name} >
            {planOption.name}
          </option>
        ))}
      </select>
      </div>
      <div className='flex space-x-4'>
        <label className='font-bold font-sans text-lg'>Amount:</label>
        <input className='rounded-md pl-2' type="number" value={amount} readOnly/>
      </div>
      <button className='border border-1 rounded-lg p-3 bg-indigo-700 hover:bg-indigo-500 text-white font-sans font-semibold' type="submit">Submit Service</button>
    </form>
  );
}

export default ServiceForm;