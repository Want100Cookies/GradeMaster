package com.datbois.grademaster.service.impl;

import com.datbois.grademaster.model.Grade;
import com.datbois.grademaster.repository.GradeRepository;
import com.datbois.grademaster.repository.UserRepository;
import com.datbois.grademaster.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Override
    public Grade save(Grade grade) {
        return gradeRepository.save(grade);
    }

    @Override
    public Grade findById(Long id) {
        return gradeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        gradeRepository.delete(id);
    }


}