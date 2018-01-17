package com.datbois.grademaster.service.impl;

import com.datbois.grademaster.model.Notification;
import com.datbois.grademaster.repository.NotificationRepository;
import com.datbois.grademaster.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification save(Notification notification) {
        notification = notificationRepository.save(notification);
        jmsTemplate.convertAndSend(Notification.QUEUE, notification.getId());

        return notification;
    }

    @Override
    public Notification findById(Long id) {
        return notificationRepository.findById(id);
    }
}
