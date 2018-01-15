package com.datbois.grademaster.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.datbois.grademaster.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Notification findById(Long id);
}
