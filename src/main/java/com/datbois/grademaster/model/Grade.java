package com.datbois.grademaster.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Grade{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private double grade;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    public User fromUser;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    public User toUser;

    public Grade(){

    }

    public Grade(double grade){
        this.grade = grade;
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