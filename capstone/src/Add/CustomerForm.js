import React, { useState } from 'react';
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
      <form onSubmit={handleSubmitCustomer} className='flex flex-col rounded-md items-center max-w-md mx-auto mt-20 pt-4 h-60 w-80 space-y-4 bg-gray-400'>
        <label className='font-bold text-lg'>Add Customer Details</label>
        <div className='flex space-x-4'>
          <label className='font-bold font-sans text-lg'>Name:</label>
          <input className='rounded-md pl-2' type="text" value={name} onChange={(e) => setName(e.target.value)} required/>
        </div>
        <div className='flex space-x-4'>
        <label className='font-bold font-sans text-lg'>Email:</label>
        <input className='rounded-md pl-2' type="email" value={email} onChange={(e) => setEmail(e.target.value)} required/>
        </div>
        <button className='border border-1 rounded-lg p-3 bg-indigo-700 hover:bg-indigo-500 text-white font-sans font-semibold' type="submit">Submit Customer</button>
      </form>
  
    );
  }

  export default CustomerForm;