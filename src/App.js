import { useEffect, useState } from "react";
import "./App.css";
import injectedModule from "@web3-onboard/injected-wallets";
import { init, useConnectWallet } from "@web3-onboard/react";
import Web3 from "web3";
import axios from "axios";

const injected = injectedModule();

const axios_fara_cred = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true,
  headers: {
    "content-type": "application/json",
    "Access-Control-Allow-Origin": "*",
  },
  method: "POST",
});

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

const App = () => {
  const [{ wallet }, connect] = useConnectWallet();
  // const [web3, setWeb3] = useState(null);
  const [account, setAccount] = useState(null);
  const [authenticating, setAuthenticating] = useState(false);
  const [error, setError] = useState(null);
  const [auth, setAuth] = useState(null);
  const [log, setLog] = useState(false);

  const login = async () => {
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
    console.log(wallet);
  };

  useEffect(() => {
    if (wallet) sign();
  }, [wallet]);

  // useEffect(() => {
  //   console.log("asd")
  //   if (!wallet) connect();
  // }, [log]);

  // useEffect(() => {
  //   if (wallet) setWeb3(new Web3(wallet.provider));
  // }, [wallet]);

  // useEffect(() => {
  //   if (web3) {
  //     web3.eth.getAccounts().then((res) => setAccount(res[0]));
  //     sign();
  //   }
  // }, [web3]);

  const sign = async () => {
    setAuthenticating(true);
    setError(null);
    setAuth(null);
    let account;
    console.log(wallet);

    let web;
    if (wallet) {
      web = new Web3(wallet.provider);
      await web.eth.getAccounts().then((res) => {
        account = res[0];
      });
    }

    console.log(account);
    setAccount(account);
    if (account) {
      console.log("gfd");
      try {
        const challenge = await axios_fara_cred
          .get(`http://localhost:8080/api/auth/challenge/${"matei"}/${account}`)
          .then(async (res) => {
            console.log(res);
            if (res.status === 401) {
              throw new Error("This address is not registered");
            }

            const nonce = await res.data;
            const signature = await web.eth.personal.sign(
              nonce,
              account,
              "secret"
            );

            const auth = await axios_fara_cred.post(
              `http://localhost:8080/api/auth/signin`,
              {
                signature: signature,
                address: account,
                username: "matei",
                password: "matei1",
              }
            );

            if (auth.status === 200) {
              setAuth("Successfully authenticated.");
            } else {
              throw new Error(`The API returned ${auth.status}..`);
            }
          });
      } catch (error) {
        if (error instanceof Error) {
          setError(`Something went wrong: ${error.message}`);
        }
      }
      setAuthenticating(false);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-slate-100">
      <div className="flex flex-col p-4 m-3 space-y-10 bg-white rounded-2xl md:flex-row md:space-y-0 md:space-x-10 md:m-0 md:p-16">
        <div className="flex flex-col space-y-6">
          <div className="text-neutral-800 font-bold text-center">
            Hello, {account}
          </div>
          {auth && (
            <div className="w-full text-neutral-800 text-center animate-pulse">
              {auth}
            </div>
          )}
          <button
            onClick={async () => {
              // await fetch(
              //   `http://localhost:8080/challenge/${account}/register`,
              //   {
              //     method: "POST",
              //     body: {
              //       username: "Matei vrea sa",
              //     },
              //   }
              // ).then((res) => {
              //   console.log(res);
              // });
              console.log(account);
            }}
          >
            register
          </button>
          <button
            // disabled={authenticating}
            onClick={() => {
              login();
            }}
          >
            {" "}
            <span>Authenticate</span>
          </button>
          <button
            onClick={async () => {
              await axios_fara_cred
                .get("http://localhost:8080/api/auth/userInfo")
                .then((res) => {
                  console.log(res);
                });
            }}
          >
            is auth?
          </button>
          <button
            onClick={async () => {
              await axios_fara_cred
                .post("http://localhost:8080/api/auth/signout")
                .then((res) => {
                  console.log(res);
                });
            }}
          >
            logout
          </button>
          {error && <div className="text-red-500 text-center">{error}</div>}
        </div>
      </div>
    </div>
  );
};
export default App;
