package com.datbois.grademaster.service.impl;

import com.datbois.grademaster.model.Grade;
import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.model.Notification;
import com.datbois.grademaster.repository.GradeRepository;
import com.datbois.grademaster.service.GradeService;
import com.datbois.grademaster.service.GroupService;
import com.datbois.grademaster.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                .filter(user ->
                        user.getRoles()
                                .stream()
                                .filter(role ->
                                        role.getCode().equalsIgnoreCase("STUDENT_ROLE")
                                )
                                .findFirst()
                                .orElse(null)
                                != null
                )
                .count();

        Integer noGrades = group.getGrades().size();

        return (noStudents * noStudents) == noGrades;
    }

    private void sendNotificationToTeacher(Group group) {
        group.getUsers()
                .stream()
                .filter(user ->
                        user.getRoles()
                                .stream()
                                .filter(role ->
                                        role.getCode().equalsIgnoreCase("TEACHER_ROLE")
                                )
                                .findFirst()
                                .orElse(null)
                                != null
                )
                .forEach(user -> notificationService.save(new Notification(
                        String.format("All members of %s have graded their team members.", group.getGroupName()),
                        "All the team members have graded each other. You can now confirm their grades in Grade Master",
                        user,
                        "/",
                        "Open Grade Master"
                )));
    }
}