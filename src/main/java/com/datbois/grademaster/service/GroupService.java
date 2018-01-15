package com.datbois.grademaster.service;

import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.model.User;

import java.util.List;
import java.util.Set;

public interface GroupService {
    Group save(Group group);

    List<Group> findAll();

    Group findById(Long id);

    long count();

    void delete(Long id);

    Group setUsers(Group group, Set<User> users);
}