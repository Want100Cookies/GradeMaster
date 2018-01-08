package com.datbois.grademaster.repository;

import com.datbois.grademaster.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailContainingIgnoreCase(String email);

    long count();
}
