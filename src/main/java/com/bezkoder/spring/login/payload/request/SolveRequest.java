package com.bezkoder.spring.login.payload.request;

import javax.validation.constraints.NotBlank;

public class SolveRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String img;

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
}
