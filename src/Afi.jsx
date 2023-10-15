import axios from 'axios'

export default class Afi {

    _port = 8080
    _baseUrl = `http://localhost:${this._port}`;
    _headers = {
        "Content-Type": "application/json"
    }

    static debug = true;

    /**
     * Get the nonce for each account. Address describes the account ID.
     */
    challenge = ({ path, address }) => {

        let url = `${this._baseUrl}/${path}/${address}`;
        try {
            axios.get(url).then((res) => {
                if (res.status === 401) {
                    console.log(`[ACCOUNT]: No account with this address`,)
                } else {
                    return res.data;
                }
            })
        } catch (e) {
            // console.log(`[AXIOS ERROR]: ${e}`)
            console.log(`[AXIOS ERROR]: ${e}`,)
        }

    }

    auth = ({ path, signature, address }) => {

        let url = `${this._baseUrl}/${path}`
        let data = JSON.stringify({
            signature: signature,
            address: address
        })
        try {
            axios.post(url, data, {
                headers: this._headers,
            }).then((res) => {
                if (this.debug) {
                    console.log(`[AXIOS DEBUG]: ${res.data}`)
                }
            })
        } catch (e) {
            console.warn(`[AXIOS ERROR]: ${e}`,)
        }
    }

}
