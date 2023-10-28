import React, { useEffect, useRef, useState } from "react";
import { Chart, Legend } from "chart.js/auto";
import { fetchCustomers, fetchInvoices } from "../api";

function PieChart() {
  const charRef = useRef(null);
  const charInstance = useRef(null);
  const [customers, setCustomers] = useState([]);

  useEffect(() => {
    fetchCustomers().then((response) => {
      console.log(response.data);
      setCustomers(response.data);
      let basicData = 0;
      let standardData = 0;
      let premiumData = 0;
      for (const customer of response.data) {
        if (customer.services[0]?.serviceName === "Basic") basicData++;
        if (customer.services[0]?.serviceName === "Standard") standardData++;
        if (customer.services[0]?.serviceName === "Premium") premiumData++;
      }
      if (charInstance.current) charInstance.current.destroy();
      const myCharRef = charRef.current.getContext("2d");
      charInstance.current = new Chart(myCharRef, {
        type: "pie",
        data: {
          labels: ["Basic", "Standard", "Premium"],
          datasets: [
            {
              data: [basicData, standardData, premiumData],
              backgroundColor: ["powderblue", "pink", "cyan"],
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          aspectRatio: 2,
          plugins: {
            legend: {
              labels: {
                color: "black",
                font: {
                  size: 16,
                  weight: "bold",
                },
              },
            },
          },
        },
      });
    });
    return () => {
      if (charInstance.current) {
        charInstance.current.destroy();
      }
    };
  }, []);
  return (
    <>
      <div className="h-screen diagonal-background">
        <div className="h-full flex flex-col items-center ">
          <h1 className="text-2xl font-bold">
            Metrics for Service Types by Customers
          </h1>
          <div className="w-64 h-64">
            <canvas ref={charRef} className="w-full h-full" />
          </div>
        </div>
      </div>
    </>
  );
}

export default PieChart;
