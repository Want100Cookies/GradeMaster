package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.Grade;
import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.model.GroupGrade;
import com.datbois.grademaster.service.GradeService;
import com.datbois.grademaster.service.GroupGradeService;
import com.datbois.grademaster.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class GradeController{

    @Autowired
    GradeService gradeService;

    @Autowired
    GroupService groupService;

    @Autowired
    GroupGradeService groupGradeService;

    //  POST JSON:  {"grade":8.0,"motivation":"motivation","fromUser":{"id":1},"toUser":{"id":1},"group":{"id":1}}
    @RequestMapping(value = "/grade", method = RequestMethod.POST)
    public ResponseEntity createGrade(@RequestBody Grade grade){
        grade = gradeService.save(grade);
        return new ResponseEntity<>(grade, HttpStatus.OK);
    }

    //  POST JSON:  {"grade":5,"comment":"test"}
    @RequestMapping(value = "/grade/group/{groupId}", method = RequestMethod.PATCH)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity insertGroupGrade(@PathVariable Long groupId, @RequestBody GroupGrade groupGrade){
        Group exists = groupService.findById(groupId);
        exists.setGroupGrade(groupGrade);
        groupGradeService.save(groupGrade);
        groupService.save(exists);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    @RequestMapping(value = "/grade/{gradeId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.OK)
    public Grade grade(@PathVariable Long gradeId){
        return gradeService.findById(gradeId);
    }
}