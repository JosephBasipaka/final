import React, { useState } from "react";
import { Navigate, useNavigate } from "react-router-dom";
import { CURRENT_USER_TYPE, USER_TYPES } from "../App";

function Login() {
  const [email, setEmail] = useState("");
  const [emailValid, setEmailValid] = useState(true);
  const [password, setPassword] = useState("");
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log(email, password);

    setEmail("");
    setPassword("");

    if (CURRENT_USER_TYPE == USER_TYPES.ADMIN) navigate("/customer");
    else navigate("/add");
  };
  return (
    <div className="flex items-center w-full mx-auto h-screen diagonal-background">
      <form
        onSubmit={handleSubmit}
        className="grid place-items-center lg:w-5/12 sm:w-9/12 w-11/12 mx-auto bg-white text-[#4f7cff] shadow-2xl rounded-3xl"
      >
        <div className="pt-8 pb-4 text-3xl font-bold capitalize">Login</div>
        <div className="w-full flex flex-col px-14 py-4">
          <label>Email</label>
          <input
            type="email"
            className="w-full border border-gray-300 rounded-lg px-3 py-3 mt-1 text-lg outline-none"
            placeholder="Enter your Email"
            required
            value={email}
            onChange={(e) => {
              const inputEmail = e.target.value;
              setEmail(inputEmail);
              setEmailValid(emailRegex.test(inputEmail));
            }}
          ></input>
          {emailValid ? null : (
            <div className="text-red-500">Please enter a valid email</div>
          )}
        </div>
        <div className="w-full flex flex-col px-14 py-4">
          <label>Password</label>
          <input
            type="password"
            className="w-full border border-gray-300 rounded-lg px-3 py-3 mt-1 text-lg outline-none"
            placeholder="Enter Password"
            required
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          ></input>
        </div>
        <div className="w-full flex justify-between items-center px-14 pb-8 font-medium text-[#3d5fc4]">
          <div>New Account</div>
          <div>
            <a href="/register" className="hover:underline">
              Register
            </a>
          </div>
        </div>
        <div className="mx-auto flex justify-center items-center pb-8">
          <button
            type="submit"
            className="bg-[#3d5fc4] text-white rounded-md text-base uppercase w-24 py-2"
          >
            Login
          </button>
        </div>
      </form>
    </div>
  );
}

export default Login;
