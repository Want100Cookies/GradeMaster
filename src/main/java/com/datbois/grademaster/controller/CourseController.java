package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.Course;
import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1")
public class CourseController {

    @Autowired
    private CourseService courseService;

    /**
     * Get all courses
     *
     * @return All courses
     * @endpoint (GET) /api/v1/courses
     * @responseStatus OK
     */
    @RequestMapping(value = "/courses", method = RequestMethod.GET)
    public List<Course> courses() {
        return courseService.findAll();
    }

    /**
     * Get a single course
     *
     * @param courseId The id of the needed course
     * @return A single course
     * @endpoint (GET) /api/v1/courses/{courseId}
     * @responseStatus OK
     */
    @RequestMapping(value = "/courses/{courseId}", method = RequestMethod.GET)
    public Course course(@PathVariable Long courseId) {
        return courseService.findById(courseId);
    }

    /**
     * Create a new course.
     * Only admins can use this endpoint.
     *
     * @param course JSON object with {name}
     * @return The saved course
     * @endpoint (POST) /api/v1/courses
     * @responseStatus CREATED
     */
    @RequestMapping(value = "/courses", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.CREATED)
    public Course createCourse(@RequestBody Course course) {
        return courseService.save(course);
    }

    /**
     * Update the course.
     *
     * @param courseId The id of the to be updated course
     * @param course   JSON object with {name}
     * @return The updated course
     * @throws Exception If any given method is not in the Course object
     * @endpoint (PATCH) /api/v1/courses/{courseId}
     * @responseStatus OK
     */
    @RequestMapping(value = "/courses/{courseId}", method = RequestMethod.PATCH)
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    public Course updateCourse(@PathVariable Long courseId, @RequestBody Course course) throws Exception {
        Course existing = courseService.findById(courseId);

        course.copyNonNullProperties(existing);

        return courseService.save(existing);
    }

    /**
     * Delete a single course
     *
     * @param courseId The id of the to be deleted course
     * @endpoint (DELETE) /api/v1/courses/{courseId}
     * @responseStatus ACCEPTED
     */
    @RequestMapping(value = "/courses/{courseId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteCourse(@PathVariable Long courseId) {
        courseService.delete(courseId);
    }

    @RequestMapping(value = "/courses/{courseId}/groups", method = RequestMethod.GET)
    public Set<Group> groupsInCourse(@PathVariable Long courseId) {
        Course course = courseService.findById(courseId);

        return course.getGroups();
    }
}
