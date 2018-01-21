package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.Course;
import com.datbois.grademaster.model.Education;
import com.datbois.grademaster.service.EducationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1")
public class EducationController {

    @Autowired
    private EducationService educationService;

    @RequestMapping(value = "/educations", method = RequestMethod.GET)
    @ApiOperation(value = "Get all educations")
    public List<Education> educations() {
        return educationService.findAll();
    }

    @RequestMapping(value = "/educations/{educationId}", method = RequestMethod.GET)
    @ApiOperation(value = "Get a single education")
    public Education education(@PathVariable Long educationId) {
        return educationService.findById(educationId);
    }

    @RequestMapping(value = "/educations", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = "Create a new education.",
            notes = "Only admins can use this endpoint."
    )
    public Education createEducation(@RequestBody Education education) {
        return educationService.save(education);
    }

    @RequestMapping(value = "/educations/{educationId}", method = RequestMethod.PATCH)
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(value = "Update the education.")
    public Education updateEducation(@PathVariable Long educationId, @RequestBody Education education) throws Exception {
        Education existing = educationService.findById(educationId);

        education.copyNonNullProperties(existing);

        return educationService.save(existing);
    }

    @RequestMapping(value = "/educations/{educationId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(value = "Delete a single education")
    public void deleteCourse(@PathVariable Long educationId) {
        educationService.delete(educationId);
    }

    @RequestMapping(value = "/educations/{educationId}/courses", method = RequestMethod.GET)
    @ApiOperation(value = "Get all courses attached to this education")
    public Set<Course> coursesInEducation(@PathVariable Long educationId) {
        Education education = educationService.findById(educationId);

        return education.getCourses();
    }
}
