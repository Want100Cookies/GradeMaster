package com.datbois.grademaster.receiver;

import com.datbois.grademaster.model.Email;
import com.datbois.grademaster.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EmailReceiver {

    @Autowired
    private EmailService emailService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @JmsListener(destination = Email.QUEUE)
    public void sendNotificationEmail(Email mail) throws IOException {
        mail.setFrom("no-reply@grademaster.com");
        emailService.sendEmail(mail);

        logger.info("New mail send: " + mail);
    }
}
