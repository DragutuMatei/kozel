import React, { useState } from "react";
import { BsDiscord } from "react-icons/bs";
import { SiWalletconnect } from "react-icons/si";
import "../assets/style/login.scss";
import { Link } from "react-router-dom";
import axios_config from "../utils/AxiosConfig";

function Login() {
  const [username, setUsername] = useState("");
  function handleUsernameChange(e) {
    setUsername(e.target.value);
  }

  const [password, setPassword] = useState("");
  function handlePasswordChange(e) {
    setPassword(e.target.value);
  }

  const login = async () => {
    try {
      await axios_config.post()
    } catch (error) {
      
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
              First time on <span className="purple_text">FAST</span>
              <span className="green_text">LANE</span>?
            </h3>
            <br />
            <Link to="/register">
              <div className="button but1">
                <h4 className="button">Create an account</h4>
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
          {/* <p className="bold_p">Email address</p>
          <input
            type="text"
            placeholder="Kenny@fastlane.io"
            onChange={handleEmailChange}
          /> */}
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
          <button className="button but3_1" onClick={login}>
            <h4 className="button">Log in</h4>
          </button>
        </div>
      </div>
    </div>
  );
}

export default Login;
