import axios from 'axios'

function Afi() {

    const _port = 8080
    const _baseUrl = `http://localhost:${_port}`;
    const _headers = {
        "Content-Type": "application/json"
    }

    const debug = true;

    /**
     * Get the nonce for each account. Address describes the account ID.
     */
    const challenge = ({ path, address }) => {

        let url = `${_baseUrl}/${path}/${address}`;
        try {
            axios.get(url).then((res) => {
                if (res.status === 401) {
                    console.warn(`[ACCOUNT]: No account with this address`,)
                } else {
                    return res.data;
                }
            })
        } catch (e) {
            // console.log(`[AXIOS ERROR]: ${e}`)
            console.warn(`[AXIOS ERROR]: ${e}`,)
        }

    }

    const auth = ({ path, signature, address }) => {

        let url = `${_baseUrl}/${path}`
        let data = JSON.stringify({
            signature: signature,
            address: address
        })
        try {
            axios.post(url, data, {
                headers: _headers,
            }).then((res) => {
                if (debug) {
                    console.log(`[AXIOS DEBUG]: ${res.data}`)
                }
            })
        } catch (e) {
            console.warn(`[AXIOS ERROR]: ${e}`,)
        }
    }

}

export default Afi