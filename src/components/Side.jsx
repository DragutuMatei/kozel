import React, { useEffect, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { LuSearch } from "react-icons/lu";
import { BsPlusLg } from "react-icons/bs";
import { TbWorld } from "react-icons/tb";
import axios_config from "../utils/AxiosConfig";
import { projects } from "../utils/Links";
import axios from "axios";

function Side({ user }) {
  const [myProjects, setMyProjects] = useState([]);

  const getMy = async () => {
    // await axios_config.get(projects + `/getByUser/${user.id}`).then((res) => {
    //   //console.log("assssssssssssssssssssssssssssssssssssssss", res.data);
    //   setMyProjects(res.data);
    // });
  };
  
  const [searchParams, setSearchParams] = useSearchParams();

  useEffect(() => {
    if (
      searchParams.get("oauth_token") &&
      searchParams.get("oauth_token_secret")
    ) {
      localStorage.setItem("oauth_token", searchParams.get("oauth_token"));
      localStorage.setItem(
        "oauth_token_secret",
        searchParams.get("oauth_token_secret")
      );
    }
  }, []);

  const [log, setLog] = useState(false);

  useEffect(() => {
    if (localStorage.getItem("logged") == "true") {
      setLog(true);
    }
    getMy();
  }, [, user]);

  return (
    <nav>
      <Link to="/">
        <img
          src={require("../assets/images/icon_logo.svg").default}
          alt=""
          className="logo"
        />
      </Link>
      <Link to={`/proiect/${"653ec9ada32dbe1f0d4d5de6"}`} className="link">
        <TbWorld />
      </Link>
      <div className="linie"></div>
      <button
        onClick={async () => {
          await axios_config
            .get(
              `/projects/check-user-subscribed/${localStorage.getItem(
                "oauth_token"
              )}/${localStorage.getItem(
                "oauth_token_secret"
              )}/Matei17078538/{username}/{project_id}/{index_task}`
            )
            .then((res) => {
              console.log(res);
            });
        }}
      >
        click
      </button>
      <button
        onClick={async () => {
          await axios_config
            .get("/projects/oauth2/authorize/normal/twitter")
            .then(async (res) => {
              console.log(res);
              window.open(res.data, "_blank");
            });
        }}
      >
        click
      </button>
    </nav>
  );
}

export default Side;
