import React, { useEffect, useState } from "react";
import Nav from "../components/Nav";
import "../assets/style/page.scss";
import { RiTwitterXFill } from "react-icons/ri";
import { BsDiscord, BsMapFill } from "react-icons/bs";
import { FaEdit, FaTelegramPlane } from "react-icons/fa";
import { GiWallet } from "react-icons/gi";
import Side from "../components/Side";
import { MdBarChart } from "react-icons/md";
import { Link, useParams } from "react-router-dom";
import axios_config from "../utils/AxiosConfig";
import { projects } from "../utils/Links";
import {
  calculateUserPoints,
  extractTasks,
  hasValues,
  sortObjectToArray,
} from "../utils/flatter";

function Liderboard({ user }) {
  const { id } = useParams();
  const [tasks, setTasks] = useState([]);
  const [proiect, setProiect] = useState(null);

  const [isAdmin, setIsAdmin] = useState(false);
  const [isPresent, setIsPresent] = useState(false);

  const [completes, setCompletes] = useState([]);
  const [scores, setScores] = useState([]);

  const getProject = async () => {
    await axios_config.get(projects + `/getProject/${id}`).then(async (res) => {
      setProiect(res.data);
      let aux = [];
      aux.push(res.data);
      setTasks(extractTasks(aux));
      if (res.data.user_id == user.id) {
        setIsAdmin(true);
        setIsPresent(true);
      } else
        await axios_config.get(projects + `/${id}/getUsers`).then((res) => {
          if (res.data.length != 0) {
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
  }, [, user]);

  useEffect(() => {
    if (tasks.length) {
      setCompletes(calculateUserPoints(tasks));
      let score = calculateUserPoints(tasks);
      score = score.userPoints;
      score = sortObjectToArray(score);
      score = score.reverse();
      setScores(score);
    }
  }, [tasks]);

  // useEffect(() => {
  //   if (completes) {
  //     //console.log(hasValues(completes))
  //     // let score = completes.userPoints
  //     Object.keys(score).forEach(index => {
  //       //console.log(index, score[index])
  //     })
  //   }
  // }, [completes])

  return (
    <>
      <Side user={user} />

      <div className="ok">
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
                <a href={proiect && proiect.twitter}>
                  <RiTwitterXFill />
                </a>{" "}
                <a href={proiect && proiect.discord}>
                  <BsDiscord />
                </a>
                <a href={proiect && proiect.telegram}>
                  <FaTelegramPlane />
                </a>{" "}
                <a href={proiect && proiect.wallet}>
                  <GiWallet />
                </a>
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
          <div className="board">
            {hasValues(completes) &&
              scores.map((index, place) => {
                //console.log(completes)
                return (
                  <>
                    <div className="om">
                      <div className="l">
                        <h2 className="title purple_text">{place + 1}</h2>
                        <div className="pers">
                          <h3 className="title">@{index[0]}</h3>
                        </div>
                      </div>
                      <div className="r">
                        <h4 className="bold_p">
                          {completes.userPoints[index[0]]}
                        </h4>
                        <div className="vert_l"></div>
                        <h4 className="bold_p purple_text">
                          {completes.completeTasks[index[0]]}
                        </h4>
                      </div>
                    </div>{" "}
                  </>
                );
              })}
            {/* <div className="om">
              <div className="l">
                <h2 className="title purple_text">1</h2>
                <div className="pers">
                  <img
                    src="https://crew3-production.s3.eu-west-3.amazonaws.com/public/6ad99add-b90b-4e00-82a9-558dea258edc-TOMMY.jpeg"
                    alt=""
                  />
                  <h3 className="title">Shelbygrayman</h3>
                </div>
              </div>
              <div className="r">
                <h4 className="bold_p">23 quests</h4>
                <div className="vert_l"></div>
                <h4 className="bold_p purple_text">1200 FS</h4>
              </div>
            </div>{" "}
            <div className="om">
              <div className="l">
                <h2 className="title purple_text">2</h2>
                <div className="pers">
                  <img
                    src="https://crew3-production.s3.eu-west-3.amazonaws.com/public/6ad99add-b90b-4e00-82a9-558dea258edc-TOMMY.jpeg"
                    alt=""
                  />
                  <h3 className="title">Shelbygrayman</h3>
                </div>
              </div>
              <div className="r">
                <h4 className="bold_p">23 quests</h4>
                <div className="vert_l"></div>
                <h4 className="bold_p purple_text">1100 FS</h4>
              </div>
            </div>{" "}
            <div className="om">
              <div className="l">
                <h2 className="title purple_text">3</h2>
                <div className="pers">
                  <img
                    src="https://crew3-production.s3.eu-west-3.amazonaws.com/public/6ad99add-b90b-4e00-82a9-558dea258edc-TOMMY.jpeg"
                    alt=""
                  />
                  <h3 className="title">Shelbygrayman</h3>
                </div>
              </div>
              <div className="r">
                <h4 className="bold_p">23 quests</h4>
                <div className="vert_l"></div>
                <h4 className="bold_p purple_text">1000 FS</h4>
              </div>
            </div>{" "}
            <div className="om">
              <div className="l">
                <h2 className="title purple_text">4</h2>
                <div className="pers">
                  <img
                    src="https://crew3-production.s3.eu-west-3.amazonaws.com/public/6ad99add-b90b-4e00-82a9-558dea258edc-TOMMY.jpeg"
                    alt=""
                  />
                  <h3 className="title">Shelbygrayman</h3>
                </div>
              </div>
              <div className="r">
                <h4 className="bold_p">23 quests</h4>
                <div className="vert_l"></div>
                <h4 className="bold_p purple_text">900 FS</h4>
              </div>
            </div>{" "}
            <div className="om">
              <div className="l">
                <h2 className="title purple_text">5</h2>
                <div className="pers">
                  <img
                    src="https://crew3-production.s3.eu-west-3.amazonaws.com/public/6ad99add-b90b-4e00-82a9-558dea258edc-TOMMY.jpeg"
                    alt=""
                  />
                  <h3 className="title">Shelbygrayman</h3>
                </div>
              </div>
              <div className="r">
                <h4 className="bold_p">23 quests</h4>
                <div className="vert_l"></div>
                <h4 className="bold_p purple_text">800 FS</h4>
              </div>
            </div>{" "}
            <div className="om">
              <div className="l">
                <h2 className="title purple_text">6</h2>
                <div className="pers">
                  <img
                    src="https://crew3-production.s3.eu-west-3.amazonaws.com/public/6ad99add-b90b-4e00-82a9-558dea258edc-TOMMY.jpeg"
                    alt=""
                  />
                  <h3 className="title">Shelbygrayman</h3>
                </div>
              </div>
              <div className="r">
                <h4 className="bold_p">23 quests</h4>
                <div className="vert_l"></div>
                <h4 className="bold_p purple_text">700 FS</h4>
              </div>
            </div>{" "}
            <div className="om">
              <div className="l">
                <h2 className="title purple_text">7</h2>
                <div className="pers">
                  <img
                    src="https://crew3-production.s3.eu-west-3.amazonaws.com/public/6ad99add-b90b-4e00-82a9-558dea258edc-TOMMY.jpeg"
                    alt=""
                  />
                  <h3 className="title">Shelbygrayman</h3>
                </div>
              </div>
              <div className="r">
                <h4 className="bold_p">23 quests</h4>
                <div className="vert_l"></div>
                <h4 className="bold_p purple_text">600 FS</h4>
              </div>
            </div>{" "}
            <div className="om">
              <div className="l">
                <h2 className="title purple_text">8</h2>
                <div className="pers">
                  <img
                    src="https://crew3-production.s3.eu-west-3.amazonaws.com/public/6ad99add-b90b-4e00-82a9-558dea258edc-TOMMY.jpeg"
                    alt=""
                  />
                  <h3 className="title">Shelbygrayman</h3>
                </div>
              </div>
              <div className="r">
                <h4 className="bold_p">23 quests</h4>
                <div className="vert_l"></div>
                <h4 className="bold_p purple_text">500 FS</h4>
              </div>
            </div>
            <div className="om">
              <div className="l">
                <h2 className="title purple_text">1</h2>
                <div className="pers">
                  <img
                    src="https://crew3-production.s3.eu-west-3.amazonaws.com/public/6ad99add-b90b-4e00-82a9-558dea258edc-TOMMY.jpeg"
                    alt=""
                  />
                  <h3 className="title">Shelbygrayman</h3>
                </div>
              </div>
              <div className="r">
                <h4 className="bold_p">23 quests</h4>
                <div className="vert_l"></div>
                <h4 className="bold_p purple_text">1200 FS</h4>
              </div>
            </div>{" "}
            <div className="om">
              <div className="l">
                <h2 className="title purple_text">2</h2>
                <div className="pers">
                  <img
                    src="https://crew3-production.s3.eu-west-3.amazonaws.com/public/6ad99add-b90b-4e00-82a9-558dea258edc-TOMMY.jpeg"
                    alt=""
                  />
                  <h3 className="title">Shelbygrayman</h3>
                </div>
              </div>
              <div className="r">
                <h4 className="bold_p">23 quests</h4>
                <div className="vert_l"></div>
                <h4 className="bold_p purple_text">1100 FS</h4>
              </div>
            </div>{" "}
            <div className="om">
              <div className="l">
                <h2 className="title purple_text">3</h2>
                <div className="pers">
                  <img
                    src="https://crew3-production.s3.eu-west-3.amazonaws.com/public/6ad99add-b90b-4e00-82a9-558dea258edc-TOMMY.jpeg"
                    alt=""
                  />
                  <h3 className="title">Shelbygrayman</h3>
                </div>
              </div>
              <div className="r">
                <h4 className="bold_p">23 quests</h4>
                <div className="vert_l"></div>
                <h4 className="bold_p purple_text">1000 FS</h4>
              </div>
            </div>{" "}
            <div className="om">
              <div className="l">
                <h2 className="title purple_text">4</h2>
                <div className="pers">
                  <img
                    src="https://crew3-production.s3.eu-west-3.amazonaws.com/public/6ad99add-b90b-4e00-82a9-558dea258edc-TOMMY.jpeg"
                    alt=""
                  />
                  <h3 className="title">Shelbygrayman</h3>
                </div>
              </div>
              <div className="r">
                <h4 className="bold_p">23 quests</h4>
                <div className="vert_l"></div>
                <h4 className="bold_p purple_text">900 FS</h4>
              </div>
            </div>{" "}
            <div className="om">
              <div className="l">
                <h2 className="title purple_text">5</h2>
                <div className="pers">
                  <img
                    src="https://crew3-production.s3.eu-west-3.amazonaws.com/public/6ad99add-b90b-4e00-82a9-558dea258edc-TOMMY.jpeg"
                    alt=""
                  />
                  <h3 className="title">Shelbygrayman</h3>
                </div>
              </div>
              <div className="r">
                <h4 className="bold_p">23 quests</h4>
                <div className="vert_l"></div>
                <h4 className="bold_p purple_text">800 FS</h4>
              </div>
            </div>{" "}
            <div className="om">
              <div className="l">
                <h2 className="title purple_text">6</h2>
                <div className="pers">
                  <img
                    src="https://crew3-production.s3.eu-west-3.amazonaws.com/public/6ad99add-b90b-4e00-82a9-558dea258edc-TOMMY.jpeg"
                    alt=""
                  />
                  <h3 className="title">Shelbygrayman</h3>
                </div>
              </div>
              <div className="r">
                <h4 className="bold_p">23 quests</h4>
                <div className="vert_l"></div>
                <h4 className="bold_p purple_text">700 FS</h4>
              </div>
            </div>{" "}
            <div className="om">
              <div className="l">
                <h2 className="title purple_text">7</h2>
                <div className="pers">
                  <img
                    src="https://crew3-production.s3.eu-west-3.amazonaws.com/public/6ad99add-b90b-4e00-82a9-558dea258edc-TOMMY.jpeg"
                    alt=""
                  />
                  <h3 className="title">Shelbygrayman</h3>
                </div>
              </div>
              <div className="r">
                <h4 className="bold_p">23 quests</h4>
                <div className="vert_l"></div>
                <h4 className="bold_p purple_text">600 FS</h4>
              </div>
            </div>{" "}
            <div className="om">
              <div className="l">
                <h2 className="title purple_text">8</h2>
                <div className="pers">
                  <img
                    src="https://crew3-production.s3.eu-west-3.amazonaws.com/public/6ad99add-b90b-4e00-82a9-558dea258edc-TOMMY.jpeg"
                    alt=""
                  />
                  <h3 className="title">Shelbygrayman</h3>
                </div>
              </div>
              <div className="r">
                <h4 className="bold_p">23 quests</h4>
                <div className="vert_l"></div>
                <h4 className="bold_p purple_text">500 FS</h4>
              </div>
            </div> */}
          </div>
        </div>
      </div>
    </>
  );
}

export default Liderboard;
