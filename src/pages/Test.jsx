import React from "react";
import axios_config from "../utils/AxiosConfig";
import { projects } from "../utils/Links";

const CONSUMER_KEY = "zAVN863kSaGShwVZaIEy7d81Z";
const CONSUMER_SECRET = "GTDTEEYgIWKv65JosQ4A9uYl6QFeqxN6KHeebNfENyST0gNDi8";

function Test() {
  return (
    <button
      onClick={async () => {
        await axios_config
          .get(
            projects + `/check-liked-tweet/Matei17078538/1716873524470821294`
          )
          .then((res) => {
            ////console.log(res);
          });
      }}
    >
      ceva
    </button>
  );
}

export default Test;
