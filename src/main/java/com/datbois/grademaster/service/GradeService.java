package com.datbois.grademaster.service;

import com.datbois.grademaster.model.Grade;

public interface GradeService{
    Grade save (Grade grade);

    Grade findById(Long id);
}

