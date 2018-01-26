package com.datbois.grademaster.repository;

import com.datbois.grademaster.model.Role;
import com.datbois.grademaster.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailContainingIgnoreCase(String email);

    List<User> findAllByRolesContaining(Role role);

    long count();
}
