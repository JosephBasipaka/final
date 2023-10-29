/**
 * The `PieChart` component is a reusable React component that uses Chart.js to render a pie chart
 * based on the provided data, labels, backgroundColor, and onClick function.
 * @returns The `PieChart` component returns a `div` element with a `canvas` element inside. The
 * `canvas` element is assigned the `charRef` ref, which is used to create and update the Chart.js pie
 * chart. The `div` element has a class name of "w-64 h-64" and the `canvas` element has a class name
 * of "w-full h
 */
import React, { useEffect, useRef } from "react";
import { Chart } from "chart.js/auto";

function PieChart({ data, labels, backgroundColor, onClick }) {
  const charRef = useRef(null);
  const charInstance = useRef(null);

  /* The `useEffect` hook is used to perform side effects in a functional component. In this case, the
  `useEffect` hook is used to create and update a Chart.js pie chart whenever the dependencies
  (`data`, `labels`, `backgroundColor`, `onClick`) change. */
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
