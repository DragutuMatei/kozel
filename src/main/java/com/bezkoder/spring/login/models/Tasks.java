package com.bezkoder.spring.login.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tasks {
    private String id;
    private String title;
    private String description;
    private String link;
    private int reward;
    private String type;
    private List<Object> solves;

    public List<Object> getSolves() {
        return solves;
    }

    public void setSolves(List<Object> solves) {
        this.solves = solves;
    }

    public Tasks(String title, String description, String link, int reward, String type) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.link = link;
        this.reward = reward;
        this.solves = new ArrayList<>();
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
