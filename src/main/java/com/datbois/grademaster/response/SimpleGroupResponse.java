package com.datbois.grademaster.response;

import com.datbois.grademaster.model.Course;
import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.model.GroupGrade;
import com.datbois.grademaster.model.Period;

import java.util.Set;

public class SimpleGroupResponse {

    private Long id;
    private Integer startYear;
    private Integer endYear;
    private Set<Period> period;
    private Course course;
    private GroupGrade groupGrade;
    private String groupName;

    public SimpleGroupResponse() {
    }

    public SimpleGroupResponse(Group group) {
        this.id = group.getId();
        this.startYear = group.getStartYear();
        this.endYear = group.getEndYear();
        this.period = group.getPeriod();
        this.course = group.getCourse();
        this.groupGrade = group.getGroupGrade();
        this.groupName = group.getGroupName();
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

    public GroupGrade getGroupGrade() {
        return groupGrade;
    }

    public void setGroupGrade(GroupGrade groupGrade) {
        this.groupGrade = groupGrade;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
