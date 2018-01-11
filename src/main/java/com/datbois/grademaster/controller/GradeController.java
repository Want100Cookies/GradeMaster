package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.Grade;
import com.datbois.grademaster.service.GradeService;
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

//  POST JSON:  {"id":5,"grade":8.0,"fromUser":{"id":1},"toUser":{"id":1},"group":{"id":1}}
    @RequestMapping(value = "/grade", method = RequestMethod.POST)
    public ResponseEntity createGrade(@RequestBody Grade grade){
        grade = gradeService.save(grade);
        return new ResponseEntity<>(grade, HttpStatus.OK);
    }

    @RequestMapping(value = "/grade/{gradeId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.OK)
    public Grade grade(@PathVariable Long gradeId){
        return gradeService.findById(gradeId);
    }
}