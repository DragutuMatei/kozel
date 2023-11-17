import React, { useEffect, useState } from "react";
import "../assets/style/login.scss";
import { Link, useNavigate } from "react-router-dom";
import axios_config from "../utils/AxiosConfig";
import { auth } from "../utils/Links";
import injectedModule from "@web3-onboard/injected-wallets";
import { init, useConnectWallet } from "@web3-onboard/react";
import Web3 from "web3";

const injected = injectedModule();

init({
  wallets: [injected],
  chains: [
    {
      id: "0x1",
      token: "ETH",
      label: "Mainnet",
      rpcUrl: "https://cloudflare-eth.com/",
    },
  ],
  appMetadata: {
    name: "Spring Boot Web3 Demo",
    description: "A Spring Boot Web3 login demo",
  },
});
function Login({ user, setUser }) {
  const navigate = useNavigate();
  useEffect(() => {
    if (typeof user == "object") {
      navigate("/");
    }
  }, [user]);
  const [{ wallet }, connect] = useConnectWallet();
  // const [web3, setWeb3] = useState(null);
  const [account, setAccount] = useState(null);
  const [authenticating, setAuthenticating] = useState(false);
  const [error, setError] = useState(null);
  const [log, setLog] = useState(false);

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
      if (!wallet) {
        await connect({
          autoSelect: {
            label: "MetaMask",
            disableModals: true,
          },
        });
        // sign();
      } else {
        sign();
      }
      ////console.log(wallet);
    } catch (error) {
      ////console.log("error ", error);
    }
  };

  const [msg, setMsg] = useState("");
  useEffect(() => {
    if (wallet) sign();
  }, [wallet]);
  const sign = async () => {
    setAuthenticating(true);
    setError(null);
    let account;
    ////console.log(wallet);

    let web;
    if (wallet) {
      web = new Web3(wallet.provider);
      await web.eth.getAccounts().then((res) => {
        account = res[0];
      });
    }

    ////console.log(account);
    setAccount(account);
    if (account) {
      try {
        await axios_config
          .get(auth + `/challenge/${username}/${account}`)
          .then(async (res) => {
            ////console.log(res.data);
            // if (res.status === 401) {
            //   throw new Error("This address is not registered");
            // }

            const nonce = await res.data;
            const signature = await web.eth.personal.sign(
              nonce,
              account,
              "secret"
            );

            await axios_config
              .post(auth + `/signin`, {
                signature: signature,
                address: account,
                username,
              })
              .then((res) => {
                setUser(res.data);
                localStorage.setItem("logged", "true");
                ////console.log(res.data);
                navigate("/");
              })
              .catch((err) => {
                setMsg("Bad Credentials!");
              });
          })
          .catch((err) => {
            localStorage.setItem("logged", "false");
            setMsg("Username does not exist!");
          });
      } catch (error) {
        localStorage.setItem("logged", "false");
        if (error instanceof Error) {
          setError(`Something went wrong: ${error.message}`);
        }
      }
      setAuthenticating(false);
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
          <p className="bold_p">You must have MetaMask installed</p>
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
          {/* <p className="bold_p">Parola</p>
          <input
            type="password"
            placeholder="your password"
            onChange={handlePasswordChange}
          /> */}
          <button className="button but3_1" onClick={login}>
            <h4 className="button">Log in</h4>
          </button>
          {msg !== "" && <h2 className="h2">{msg}</h2>}
        </div>
      </div>
    </div>
  );
}

export default Login;
