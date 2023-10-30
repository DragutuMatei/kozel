import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { LuSearch } from "react-icons/lu";
import { BsPlusLg } from "react-icons/bs";
import { TbWorld } from "react-icons/tb";
import axios_config from "../utils/AxiosConfig";
import { projects } from "../utils/Links";

function Side({ user }) {
  const [myProjects, setMyProjects] = useState([]);

  const getMy = async () => {
    // await axios_config.get(projects + `/getByUser/${user.id}`).then((res) => {
    //   console.log("assssssssssssssssssssssssssssssssssssssss", res.data);
    //   setMyProjects(res.data);
    // });
  };

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
      {myProjects &&
        myProjects.map((my) => (
          <Link to={`/proiect/${my.id}`} className="link">
            <img src={my.img} alt="" />
          </Link>
        ))}
    </nav>
  );
}

export default Side;
