import React, { useEffect, useState } from "react";
import "../assets/style/login.scss";
import { Link, useNavigate } from "react-router-dom";
import axios_config from "../utils/AxiosConfig";
import { auth } from "../utils/Links";

function Register({ user }) {
  const [email, setEmail] = useState("");
  function handleEmailChange(e) {
    setEmail(e.target.value);
  }

  const [username, setUsername] = useState("");
  function handleUsernameChange(e) {
    setUsername(e.target.value);
  }

  const [password, setPassword] = useState("");
  function handlePasswordChange(e) {
    setPassword(e.target.value);
  }
  const navigate = useNavigate();
  useEffect(() => {
    if (typeof user == "object") {
      navigate("/");
    }
  }, [user]);

  const [msg, setMsg] = useState("");

  const register = async () => {
    try {
      await axios_config
        .post(auth + "/signup", {
          username,
          email,
          password,
        })
        .then((res) => {
          console.log(res);
          setMsg(res.data.message);
        });
    } catch (error) {
      setMsg("Completeaza corect toate datele!");
      console.log(error);
    }
  };

  return (
    <div className="auth">
      <div className="content">
        <div className="top">
          <Link to="/">
            <img
              src={require("../assets/images/icon_logo.svg").default}
              alt=""
              className="logo"
            />
          </Link>
          <div className="h3s">
            <h3 className="title">
              Already have an account on{" "}
              <span className="purple_text">FAST</span>
              <span className="green_text">LANE</span>?
            </h3>
            <br />
            <Link to="/login">
              <div className="button but1">
                <h4 className="button">Login into account</h4>
              </div>
            </Link>
          </div>
        </div>
        <div className="rest">
          <h2 className="h2">
            Log in to <span className="purple_text">FAST</span>
            <span className="green_text">LANE</span>
          </h2>
          <p className="bold_p">Welcome back!</p>

          {/* <div className="button but3_1">
            <h4 className="button">
              <BsDiscord />
              Log in with Discord
            </h4>
          </div> */}
          {/* <div className="button but3_1">
            <h4 className="button">
              <SiWalletconnect />
              Log in with WalletConnect
            </h4>
          </div> */}
          {/* <div className="or">
            <div className="liner"></div>
            <h4 className="button">OR</h4>
          </div> */}
          <p className="bold_p">Email address</p>
          <input
            type="text"
            placeholder="Kenny@fastlane.io"
            onChange={handleEmailChange}
          />
          <p className="bold_p">Username</p>
          <input
            type="text"
            placeholder="fastlane2722"
            onChange={handleUsernameChange}
          />
          <p className="bold_p">Parola</p>
          <input
            type="password"
            placeholder="your password"
            onChange={handlePasswordChange}
          />
          <button className="button but3_1" onClick={register}>
            <h4 className="button">Register</h4>
          </button>
          <h2 className="h2">{msg !== "" && msg}</h2>
        </div>
      </div>
    </div>
  );
}

export default Register;
