package com.datbois.grademaster.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class GroupGrade extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private double grade;

    private String comment;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "groupGrade")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    private User teacher;

    public GroupGrade() {
    }

    public GroupGrade(double grade, String comment, Group group, User teacher) {
        this.grade = grade;
        this.comment = comment;
        this.group = group;
        this.teacher = teacher;
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "GroupGrade{" +
                "id=" + id +
                ", grade=" + grade +
                ", comment='" + comment + '\'' +
                ", group=" + group +
                ", teacher=" + teacher +
                '}';
    }
}
