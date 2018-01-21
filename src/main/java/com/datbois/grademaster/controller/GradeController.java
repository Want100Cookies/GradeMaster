package com.datbois.grademaster.controller;

import com.datbois.grademaster.exception.NotFoundException;
import com.datbois.grademaster.model.*;
import com.datbois.grademaster.response.AllGradesInGroupResponse;
import com.datbois.grademaster.response.FinalGradeResponse;
import com.datbois.grademaster.response.GradeResponse;
import com.datbois.grademaster.response.StatusResponse;
import com.datbois.grademaster.service.GradeService;
import com.datbois.grademaster.service.GroupGradeService;
import com.datbois.grademaster.service.GroupService;
import com.datbois.grademaster.service.UserService;
import com.datbois.grademaster.util.CsvGeneratorUtil;
import com.datbois.grademaster.util.PdfGeneratorUtil;
import com.lowagie.text.DocumentException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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

    @RequestMapping(value = "/grades/status/groups/{groupId}", method = RequestMethod.GET)
    @PreAuthorize("isInGroup(#groupId)")
    @ApiOperation(
            value = "Get grading status",
            notes = " Only available if in group.\n" +
                    "     <p>\n" +
                    "     OPEN: Group exists but no group grade has been set.\n" +
                    "     PENDING: Group exists and group grade is set, students can start grading.\n" +
                    "     CLOSED: All students have received a grade from teacher, grading closed."
    )
    public StatusResponse getGradingStatus(@PathVariable Long groupId) {
        Status current;

        Group group = groupService.findById(groupId);

        if (group == null) {
            throw new NotFoundException();
        }

        current = Status.OPEN;
        if (group.getGroupGrade() != null) {
            current = Status.PENDING;

            if (groupService.getStudents(groupId).size() == groupService.getGradesFromTeacherToStudent(groupId).size()) {
                current = Status.CLOSED;
            }
        }

        return new StatusResponse(current);
    }

    @RequestMapping(value = "/grades/groups/{groupId}/users/{userId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE','ADMIN_ROLE') or isCurrentUser(#userId)")
    @ApiOperation(
            value = "Get final grade for a student in a particular group",
            notes = "Only if logged in as student or teacher."
    )
    public FinalGradeResponse getFinalGrade(@PathVariable Long groupId, @PathVariable Long userId) {
        Group group = groupService.findById(groupId);
        User user = group
                .getUsers()
                .stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElse(null);

        if (user == null) {
            throw new NotFoundException("Given user is not in group");
        }

        Grade grade = group
                .getGrades()
                .stream()
                .filter(g -> g.getToUser().getId().equals(userId))
                .filter(g -> g.getFromUser().hasAnyRole("TEACHER_ROLE"))
                .findFirst()
                .orElse(null);

        if (grade == null) {
            throw new NotFoundException("This user has no final grade yet");
        }

        return new FinalGradeResponse(grade);
    }

    @RequestMapping(value = "/grades/groups/{groupId}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE','ADMIN_ROLE') or isInGroup(#groupId)")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = "Insert grades for all group members",
            notes = "Only if user is in group, teacher or admin"
    )
    public List<GradeResponse> createGrade(@PathVariable Long groupId, @RequestBody Grade[] grades) {

        List<GradeResponse> responses = new ArrayList<>();

        Group group = groupService.findById(groupId);

        Arrays
                .stream(grades)
                .filter(grade -> groupService.userIsInGroup(grade.getFromUser().getId(), groupId)
                        && groupService.userIsInGroup(grade.getToUser().getId(), groupId)
                )
                .forEach(grade -> {

                    grade.setValid(!(grade.getMotivation().equals("")
                            && grade.getFromUser().hasAnyRole("STUDENT_ROLE"))
                    );

                    grade.setGroup(group);

                    responses.add(new GradeResponse(gradeService.save(grade)));
                });

        return responses;
    }

    @RequestMapping(value = "/grades/groups/{groupId}", method = RequestMethod.PATCH)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = "Insert group grade for a group",
            notes = "Only possible if logged in as teacher or admin"
    )
    public GroupGrade insertGroupGrade(Authentication authentication, @PathVariable Long groupId, @RequestBody GroupGrade groupGrade) {
        User user = ((UserDetails) authentication.getPrincipal()).getUser();
        Group group = groupService.findById(groupId);

        groupGrade.setTeacher(user);
        groupGrade.setGroup(group);

        groupGrade = groupGradeService.save(groupGrade);

        group.setGroupGrade(groupGrade);
        groupService.save(group);

        return groupGrade;
    }

    @RequestMapping(value = "/grades/groups/{groupId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE','ADMIN_ROLE')")
    @ApiOperation(
            value = "Get group grade and all grades assigned and received by users of this particular group",
            notes = "Only possible if logged in as teacher and admin"
    )
    public AllGradesInGroupResponse getAllGradesForAGroup(@PathVariable Long groupId) {
        Group group = groupService.findById(groupId);

        return new AllGradesInGroupResponse(group.getGroupGrade(), group.getGrades());
    }

    @RequestMapping(value = "/grades/groups/{groupId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(
            value = "Delete grades given by students only for this particular group",
            notes = "Only possible if logged in as teacher or admin"
    )
    public void removeGrades(@PathVariable Long groupId) {
        Group exists = groupService.findById(groupId);

        for (Grade grade : exists.getGrades()) {
            if (grade.getFromUser().hasAnyRole("STUDENT_ROLE")) {
                gradeService.delete(grade.getId());
            }
        }
    }

    @RequestMapping(value = "/grades/groups/{groupId}/export.csv", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE') and isInGroup(#groupId)")
    @ApiOperation(
            value = "Export all grades for this group to CSV (tab separated)",
            notes = "Users without a reference ID are not added to the export"
    )
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
    @ApiOperation(value = "Export all grades for this group to PDF")
    public void exportGradesPdf(HttpServletResponse response, @PathVariable Long groupId) throws IOException, DocumentException {
        response.addHeader("Content-disposition", "inline;filename=grade.pdf");
        response.addHeader("Content-type", "application/pdf");

        Map<String, Object> params = new HashMap<>();
        params.put("group", groupService.findById(groupId));

        pdfGenerator.renderPdf("grades", params, response.getOutputStream());
        response.flushBuffer();
    }
}