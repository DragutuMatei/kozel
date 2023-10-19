import React, { useEffect, useState } from "react";

import "../assets/style/login.scss";
import { Link } from "react-router-dom";
import axios_config from "../utils/AxiosConfig";
import { projects } from "../utils/Links";

function CreateCommunity({ user }) {
  const [tags, setTags] = useState([
    "Gaming",
    "Startup",
    "Music",
    "Metaverse",
    "Education",
    "NFT"
  ]);
  // useEffect(() => {
  //   // make it array of tags
  //   setTags((prevTag) => [...prevTag, "Gaming"]);
  //   setTags((prevTag) => [...prevTag, "Startup"]);
  //   setTags((prevTag) => [...prevTag, "Music"]);
  //   setTags((prevTag) => [...prevTag, "Metaverse"]);
  //   setTags((prevTag) => [...prevTag, "Education"]);
  //   setTags((prevTag) => [...prevTag, "NFT"]);
  // }, []);

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

  const [logo, setLogo] = useState("");
  function handleLogoChange(e) {
    setLogo(e.target.value);
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

  const createCommunity = () => {
    let ok = true
    let requirements = [name, user.id, description, website, category, twitter, discord, telegram, wallet]
    requirements.forEach((req, index) => {
      if (req == undefined || req == null || req == '') {
        console.log(req, index)
        console.warn('Field(s) not filled.')
        ok = false
      }
    })
    if (ok == true)
      axios_config.post(`${projects}/addProject`, {
        recurrence: 'all',
        title: name,
        user_id: user.id,
        description: description,
        link: website,
        category: category,
        twitter: twitter,
        discord: discord,
        telegram: telegram,
        wallet: wallet,
      }).then((res) => {
        console.log(res)
      })
  }

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
              {tags.map((tag, index) => (
                <option value={index}>{tag}</option>
              ))}
            </select>
          </div>

          <p className="bold_p">Logo</p>
          <input type="file" id="logo" onChange={handleLogoChange} />

          <br />

          <div className="button but3_1">
            <h4 className="button" onClick={() => { createCommunity() }}>Create your community</h4>
          </div>
        </div>
      </div>
    </div>
  );
}

export default CreateCommunity;
