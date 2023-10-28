import React, { useState } from "react";
function ServiceForm({ onSubmitService, customerData }) {
  const [selectedPlan, setSelectedPlan] = useState("");
  const [amount, setAmount] = useState(0);

  const planOptions = [
    { name: "Basic", amount: 1000 },
    { name: "Standard", amount: 2000 },
    { name: "Premium", amount: 5000 },
  ];

  const handlePlanChange = (e) => {
    const selectedOption = e.target.value;
    setSelectedPlan(selectedOption);
    const selectedPlanData = planOptions.find(
      (plan) => plan.name === selectedOption
    );
    if (selectedPlanData) {
      setAmount(selectedPlanData.amount);
    }
  };

  const handleSubmitService = async (e) => {
    e.preventDefault();
    let resCustomer;
    const responseCustomer = await fetch(
      `http://localhost:8080/api/customer/id?name=${customerData.name}&email=${customerData.email}`
    );
    if (responseCustomer.status === 200) {
      resCustomer = await responseCustomer.json();
    }
    let customer = resCustomer;
    let serviceName = selectedPlan;
    let serviceCost = amount;
    let status = "Active";
    console.log(
      "In service " +
        "amount" +
        serviceCost +
        "and" +
        amount +
        "serviceName" +
        serviceName +
        "and" +
        selectedPlan +
        "customerId" +
        customer +
        "Customer_name" +
        customerData.name +
        "Customer_email" +
        customerData.email
    );
    const response = await fetch("http://localhost:8080/api/addService", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ serviceName, serviceCost, status, customer }),
    });
    if (response.status === 201) {
      console.log("Service added successfully");
    } else {
      console.error("Failed to add service");
    }
    const serviceData = { selectedPlan, amount, customer };
    onSubmitService(serviceData, customerData);
  };

  return (
    <div className="flex items-center w-full mx-auto h-screen diagonal-background">
      <form
        onSubmit={handleSubmitService}
        className="grid place-items-center lg:w-5/12 sm:w-8/12 w-11/12 mx-auto bg-white text-[#4f7cff] shadow-2xl rounded-3xl"
      >
        <label className="pt-8 pb-4 text-3xl font-bold capitalize">
          Add Service Details
        </label>
        <div className="w-full flex flex-col px-14 py-4">
          <label>Service Plan:</label>
          <select
            className="w-full border border-gray-300 rounded-lg px-3 py-3 mt-1 text-lg outline-none"
            value={selectedPlan}
            onChange={handlePlanChange}
            required
          >
            <option value="">Select a plan</option>
            {planOptions.map((planOption) => (
              <option key={planOption.name} value={planOption.name}>
                {planOption.name}
              </option>
            ))}
          </select>
        </div>
        <div className="w-full flex flex-col px-14 py-4">
          <label>Amount:</label>
          <input
            className="w-full border border-gray-300 rounded-lg px-3 py-3 mt-1 text-lg outline-none"
            type="number"
            value={amount}
            readOnly
          />
        </div>
        <div className="mx-auto flex justify-center items-center pb-8">
          <button
            className="bg-[#3d5fc4] text-white rounded-md text-base uppercase w-fit p-2 hover:bg-blue-800"
            type="submit"
          >
            Submit Service
          </button>
        </div>
      </form>
    </div>
  );
}

export default ServiceForm;
