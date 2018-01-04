package com.datbois.grademaster.service;

import com.datbois.grademaster.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAll();

    Role findByName(String name);
}
