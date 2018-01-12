package com.datbois.grademaster.service;

import com.datbois.grademaster.model.GroupGrade;

public interface GroupGradeService {
    GroupGrade save(GroupGrade groupGrade);

    GroupGrade findById(Long id);
}
