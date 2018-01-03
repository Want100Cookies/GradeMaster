package com.datbois.grademaster.service;

import com.datbois.grademaster.model.User;

import java.util.List;

public interface UserService {
    User save(User user);

    List<User> findAll();

    void delete(Long id);
}
