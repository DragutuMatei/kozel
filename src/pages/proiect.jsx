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

function Project() {
  const { title } = useParams();

  const [display, setDisplay] = useState(false);
  const [proiect, setProiect] = useState(null);
  const discover = (proiect) => {
    setProiect(proiect);
    setDisplay(true);
  };
  const [tasks, setTasks] = useState({})

  useEffect(() => {
    axios_config.get(`${projects}/getProject/${title}`).then((res) => {
      if (res.data == false) {
        throw "No project with this id"
      } else {
        setProiect(res.data)
        console.log(res.data)
      }
    }).catch(e => {
      console.warn(e)
    })
  }, [])

  useEffect(() => {
    if (proiect)
      axios_config.get(`${projects}/${proiect.id}/getTasks`).then((res) => {
        setTasks(res.data)
      }).catch(e => { console.warn(e) })
  }, [proiect])

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
                <h3 className="h3">
                  {proiect.title} <span id="once">ONCE</span>{" "}
                </h3>
              </div>
              <div className="bottom">
                <div className="left">
                  {/* <Link to={"/auth"} className="button but1">
                    <h4 className="button">Login</h4>
                  </Link> */}
                  <h4 className="buton">
                    connected with <span className="green_text">@demo</span>
                  </h4>
                  <h3 className="title">MISSION 🎯</h3>
                  <p className="p1">{proiect.description}</p>
                  {proiect.list1 && (
                    <ol>
                      {proiect.list1.map((list, i) => {
                        return (
                          <li>
                            <p className="bold_p">{list}</p>
                          </li>
                        );
                      })}
                    </ol>
                  )}
                  {proiect.desc2 && <h3 className="h3">{proiect.desc2}</h3>}
                  {proiect.list2 && (
                    <ul>
                      {proiect.list2.map((list) => {
                        return (
                          <li>
                            <p className="bold_p">{list}</p>
                          </li>
                        );
                      })}
                    </ul>
                  )}
                  {proiect.link && (
                    <a href={proiect.link} className="green_text">
                      {proiect.link}
                    </a>
                  )}
                  <h3 className="title" style={{ marginTop: 50 }}>
                    To be checked 📝
                  </h3>
                  <p className="bold_p">Upload proof</p>
                  <input type="file" id="proof" onChange={handleProofChange} />
                  {/* <div className="line"></div>{" "}
                  <p className="p1" style={{ textAlign: "center" }}>
                    ⚠︎ After completion, it can take up to 10s before your claim
                    succeeds.
                  </p>
                  <h4 className="claim">Claim FS Token</h4> */}
                </div>
                <div className="right">
                  <h3 className="h3">Reward</h3>
                  <h3 className="title">
                    {proiect.reward}
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
            <Link to={"/admin/" + title}>
              <div className="button but1">
                <h4 className="button">Admin Panel</h4>
              </div>
            </Link>
          </div>
          <header>
            <img src={require("../assets/images/user.png")} alt="" />
            <div className="titles">
              <h1 className="h1"> {proiect ? proiect.title : null} </h1>
              <p className="p1">
                Explore our vision for a rewarding tomorrow, where engagement
                ignites innovation and collaboration leads the way. Join us as
                we forge a new path, shaping the future of community
                interaction, one task at a time.
              </p>
              <div className="l">
                <a href="https://twitter.com/fstlaneapp">
                  <RiTwitterXFill />
                </a>
                <a href="">
                  <BsDiscord />
                </a>
                <a href="">
                  <FaTelegramPlane />
                </a>
                <a href="">
                  <GiWallet />
                </a>
              </div>
              <div className="l">
                <Link to={"/proiect/test"} className="longl">
                  <BsMapFill id="map" />
                  <h3 className="p1">Quests</h3>
                </Link>
                <Link to={"/leaderboard"} className="longl">
                  <MdBarChart id="chart" />
                  <h3 className="p1">Leaderboard</h3>
                </Link>
                {/* aici pui tu chestia cu id */}
                <Link
                  to={"/create-task/" + title}
                  className="longl"
                  id="blur"
                >
                  {/* <Link to={"/create-task"} className="longl" id="blur"> */}
                  <FaEdit id="map" />
                  <h3 className="bold_p">Create task</h3>
                </Link>
              </div>
            </div>
          </header>
          <div className="line"></div>
          <div className="taske">
            {Object.keys(tasks).length > 0 && tasks.map((task) => {
              return (
                <div className="task">
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
                      <h4 className="button">Complete task</h4>
                    </div>
                    <div className="button but1" id="xp">
                      <h4 className="button">{task.reward}FS</h4>
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

export default Project;
