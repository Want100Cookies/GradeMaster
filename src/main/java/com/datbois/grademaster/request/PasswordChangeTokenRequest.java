package com.datbois.grademaster.request;

public class PasswordChangeTokenRequest {

    private String email;

    public PasswordChangeTokenRequest() {
    }

    public PasswordChangeTokenRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
