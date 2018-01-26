package com.datbois.grademaster.repository;

import com.datbois.grademaster.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Group findById(Long id);
}