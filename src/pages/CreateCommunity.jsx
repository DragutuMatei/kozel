import React, { useEffect, useState } from "react";

import "../assets/style/login.scss";
import { Link, useNavigate } from "react-router-dom";
import axios_config from "../utils/AxiosConfig";
import { projects } from "../utils/Links";

function CreateCommunity({ user }) {
  const [tags, setTags] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    if (localStorage.getItem("logged") == "false") {
      navigate("/");
    }
  }, [user]);

  useEffect(() => {
    // make it array of tags
    setTags((prevTag) => [...prevTag, "Choose"]);
    setTags((prevTag) => [...prevTag, "Gaming"]);
    setTags((prevTag) => [...prevTag, "Startup"]);
    setTags((prevTag) => [...prevTag, "Music"]);
    setTags((prevTag) => [...prevTag, "Metaverse"]);
    setTags((prevTag) => [...prevTag, "Education"]);
    setTags((prevTag) => [...prevTag, "NFT"]);
  }, []);

  const [name, setName] = useState("");
  function handleNameChange(e) {
    setName(e.target.value);
  }

  const [description, setDescription] = useState("");
  function handleDescriptionChange(e) {
    setDescription(e.target.value);
  }

  const [website, setWebsite] = useState("");
  function handleWebsiteChange(e) {
    setWebsite(e.target.value);
  }

  function imageUploaded(file) {}
  const [img, setImg] = useState("");

  const [logo, setLogo] = useState("");
  function handleLogoChange(e) {
    // setLogo(e.target.files[0]);
    // //console.log(e.target.files[0]);
    const file = e.target.files[0];

    let imageBase64Stringsep, base64String;
    let reader = new FileReader();
    //console.log("next");

    reader.onload = function () {
      base64String = reader.result;

      imageBase64Stringsep = base64String;

      setImg(base64String);
    };
    reader.readAsDataURL(file);
  }

  const [category, setCategory] = useState("");
  function handleTagChange(e) {
    setCategory(e.target.value);
  }

  const [twitter, setTwitter] = useState("");
  function handleTwitterChange(e) {
    setTwitter(e.target.value);
  }

  const [discord, setDiscord] = useState("");
  function handleDiscordChange(e) {
    setDiscord(e.target.value);
  }

  const [telegram, setTelegram] = useState("");
  function handleTelegramChange(e) {
    setTelegram(e.target.value);
  }

  const [wallet, setWallet] = useState("");
  function handleWalletChange(e) {
    setWallet(e.target.value);
  }

  const [msg, setMsg] = useState("");

  const createcom = async () => {
    let requirements = [
      img,
      name,
      user.id,
      description,
      website,
      category,
      twitter,
      discord,
      telegram,
      wallet,
    ];
    let ok = true;
    requirements.forEach((req) => {
      if (req == undefined || req == null || req == "") {
        ok = false;
        setMsg("Complete all fields");
      }
    });
    if (ok)
      await axios_config
        .post(projects + "/addProject", {
          img,
          title: name,
          user_id: user.id,
          description,
          link: website,
          category,
          twitter,
          discord,
          telegram,
          wallet,
        })
        .then((res) => {
          //console.log(res);
          setMsg("Community added succesfully!");
          navigate(-1);
        })
        .catch((err) => {
          setMsg("Complete all fields!");
          //console.log(err);
        });
  };

  return (
    <div className="auth">
      <div className="content">
        <div className="top">
          <Link to="/">
            <img
              src={require("../assets/images/icon_logo.svg").default}
              alt=""
              className="logo"
            />
          </Link>
          <div className="h3s">
            <h3 className="title">
              Want to explore the rest of{" "}
              <span className="purple_text">FAST</span>
              <span className="green_text">LANE</span>?
            </h3>
            <br />
            <a href="/">
              <div className="button but1">
                <h4 className="button">Return to home</h4>
              </div>
            </a>
          </div>
        </div>
        <div className="rest">
          <h2 className="h2">Let's create your community together.</h2>
          <p className="bold_p">
            Our users like to know more about a community before they get
            involved. Please include any information they may need.
          </p>

          <p className="bold_p">Name</p>
          <input
            type="text"
            id="name"
            placeholder="name of your community - mandatory"
            onChange={handleNameChange}
          />
          <p className="bold_p">Description</p>
          <textarea
            id="description"
            placeholder="Describe your community"
            rows={5}
            onChange={handleDescriptionChange}
          />
          <p className="bold_p">Website</p>
          <input
            type="text"
            id="website"
            placeholder="link to your website"
            onChange={handleWebsiteChange}
          />

          <p className="bold_p">Twitter</p>
          <input
            type="text"
            id="twitter"
            placeholder="link to your twitter"
            onChange={handleTwitterChange}
          />

          <p className="bold_p">Discord</p>
          <input
            type="text"
            id="discord"
            placeholder="link to your discord"
            onChange={handleDiscordChange}
          />

          <p className="bold_p">Telegram</p>
          <input
            type="text"
            id="telegram"
            placeholder="link to your telegram"
            onChange={handleTelegramChange}
          />

          <p className="bold_p">Wallet</p>
          <input
            type="text"
            id="wallet"
            placeholder="link to your wallet"
            onChange={handleWalletChange}
          />

          <p className="bold_p">Category</p>
          <div className="tags">
            <select name="tags" id="tags" onChange={handleTagChange}>
              {tags.map((tag) => (
                <option value={tag}>{tag}</option>
              ))}
            </select>
          </div>

          <p className="bold_p">Logo</p>
          <input type="file" id="logo" onChange={handleLogoChange} />

          <br />

          <button className="button but3_1" onClick={createcom}>
            <h4 className="button">Create your community</h4>
          </button>
          {msg !== "" && <h2 className="h2">{msg}</h2>}
        </div>
      </div>
    </div>
  );
}

export default CreateCommunity;
