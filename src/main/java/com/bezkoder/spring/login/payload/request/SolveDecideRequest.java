package com.bezkoder.spring.login.payload.request;

import javax.validation.constraints.NotBlank;

public class SolveDecideRequest {
//    @NotBlank
    public boolean decide;

    public boolean isDecide() {
        return decide;
    }

    public void setDecide(boolean decide) {
        this.decide = decide;
    }
}
