package com.datbois.grademaster.service.impl;

import com.datbois.grademaster.configuration.DomainProperties;
import com.datbois.grademaster.model.Email;
import com.datbois.grademaster.model.Notification;
import com.datbois.grademaster.repository.NotificationRepository;
import com.datbois.grademaster.service.EmailService;
import com.datbois.grademaster.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private DomainProperties domainProperties;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification save(Notification notification) {
        notification = notificationRepository.save(notification);

        Email mail = new Email();

        mail.setBody(notification.getMessage());
        mail.setSubject(notification.getTitle());
        mail.setTo(notification.getUser().getEmail());

        if (notification.getLink() != null && notification.getLinkText() != null) {
            mail.setLinkText(notification.getLinkText());
            mail.setLink(domainProperties.getBase() + notification.getLink());
        }

        emailService.sendToEmailQueue(mail);

        return notification;
    }

    @Override
    public Notification findById(Long id) {
        return notificationRepository.findById(id);
    }
}
