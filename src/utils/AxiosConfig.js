import axios from "axios";

const axios_config = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true,
  headers: {
    "content-type": "application/json",
    "Access-Control-Allow-Origin": "*",
  },
});


export default axios_config;
