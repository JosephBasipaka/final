import React, { useEffect, useRef } from "react";
import { Chart } from "chart.js/auto";

function PieChart({ data, labels, backgroundColor, onClick }) {
  const charRef = useRef(null);
  const charInstance = useRef(null);

  useEffect(() => {
    if (charInstance.current) {
      charInstance.current.destroy();
    }
    const myCharRef = charRef.current.getContext("2d");
    charInstance.current = new Chart(myCharRef, {
      type: "pie",
      data: {
        labels,
        datasets: [
          {
            data,
            backgroundColor,
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
        onClick: function (event, elements) {
          if (elements.length > 0) {
            const index = elements[0].index;
            onClick(index);
          }
        },
      },
    });
  }, [data, labels, backgroundColor, onClick]);

  return (
    <div className="w-64 h-64">
      <canvas ref={charRef} className="w-full h-full" />
    </div>
  );
}

export default PieChart;
