import React, { useEffect, useState } from 'react';
import { fetchCustomers, fetchServices } from '../api';

const Dunning = () => {
  const [customers, setCustomers] = useState([]);
  const [showReminderDetails, setShowReminderDetails] = useState(false);
  const [showTerminationDetails, setShowTerminationDetails] = useState(false);
  const [services, setServices] = useState([]);
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    fetchCustomers()
      .then((response) => {
        console.log(response.data)
        setCustomers(response.data);
        setLoading(false);
      })
      .catch((error) => {
        console.error('Error fetching data:', error);
        setLoading(false);
      });
      fetchServices()
      .then((response) => {
        console.log(response.data)
        setServices(response.data);
        setLoading(false);
      })
      .catch((error) => {
        console.error('Error fetching data:', error);
        setLoading(false);
      });
    },[]);
  
  async function sendRemainders() {
    console.log("Working Remainder function");
    const response = await fetch('http://localhost:8080/api/dunning/intReminder', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
    },
    body: JSON.stringify({}),
    });
    if (response.status === 201) {
        console.log('Remainder Mail Sent');
      } else {
        console.error('Failed to send remainder');
    }
  }
  return (
      <>
      <div className='flex flex-col space-y-4 mt-4'>
      <div className='flex flex-row items-center space-x-2 ml-8'>
        <h1 className='font-medium text-black font-sans'>Send Reminders to all the Customers with Due Payments</h1>
        <button className = "border border-1 rounded-lg p-3 bg-indigo-700 hover:bg-indigo-500 text-white font-sans font-semibold " onClick={() => sendRemainders()}>Send</button>
      </div>
      <div className='flex flex-row items-center space-x-2 ml-8'>
        <h1 className='font-medium text-black font-sans'>Send Termination to customers who doesnot responded to remainder mails</h1>
        <button className = "border border-1 rounded-lg p-3 bg-red-700 hover:bg-red-500 text-white font-sans font-semibold " onClick={() => sendRemainders()}>Send</button>
      </div>
      </div>
      <button className = {`ml-4 mt-6 p-4 rounded-l-md rounded-r-3xl transition duration-300 font-sans font-semibold text-base ${
          showReminderDetails
            ? 'bg-indigo-500 text-white hover:bg-indigo-600 focus:bg-indigo-400 focus:ring focus:ring-indigo-600'
            : 'bg-slate-400 text-black hover:bg-gray-700 focus:bg-gray-400 focus:ring focus:ring-gray-600'}`} 
            onClick={() => {
              setShowReminderDetails(!showReminderDetails);
              if (showTerminationDetails) {
                setShowTerminationDetails(false);
              }
            }}>
            Reminder Details
      </button>

      <button className = {`ml-4 mt-6 p-4 rounded-l-md rounded-r-3xl transition duration-300 font-sans font-semibold text-base ${
          showTerminationDetails
            ? 'bg-indigo-500 text-white hover:bg-indigo-600 focus:bg-indigo-400 focus:ring focus:ring-indigo-600'
            : 'bg-slate-400 text-black hover:bg-gray-700 focus:bg-gray-400 focus:ring focus:ring-gray-600'}`} 
            onClick={() => {
              setShowTerminationDetails(!showTerminationDetails);
              if (showReminderDetails) {
                setShowReminderDetails(false);
              }
            }}>
            Terminated Details
      </button>

    {showReminderDetails && (
    <div className=" h-screen m-4">
  <h1 className="text-2xl text-blue-800 font-bold ">Reminder Details</h1>
  {loading ? (
    <p>Loading...</p>
  ) : customers.length === 0 ? (
    <p>No customers found.</p>
  ) : (
    <table className="table w-full mt-4 rounded-lg">
      <thead>
        <tr className=' bg-blue-800 text-white'>
          <th className="border border-gray-400 px-4 py-2">Name and Email</th>
          <th className="border border-gray-400 px-4 py-2">Service Details</th>
          <th className="border border-gray-400 px-4 py-2">Remainder Status</th>
          <th className="border border-gray-400 px-4 py-2">Send Remainder</th>
        </tr>
      </thead>
      <tbody className='bg-gray-200'>
        {customers.map((customer) => (
          <tr key={customer.id} className='font-semibold'>
          {customer?.services[0]?.status === 'Active' ? (
            <>
            <td className="border border-gray-400 px-4 py-2">{customer?.name} - {customer?.email}</td>
            <td className="border border-gray-400 px-4 py-2">
            <div >
              {customer.services.map((service, serviceIndex) => (
                <div key={serviceIndex} >
                  <p>Name: {service.serviceName}</p>
                  <p>Cost: {service.serviceCost}</p>
                  <p>Status: {service.status}</p>
                  {/* {customer.invoices.map((invoice, invoiceIndex) => (
                    <div key={invoiceIndex}>
                      <p>Due Date: {invoice.dueDate}</p>
                    </div>
                  ))} */}
                  {serviceIndex < customer.services.length - 1 && (
                    <div className="border-b border-gray-400 my-4" />
                  )}
                </div>
              
              ))}
            </div>
            </td>
            <td className="border border-gray-400 px-4 py-2">
              <div>
              {customer.services.map((service, serviceIndex) => (
                <div key = {serviceIndex}>
                  <p>Initial Remainder : </p>
                  <p>Follow-Up Remainder Count : </p>
                  <p>Final Remainder : </p>
                  {serviceIndex < customer.services.length - 1 && (
                    <div className="border-b border-gray-400 my-4" />
                  )}
                </div>
              ))}
              </div>
            </td>
            <td className="border border-gray-400 px-4 py-2">
            <div>
              {customer.services.map((service, serviceIndex) => (
              <div key = {serviceIndex} className='flex flex-col '>
              <button className = "border border-1 h-10 w-auto rounded-lg bg-indigo-700 hover:bg-indigo-500 text-white font-sans font-semibold " onClick={() => sendRemainders()}>Send Reminder</button>
              <button className = "border border-1 h-10 w-auto rounded-lg bg-red-700 hover:bg-red-500 text-white font-sans font-semibold " onClick={() => sendRemainders()}>Send Termination</button>
              {serviceIndex < customer.services.length - 1 && (
                    <div className="border-b border-gray-400 my-4" />
                  )}
              </div>
              ))}
              </div>
            </td>
            </>
          ): null }
          </tr>
        ))}
      </tbody>
    </table>
  )}
  </div> )}

  {showTerminationDetails && (
    <div className=" h-screen m-4">
  <h1 className="text-2xl text-blue-800 font-bold ">Terminated Details</h1>
  {loading ? (
    <p>Loading...</p>
  ) : customers.length === 0 ? (
    <p>No customers found.</p>
  ) : (
    <table className="table w-full mt-4 rounded-lg">
      <thead>
        <tr className=' bg-blue-800 text-white'>
          <th className="border border-gray-400 px-4 py-2">Name and Email</th>
          <th className="border border-gray-400 px-4 py-2">Service Details</th>
        </tr>
      </thead>
      <tbody className='bg-gray-200'>
        {customers.map((customer) => (
          <tr key={customer.id} className='font-semibold'>
          {customer?.services[0]?.status === 'Terminated' ? (
            <>
            <td className="border border-gray-400 px-4 py-2">{customer?.name} - {customer?.email}</td>
            <td className="border border-gray-400 px-4 py-2">
            <div >
              {customer.services.map((service, serviceIndex) => (
                <div key={serviceIndex} >
                  <p>Name: {service.serviceName}</p>
                  <p>Cost: {service.serviceCost}</p>
                  <p>Status: {service.status}</p>
                  {/* {customer.invoices.map((invoice, invoiceIndex) => (
                    <div key={invoiceIndex}>
                      <p>Due Date: {invoice.dueDate}</p>
                    </div>
                  ))} */}
                  {serviceIndex < customer.services.length - 1 && (
                    <div className="border-b border-gray-400 my-4" />
                  )}
                </div>
              
              ))}
            </div>
            </td>
            </>
          ): null }
          </tr>
        ))}
      </tbody>
    </table>
  )}
  </div> )}
      </>
  )
};

export default Dunning;
