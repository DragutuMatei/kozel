import React, { useEffect, useState } from "react";

import "../assets/style/login.scss";
import { Link, useParams } from "react-router-dom";

function CreateTask() {
  const { id } = useParams();
  const [submission_types, setsubmission_types] = useState([]);
  useEffect(() => {
    // make it array of submission_types
    setsubmission_types((prevsubmission_type) => [...prevsubmission_type, "URL"]);
    setsubmission_types((prevsubmission_type) => [...prevsubmission_type, "Image"]);
    setsubmission_types((prevsubmission_type) => [...prevsubmission_type, "Text"]);
    setsubmission_types((prevsubmission_type) => [...prevsubmission_type, "Quiz"]);
    setsubmission_types((prevsubmission_type) => [...prevsubmission_type, "Visit Link"]);
    setsubmission_types((prevsubmission_type) => [...prevsubmission_type, "Empty"]);
    setsubmission_types((prevsubmission_type) => [...prevsubmission_type, "Twitter"]);
    setsubmission_types((prevsubmission_type) => [...prevsubmission_type, "Join Discord"]);
    setsubmission_types((prevsubmission_type) => [...prevsubmission_type, "Join Telegram"]);
    setsubmission_types((prevsubmission_type) => [...prevsubmission_type, "Invites"]);
  }, []);

  const [recurrence, setrecurrence] = useState([]);
  useEffect(() => {
    // make it array of recurrence
    setrecurrence((prevrecurrence) => [...prevrecurrence, "Daily"]);
    setrecurrence((prevrecurrence) => [...prevrecurrence, "Weekly"]);
    setrecurrence((prevrecurrence) => [...prevrecurrence, "Monthly"]);
    setrecurrence((prevrecurrence) => [...prevrecurrence, "Yearly"]);
    setrecurrence((prevrecurrence) => [...prevrecurrence, "Once"]);
  }, []);



  const [name, setName] = useState("");
  function handleNameChange(e) {
    setName(e.target.value);
  }

  const [description, setDescription] = useState("");
  function handleDescriptionChange(e) {
    setDescription(e.target.value);
  }

  const [category, setCategory] = useState("");
  function handlesubmission_typeChange(e) {
    setCategory(e.target.value);
  }

  const [recurrence_type, setRecurrence] = useState("");
  function handleRecurrenceChange(e) {
    setRecurrence(e.target.value);
  }

  const [xp, setXP] = useState("");
  function handleXPChange(e) {
    setXP(e.target.value);
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
          <h2 className="h2">Add a task to the community</h2>
          <p className="bold_p">
            Our users like to know more about a community before they get
            involved. Please include any information they may need.
          </p>

          <p className="bold_p">Name</p>
          <input
            type="text"
            id="name"
            placeholder="name of your task - mandatory"
            onChange={handleNameChange}
          />
          <p className="bold_p">Description</p>
          <textarea
            id="description"
            placeholder="Describe your task"
            rows={5}
            onChange={handleDescriptionChange}
          />

          <p className="bold_p">Submission Type</p>
          <div className="tags">
            <select name="submission_types" id="submission_types" onChange={handlesubmission_typeChange}>
              {submission_types.map((submission_type, index) => (
                <option value={index}>{submission_type}</option>
              ))}
            </select>
          </div>
          <p className="bold_p">Recurrence</p>
          <div className="tags">
            <select name="recurrence" id="recurrence" onChange={handleRecurrenceChange}>
              {recurrence.map((recurrence, index) => (
                <option value={index}>{recurrence}</option>
              ))}
            </select>
          </div>

          <p className="bold_p">Reward - FL</p>
          <input
            type="number"
            id="reward"
            placeholder="reward for your task"
            onChange={handleXPChange}
          />


          <br />

          <div className="button but3_1">
            <h4 className="button">Create your task</h4>
          </div>
        </div>
      </div>
    </div>
  );
}

export default CreateTask;
