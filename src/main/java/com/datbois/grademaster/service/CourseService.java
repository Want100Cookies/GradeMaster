package com.datbois.grademaster.service;

import com.datbois.grademaster.model.Course;

import java.util.List;

public interface CourseService {
    Course findById(Long id);

    List<Course> findAll();

    Course save(Course course);

    long count();

    void delete(Long id);
}
