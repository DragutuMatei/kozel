import React, { useEffect, useMemo, useState } from "react";
import "../assets/style/page.scss";
import "../assets/style/home.scss";
import { RiTwitterXFill } from "react-icons/ri";
import { BsDiscord, BsStars } from "react-icons/bs";
import { AiOutlineUser, AiOutlineLink } from "react-icons/ai";
import json from "../utils/Projects.json";

import { Link } from "react-router-dom";
import Side from "../components/Side";
import axios_config from "../utils/AxiosConfig";
import { projects } from "../utils/Links";

function Home({ logout, user }) {
  const [tags, setTag] = useState([]);
  const [tagIndex, setTagIndex] = useState(0);
  const handleNextTag = () => {
    setTagIndex((prevIndex) => (prevIndex + 5) % tags.length);
  };

  const handlePrevTag = () => {
    setTagIndex((prevIndex) => (prevIndex - 5 + tags.length) % tags.length);
  };

  useEffect(() => {
    // make it array of tags
    setTag((prevTag) => [...prevTag, "Gaming"]);
    setTag((prevTag) => [...prevTag, "Startup"]);
    setTag((prevTag) => [...prevTag, "Music"]);
    setTag((prevTag) => [...prevTag, "Metaverse"]);
    setTag((prevTag) => [...prevTag, "Education"]);
    setTag((prevTag) => [...prevTag, "NFT"]);
  }, []);

  const [CardCategoryList, setCardCategoryList] = useState([]);

  const [selectedCategory, setSelectedCategory] = useState();

  var haveProject = true;
  const [myProjects, setMyProjects] = useState([]);
  // Add default value on page load

  const [all, setAll] = useState([]);
  useEffect(() => {
    // setCardCategoryList(json.projects);

    const getMy = async () => {
      if (user)
        await axios_config
          .get(projects + `/getByUser/${user.id}`)
          .then((res) => {
            console.log(res.data);
            setMyProjects(res.data);
          })
          .catch((err) => {
            console.log(err);
          });
    };
    const getAll = async () => {
      await axios_config
        .get(projects + "/getProjects")
        .then((res) => {
          setAll(res.data);
          setCardCategoryList(res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    };

    if (localStorage.getItem("logged") == "true") {
      console.log("ada");
      getMy();
    }
    getAll();
  }, [user]);

  // Function to get filtered list
  function getFilteredList() {
    // Avoid filter when selectedCategory is null
    // if (!selectedCategory || selectedCategory === "all") {
    //   return CardCategoryList;
    // }
    // return CardCategoryList.filter(
    //   (item) => item.category.toLowerCase() === selectedCategory.toLowerCase()
    // );
  }

  // Avoid duplicate function calls with useMemo
  var filteredList = useMemo(getFilteredList, [
    selectedCategory,
    CardCategoryList,
  ]);

  const delete_project = async (id) => {
    await axios_config.delete(projects + `/deleteProject/${id}`).then((res) => {
      setMyProjects((old) => old.filter((pr) => pr.id != id));
    });
  };

  return (
    <>
      <Side user={user} />

      <div className="ok sherlock">
        {/* <Nav /> */}
        <div className="page">
          {user !== "" ? (
            <div
              className="logout"
              style={{ zIndex: 50, position: "relative" }}
            >
              {/* <div className="button but2">
                  <h4 className="button">Connect with metamask</h4>
                </div> */}
              {/* <Link to="/auth"> */}
              <div className="button but1" onClick={logout}>
                <h4 className="button">Log out</h4>
              </div>
              {/* </Link> */}
            </div>
          ) : (
            <div
              className="logout"
              style={{ zIndex: 50, position: "relative" }}
            >
              <Link to={"/login"} className="login">
                <div className="button but1"
              style={{ zIndex: 50, position: "relative" }}>
                  <h4 className="button">Login</h4>
                </div>
              </Link>
              {/* <Link to={"/register"}>
                <div className="button but2">
                  <h4 className="button">Register</h4>
                </div>
              </Link> */}
            </div>
          )}

          <div className="home_overlay"></div>

          {localStorage.getItem("logged") == "true" && myProjects && (
            <>
              <header className="project_lui">
                <div className="titles">
                  <h1 className="h1">Your projects</h1>
                  <br />
                  <p className="p1">Manage your communities.</p>
                </div>
              </header>
              <div className="line"></div>
              <div className="proiecte_personale">
                {myProjects.map((proiect) => {
                  return (
                    <div className="proiect">
                      <div
                        // onClick={() => delete_project(proiect.id)}
                        className="delete button"
                      >
                        <h4 className="button">X</h4>
                      </div>
                      <Link to={"/proiect/" + proiect.id}>
                        <>
                          <h3 className="title">{proiect.title}</h3>
                          <p className="p1">
                            {proiect.description.length > 100
                              ? proiect.description.substring(0, 100) + " ..."
                              : proiect.description}
                          </p>
                          <div className="long">
                            <div
                            // href={proiect.link}
                            >
                              <div className="button but1">
                                <h4 className="button">
                                  <AiOutlineLink />
                                </h4>
                              </div>
                            </div>
                            <div className="button but1">
                              <h4 className="button">
                                <AiOutlineUser /> &ensp; {proiect.users.length}
                              </h4>
                            </div>
                            <a href={proiect.twitter} className="button but1">
                              <h4 className="button">
                                <RiTwitterXFill />
                              </h4>
                            </a>
                            <a href={proiect.discord} className="button but1">
                              <h4 className="button">
                                <BsDiscord />
                              </h4>
                            </a>
                          </div>
                        </>
                      </Link>
                    </div>
                  );
                })}
              </div>
              <div className="line"></div>
            </>
          )}
          <header>
            <div className="titles">
              <h1 className="h1">Community projects</h1>
              <br />
              <p className="p1">
                Explore our community projects and earn FS tokens.
              </p>

              <div className="l tags">
                {/* <Link to={"/"} className="shortL">
                  <BsStars />
                  <h3 className="p1">New</h3>
                </Link> */}
                <div
                  className="shortL button but3"
                  onClick={() => setSelectedCategory("all")}
                >
                  <BsStars />
                  <h3 className="p1">All</h3>
                </div>
                {tags.slice(tagIndex, tagIndex + 6).map((tag) => {
                  return (
                    <div
                      className="shortL button but3"
                      // onClick={() => setSelectedCategory(tag)}
                    >
                      <BsStars />
                      <h3 className="p1">{tag}</h3>
                    </div>
                  );
                })}
                <div className="buttonlist">
                  <button
                    // onClick={handlePrevTag}
                    className="button but2"
                  >
                    Prev
                  </button>
                  <button
                    // onClick={handleNextTag}
                    className="button but1"
                  >
                    Next
                  </button>
                </div>
              </div>
            </div>
          </header>

          <div className="line"></div>
          <div className="proiecte">
            {CardCategoryList &&
              CardCategoryList.map((proiect) => {
                return (
                  <Link to={"/proiect/" + proiect.id} className="proiect">
                    <>
                      <h3 className="title">{proiect.title}</h3>
                      <p className="p1">
                        {proiect.description.length > 100
                          ? proiect.description.substring(0, 100) + " ..."
                          : proiect.description}
                      </p>
                      <div className="long">
                        <a href={proiect.link}>
                          <div className="button but1">
                            <h4 className="button">
                              <AiOutlineLink />
                            </h4>
                          </div>
                        </a>
                        <div className="button but1">
                          <h4 className="button">
                            <AiOutlineUser /> &ensp; {proiect.users.length}
                          </h4>
                        </div>
                        <div className="button but1">
                          <h4 className="button">
                            {" "}
                            <RiTwitterXFill />
                          </h4>
                        </div>
                        <div className="button but1">
                          <h4 className="button">
                            <BsDiscord />
                          </h4>
                        </div>
                      </div>
                    </>
                  </Link>
                );
              })}
          </div>
        </div>
      </div>
    </>
  );
}

export default Home;
