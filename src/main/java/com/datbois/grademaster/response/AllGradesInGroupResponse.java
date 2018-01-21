package com.datbois.grademaster.response;

import com.datbois.grademaster.model.Grade;
import com.datbois.grademaster.model.GroupGrade;

import java.util.ArrayList;
import java.util.List;

public class AllGradesInGroupResponse {

    private GroupGrade groupGrade;
    private List<SimpleGradeResponse> grades;

    public AllGradesInGroupResponse() {
    }

    public AllGradesInGroupResponse(GroupGrade groupGrade, List<Grade> grades) {
        this.groupGrade = groupGrade;

        this.grades = new ArrayList<>();

        for (Grade grade : grades) {
            this.grades.add(new SimpleGradeResponse(grade));
        }
    }

    public GroupGrade getGroupGrade() {
        return groupGrade;
    }

    public void setGroupGrade(GroupGrade groupGrade) {
        this.groupGrade = groupGrade;
    }

    public List<SimpleGradeResponse> getGrades() {
        return grades;
    }

    public void setGrades(List<SimpleGradeResponse> grades) {
        this.grades = grades;
    }
}
