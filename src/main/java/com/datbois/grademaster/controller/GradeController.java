package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.*;
import com.datbois.grademaster.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    UserService userService;

    /**
     * Insert grades for all group members.
     * Only if logged in as student, teacher or admin.
     * @endpoint (POST) /api/v1/grade/users/{userId}
     * @return Inserted grades
     */
    @RequestMapping(value = "/grades/users/{userId}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE') or isCurrentUser(#userId)")
    public ResponseEntity createGrade(@PathVariable Long userId, @RequestBody Grade[] grades){

        List<Map<String, Object>> response = new ArrayList<>();

        for(Grade grade : grades){
            gradeService.save(grade);
            Map<String, Object> data = new HashMap<>();
            data.put("grade", grade.getGrade());
            data.put("motivation", grade.getMotivation());
            data.put("fromUser", grade.getFromUser().getId());
            data.put("toUser", grade.getToUser().getId());
            data.put("group", grade.getGroup().getId());
            response.add(data);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Insert group grade for a group.
     * Only possible if logged in as teacher or admin.
     * @endpoint (PATCH) /api/v1/grade/group/{groupId}
     * @return Inserted group grade
     * @responseStatus OK
     */
    @RequestMapping(value = "/grades/groups/{groupId}", method = RequestMethod.PATCH)
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

    /**
     * Get group grade and all grades assigned and received by users of this particular group.
     * Only possible if logged in as teacher and admin.
     * @endpoint (GET) /api/v1/grade/group/{groupId}
     * @return Group grades and grades assigned and received by users
     * @responseStatus OK
     */
    @RequestMapping(value = "/grades/groups/{groupId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getAllGradesForAGroup(@PathVariable Long groupId){
        Group exists = groupService.findById(groupId);

        Map<Object, Object> response = new HashMap<>();
        response.put("groupId", exists.getId());
        response.put("groupGrade", exists.getGroupGrade().getId());

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

    /**
     * Delete grades given by students only for this particular group.
     * Only possible if logged in as teacher or admin.
     * @endpoint (DELETE) /api/v1/grade/group/{groupId}
     * @responseStatus ACCEPTED
     */
    @RequestMapping(value = "/grades/groups/{groupId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void removeGrades(@PathVariable Long groupId){
        Group exists = groupService.findById(groupId);

        for(Grade grade : exists.getGrades()){
            for(Role role : grade.getFromUser().getRoles()){
                if(role.getCode().contains("STUDENT_ROLE")){
                    gradeService.delete(grade.getId());
                }
            }
        }
    }
}