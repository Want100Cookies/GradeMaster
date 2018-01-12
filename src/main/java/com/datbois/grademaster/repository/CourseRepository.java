package com.datbois.grademaster.repository;

import com.datbois.grademaster.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findById(Long id);
}
