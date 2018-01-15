package com.datbois.grademaster.service.impl;

import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.repository.GroupRepository;
import com.datbois.grademaster.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public Group findByGroupName(String groupName) {
        return groupRepository.findByGroupNameContainingIgnoreCase(groupName);
    }

    @Override
    public Group findById(Long id) {
        return groupRepository.findOne(id);
    }

    @Override
    public long count() {
        return groupRepository.count();
    }

    @Override
    public void delete(Long id) {
        groupRepository.delete(id);
    }
}
