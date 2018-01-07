package com.datbois.grademaster.service;

import com.datbois.grademaster.model.User;

import java.util.List;

public interface UserService {
    User save(User user);

    User update(User user);

    List<User> findAll();

    User findByEmail(String email);

    User findById(Long id);

    long count();

    void delete(Long id);
}
