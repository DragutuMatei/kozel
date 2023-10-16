import React from "react";
import { BsMapFill } from "react-icons/bs";
import { MdBarChart } from "react-icons/md";
import { Link } from "react-router-dom";

function Nav() {
  return (
    <div className="user">
      <img src={require("../assets/images/user.png")} alt="" />
      <Link to={"/"} className="longl">
        <BsMapFill id="map" />
        <h3 className="p1">Quests</h3>
      </Link>
      <Link to={"/leaderboard"} className="longl">
        <MdBarChart id="chart" />
        <h3 className="p1">Leaderboard</h3>
      </Link>
    </div>
  );
}

export default Nav;
