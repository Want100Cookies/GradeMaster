package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.Notification;
import com.datbois.grademaster.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * GET all notifications
     *
     * @return All notifications
     * @endpoint (GET) /api/v1/notifications
     * @responseStatus OK
     */
    @RequestMapping(value = "/notifications", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> notifications(){ return notificationService.findAll(); }


    /**
     * PATCH all notifications
     *
     * @return All notifications as seen
     * @endpoint (PATCH) /api/v1/notifications
     * @responseStatus OK
     */
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

    /**
     * PATCH a specific notification
     *
     * @return A notification set as seen
     * @endpoint (PATCH) /api/v1/notifications/{notificationId}
     * @responseStatus OK
     */
    @RequestMapping(value = "/notifications/{notificationId}", method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.OK)
    public Notification markNotificationSeen(@PathVariable Long notificationId){
        Notification notify = notificationService.findById(notificationId);
        boolean seen = true;

        notify.setSeen(seen);

        return notificationService.save(notify);
    }
}
