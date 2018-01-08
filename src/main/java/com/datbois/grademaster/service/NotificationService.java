package com.datbois.grademaster.service;

import com.datbois.grademaster.model.Notification;

import java.util.List;

public interface NotificationService {

    Notification findById(Long id);

    Notification save(Notification notification);

    List<Notification> findAll();

    void delete(Long id);
}
