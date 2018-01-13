package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.Course;
import com.datbois.grademaster.model.Education;
import com.datbois.grademaster.service.EducationService;
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

    /**
     * Get all educations
     *
     * @return All educations
     * @endpoint (GET) /api/v1/educations
     * @responseStatus OK
     */
    @RequestMapping(value = "/educations", method = RequestMethod.GET)
    public List<Education> educations() {
        return educationService.findAll();
    }

    /**
     * Get a single education
     *
     * @param educationId The id of the needed education
     * @return A single education
     * @endpoint (GET) /api/v1/educations/{educationId}
     * @responseStatus OK
     */
    @RequestMapping(value = "/educations/{educationId}", method = RequestMethod.GET)
    public Education education(@PathVariable Long educationId) {
        return educationService.findById(educationId);
    }

    /**
     * Create a new education.
     * Only admins can use this endpoint.
     *
     * @param education JSON object with {name}
     * @return The saved education
     * @endpoint (POST) /api/v1/educations
     * @responseStatus CREATED
     */
    @RequestMapping(value = "/educations", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.CREATED)
    public Education createEducation(@RequestBody Education education) {
        return educationService.save(education);
    }

    /**
     * Update the education.
     *
     * @param educationId The id of the to be updated education
     * @param education   JSON object with {name}
     * @return The updated education
     * @throws Exception If any given method is not in the Education object
     * @endpoint (PATCH) /api/v1/educations/{educationId}
     * @responseStatus OK
     */
    @RequestMapping(value = "/educations/{educationId}", method = RequestMethod.PATCH)
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    public Education updateEducation(@PathVariable Long educationId, @RequestBody Education education) throws Exception {
        Education existing = educationService.findById(educationId);

        education.copyNonNullProperties(existing);

        return educationService.save(existing);
    }

    /**
     * Delete a single education
     *
     * @param educationId The id of the to be deleted education
     * @endpoint (DELETE) /api/v1/educations/{educationId}
     * @responseStatus ACCEPTED
     */
    @RequestMapping(value = "/educations/{educationId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteCourse(@PathVariable Long educationId) {
        educationService.delete(educationId);
    }

    /**
     * Get all courses attached to this education
     *
     * @param educationId The id of the education
     * @return A list with courses
     */
    @RequestMapping(value = "/educations/{educationId}/courses", method = RequestMethod.GET)
    public Set<Course> coursesInEducation(@PathVariable Long educationId) {
        Education education = educationService.findById(educationId);

        return education.getCourses();
    }
}
