import axios from "axios";
class Afi {
  axios_instance = axios.create({
    withCredentials: true,
    headers: {
      "Content-Type": "application/json",
      "Access-Control-Allow-Origin": "*",
    },
  });

  _port = 8080;
  _baseUrl = `http://localhost:${this._port}`;

  static debug = true;

  /**
   * Get the nonce for each account. Address describes the account ID.
   */
  challenge = ({ path, address }) => {
    let url = `${this._baseUrl}/${path}/${address}`;
    try {
      this.axios_instance.get(url).then((res) => {
        if (res.status === 401) {
          console.warn(`[ACCOUNT]: No account with this address`);
        } else {
          return res.data;
        }
      });
    } catch (e) {
      // ////console.log(`[AXIOS ERROR]: ${e}`)
      console.warn(`[AXIOS ERROR]: ${e}`);
    }
  };

  auth = ({ path, signature, address }) => {
    let url = `${this._baseUrl}/${path}`;
    let data = JSON.stringify({
      signature: signature,
      address: address,
    });
    try {
      axios
        .post(url, data, {
          headers: this._headers,
        })
        .then((res) => {
          if (this.debug) {
            ////console.log(`[AXIOS DEBUG]: ${res.data}`)
          }
        });
    } catch (e) {
      console.warn(`[AXIOS ERROR]: ${e}`);
    }
  };

  signup = ({ user, email, password }) => {
    let signup_path = "signup";
    let url = `${this._baseUrl}/${signup_path}`;
    let data = JSON.stringify({
      email: email,
      username: user,
      password: password,
    });
    try {
      this.axios_instance
        .post(url, data, { headers: this._headers })
        .then((res) => {
          if (this.debug) {
            ////console.log(`[AXIOS DEBUG]: ${res.data}`)
          }
        });
    } catch (e) {
      console.warn(`[AXIOS ERROR]: ${e}`);
    }
  };
}

export default Afi;
