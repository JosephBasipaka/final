import React from 'react';
import { Pie } from 'react-chartjs-2';

function PieChart({ data }) {
  return (
    <div>
      <Pie data={data} />
    </div>
  );
}

export default PieChart;