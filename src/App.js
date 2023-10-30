import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import "./assets/style/side.scss";
import Liderboard from "./pages/Liderboard";
import Login from "./pages/Login";
import Home from "./pages/Home";
import CreateCommunity from "./pages/CreateCommunity";
import Register from "./pages/Register";
import CreateTask from "./pages/CreateTask";
import Admin from "./pages/Admin";
import { useEffect, useState } from "react";
import axios from "axios";
import axios_config from "./utils/AxiosConfig";
import { auth } from "./utils/Links";
import Cookies from "js-cookie";
import Project from "./pages/proiect";

function App() {
  const [user, setUser] = useState("");

  // useEffect(() => {
  //   console.log(user);
  // }, [user]);

  useEffect(() => {
    axios_config
      .get(auth + "/userInfo")
      .then((res) => {
        setUser(res.data);
        console.log(typeof res.data);
        if (res.data != "") localStorage.setItem("logged", "true");
        else localStorage.setItem("logged", "false");
      })
      .catch((er) => {
        localStorage.setItem("logged", "false");
      });
  }, []);

  const logout = async () => {
    await axios_config.post(auth + "/signout").then((res) => {
      alert("User logged out");
      localStorage.setItem("logged", "false");
      setUser("");
    });
  };

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home logout={logout} user={user} />} />
        <Route path="/proiect/:id" element={<Project user={user} />} />
        <Route
          path="/create-community"
          element={<CreateCommunity user={user} />}
        />
        <Route path="/leaderboard/:id" element={<Liderboard user={user} />} />
        <Route
          path="/login"
          element={<Login setUser={setUser} user={user} />}
        />
        {/* <Route path="/register" element={<Register user={user} />} /> */}
        <Route
          path="/create-task/:id/:user_id"
          element={<CreateTask user={user} />}
        />
        <Route path="/admin/:id" element={<Admin user={user} />} />
      </Routes>
    </Router>
  );
}

export default App;
