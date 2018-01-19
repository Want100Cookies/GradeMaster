package com.datbois.grademaster.service.impl;

import com.datbois.grademaster.model.GroupGrade;
import com.datbois.grademaster.model.Notification;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.repository.GroupGradeRepository;
import com.datbois.grademaster.service.GroupGradeService;
import com.datbois.grademaster.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupGradeServiceImpl implements GroupGradeService {

    private static final String TITLE = "%s has been graded";
    private static final String BODY = "Your group with group name \"%s\" has been graded by %s. You can now view your group grade and grade your team members.";

    @Autowired
    private GroupGradeRepository groupGradeRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public GroupGrade save(GroupGrade groupGrade) {
        for (User user : groupGrade.getGroup().getUsers()) {
            if (user.getRoles().stream()
                    .filter(role -> role.getCode().equalsIgnoreCase("STUDENT_ROLE"))
                    .findFirst().orElse(null) != null) {
                notificationService.save(new Notification(
                        String.format(TITLE, groupGrade.getGroup().getGroupName()),
                        String.format(BODY, groupGrade.getGroup().getGroupName(), "teacher"),
                        user,
                        "/",
                        "Click here to launch Grade Master"
                ));
            }
        }

        return groupGradeRepository.save(groupGrade);
    }

    @Override
    public GroupGrade findById(Long id) {
        return groupGradeRepository.findById(id);
    }
}
