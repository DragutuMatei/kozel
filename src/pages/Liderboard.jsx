import React from "react";
import Nav from "../components/Nav";
import "../assets/style/page.scss";
import { RiTwitterXFill } from "react-icons/ri";
import { BsDiscord, BsMapFill } from "react-icons/bs";
import { FaEdit, FaTelegramPlane } from "react-icons/fa";
import { GiWallet } from "react-icons/gi";
import Side from "../components/Side";
import { MdBarChart } from "react-icons/md";
import { Link } from "react-router-dom";

function Liderboard() {
  return (
    <>
      <Side />

      <div className="ok">
        <div className="page">
          <header>
            <img src={require("../assets/images/user.png")} alt="" />
            <div className="titles">
              <h1 className="h1">Fast Lane</h1>
              <p className="p1">
                EFSlore our vision for a rewarding tomorrow, where engagement
                ignites innovation and collaboration leads the way. Join us as
                we forge a new path, shaping the future of community
                interaction, one task at a time.
              </p>
              <div className="l logos">
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
                <Link to={"/dashboard/test"} className="longl">
                  <BsMapFill id="map" />
                  <h3 className="p1">Quests</h3>
                </Link>
                <Link to={"/leaderboard"} className="longl">
                  <MdBarChart id="chart" />
                  <h3 className="p1">Leaderboard</h3>
                </Link>

                <Link to={"#"} className="longl" id="blur">
                  <FaEdit id="map" />
                  <h3 className="p1">Creators</h3>
                </Link>
              </div>
            </div>
          </header>
          <div className="line"></div>
          <div className="board">
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
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default Liderboard;
