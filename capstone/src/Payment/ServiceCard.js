import React from "react";

function ServiceCard({ service, handleCreatePaymentPlanClick }) {
  return (
    <div className="rounded-3xl h-40 w-60 mx-auto flex flex-col bg-white p-3 shadow-md hover:shadow-lg">
      <div className="text-lg font-sans flex">
        <h1 className="font-semibold">Service Name: </h1>
        <span className="text-blue-600 ml-2">
          {service?.services[0]?.serviceName}
        </span>
      </div>
      <div className="text-lg font-sans flex">
        <h1 className="font-semibold">Price: </h1>
        <span className="text-green-600 ml-2">
          {service?.services[0]?.serviceCost}
        </span>
      </div>
      <div className="text-lg font-sans flex">
        <h1 className="font-semibold">Due Date: </h1>
        <span className="text-red-600 ml-2">
          {service?.invoices && service.invoices.length > 0
            ? new Date(service.invoices[0].dueDate).toLocaleDateString("en-GB")
            : "N/A"}
        </span>
      </div>
      <button
        onClick={handleCreatePaymentPlanClick}
        className="mt-4 bg-blue-600 text-white rounded-lg px-4 py-2 hover:bg-blue-700 hover:text-black"
      >
        See Payment Plans
      </button>
    </div>
  );
}

export default ServiceCard;
