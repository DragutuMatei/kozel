import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import "./assets/style/side.scss";
import Liderboard from "./pages/Liderboard";
import Login from "./pages/Login";
import Dashboard from "./pages/proiect";
import Home from "./pages/Home";
import CreateCommunity from "./pages/CreateCommunity";
import Register from "./pages/Register";
import CreateTask from "./pages/CreateTask";
import Admin from "./pages/Admin";
import { useEffect, useState } from "react";
import axios from "axios";
import axios_config from "./utils/AxiosConfig";
import { auth } from "./utils/Links";

function App() {
  const [user, setUser] = useState();

  useEffect(() => {
    console.log(user);
  }, [user]);

  useEffect(() => {
    axios_config.get(auth + "/userInfo").then((res) => {
      setUser(res.data);
    });
  }, []);

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/proiect/:title" element={<Dashboard />} />
        <Route path="/create-community" element={<CreateCommunity user={user} />} />
        <Route path="/leaderboard" element={<Liderboard />} />
        <Route path="/login" element={<Login setUser={setUser} />} />
        <Route path="/register" element={<Register />} />
        <Route path="/create-task/:id" element={<CreateTask />} />
        <Route path="/admin/:title" element={<Admin />} />
      </Routes>
    </Router>
  );
}

export default App;
