import React from "react";

function PaymentPlan({ paymentPlans, handlePayment }) {
  return (
    <>
      <div className="rounded-3xl h-40 w-60 mx-auto flex flex-col bg-white p-3 shadow-md hover:shadow-lg">
        <h1>Total Amount : {paymentPlans.totalAmount}</h1>
        <h1>Agreed Installments: {paymentPlans.numberOfInstallments}</h1>
        <h1>Installment Amount: {paymentPlans.installmentAmount}</h1>
        <button
          className="mt-4 bg-blue-600 text-white rounded-lg px-4 py-2 hover:bg-blue-700 hover:text-black"
          onClick={handlePayment}
        >
          Pay Now {paymentPlans.installmentAmount}
        </button>
      </div>
    </>
  );
}

export default PaymentPlan;
