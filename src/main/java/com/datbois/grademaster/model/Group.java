package com.datbois.grademaster.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Group extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer startYear;
    private Integer endYear;

    @ElementCollection(targetClass = Period.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "group_periods")
    private Set<Period> period;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToMany(mappedBy = "groups", fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Set<User> users;

    @OneToOne(fetch = FetchType.LAZY)
    private GroupGrade groupGrade;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    private List<Grade> grades;

    private String groupName;

    public Group() {
    }

    public Group(int startYear, int endYear, Set<Period> period, Course course, String groupName, Set<User> users) {
        this.startYear = startYear;
        this.endYear = endYear;
        this.period = period;
        this.course = course;
        this.groupName = groupName;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public Set<Period> getPeriod() {
        return period;
    }

    public void setPeriod(Set<Period> period) {
        this.period = period;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public GroupGrade getGroupGrade() {
        return groupGrade;
    }

    public void setGroupGrade(GroupGrade groupGrade) {
        this.groupGrade = groupGrade;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", startYear=" + startYear +
                ", endYear=" + endYear +
                ", period=" + period +
                ", course='" + course + '\'' +
                ", groupName='" + groupName + '\'' +
                ", users='" + users +
                '}';
    }
}
