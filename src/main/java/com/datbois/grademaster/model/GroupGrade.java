package com.datbois.grademaster.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class GroupGrade extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private double grade;

    private String comment;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "groupGrade")
    private Group group;

    public GroupGrade() {
    }

    public GroupGrade(double grade, String comment) {
        this.grade = grade;
        this.comment = comment;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "GroupGrade{" +
                "id=" + id +
                ", grade='" + grade + '\'' +
                ", comment=" + comment +
                '}';
    }
}
