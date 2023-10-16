package com.bezkoder.spring.login.models;

public class Solve {
    private String username;
    private String img;
    private boolean accept;
    private boolean viewed;

    public Solve(String username, String img) {
        this.username = username;
        this.img = img;
        this.viewed = false;
        this.accept = false;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    public Solve() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }
}
