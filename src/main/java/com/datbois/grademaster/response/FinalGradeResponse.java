package com.datbois.grademaster.response;

import com.datbois.grademaster.model.Grade;
import com.datbois.grademaster.model.User;

public class FinalGradeResponse {

    private Long id;
    private Double grade;
    private String motivation;
    private User teacher;

    public FinalGradeResponse(Grade grade) {
        this.id = grade.getId();
        this.grade = grade.getGrade();
        this.motivation = grade.getMotivation();
        this.teacher = grade.getFromUser();
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

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }
}
