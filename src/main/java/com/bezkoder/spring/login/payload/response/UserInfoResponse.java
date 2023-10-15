package com.bezkoder.spring.login.payload.response;

import java.util.List;

public class UserInfoResponse {
    private String id;
    private String username;
    private String email;


    private String address;

    private String nonce;

    public UserInfoResponse(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public UserInfoResponse(String id, String username, String email, String nonce, String address) {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
