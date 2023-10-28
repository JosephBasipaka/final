import React, { useState } from "react";
import ServiceCard from "./ServiceCard";
import PaymentPlanForm from "./PaymentPlanForm";
import PaymentPlan from "./PaymentPlan";
import { useNavigate } from "react-router-dom";

function Payment() {
  const [name, setName] = useState("");
  const [nameValid, setNameValid] = useState(true);
  const nameRegex = /^[A-Za-z]+(?: [A-Za-z]+)*$/;
  const [selectedPlan, setSelectedPlan] = useState("");
  const [services, setServices] = useState(null);
  const [showPaymentPlanForm, setShowPaymentPlanForm] = useState(false);
  const [paymentPlans, setPaymentPlans] = useState(null);
  const navigate = useNavigate();

  const planOptions = [
    { name: "Basic", amount: 1000 },
    { name: "Standard", amount: 2000 },
    { name: "Premium", amount: 5000 },
  ];

  const handlePlanChange = (e) => {
    const selectedOption = e.target.value;
    setSelectedPlan(selectedOption);
  };

  const handleCreatePaymentPlanClick = () => {
    fetchPaymentPlans(services.id).then((result) => {
      if (!result) {
        setShowPaymentPlanForm(true);
      }
    });
  };

  const fetchServices = async () => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/find?customerName=${name}&serviceName=${selectedPlan}`
      );
      if (response.status === 200) {
        const service = await response.json();
        console.log(service);
        setServices(service);
      } else {
        setServices(null);
      }
    } catch (error) {
      setServices(null);
      console.error(error);
    }
  };

  const fetchPaymentPlans = async (customerId) => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/curing/paymentPlansByCustomer/${customerId}`
      );
      if (response.status === 200) {
        const responseText = await response.text();

        if (responseText) {
          const paymentPlan = JSON.parse(responseText);
          console.log(paymentPlan);
          setPaymentPlans(paymentPlan);
        } else {
          setPaymentPlans(null);
        }
      } else {
        setPaymentPlans(null);
      }
    } catch (error) {
      setPaymentPlans(null);
      console.error(error);
    }
  };

  const handleSubmit = (e) => {
    if (e) {
      e.preventDefault();
    }
    fetchServices();
  };

  const handlePayment = async (e) => {
    console.log("Payment Successfull");
    const response = await fetch(
      `http://localhost:8080/api/curing/trackPayment?customerId=${services.id}&paymentPlanId=${services.paymentPlan[0].id}&paymentAmount=${services.paymentPlan[0].installmentAmount}&servicePlan=${services.services[0].serviceName}`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({}),
      }
    );
    const responseData = await response.json();
    console.log(responseData);
    if (response.status === 200) {
      navigate("/success");
    }
  };

  return (
    <>
      <div className="flex  flex-col items-center w-full space-y-4 mx-auto h-[200vh] diagonal-background">
        <div>
          <form
            onSubmit={handleSubmit}
            className="grid place-items-center lg:w-11/12 sm:w-9/12 w-11/12 mx-auto mt-20 bg-white text-[#4f7cff] shadow-2xl rounded-3xl"
          >
            <div className="w-full flex items-center space-x-2 px-14 pt-4 pb-1">
              <label className="font-bold text-lg">Name</label>
              <input
                type="text"
                className="w-full border border-gray-300 rounded-lg px-2 py-2 mt-1 text-lg outline-none"
                placeholder="Enter your name"
                required
                value={name}
                onChange={(e) => {
                  const inputName = e.target.value;
                  setName(inputName);
                  setNameValid(nameRegex.test(inputName));
                }}
              ></input>
            </div>
            {nameValid ? null : (
              <div className="text-red-500">Please enter a valid Name</div>
            )}
            <div className="w-full flex flex-col px-14 py-4">
              <label className="font-bold text-lg">Service Plan:</label>
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
            <div className="mx-auto flex justify-center items-center p-4">
              <button
                type="submit"
                className="bg-[#3d5fc4] text-white rounded-md text-base uppercase w-24 py-2"
              >
                Submit
              </button>
            </div>
          </form>
        </div>
        <div>
          {services ? (
            <ServiceCard
              service={services}
              handleCreatePaymentPlanClick={handleCreatePaymentPlanClick}
            />
          ) : (
            <div className="text-white border-2px ">
              No service data available with these details
            </div>
          )}
        </div>
        {!paymentPlans && showPaymentPlanForm && (
          <PaymentPlanForm
            customerId={services.id}
            Amount={services.services[0]?.serviceCost}
          />
        )}

        <div>
          {paymentPlans && (
            <PaymentPlan
              paymentPlans={paymentPlans}
              handlePayment={handlePayment}
            />
          )}
        </div>
      </div>
    </>
  );
}

export default Payment;
