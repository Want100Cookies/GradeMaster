package com.datbois.grademaster.service.impl;

import com.datbois.grademaster.model.Notification;
import com.datbois.grademaster.repository.NotificationRepository;
import com.datbois.grademaster.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Notification findById(Long id){ return notificationRepository.findById(id); }

    @Override
    public Notification save(Notification notification){ return notificationRepository.save(notification); }

    @Override
    public List<Notification> findAll(){ return notificationRepository.findAll(); }

    @Override
    public void delete(Long id){ notificationRepository.delete(id);}
}
