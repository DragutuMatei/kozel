package com.bezkoder.spring.login.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TaskRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String link;
    @NotNull
    private int reward;

    @NotBlank
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
