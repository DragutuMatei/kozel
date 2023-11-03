package com.bezkoder.spring.login.models;

import org.springframework.data.mongodb.core.index.Indexed;

public class AutoSolve {
    private String xusername;
    @Indexed(unique = true)
    private String username;
    private boolean status;

    public AutoSolve(String username,String xusername) {
        this.username = username;
        this.xusername = xusername;
        this.status = false;
    }

    public String getXusername() {
        return xusername;
    }

    public void setXusername(String xusername) {
        this.xusername = xusername;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
