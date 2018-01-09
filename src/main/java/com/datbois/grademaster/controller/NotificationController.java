package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.Notification;
import com.datbois.grademaster.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "/notifications", method = RequestMethod.GET)
    public List<Notification> notifications() { return notificationService.findAll(); }

    @RequestMapping(value = "/notifications/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Notification findById(@PathVariable long id){ return notificationService.findById(id); }

    @RequestMapping(value = "/notifications/{id}", method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.OK)
    public Notification updateNotification(@PathVariable long id, @RequestBody Notification notification){
        Notification notify = notificationService.findById(id);
        boolean sean = notify.isSeen();

        notification.setSeen(sean);

        return notificationService.save(notify);
    }
}
