package com.bezkoder.spring.login.payload.response;

import java.util.List;

public class UserInfoResponse {
    private String id;
    private String username;
    private String address;

    private String nonce;

    public UserInfoResponse() {
    }

    public UserInfoResponse(String id, String username,  String address, String nonce) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
