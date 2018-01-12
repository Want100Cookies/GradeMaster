package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.Notification;
import com.datbois.grademaster.service.NotificationService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "/notifications", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> notifications(){ return notificationService.findAll(); }


    @RequestMapping(value = "/notifications", method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> markAllNotificationsSeen(){
        List<Notification> notifications = notificationService.findAll();

        boolean seen = true;

        for(Notification notification : notifications){
            notification.setSeen(seen);
        }

        return notifications;
    }

    @RequestMapping(value = "/notifications/{notificationId}", method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.OK)
    public Notification markNotificationSeen(@PathVariable Long notificationId){
        Notification notify = notificationService.findById(notificationId);
        boolean seen = true;

        notify.setSeen(seen);

        return notificationService.save(notify);
    }
}
