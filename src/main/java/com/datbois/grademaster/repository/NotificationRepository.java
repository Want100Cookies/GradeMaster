package com.datbois.grademaster.repository;

import com.datbois.grademaster.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification findById(Long id);
}
