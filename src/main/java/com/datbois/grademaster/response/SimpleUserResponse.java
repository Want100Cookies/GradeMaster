package com.datbois.grademaster.response;

import com.datbois.grademaster.model.User;

public class SimpleUserResponse {

    private Long id;
    private String name;
    private String email;
    private String referenceId;

    public SimpleUserResponse() {
    }

    public SimpleUserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.referenceId = user.getReferenceId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}
