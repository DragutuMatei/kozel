import React, { useEffect, useState } from "react";
import "../assets/style/page.scss";
import { IoIosClose } from "react-icons/io";
import { RiTwitterXFill } from "react-icons/ri";
import { BsDiscord, BsMapFill } from "react-icons/bs";
import { FaEdit, FaTelegramPlane } from "react-icons/fa";
import { GiWallet } from "react-icons/gi";
import json from "../utils/Tasks.json";
import { Link, useNavigate, useParams } from "react-router-dom";
import Side from "../components/Side";
import { MdBarChart } from "react-icons/md";
import { projects } from "../utils/Links";
import axios_config from "../utils/AxiosConfig";

function Admin({ user }) {
  const { id } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    // if (typeof user == "object") {
    //   navigate("/");
    // }
    if (localStorage.getItem("logged") == "false") {
      navigate("/");
    }
  }, [user]);

  const [display, setDisplay] = useState(false);
  const [proiect, setProiect] = useState(null);
  const [solves, setSolves] = useState(null);
  const [taskIndex, setTaskIndex] = useState(null);
  
  const discover = (task, index) => {
    setTaskIndex(index);
    setDisplay(true);
  };

  // aici faci tu altfel cu id
  // var id_task = 0;

  const [proof, setProof] = useState("");
  function handleProofChange(e) {
    setProof(e.target.value);
  }

  useEffect(() => {
    const getProject = async () => {
      await axios_config.get(projects + `/getProject/${id}`).then((res) => {
        setProiect(res.data);
      });
    };
    getProject();

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

  const getsolves = async () => {
    await axios_config
      .get(`${projects}/${proiect.id}/${taskIndex}/getSolves`, {
        project_id: proiect.id,
        index_task: taskIndex,
      })
      .then((res) => {
        setSolves(res.data);
      })
      .catch((e) => {
        console.warn(e);
      });
  };

  useEffect(() => {
    if (display) {
      getsolves();
    }
  }, [display]);

  const deleteTask = async (taskID) => {
    await axios_config
      .delete(`${projects}/${proiect.id}/deleteTask/${taskID}`, {
        project_id: proiect.id,
        task_id: taskID,
      })
      .then(async (res) => {
        console.log(res);
        let tasks = proiect.tasks.filter((task) => task.id != taskID);
        setProiect({ ...proiect, tasks: tasks });
      });
  };

  const decideTask = async (decisionBoolean, username) => {
    await axios_config
      .post(`${projects}/${proiect.id}/${taskIndex}/decideSolve/${username}`, {
        decide: decisionBoolean,
      })
      .then((res) => {
        if (res.data != false) setSolves(res.data);
      })
      .catch((e) => {
        console.warn(e);
      });
  };

  return (
    <>
      <Side user={user} />

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
                <h3 className="h3">{proiect && proiect.title} </h3>
              </div>
              <div className="bottom">
                {proiect && solves && solves.length != 0 ? (
                  solves &&
                  solves.map((solve) => {
                    // if(!solve.accept)
                    return (
                      <div className="left">
                        <h4 className="buton">
                          <span className="green_text">@{solve.username}</span>
                        </h4>
                        <br />
                        <div className="title">
                          <img width={"100%"} src={solve.img} alt="" />
                        </div>
                        <div className="aprprove-denial">
                          <div
                            className="button but2"
                            onClick={() => {
                              decideTask(true, solve.username);
                            }}
                            style={{
                              background:
                                solve.viewed &&
                                solve.accept &&
                                "rgba(42, 255, 178, 0.4)",
                            }}
                          >
                            <h4 className="button">Approve</h4>
                          </div>
                          <div
                            className="button but1"
                            style={{
                              background:
                                solve.viewed &&
                                !solve.accept &&
                                "rgba(113, 32, 216, 0.4)",
                            }}
                            onClick={() => {
                              decideTask(false, solve.username);
                            }}
                          >
                            <h4 className="button">Reject</h4>
                          </div>
                        </div>
                      </div>
                    );
                  })
                ) : (
                  <h2 className="title">No solves submited for this task!</h2>
                )}
                {/* <div className="left">
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
              <h1 className="h1"> {proiect && proiect.title} </h1>
              <br />
              <p className="p1">
                You can verify your users tasks on this page by clicking on the
                task card from your community.
              </p>
              <br />
              <br />
              {proiect && (
                <>
                  <Link
                    to={`/create-task/${proiect.id}/${proiect.user_id}`}
                    style={{
                      display: "flex",
                      width: "150px",
                    }}
                  >
                    <div
                      className="button but2"
                      style={{
                        width: "150px",
                        padding: "8px 10px",
                        textAlign: "center",
                      }}
                    >
                      <h4 className="h2">Add task</h4>
                    </div>
                  </Link>
                </>
              )}
            </div>
          </header>
          <div className="line"></div>
          <div className="proiecte">
            {proiect &&
              proiect.tasks.map((task, index) => {
                return (
                  <div className="proiect">
                    {/* aici e buton de stergere daca e gen user proprietar */}
                    <div
                      className="delete button"
                      onClick={() => {
                        deleteTask(task.id);
                      }}
                    >
                      <h4 className="button">X</h4>
                    </div>

                    <h3 className="title">{task.title}</h3>
                    <p className="p1">{task.description}</p>
                    <div className="long">
                      <div
                        href=""
                        className="button but2"
                        onClick={() => discover(task, index)}
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
