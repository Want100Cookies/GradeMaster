package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.*;
import com.datbois.grademaster.service.GradeService;
import com.datbois.grademaster.service.GroupGradeService;
import com.datbois.grademaster.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class GradeController {

    @Autowired
    GradeService gradeService;

    @Autowired
    GroupService groupService;

    @Autowired
    GroupGradeService groupGradeService;

    //  POST JSON:  {"grade":8.0,"motivation":"motivation","fromUser":{"id":1},"toUser":{"id":1},"group":{"id":1}}
    @RequestMapping(value = "/grade", method = RequestMethod.POST)
    public Map<Object, Object> createGrade(@RequestBody Grade grade) {
        grade = gradeService.save(grade);

        Map<Object, Object> response = new HashMap<>();
        response.put("grade", grade.getGrade());
        response.put("motivation", grade.getMotivation());
        response.put("fromUser", grade.getFromUser().getId());
        response.put("toUser", grade.getToUser().getId());
        response.put("group", grade.getGroup().getId());

        return response;
    }

    //  POST JSON:  {"grade":5,"comment":"test"}
    @RequestMapping(value = "/grade/group/{groupId}", method = RequestMethod.PATCH)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    public GroupGrade insertGroupGrade(Authentication authentication, @PathVariable Long groupId, @RequestBody GroupGrade groupGrade) {
        User user = ((UserDetails) authentication.getPrincipal()).getUser();
        groupGrade.setTeacher(user);

        Group exists = groupService.findById(groupId);
        exists.setGroupGrade(groupGrade);

        groupGrade = groupGradeService.save(groupGrade);

        groupService.save(exists);

        return groupGrade;
    }

    @RequestMapping(value = "/grade/{gradeId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    public Grade grade(@PathVariable Long gradeId) {
        return gradeService.findById(gradeId);
    }
}