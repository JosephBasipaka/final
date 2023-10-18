import React from 'react';
import { Link } from 'react-router-dom';

function SuccessPage() {
  return (
    <div>
      <h2>Submitted Successfully</h2>
      <p>You Can enjoy the service now</p>
      <Link to="/">Home Page</Link>
    </div>
  );
}

export default SuccessPage;
