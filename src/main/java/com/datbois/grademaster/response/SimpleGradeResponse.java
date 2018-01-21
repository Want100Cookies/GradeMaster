package com.datbois.grademaster.response;

import com.datbois.grademaster.model.Grade;

public class SimpleGradeResponse {

    private Long id;
    private Double grade;
    private String motivation;
    private SimpleUserResponse fromUser;
    private SimpleUserResponse toUser;
    private boolean valid;

    public SimpleGradeResponse() {
    }

    public SimpleGradeResponse(Grade grade) {
        this.id = grade.getId();
        this.grade = grade.getGrade();
        this.motivation = grade.getMotivation();
        this.fromUser = new SimpleUserResponse(grade.getFromUser());
        this.toUser = new SimpleUserResponse(grade.getToUser());
        this.valid = grade.isValid();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public SimpleUserResponse getFromUser() {
        return fromUser;
    }

    public void setFromUser(SimpleUserResponse fromUser) {
        this.fromUser = fromUser;
    }

    public SimpleUserResponse getToUser() {
        return toUser;
    }

    public void setToUser(SimpleUserResponse toUser) {
        this.toUser = toUser;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

}
