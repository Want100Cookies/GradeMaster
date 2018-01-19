package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.*;
import com.datbois.grademaster.service.GradeService;
import com.datbois.grademaster.service.GroupGradeService;
import com.datbois.grademaster.service.GroupService;
import com.datbois.grademaster.service.UserService;
import com.datbois.grademaster.util.CsvGeneratorUtil;
import com.datbois.grademaster.util.PdfGeneratorUtil;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupGradeService groupGradeService;

    @Autowired
    private CsvGeneratorUtil csvGenerator;

    @Autowired
    private PdfGeneratorUtil pdfGenerator;

    @Autowired
    private UserService userService;

    /**
     * Get grading status.
     * Only available if in group.
     *
     * OPEN: Group exists but no group grade has been set.
     * PENDING: Group exists and group grade is set, students can start grading.
     * CLOSED: All students have received a grade from teacher, grading closed.
     *
     * @endpoint (GET) /grades/status/groups/{groupId}
     * @return grading status
     */
    @RequestMapping(value = "/grades/status/groups/{groupId}", method = RequestMethod.GET)
    @PreAuthorize("isInGroup(#groupId)")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getGradingStatus(@PathVariable Long groupId){
        Map<String, Status> status = new HashMap<>();

        Status current;

        Group group = groupService.findById(groupId);

        if(group != null){
            current = Status.Open;
            if(group.getGroupGrade() != null){
                current = Status.Pending;

                if(groupService.getStudents(groupId).size() == groupService.getGradesFromTeacherToStudent(groupId).size()){
                    current = Status.Closed;
                }
            }
            status.put("status", current);
        }

        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Get final grade for a student in a particular group.
     * Only if logged in as student or teacher.
     *
     * @return final grade
     * @endpoint (GET) /grades/groups/{groupId}/users/{userId}
     */
    @RequestMapping(value = "/grades/groups/{groupId}/users/{userId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE','ADMIN_ROLE') or isCurrentUser(#userId)")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getFinalGrade(@PathVariable Long groupId, @PathVariable Long userId) {
        Map<String, Object> finalGrade = new HashMap<>();

        Group exists = groupService.findById(groupId);
        for (User user : exists.getUsers()) {
            if (user.getId() == userId) {
                for (Grade grade : exists.getGrades()) {
                    for (Role role : grade.getFromUser().getRoles()) {
                        if (role.getCode().contains("TEACHER_ROLE")) {
                            finalGrade.put("grade", grade.getGrade());
                        }
                    }
                }
            }
        }
        return new ResponseEntity<>(finalGrade, HttpStatus.OK);
    }

    /**
     * Insert grades for all group members.
     * Only if user is in group, teacher or admin.
     * @endpoint (POST) /api/v1/grade/users/{userId}
     * @return Inserted grades
     * @endpoint (POST) /api/v1/grade/users/{userId}
     */
    @RequestMapping(value = "/grades/groups/{groupId}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE','ADMIN_ROLE') or isInGroup(#groupId)")
    public ResponseEntity createGrade(@PathVariable Long groupId, @RequestBody Grade[] grades) {

        List<Map<String, Object>> response = new ArrayList<>();

        Group group = groupService.findById(groupId);

        for (Grade grade : grades) {
            if(groupService.userIsInGroup(grade.getFromUser().getId(), groupId) && groupService.userIsInGroup(grade.getToUser().getId(), groupId)){
                if (grade.getMotivation() == "") {
                    for (Role role : userService.findById(grade.getFromUser().getId()).getRoles()) {
                        if (role.getCode().contains("STUDENT_ROLE")) {
                            grade.setValid(false);
                        }
                    }
                }

                grade.setGroup(group);
                gradeService.save(grade);
                Map<String, Object> data = new HashMap<>();
                data.put("grade", grade.getGrade());
                data.put("motivation", grade.getMotivation());
                data.put("fromUser", grade.getFromUser().getId());
                data.put("toUser", grade.getToUser().getId());
                data.put("group", group.getId());
                data.put("valid", grade.isValid());
                response.add(data);
            }
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Insert group grade for a group.
     * Only possible if logged in as teacher or admin.
     *
     * @return Inserted group grade
     * @endpoint (PATCH) /api/v1/grade/group/{groupId}
     * @responseStatus OK
     */
    @RequestMapping(value = "/grades/groups/{groupId}", method = RequestMethod.PATCH)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    public GroupGrade insertGroupGrade(Authentication authentication, @PathVariable Long groupId, @RequestBody GroupGrade groupGrade) {
        User user = ((UserDetails) authentication.getPrincipal()).getUser();
        Group group = groupService.findById(groupId);

        groupGrade.setTeacher(user);
        groupGrade.setGroup(group);

        return groupGradeService.save(groupGrade);
    }

    /**
     * Get group grade and all grades assigned and received by users of this particular group.
     * Only possible if logged in as teacher and admin.
     *
     * @return Group grades and grades assigned and received by users
     * @endpoint (GET) /api/v1/grade/group/{groupId}
     * @responseStatus OK
     */
    @RequestMapping(value = "/grades/groups/{groupId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE','ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getAllGradesForAGroup(@PathVariable Long groupId) {
        Group exists = groupService.findById(groupId);

        Map<Object, Object> response = new HashMap<>();
        response.put("groupId", exists.getId());
        response.put("groupGrade", exists.getGroupGrade().getId());

        Map<Object, Object> users = new HashMap<>();

        for (User user : exists.getUsers()) {
            List<Object[]> grades = new ArrayList<>();
            for (Grade grade : user.getGradesReceived()) {
                if (exists.getId().equals(grade.getGroup().getId())) {
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
     *
     * @endpoint (DELETE) /api/v1/grade/group/{groupId}
     * @responseStatus ACCEPTED
     */
    @RequestMapping(value = "/grades/groups/{groupId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void removeGrades(@PathVariable Long groupId) {
        Group exists = groupService.findById(groupId);

        for (Grade grade : exists.getGrades()) {
            for (Role role : grade.getFromUser().getRoles()) {
                if (role.getCode().contains("STUDENT_ROLE")) {
                    gradeService.delete(grade.getId());
                }
            }
        }
    }

    @RequestMapping(value = "/grades/groups/{groupId}/export.csv", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE') and isInGroup(#groupId)")
    public void exportGradesCsv(HttpServletResponse response, @PathVariable Long groupId) throws IOException {
        Group group = groupService.findById(groupId);

        response.addHeader("Content-disposition", "attachment;filename=grade.csv");
        response.addHeader("Content-type", "text/csv");

        for (Grade grade : group.getGrades()) {
            if (grade.getToUser().getReferenceId() != null) {
                List<String> line = new ArrayList<>();
                line.add(grade.getToUser().getReferenceId());
                line.add(Double.toString(grade.getGrade()));

                csvGenerator.writeLine(response.getWriter(), line, '\t');
            }
        }

        response.flushBuffer();
    }

    @RequestMapping(value = "/grades/groups/{groupId}/export.pdf", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE') and isInGroup(#groupId)")
    public void exportGradesPdf(HttpServletResponse response, @PathVariable Long groupId) throws IOException, DocumentException {
        response.addHeader("Content-disposition", "inline;filename=grade.pdf");
        response.addHeader("Content-type", "application/pdf");

        Map<String, Object> params = new HashMap<>();
        params.put("group", groupService.findById(groupId));

        pdfGenerator.renderPdf("grades", params, response.getOutputStream());
        response.flushBuffer();
    }
}