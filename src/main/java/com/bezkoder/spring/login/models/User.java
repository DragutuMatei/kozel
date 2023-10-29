package com.bezkoder.spring.login.models;


import org.springframework.data.annotation.Id;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;


@Document(collection = "user")
public class User {
    @Id
    private String id;

    private String username;

    private String address;

    private String nonce;

    public User() {
    }


    public User(String username, String address) {
        this.username = username;
        this.address = address;
        this.nonce = UUID.randomUUID().toString();
    }

//    public User(String username) {
//        this.username = username;
//        this.nonce = UUID.randomUUID().toString();
//    }

    public User(String address) {
        this.address = address;
        this.nonce = UUID.randomUUID().toString();
    }


    public User(String id, String username, String address, String nonce) {
        this.id = id;
        this.username = username;
        this.address = address;
        this.nonce = nonce;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
}
