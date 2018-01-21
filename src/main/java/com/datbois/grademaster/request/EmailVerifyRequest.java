package com.datbois.grademaster.request;

public class EmailVerifyRequest {

    private String email;
    private String token;

    public EmailVerifyRequest() {
    }

    public EmailVerifyRequest(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
