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

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/proiect/:title" element={<Dashboard />} />
        <Route path="/create-community" element={<CreateCommunity />} />
        <Route path="/leaderboard" element={<Liderboard />} />
        <Route path="/auth" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/create-task/:id" element={<CreateTask />} />
        <Route path="/admin/:title" element={<Admin />} />
      </Routes>
    </Router>
  );
}

export default App;
