import React, { useState } from "react";
function CustomerForm({ onSubmit }) {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [nameValid, setNameValid] = useState(true);
  const [emailValid, setEmailValid] = useState(true);
  const nameRegex = /^[A-Za-z]+(?: [A-Za-z]+)*$/;
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  const handleSubmitCustomer = async (e) => {
    e.preventDefault();
    const response = await fetch("http://localhost:8080/api/addCustomer", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ name, email }),
    });
    if (response.status === 201) {
      console.log("Customer added successfully");
    } else {
      console.error("Failed to add customer");
    }
    const customerData = { name, email };
    onSubmit(customerData);
  };

  return (
    <div className="flex items-center w-full mx-auto h-screen diagonal-background">
      <form
        onSubmit={handleSubmitCustomer}
        className="grid place-items-center lg:w-5/12 sm:w-8/12 w-11/12 mx-auto bg-white text-[#4f7cff] shadow-2xl rounded-3xl"
      >
        <label className="pt-8 pb-4 text-3xl font-bold capitalize">
          Add Customer Details
        </label>
        <div className="w-full flex flex-col px-14 py-4">
          <label>Name:</label>
          <input
            className="w-full border border-gray-300 rounded-lg px-3 py-3 mt-1 text-lg outline-none"
            type="text"
            value={name}
            onChange={(e) => {
              const inputName = e.target.value;
              setName(inputName);
              setNameValid(nameRegex.test(inputName));
            }}
            required
          />
          {nameValid ? null : (
            <div className="text-red-500">Please enter a valid Name</div>
          )}
        </div>
        <div className="w-full flex flex-col px-14 py-4">
          <label>Email:</label>
          <input
            className="w-full border border-gray-300 rounded-lg px-3 py-3 mt-1 text-lg outline-none"
            type="email"
            value={email}
            onChange={(e) => {
              const inputEmail = e.target.value;
              setEmail(inputEmail);
              setEmailValid(emailRegex.test(inputEmail));
            }}
            required
          />
          {emailValid ? null : (
            <div className="text-red-500">
              Please enter a valid email like="example.email@example.com"
            </div>
          )}
        </div>
        <div className="mx-auto flex justify-center items-center pb-8">
          <button
            className="bg-[#3d5fc4] text-white rounded-md text-base uppercase w-fit p-2 hover:bg-blue-800"
            type="submit"
          >
            Submit Customer
          </button>
        </div>
      </form>
    </div>
  );
}

export default CustomerForm;
