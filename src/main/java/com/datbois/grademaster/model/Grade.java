package com.datbois.grademaster.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Set;

@Entity
public class Grade extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Null
    private double grade;

    @Null
    private String motivation;

    @Null
    @ManyToOne(fetch = FetchType.LAZY)
    private User fromUser;

    @Null
    @ManyToOne(fetch = FetchType.LAZY)
    private User toUser;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    public Group group;

    public Grade(){

    }

    public Grade(double grade, String motivation){
        this.grade = grade;
        this.motivation = motivation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", grade='" + grade + '\'' +
                '}';
    }
}