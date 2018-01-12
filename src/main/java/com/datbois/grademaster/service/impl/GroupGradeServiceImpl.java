package com.datbois.grademaster.service.impl;

import com.datbois.grademaster.model.Grade;
import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.model.GroupGrade;
import com.datbois.grademaster.repository.GroupGradeRepository;
import com.datbois.grademaster.service.GroupGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupGradeServiceImpl implements GroupGradeService {

    @Autowired
    private GroupGradeRepository groupGradeRepository;

    @Override
    public GroupGrade save(GroupGrade groupGrade) {
        return groupGradeRepository.save(groupGrade);
    }

    @Override
    public GroupGrade findById(Long id) {
        return groupGradeRepository.findById(id);
    }
}
