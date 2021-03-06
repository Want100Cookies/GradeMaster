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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public Group save(Group group, Set<User> users) {
        return groupRepository.save(setUsers(groupRepository.save(group), users));
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
        if (users == null) return group;
        Group existingGroup = groupRepository.findById(group.getId());

        for (User u : existingGroup.getUsers()) { // Delete group from the users
            User user = userService.findById(u.getId());
            Set<Group> groups = user.getGroups();
            groups.remove(group);
            user.setGroups(groups);
            userService.save(user);
        }

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

    public Set<User> getStudents(Long groupId) {
        return groupRepository
                .findOne(groupId)
                .getUsers()
                .stream()
                .filter(user -> user.hasAnyRole("STUDENT_ROLE"))
                .collect(Collectors.toSet());
    }

    public List<Grade> getGradesFromTeacherToStudent(Long groupId) {
        List<Grade> grades = new ArrayList<>();

        for (Grade grade : groupRepository.findOne(groupId).getGrades()) {
            if (grade.getFromUser().hasAnyRole("TEACHER_ROLE")) {
                grades.add(grade);
            }
        }

        return grades;
    }

    public boolean userIsInGroup(Long userId, Long groupId) {
        boolean is = false;

        for (User user : groupRepository.findOne(groupId).getUsers()) {
            if (user.getId() == userId) {
                is = true;
            }
        }

        return is;
    }

}
