import React, { useEffect, useMemo, useState } from "react";
import "../assets/style/page.scss";
import { RiTwitterXFill } from "react-icons/ri";
import { BsDiscord, BsStars } from "react-icons/bs";
import { AiOutlineUser, AiOutlineLink } from "react-icons/ai";
import json from "../utils/Projects.json";

import { Link } from "react-router-dom";
import Side from "../components/Side";
import axios_config from "../utils/AxiosConfig";
import { projects } from "../utils/Links";

function Home({ user, logout }) {
  const [tags, setTag] = useState(["Gaming", "Startup", "Music", "Metaverse", "Education", "NFT"]);
  const [tagIndex, setTagIndex] = useState(0);
  const handleNextTag = () => {
    setTagIndex((prevIndex) => (prevIndex + 5) % tags.length);
  };

  const handlePrevTag = () => {
    setTagIndex((prevIndex) => (prevIndex - 5 + tags.length) % tags.length);
  };

  // useEffect(() => {
  //   // make it array of tags
  //   setTag((prevTag) => [...prevTag, "Gaming"]);
  //   setTag((prevTag) => [...prevTag, "Startup"]);
  //   setTag((prevTag) => [...prevTag, "Music"]);
  //   setTag((prevTag) => [...prevTag, "Metaverse"]);
  //   setTag((prevTag) => [...prevTag, "Education"]);
  //   setTag((prevTag) => [...prevTag, "NFT"]);
  // }, []);

  const [CardCategoryList, setCardCategoryList] = useState([]);

  const [selectedCategory, setSelectedCategory] = useState();

  // Add default value on page load
  // TEO replace this with fetch
  useEffect(() => {
    // setCardCategoryList(json.projects);
    try {
      axios_config.get(`${projects}/getProjects`).then((res) => {
        console.log(res)
        if (res.status == 401) {
          console.log('atuh')
        } else {
          setCardCategoryList(res.data)
          console.log(res.data)
        }
      })
      // .catch((e)=>{
      //   console.log(e)
      // })
    } catch (e) {
      console.warn(e)
    }
  }, []);

  // Function to get filtered list
  function getFilteredList() {
    // Avoid filter when selectedCategory is null
    if (!selectedCategory || selectedCategory === "all") {
      return CardCategoryList;
    }
    return CardCategoryList.filter(
      (item) => item.category.toLowerCase() === selectedCategory.toLowerCase()
    );
  }

  // Avoid duplicate function calls with useMemo
  var filteredList = useMemo(getFilteredList, [
    selectedCategory,
    CardCategoryList,
  ]);

  // const [categoryList, setCategoryList] = useState([]);

  // function getFilteredCategoryList() {
  //   if (!categoryList || category === "all") {
  //     return setCategoryList(json.projects);
  //   } else {
  //     return setCategoryList(
  //       json.projects.filter((proiect) => proiect.tags.includes(category))
  //     );
  //   }
  // }
  // var filteredCategoryList = useMemo(getFilteredCategoryList, [category, tags]);
  return (
    <>
      <Side />

      <div className="ok">
        {/* <Nav /> */}
        <div className="page">
          {user !== "" ? (
            <div className="logout">
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
            <div className="logout">
              <Link to={"/login"}>
                <div className="button but1">
                  <h4 className="button">Login</h4>
                </div>
              </Link>
              <Link to={"/register"}>
                <div className="button but2">
                  <h4 className="button">Register</h4>
                </div>
              </Link>
            </div>
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
                {tags.slice(tagIndex, tagIndex + 6).map((tag, index) => {
                  return (
                    <div
                      key={index}
                      className="shortL button but3"
                      onClick={() => setSelectedCategory(tag)}
                    >
                      <BsStars />
                      <h3 className="p1">{tag}</h3>
                    </div>
                  );
                })}
                <div className="buttonlist">
                  <button onClick={handlePrevTag} className="button but2">
                    Prev
                  </button>
                  <button onClick={handleNextTag} className="button but1">
                    Next
                  </button>
                </div>
              </div>
            </div>
          </header>

          <div className="line"></div>
          <div className="proiecte">
            {filteredList.map((proiect) => {
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
                          <AiOutlineUser /> &ensp; {proiect.users}
                        </h4>
                      </div>
                      <div className="button but1">
                        <h4 className="button">
                          {" "}
                          <RiTwitterXFill /> &ensp; {proiect.twitter}
                        </h4>
                      </div>
                      <div className="button but1">
                        <h4 className="button">
                          <BsDiscord />
                          &ensp; {proiect.discord}
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
