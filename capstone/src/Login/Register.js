import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

function Register() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [nameValid, setNameValid] = useState(true);
  const [emailValid, setEmailValid] = useState(true);
  const [password, setPassword] = useState("");
  const [passwordValid, setPasswordValid] = useState(true);
  const nameRegex = /^[A-Za-z]+(?: [A-Za-z]+)*$/;
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  const passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W).{8,}$/;
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log(name, email, password);
    if (!name || !email || !password) {
      console.log("please fill all the fields");
      return;
    }
    setName("");
    setEmail("");
    setPassword("");

    navigate("/");
  };
  return (
    <div className="flex items-center w-full mx-auto h-screen diagonal-background">
      <form
        onSubmit={handleSubmit}
        className="grid place-items-center lg:w-5/12 sm:w-8/12 w-11/12 mx-auto bg-white text-[#4f7cff] shadow-2xl rounded-3xl"
      >
        <div className="pt-8 pb-4 text-3xl font-bold capitalize">Register</div>
        <div className="w-full flex flex-col px-14 py-4">
          <label>Name</label>
          <input
            type="text"
            className="w-full border border-gray-300 rounded-lg px-3 py-3 mt-1 text-lg outline-none"
            placeholder="Enter your name"
            required
            value={name}
            onChange={(e) => {
              const inputName = e.target.value;
              setName(inputName);
              setNameValid(nameRegex.test(inputName));
            }}
          ></input>
          {nameValid ? null : (
            <div className="text-red-500">Please enter a valid Name</div>
          )}
        </div>
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
            onChange={(e) => {
              const inputPassword = e.target.value;
              setPassword(inputPassword);
              setPasswordValid(passwordRegex.test(inputPassword));
            }}
          ></input>
          {!passwordValid && (
            <div className="text-red-500">
              <p>Password should:</p>
              <ul>
                <li>Be at least 8 characters long</li>
                <li>Contain at least one digit (0-9)</li>
                <li>Include at least one lowercase letter (a-z)</li>
                <li>Have at least one uppercase letter (A-Z)</li>
                <li>Contain at least one special character (!@#$%^&*)</li>
              </ul>
            </div>
          )}
        </div>
        <div className="w-full flex justify-between items-center px-14 pb-8 font-medium text-[#3d5fc4]">
          <div>Already have an account?</div>
          <div>
            <a href="/" className="hover:underline">
              Login Now
            </a>
          </div>
        </div>
        <div className="mx-auto flex justify-center items-center pb-8">
          <button
            type="submit"
            className="bg-[#3d5fc4] text-white rounded-md text-base uppercase w-24 py-2"
          >
            Register
          </button>
        </div>
      </form>
    </div>
  );
}

export default Register;
