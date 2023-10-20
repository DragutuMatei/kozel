package com.bezkoder.spring.login.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "projects")
public class Projects {
    @Id
    private String id;
    private String user_id;
    private String img;
    private String title;
    private String description;
    private String link;
    private String twitter;
    private String discord;
    private String telegram;
    private String wallet;
    private String category;
    private List<User> users;
    private List<Tasks> tasks;

    public Projects( String user_id,String img, String title, String description, String link, String twitter, String discord, String telegram, String wallet, String category) {
        this.user_id = user_id;
        this.img = img;
        this.title = title;
        this.description = description;
        this.link = link;
        this.twitter = twitter;
        this.discord = discord;
        this.telegram = telegram;
        this.wallet = wallet;
        this.category = category;
        this.users = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getDiscord() {
        return discord;
    }

    public void setDiscord(String discord) {
        this.discord = discord;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Tasks> getTasks() {
        return tasks;
    }

    public void setTasks(List<Tasks> tasks) {
        this.tasks = tasks;
    }

    public Projects() {

    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Projects{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", img='" + img + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", twitter='" + twitter + '\'' +
                ", discord='" + discord + '\'' +
                ", telegram='" + telegram + '\'' +
                ", wallet='" + wallet + '\'' +
                ", category='" + category + '\'' +
                ", users=" + users +
                ", tasks=" + tasks +
                '}';
    }
}
