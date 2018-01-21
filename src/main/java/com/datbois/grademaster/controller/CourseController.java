package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.Course;
import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.response.SimpleGroupResponse;
import com.datbois.grademaster.service.CourseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @RequestMapping(value = "/courses", method = RequestMethod.GET)
    @ApiOperation(value = "Get all courses")
    public List<Course> courses() {
        return courseService.findAll();
    }

    @RequestMapping(value = "/courses/{courseId}", method = RequestMethod.GET)
    @ApiOperation(value = "Get a single course")
    public Course course(@PathVariable Long courseId) {
        return courseService.findById(courseId);
    }

    @RequestMapping(value = "/courses", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = "Create a new course",
            notes = "Only admins can use this endpoint"
    )
    public Course createCourse(@RequestBody Course course) {
        return courseService.save(course);
    }

    @RequestMapping(value = "/courses/{courseId}", method = RequestMethod.PATCH)
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(value = "Update the course")
    public Course updateCourse(@PathVariable Long courseId, @RequestBody Course course) throws Exception {
        Course existing = courseService.findById(courseId);

        course.copyNonNullProperties(existing);

        return courseService.save(existing);
    }

    @RequestMapping(value = "/courses/{courseId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(value = "Delete a single course")
    public void deleteCourse(@PathVariable Long courseId) {
        courseService.delete(courseId);
    }

    @RequestMapping(value = "/courses/{courseId}/groups", method = RequestMethod.GET)
    @ApiOperation(value = "Get all groups under this course")
    public List<SimpleGroupResponse> groupsInCourse(@PathVariable Long courseId) {
        Course course = courseService.findById(courseId);
        List<SimpleGroupResponse> response = new ArrayList<>();

        for (Group group : course.getGroups()) {
            response.add(new SimpleGroupResponse(group));
        }

        return response;
    }
}
