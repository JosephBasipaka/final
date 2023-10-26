import React, { useState } from "react";
import PopupMessage from "../Dunning/PopUpMessage";

function PaymentPlanForm({ customerId, Amount }) {
  const [installmentOption, setInstallmentOption] = useState("onetime");
  const [numberOfInstallments, setNumberOfInstallments] = useState(2);
  const [dueDate, setDueDate] = useState("");
  const [showSuccessMessage, setShowSuccessMessage] = useState(false);
  const [popupMessage, setPopupMessage] = useState("");

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    console.log(
      "For" +
        customerId +
        "and" +
        Amount +
        "Created a plan with " +
        numberOfInstallments +
        " and " +
        dueDate
    );
    const response = await fetch(
      `http://localhost:8080/api/curing/createPaymentPlan?customerId=${customerId}&totalAmount=${Amount}&dueDate=${dueDate}&numberOfInstallments=${numberOfInstallments}`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({}),
      }
    );
    console.log(response.body);
    if (response.status === 201) {
      setShowSuccessMessage(true);

      console.log("Payment Plan Added Successfully");
    } else {
      console.error("Failed to add Payment Plan");
    }
  };

  const handleInstallmentOptionChange = (e) => {
    const selectedOption = e.target.value;
    setInstallmentOption(selectedOption);

    if (selectedOption === "onetime") {
      setNumberOfInstallments(1);
      setDueDate(new Date().toISOString().split("T")[0]);
    } else if (selectedOption === "2times") {
      setNumberOfInstallments(2);
      const date = new Date();
      date.setDate(date.getDate() + 15);
      setDueDate(date.toISOString().split("T")[0]);
    } else if (selectedOption === "4times") {
      setNumberOfInstallments(4);
      const date = new Date();
      date.setDate(date.getDate() + 30);
      setDueDate(date.toISOString().split("T")[0]);
    }
  };

  return (
    <div className="rounded-3xl h-auto w-auto mx-auto flex flex-col bg-white p-3 shadow-md hover:shadow-lg">
      <form onSubmit={handleFormSubmit}>
        <div>
          <label>Payment Option:</label>
          <select
            value={installmentOption}
            onChange={handleInstallmentOptionChange}
          >
            <option value="onetime">One-Time Payment</option>
            <option value="2times">2 Installments</option>
            <option value="4times">4 Installments</option>
          </select>
        </div>
        {installmentOption !== "onetime" && (
          <div>
            <label>For {numberOfInstallments} Installments :</label>
            <div className="flex space-x-2">
              <label>Amount Payable for one installment</label>
              <input
                type="number"
                value={Amount / numberOfInstallments}
                readOnly
              />
            </div>
            <div className="flex space-x-2">
              <label>DueDate to close the totalAmount is : </label>
              <input type="date" value={dueDate} readOnly />
            </div>
          </div>
        )}
        <button
          type="submit"
          onClick={() => setPopupMessage("Payment Plan Added Successfully")}
        >
          Create Payment Plan
        </button>
        <PopupMessage
          show={showSuccessMessage}
          message={popupMessage}
          onClose={() => setShowSuccessMessage(false)}
        />
      </form>
    </div>
  );
}

export default PaymentPlanForm;
