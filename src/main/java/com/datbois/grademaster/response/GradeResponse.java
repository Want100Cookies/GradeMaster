package com.datbois.grademaster.response;

import com.datbois.grademaster.model.Grade;

public class GradeResponse {

    private Long id;
    private Double grade;
    private String motivation;
    private SimpleUserResponse fromUser;
    private SimpleUserResponse toUser;
    private SimpleGroupResponse group;
    private boolean valid;

    public GradeResponse() {
    }

    public GradeResponse(Grade grade) {
        this.id = grade.getId();
        this.grade = grade.getGrade();
        this.motivation = grade.getMotivation();
        this.fromUser = new SimpleUserResponse(grade.getFromUser());
        this.toUser = new SimpleUserResponse(grade.getToUser());
        this.group = new SimpleGroupResponse(grade.getGroup());
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

    public SimpleGroupResponse getGroup() {
        return group;
    }

    public void setGroup(SimpleGroupResponse group) {
        this.group = group;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
