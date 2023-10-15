import { useEffect, useState } from "react";
import "./App.css";
import injectedModule from "@web3-onboard/injected-wallets";
import { init, useConnectWallet } from "@web3-onboard/react";
import Web3 from "web3";
import Afi from "./Afi";
import axios from "axios";
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

const App = () => {
  const [{ wallet }, connect] = useConnectWallet();
  const [web3, setWeb3] = useState(null);
  const [account, setAccount] = useState(null);
  const [authenticating, setAuthenticating] = useState(false);
  const [error, setError] = useState(null);
  const [auth, setAuth] = useState(null);
  const [nonce, setNonce] = useState(null)

  const api = new Afi();

  // useEffect(() => {
  //   if (!wallet) connect();
  // }, [wallet, connect]);

  useEffect(() => {
    if (wallet) setWeb3(new Web3(wallet.provider));
  }, [wallet]);

  useEffect(() => {
    if (web3) web3.eth.getAccounts().then((res) => setAccount(res[0]));
  }, [, web3]);

  useEffect(() => {
    if (account !== null && account !== undefined) sign();
  }, [account]);

  const sign = async () => {
    setAuthenticating(true);
    setError(null);
    setAuth(null);

    // console.log(account);

    if (account) {

      try {
        // const challenge = await fetch(
        //   `http://localhost:8080/challenge/${account}`
        // );
        // if (challenge.status === 401) {
        //   throw new Error("This address is not registered");
        // }
        let requestChallenge = axios.get(`http://localhost:8080/challenge/${account}`)
        const challenge = await requestChallenge;
        if (challenge.status === 401) {
          throw new Error("Address is not registered")
        }
        const nonce = challenge.data;
        console.log(nonce)
        const signature = await web3.eth.personal.sign(
          nonce,
          account,
          "secret"
        );
        // const auth = await fetch(`http://localhost:8080/auth`, {
        //   method: "POST",
        //   headers: { "Content-Type": "application/json" },
        //   body: JSON.stringify({ signature: signature, address: account }),
        // });

        // const requestAuth = axios.post(`http://localhost:8080/auth`, JSON.stringify({
        //   signature: signature,
        //   address: account
        // }, { headers: { "Content-Type": "application/json" } }
        // ))
        const requestAuth = axios.post(`http://localhost:8080/auth`, JSON.stringify({
          signature: signature,
          address: account
        }), { headers: { "Content-Type": "application/json" } })
        const auth = await requestAuth;
        const status = auth.status;

        if (status === 200) {
          setAuth("Successfully authenticated.");
        } else {
          throw new Error(`The API returned ${auth.status}..`);
        }
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
              await fetch(
                `http://localhost:8080/challenge/${account}/register`,
                {
                  method: "POST",
                  body: {
                    username: "Matei vrea sa",
                  },
                }
              ).then((res) => {
                console.log(res);
              });
            }}
          >
            register
          </button>
          <button
            disabled={authenticating}
            onClick={() => {
              connect();
            }}
          >
            {" "}
            <span>Authenticate</span>
          </button>
          {error && <div className="text-red-500 text-center">{error}</div>}
        </div>
      </div>
    </div>
  );
};
export default App;
