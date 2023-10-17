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

    private String email;

    private String password;

    private String address;

    private String nonce;

    public User() {
    }

    public User(String id, String username, String email, String address) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.address = address;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nonce = UUID.randomUUID().toString();
    }

    public User(String id, String username, String email, String password, String address, String nonce) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
