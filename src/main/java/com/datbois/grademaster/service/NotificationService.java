package com.datbois.grademaster.service;

import com.datbois.grademaster.model.Notification;

import java.util.List;

public interface NotificationService {

    List<Notification> findAll();

    Notification save(Notification notification);

    Notification findById(Long id);
}
