package com.datbois.grademaster.service.impl;

import com.datbois.grademaster.model.Grade;
import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.model.Role;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.repository.GradeRepository;
import com.datbois.grademaster.repository.GroupRepository;
import com.datbois.grademaster.repository.UserRepository;
import com.datbois.grademaster.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
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
            User user = userRepository.findOne(u.getId());
            Set<Group> groups = user.getGroups();
            groups.remove(group);
            user.setGroups(groups);
            userRepository.save(user);
        }
        List<Grade> grades = group.getGrades();
        for(Grade grade : grades){
            gradeRepository.delete(grade.getId());
        }
        groupRepository.delete(id);
    }

    @Override
    public Group setUsers(Group group, Set<User> users) {
        group.setUsers(users);
        for (User u : users) {
            User user = userRepository.findOne(u.getId());
            Set<Group> groups = user.getGroups();
            groups.add(group);
            user.setGroups(groups);
            userRepository.save(user);
        }
        return group;
    }
}
