package com.datbois.grademaster.service.impl;

import com.datbois.grademaster.model.Grade;
import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.model.Notification;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.repository.GradeRepository;
import com.datbois.grademaster.service.GradeService;
import com.datbois.grademaster.service.GroupService;
import com.datbois.grademaster.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private GroupService groupService;

    @Override
    public Grade save(Grade grade) {
        grade = gradeRepository.save(grade);

        Group group = groupService.findById(grade.getGroup().getId());
        if (allGroupMembersHaveGraded(group)) {
            sendNotificationToTeacher(group);
        }

        return grade;
    }

    @Override
    public Grade findById(Long id) {
        return gradeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        gradeRepository.delete(id);
    }

    private boolean allGroupMembersHaveGraded(Group group) {
        Integer noStudents = (int) group.getUsers()
                .stream()
                .filter(user -> user.hasAnyRole("STUDENT_ROLE"))
                .count();

        Integer noGrades = group.getGrades().size();

        return (noStudents * noStudents) == noGrades;
    }

    private void sendNotificationToTeacher(Group group) {
        List<User> users = group.getUsers()
                .stream()
                .filter(user -> user.hasAnyRole("TEACHER_ROLE"))
                .collect(Collectors.toList());

        for (User teacher : users) {
            notificationService.save(new Notification(
                    String.format("All members of %s have graded their team members.", group.getGroupName()),
                    "All the team members have graded each other. You can now confirm their grades in Grade Master",
                    teacher,
                    "/",
                    "Open Grade Master"
            ));
        }
    }
}