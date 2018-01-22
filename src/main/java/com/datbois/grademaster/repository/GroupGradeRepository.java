package com.datbois.grademaster.repository;

import com.datbois.grademaster.model.GroupGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupGradeRepository extends JpaRepository<GroupGrade, Long> {
    GroupGrade findById(Long id);
}
