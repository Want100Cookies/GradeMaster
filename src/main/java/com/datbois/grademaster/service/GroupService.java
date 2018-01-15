package com.datbois.grademaster.service;

import com.datbois.grademaster.model.Group;

import java.util.List;

public interface GroupService {
    Group save(Group group);

    List<Group> findAll();

    Group findByGroupName(String groupName);

    Group findById(Long id);

    long count();

    void delete(Long id);
}