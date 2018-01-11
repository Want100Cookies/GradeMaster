package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.Notification;
import com.datbois.grademaster.service.EmailService;
import com.datbois.grademaster.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "/notifications", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> notifications(){return notificationService.findAll(); }

    @RequestMapping(value = "/notifications/{notificationId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Notification notification(@PathVariable long notificationId){
        return notificationService.findById(notificationId);
    }
}
