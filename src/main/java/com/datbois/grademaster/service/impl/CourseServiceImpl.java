package com.datbois.grademaster.service.impl;

import com.datbois.grademaster.model.Course;
import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.repository.CourseRepository;
import com.datbois.grademaster.service.CourseService;
import com.datbois.grademaster.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GroupService groupService;

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
    public long count() {
        return courseRepository.count();
    }

    @Override
    public void delete(Long id) {
        Course course = courseRepository.findById(id);
        Set<Group> groups = course.getGroups();
        for (Group group : groups) {
            groupService.delete(group.getId());
        }
        courseRepository.delete(id);
    }
}
