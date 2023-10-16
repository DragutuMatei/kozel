import React, { useEffect, useState } from "react";
import "../assets/style/page.scss";
import { IoIosClose } from "react-icons/io";
import { RiTwitterXFill } from "react-icons/ri";
import { BsDiscord, BsMapFill } from "react-icons/bs";
import { FaEdit, FaTelegramPlane } from "react-icons/fa";
import { GiWallet } from "react-icons/gi";
import json from "../utils/Tasks.json";
import { Link, useParams } from "react-router-dom";
import Side from "../components/Side";
import { MdBarChart } from "react-icons/md";

function Admin() {
  const { title } = useParams();

  const [display, setDisplay] = useState(false);
  const [proiect, setProiect] = useState(null);
  const discover = (proiect) => {
    setProiect(proiect);
    setDisplay(true);
  };

  // aici faci tu altfel cu id
  var id_task = 0;

  const [proof, setProof] = useState("");
  function handleProofChange(e) {
    setProof(e.target.value);
  }

  useEffect(() => {
    const keyDownHandler = (event) => {
      if (event.key === "Escape") {
        event.preventDefault();
        setDisplay(false);
      }
    };

    document.addEventListener("keydown", keyDownHandler);

    return () => {
      document.removeEventListener("keydown", keyDownHandler);
    };
  }, []);

  return (
    <>
      <Side />

      <div className="ok">
        {display && proiect && (
          <div
            className="overlay"
            onClick={(e) => {
              if (e.target.className === "overlay") {
                setDisplay(false);
              }
            }}
          >
            <div className="discover but3">
              <IoIosClose
                id="close"
                onClick={() => {
                  setDisplay(false);
                }}
              />
              <div className="top">
                <h3 className="h3">{proiect.title} </h3>
              </div>
              <div className="bottom">
                <div className="left">
                  <h4 className="buton">
                    <span className="green_text">@demo</span>
                  </h4>
                  <br />
                  <div className="title">
                    <img src={require("../assets/images/user.png")} alt="" />
                  </div>
                  <div className="aprprove-denial">
                    <div className="button but2">
                      <h4 className="button">Approve</h4>
                    </div>
                    <div className="button but2">
                      <h4 className="button">Reject</h4>
                    </div>
                  </div>
                </div>
                <div className="left">
                  <h4 className="buton">
                    <span className="green_text">@demo</span>
                  </h4>
                  <br />
                  <div className="title">
                    <img src={require("../assets/images/user.png")} alt="" />
                  </div>
                  <div className="aprprove-denial">
                    <div className="button but2">
                      <h4 className="button">Approve</h4>
                    </div>
                    <div className="button but2">
                      <h4 className="button">Reject</h4>
                    </div>
                  </div>
                </div>

                {/* <div className="right">
                  <h4 className="buton">
                    <span className="green_text">@demo</span>
                  </h4>
                  <br />
                  <div className="title">
                    <img src={require("../assets/images/user.png")} alt="" />
                  </div>
                  <div className="aprprove-denial">
                    <div className="button but2">
                      <h4 className="button">Approve</h4>
                    </div>
                    <div className="button but2">
                      <h4 className="button">Reject</h4>
                    </div>
                  </div>
                </div> */}
              </div>
            </div>
          </div>
        )}
        {/* <Nav /> */}
        <div className="page">
          {/* <div className="logout">
             <div className="button but2">
              <h4 className="button">Connect with metamask</h4>
            </div> 
             <Link to="/auth">
              <div className="button but1">
                <h4 className="button">Log out</h4>
              </div>
            </Link> 
          </div> */}
          <header>
            <div className="titles">
              <h1 className="h1"> {title} </h1>
              <p className="p1">
                You can verify your users tasks on this page by clicking on the
                task card from your community.
              </p>
            </div>
          </header>
          <div className="line"></div>
          <div className="proiecte">
            {json.tasks.map((task) => {
              return (
                <div className="proiect">
                  {/* aici e buton de stergere daca e gen user proprietar */}
                  <div className="delete button">
                    <h4 className="button">X</h4>
                  </div>

                  <h3 className="title">{task.title}</h3>
                  <p className="p1">{task.description}</p>
                  <div className="long">
                    <div
                      href=""
                      className="button but2"
                      onClick={() => discover(task)}
                    >
                      <h4 className="button">View Users</h4>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>
    </>
  );
}

export default Admin;
