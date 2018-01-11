package com.datbois.grademaster.service.impl;

import com.datbois.grademaster.model.Course;
import com.datbois.grademaster.repository.CourseRepository;
import com.datbois.grademaster.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Course findById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public void delete(Long id) {
        courseRepository.delete(id);
    }
}
