import React from "react";
import { useNavigate } from "react-router-dom";

function Success() {
  const navigate = useNavigate();
  const handleProceed = () => {
    navigate("/add");
  };
  return (
    <div className="m-0 p-0">
      <div className="w-full mi-h-[80vh] flex flex-col justify-center items-center">
        <div className="my-10 text-green-600 text-2xl mx-auto flex flex-col justify-center items-center">
          <img src="success.png" alt="" width={220} height={220} />
          <h3 className="text-4xl pt-20 lg:pt-0 font-bold text-center text-slate-700">
            Payment Successfull
          </h3>
          <button
            onClick={handleProceed}
            className="w-40 uppercase bg-[#009C96] text-white text-xl my-16 px-2 py-2 rounded"
          >
            Proceed
          </button>
        </div>
      </div>
    </div>
  );
}

export default Success;
