package com.datbois.grademaster.service.impl;

import com.datbois.grademaster.model.Education;
import com.datbois.grademaster.repository.EducationRepository;
import com.datbois.grademaster.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EducationServiceImpl implements EducationService {

    @Autowired
    private EducationRepository educationRepository;

    @Override
    public Education findById(Long id) {
        return educationRepository.findById(id);
    }

    @Override
    public List<Education> findAll() {
        return educationRepository.findAll();
    }

    @Override
    public Education save(Education course) {
        return educationRepository.save(course);
    }

    @Override
    public void delete(Long id) {
        educationRepository.delete(id);
    }
}
