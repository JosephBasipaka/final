// api.js

import axios from "axios";

const API_BASE_URL = "http://localhost:8080";

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

export const fetchCustomers = () => {
  return api.get("/api/customers");
};
export const fetchInvoices = () => {
  return api.get("/api/invoice");
};
export const fetchServices = () => {
  return api.get("/api/services");
};

export default api;
