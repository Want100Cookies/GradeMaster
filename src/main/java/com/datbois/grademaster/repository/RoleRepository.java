package com.datbois.grademaster.repository;

import com.datbois.grademaster.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByLabel(String name);
}
