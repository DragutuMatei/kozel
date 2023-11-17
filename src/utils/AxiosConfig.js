import axios from "axios";

const axios_config = axios.create({
  // baseURL: "https://fastlane-nfzl.onrender.com/api",
  baseURL: "http://localhost:8080/api",
  // baseURL: "http://localhost:8080/",
  withCredentials: true,
  headers: {
    // "access-control-expose-headers": "Set-Cookie",
    "content-type": "application/json",
    "Access-Control-Allow-Origin": "*",
  },
});

export default axios_config;