package com.datbois.grademaster.repository;

import com.datbois.grademaster.model.Education;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationRepository extends JpaRepository<Education, Long> {
    Education findById(Long id);
}
