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
import axios_config from "../utils/AxiosConfig";
import { projects } from "../utils/Links";
import { isRegisterWithTwitter } from "../utils/Poropescovici";

function Project({ user }) {
  const { id } = useParams();
  const [isProofSubmited, setSubmitedProof] = useState(false);
  const [display, setDisplay] = useState(false);
  const [proiect, setProiect] = useState(null);
  const [task, setTask] = useState(null);
  const [taskIndex, setTaskIndex] = useState(null);
  const [ok, setOk] = useState(false);

  const discover = (task, index) => {
    setTaskIndex(index);
    setTask(task);
    setOk(false);
    //console.log(task);
    setDisplay(true);
    //console.log(task.solves.length);
    //console.log(ok);
    for (let i = 0; i < task.solves.length; i++) {
      const element = task.solves[i];
      if (element.username == user.username) {
        setOk(element.accept);
        //console.log("=-============", element.accept);
      }
    }
  };

  // aici faci tu altfel cu id
  // var id_task = 0;

  const [proof, setProof] = useState("");
  function handleProofChange(e) {
    const file = e.target.files[0];
    if (file) {
      let imageBase64Stringsep, base64String;
      let reader = new FileReader();

      reader.onload = function () {
        base64String = reader.result;
        imageBase64Stringsep = base64String;

        setProof(base64String);
        setSubmitedProof(true);
      };
      reader.readAsDataURL(file);
    }
  }

  const [xUsername, setxUsername] = useState("");
  function handleUsernameChange(e) {
    setxUsername(e.target.value);
  }
  const isTwitter = isRegisterWithTwitter();

  const [msg, setMsg] = useState("");
  const sendSolve = async () => {
    console.log(task.type)
    if (task.type == "other") {
      await axios_config
        .post(projects + `/${proiect.id}/${taskIndex}/addOtherSolve`, {
          project_id: proiect.id,
          index_task: taskIndex,
          username: user.username,
          img: proof,
        })
        .then((res) => {
          //console.log(res.data);
          if (res.data) setMsg("Proof submited!");
          else setMsg("Proof not submited!");
        })
        .catch((e) => {
          console.warn(e);
        });
    } else if (task.type == "like") {
      await axios_config
        .post(
          projects + `/${task.link}/${proiect.id}/${taskIndex}/addAutoSolve`,
          {
            xusername: xUsername,
            username: user.username,
          }
        )
        .then((res) => {
          //console.log(res.data);
          if (res.data) setMsg("Validated!");
          else setMsg("Not validated!");
        })
        .catch((e) => {
          console.warn(e);
        });
    } else if (task.type == "tweet") {
      handleTweetButtonClick(task.link);
      await axios_config
        .post(projects + `/${proiect.id}/${taskIndex}/addSimpleAutoSolve`, {
          xusername: xUsername,
          username: user.username,
        })
        .then((res) => {
          console.log(res);
          setMsg("To be verified!");
        });
    } else if (task.type == "follow") {
      await axios_config
      .get(
        `/projects/check-user-subscribed/${localStorage.getItem(
          "oauth_token"
        )}/${localStorage.getItem(
          "oauth_token_secret"
        )}/${xUsername}/${user.username}/${proiect.id}/${taskIndex}`
      )
      .then((res) => {
        console.log(res);
        if (res.data) setMsg("Validated!");
        else setMsg("Not validated!");
     });
    }
  };

  const [isAdmin, setIsAdmin] = useState(false);
  const [isPresent, setIsPresent] = useState(false);

  // useEffect(() => {
  //   const keyDownHandler = (event) => {
  //     if (event.key === "Escape") {
  //       event.preventDefault();
  //       setDisplay(false);
  //     }
  //   };

  //   document.addEventListener("keydown", keyDownHandler);

  //   return () => {
  //     document.removeEventListener("keydown", keyDownHandler);
  //   };
  // }, []);

  const getProject = async () => {
    //console.log("asdad");
    await axios_config.get(projects + `/getProject/${id}`).then(async (res) => {
      setProiect(res.data);
      if (res.data.user_id == user.id) {
        setIsAdmin(true);
        setIsPresent(true);
        //console.log("asddddddddddddddddddd");
      } else
        await axios_config.get(projects + `/${id}/getUsers`).then((res) => {
          //console.log("ooooooooooooooooooo");
          //console.log(res.data);
          if (res.data.length != 0) {
            if (res.data != false)
              res.data.forEach((users) => {
                if (user.id == users.id) {
                  setIsPresent(true);
                }
              });
          }
        });
    });
  };

  useEffect(() => {
    getProject();
    //console.log("---------------------------------------------------");
  }, [, id, window, user]);

  const handleTweetButtonClick = (text) => {
    const tweetIntentUrl = `https://twitter.com/intent/tweet?text=${encodeURIComponent(
      text
    )}`;
    window.open(tweetIntentUrl, "_blank");
  };

  return (
    <>
      <Side user={user} />

      <div className="ok">
        {display && task && (
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
                <h3 className="h3">
                  {task.title} <span id="once">ONCE</span>{" "}
                </h3>
              </div>
              <div className="bottom">
                <div className="left">
                  {/* <Link to={"/auth"} className="button but1">
                    <h4 className="button">Login</h4>
                  </Link> */}
                  <h4 className="buton">
                    connected with{" "}
                    <span className="green_text">@{user.username}</span>
                  </h4>
                  <h3 className="title">MISSION 🎯</h3>
                  <p className="p1">{task.description}</p>
                
                  <div className="long">
                    <h3 className="title" style={{ marginTop: 50 }}>
                      Waiting for validation 📝
                    </h3>
                    {task && task.type === "other" ? (
                      <>
                        <p className="bold_p">Upload proof</p>
                        {!ok && (
                          <input
                            type="file"
                            id="proof"
                            onChange={handleProofChange}
                          />
                        )}
                        {!ok && isProofSubmited && (
                          <>
                            <button
                              className="button but2 claim"
                              onClick={() => {
                                sendSolve();
                              }}
                            >
                              <h6 className="h7">Submit</h6>
                            </button>
                          </>
                        )}
                        {msg != "" && <p className="p1">{msg}</p>}
                        <br />
                        <br />
                        {ok ? (
                          <p className="p1">Accepted</p>
                        ) : (
                          <p className="p1">Not accepted yet</p>
                        )}
                      </>
                    ) : task.type == "follow" ? (<>
                    
                        {
                          isTwitter ? <>
                          <input
                            type="text"
                            id="proof"
                            placeholder="@x_username"
                            onChange={handleUsernameChange}
                            />
                            <button
                              className="button but2 claim"
                              onClick={() => {
                                sendSolve();
                              }}
                            >
                              <h6 className="h7">Submit</h6>
                            </button>
                          </> : <>
                              <br />
                          <button
                              className="button but1"
                              onClick={async () => {
                                let a = window.location.pathname.substring(
                                  1,
                                  window.location.pathname.length
                                );
                                await axios_config
                                  .get(
                                    `/projects/oauth2/authorize/normal/twitter/${a}`
                                  )
                                  .then(async (res) => {
                                    console.log(res);
                                    window.open(res.data, "_blank");
                                  });
                              }}
                            >
                              <h6 className="h7">Register with Twitter</h6>
                            </button>
                              
                            </>
                        }
                        
                        
                      </>) : (
                      <>
                        <p className="bold_p">X username</p>
                        {!ok && (
                          <input
                            type="text"
                            id="proof"
                            placeholder="@x_username"
                            onChange={handleUsernameChange}
                          />
                        )}

                        {!ok && (
                          <>
                            <button
                              className="button but2 claim"
                              onClick={() => {
                                sendSolve();
                              }}
                            >
                              <h6 className="h7">Submit</h6>
                            </button>
                          </>
                        )}
                        {msg != "" && <p className="p1">{msg}</p>}
                        <br />
                        <br />
                      </>
                    )}{" "}
                  </div> 
                </div>
                <div className="right">
                  <h3 className="h3">Reward</h3>
                  <h3 className="title">
                    {task.reward}
                    <img
                      src={require("../assets/images/icon_logo.svg").default}
                      alt=""
                    />
                  </h3>
                </div>
              </div>
            </div>
          </div>
        )}
        {/* <Nav /> */}
        <div className="page">
          <div className="logout">
            {/* <div className="button but2">
              <h4 className="button">Connect with metamask</h4>
            </div>  */}
            {proiect && isAdmin && (
              <Link to={"/admin/" + proiect.id}>
                <div className="button but1">
                  <h4 className="button">Admin Panel</h4>
                </div>
              </Link>
            )}
          </div>
          <header>
            <img src={`${proiect ? proiect.img : null}`} alt="" />
            <div className="titles">
              <h1 className="h1"> {proiect && proiect.title} </h1>
              <p className="p1">{proiect && proiect.description}</p>
              <div className="l">
                <a target="_blank" href={proiect && proiect.twitter}>
                  <RiTwitterXFill />
                </a>{" "}
                {/* <a href={proiect && proiect.discord}>
                  <BsDiscord />
                </a> */}
                <a target="_blank" href={proiect && proiect.telegram}>
                  <FaTelegramPlane />
                </a>{" "}
                {/* <a href={proiect && proiect.wallet}>
                  <GiWallet />
                </a> */}
              </div>
              <div className="l">
                <Link to={`/proiect/${id}`} className="longl">
                  <BsMapFill id="map" />
                  <h3 className="p1">Quests</h3>
                </Link>
                <Link to={`/leaderboard/${id}`} className="longl">
                  <MdBarChart id="chart" />
                  <h3 className="p1">Leaderboard</h3>
                </Link>
                {/* aici pui tu chestia cu id */}

                {isAdmin
                  ? proiect && (
                      <Link
                        to={
                          "/create-task/" +
                          (proiect && proiect.id) +
                          "/" +
                          (proiect && proiect.user_id)
                        }
                        className="longl"
                        id="blur"
                      >
                        {/* <Link to={"/create-task"} className="longl" id="blur"> */}
                        <FaEdit id="map" />
                        <h3 className="bold_p">Create task</h3>
                      </Link>
                    )
                  : proiect &&
                    user &&
                    !isPresent && (
                      <Link
                        to=""
                        className="longl"
                        onClick={async () => {
                          await axios_config
                            .post(projects + `/${proiect.id}/addUser`, {
                              user_id: user.id,
                            })
                            .then(async (res) => {
                              await getProject();
                              alert("Joined community");
                            });
                        }}
                      >
                        <h3 className="bold_p">Join Community</h3>
                      </Link>
                    )}
              </div>
            </div>
          </header>
          <div className="line"></div>
          <div className="taske">
            {/* isPresent || isAdmin */}
            {true ? (
              proiect &&
              proiect.tasks.map((task, index) => {
                return (
                  <div className="task">
                    {isAdmin && (
                      <div className="delete button">
                        <h4 className="button">X</h4>
                      </div>
                    )}
                    <h3 className="title">{task.title}</h3>
                    <p className="p1">{task.description}</p>
                    <div className="long">
                      {isPresent && (
                        <div
                          className="button but2"
                          onClick={() => discover(task, index)}
                        >
                          <h4 className="button">Complete task</h4>
                        </div>
                      )}{" "}
                      <div className="button but1" id="xp">
                        <h4 className="button">{task.reward}FS</h4>
                      </div>
                    </div>
                  </div>
                );
              })
            ) : (
              <h2 className="title" style={{ color: "white" }}>
                Join community first!
              </h2>
            )}
          </div>
        </div>
      </div>
    </>
  );
}

export default Project;
