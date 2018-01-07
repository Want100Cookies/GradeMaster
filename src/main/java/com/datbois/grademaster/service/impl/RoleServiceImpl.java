package com.datbois.grademaster.service.impl;

import com.datbois.grademaster.model.Role;
import com.datbois.grademaster.repository.RoleRepository;
import com.datbois.grademaster.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByLabel(name);
    }
}
