package com.bezkoder.spring.login.payload.request;

public class AutoSolveRequest {
    private String username;
    private String xusername;
    private boolean status;

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
