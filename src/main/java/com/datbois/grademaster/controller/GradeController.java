package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.*;
import com.datbois.grademaster.service.GradeService;
import com.datbois.grademaster.service.GroupGradeService;
import com.datbois.grademaster.service.GroupService;
import com.datbois.grademaster.service.RoleService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class GradeController{

    @Autowired
    GradeService gradeService;

    @Autowired
    GroupService groupService;

    @Autowired
    GroupGradeService groupGradeService;

    @RequestMapping(value = "/grade/users/{userId}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE') or isCurrentUser(#userId)")
    public ResponseEntity createGrade(@PathVariable Long userId, @RequestBody Grade[] grades){

        for(Grade grade : grades){
            grade = gradeService.save(grade);
        }

        return new ResponseEntity<>(grades, HttpStatus.OK);
    }

    @RequestMapping(value = "/grade/group/{groupId}", method = RequestMethod.PATCH)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity insertGroupGrade(@PathVariable Long groupId, @RequestBody GroupGrade groupGrade){
        Group exists = groupService.findById(groupId);
        exists.setGroupGrade(groupGrade);
        groupGradeService.save(groupGrade);
        groupService.save(exists);
        return new ResponseEntity<>(groupGrade, HttpStatus.OK);
    }

    @RequestMapping(value = "/grade/group/{groupId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getAllGradesForAGroup(@PathVariable Long groupId){
        Group exists = groupService.findById(groupId);

        Map<Object, Object> response = new HashMap<>();
        response.put("groupId", exists.getId());
        response.put("groupGrade", exists.getGroupGrade());

        Map<Object, Object> users = new HashMap<>();

        for(User user : exists.getUsers()){
            List<Object[]> grades = new ArrayList<>();
            for(Grade grade : user.getGradesReceived()){
                if(exists.getId().equals(grade.getGroup().getId())){
                    grades.add(new Object[]{grade.getGrade(), grade.getMotivation()});
                }
            }
            users.put(user.getId(), grades);
        }

        response.put("user", users);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/grade/group/{groupId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity removeGrades(@PathVariable Long groupId){
        Group exists = groupService.findById(groupId);

//        List<Object> deleted = new ArrayList<>();

        Long id = 0L;

        for(Grade grade : exists.getGrades()){
            for(Role role : grade.getFromUser().getRoles()){
                if(role.getCode().contains("STUDENT_ROLE")){
                    id = grade.getId();
                    gradeService.delete(grade.getId());
                }
            }
        }

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

}