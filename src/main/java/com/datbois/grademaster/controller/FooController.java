package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.Notification;
import com.datbois.grademaster.service.NotificationService;
import com.datbois.grademaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FooController {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "/foo")
    public String foo() {
        Notification notification = new Notification();
        notification.setTitle("<teacher> graded <group>");
        notification.setMessage("<teacher> has assigned a <grade> to your group. You can now grade your group members.");
        notification.setUser(userService.findByEmail("john.doe@student.stenden.com"));

        notification = notificationService.save(notification);
        jmsTemplate.convertAndSend(Notification.QUEUE, notification.getId());

        return "Bar";
    }
}
