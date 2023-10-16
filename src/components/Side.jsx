import React from "react";
import { Link } from "react-router-dom";
import { LuSearch } from "react-icons/lu";
import { BsPlusLg } from "react-icons/bs";
import { TbWorld } from "react-icons/tb";

function Side() {
  return (
    <nav>
      <Link to="/">
        <img
          src={require("../assets/images/icon_logo.svg").default}
          alt=""
          className="logo"
        />
      </Link>
      <Link to="" className="link">
        <LuSearch />
      </Link>
      <Link to="/create-community" className="link">
        <BsPlusLg />
      </Link>
      <Link to="/proiect/test" className="link">
        <TbWorld />
      </Link>
    </nav>
  );
}

export default Side;
