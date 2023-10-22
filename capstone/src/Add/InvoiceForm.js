import React, { useState } from "react";
function InvoiceForm({onSubmitInvoice, customerData, serviceData }) {
    const [invoiceAmount, setInvoiceAmount] = useState(0);
    const [dueDate, setDueDate] = useState(null);
    const [selectedValue, setSelectedValue] = useState('');
    const [paid, setPaid] = useState(false);
  
    const handleSubmitInvoice = async (e) => {
      e.preventDefault();
      let resCustomer;
      const responseCustomer = await fetch(`http://localhost:8080/api/customer/id?name=${customerData.name}&email=${customerData.email}`);
      if (responseCustomer.status === 200) {
          resCustomer= await responseCustomer.json();
      }
      let resService;
      console.log("In invoice " + "customerId" + resCustomer.id + " " + resCustomer +  "Customer_name" + customerData.name + "Customer_email" + customerData.email + "Service" + serviceData.selectedPlan + " And " + serviceData.amount)
      const responseService = await fetch(`http://localhost:8080/api/service/id?customerId=${resCustomer.id}&serviceName=${serviceData.selectedPlan}`);
      if (responseService.status === 200) {
        resService= await responseService.json();
      }
      let service = resService;
      let customer = resCustomer;
      let amount = invoiceAmount;
      console.log("In invoice " + "amount" + amount + "dueDate" + dueDate + "customerId" + customer +  "Customer_name" + customerData.name + "Customer_email" + customerData.email + "Service" + serviceData.serviceName + " And " + serviceData.serviceCost)
      const response = await fetch('http://localhost:8080/api/addInvoice', {
      method: 'POST',
      headers: {
          'Content-Type': 'application/json',
      },
      body: JSON.stringify({ amount, dueDate, paid ,customer, service}),
      });
      if (response.status === 201) {
          console.log('Invoice added successfully');
        } else {
          console.error('Failed to add invoice');
        }
      const invoiceData = { invoiceAmount, dueDate, paid ,customer, service};
      onSubmitInvoice(invoiceData, customerData, serviceData );
      };
      const handleDueDateChange = (event) => {
        const newValue = event.target.value;
        setSelectedValue(newValue);
    
        if (newValue === '15' || newValue === '30') {
          const currentDate = new Date();
          currentDate.setDate(currentDate.getDate() + parseInt(newValue, 10));
          const year = currentDate.getFullYear();
          const month = String(currentDate.getMonth() + 1).padStart(2, '0');
          const day = String(currentDate.getDate()).padStart(2, '0');
        const formattedDate = `${year}-${month}-${day}`;
        
        setDueDate(formattedDate);
        } else {
          setDueDate(null);
        }
      };
    return (
      <form onSubmit={handleSubmitInvoice} className='flex flex-col rounded-md items-center max-w-md mx-auto mt-20 pt-4 h-60 w-80 space-y-4 bg-gray-400'>
        <label className='font-bold text-lg'>Add Invoice Details</label>
        <div className='flex space-x-4'>
        <label className='font-bold font-sans text-lg'>Amount:</label>
        <input className='rounded-md pl-2'type="number" value={invoiceAmount} onChange={(e) => setInvoiceAmount(e.target.value)} max={5000} min={1} required/>
        </div>
        <div>
        <label className='font-bold font-sans text-lg'>Select a due date to pay:</label>
        <select value={selectedValue} onChange={handleDueDateChange} required>
          <option value="15">15 days</option>
          <option value="30">30 days</option>
        </select>
        </div>
        <button className='border border-1 rounded-lg p-3 bg-indigo-700 hover:bg-indigo-500 text-white font-sans font-semibold'type="submit">Submit Invoice</button>
      </form>
    );
  }

  export default InvoiceForm;