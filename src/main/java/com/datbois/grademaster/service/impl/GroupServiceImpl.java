package com.datbois.grademaster.service.impl;

import com.datbois.grademaster.model.Grade;
import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.repository.GroupRepository;
import com.datbois.grademaster.service.GradeService;
import com.datbois.grademaster.service.GroupService;
import com.datbois.grademaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GradeService gradeService;

    @Override
    public Group save(Group group) {
        return groupRepository.save(setUsers(groupRepository.save(group), group.getUsers()));
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
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
        Group group = groupRepository.findOne(id);
        for (User u : group.getUsers()) { // Delete group from the users
            User user = userService.findById(u.getId());
            Set<Group> groups = user.getGroups();
            groups.remove(group);
            user.setGroups(groups);
            userService.save(user);
        }
        List<Grade> grades = group.getGrades();
        for (Grade grade : grades) {
            gradeService.delete(grade.getId());
        }
        groupRepository.delete(id);
    }

    @Override
    public Group setUsers(Group group, Set<User> users) {
        if(users == null) return group;
        group.setUsers(users);
        for (User u : users) {
            User user = userService.findById(u.getId());
            Set<Group> groups = user.getGroups();
            groups.add(group);
            user.setGroups(groups);
            userService.save(user);
        }
        return group;
    }
}
