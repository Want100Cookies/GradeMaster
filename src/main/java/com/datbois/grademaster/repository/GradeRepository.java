package com.datbois.grademaster.repository;


import com.datbois.grademaster.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    Grade findById(Long id);
}
