package com.datbois.grademaster.service;

import com.datbois.grademaster.model.Notification;

import java.util.List;

public interface NotificationService {

    List<Notification> findAll();

    Notification findById(Long id);

    Notification save(Notification notification);

    void delete(Long id);
}
