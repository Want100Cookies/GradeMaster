package com.datbois.grademaster.service;

import com.datbois.grademaster.model.Education;

import java.util.List;

public interface EducationService {
    Education findById(Long id);

    List<Education> findAll();

    Education save(Education course);

    void delete(Long id);
}
