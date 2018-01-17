package com.datbois.grademaster.receiver;

import com.datbois.grademaster.model.Email;
import com.datbois.grademaster.model.Notification;
import com.datbois.grademaster.repository.NotificationRepository;
import com.datbois.grademaster.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class NotificationReceiver {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmailService emailService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @JmsListener(destination = "NotificationQueue")
    public void sendNotificationEmail(Long notificationId) throws IOException {
        Notification notification = notificationRepository.findById(notificationId);

        Email mail = new Email();

        mail.setFrom("no-reply@grademaster.com");
        mail.setBody(notification.getMessage());
        mail.setSubject(notification.getTitle());
        mail.setTo(notification.getUser().getEmail());

        if (notification.getLink() != null && notification.getLinkText() != null) {
            mail.setLinkText(notification.getLinkText());
            mail.setLink("http://localhost:8080" + notification.getLink());
        }

        emailService.sendEmail(mail);

        logger.info("New mail send: " + mail);
    }
}
