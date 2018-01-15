package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.Notification;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.model.UserDetails;
import com.datbois.grademaster.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * GET all notifications for a specific user
     *
     * @return All notifications
     * @endpoint (GET) /api/v1/notifications
     * @responseStatus OK
     */
    @RequestMapping(value = "/notifications", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> getAllUserNotifications(Authentication authentication){
        User user = ((UserDetails) authentication.getPrincipal()).getUser();

        return user.getNotificationList();
    }

    /**
     * PATCH all notifications
     *
     * @return All notifications as seen
     * @endpoint (PATCH) /api/v1/notifications
     * @responseStatus OK
     */
    @RequestMapping(value = "/notifications", method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> markAllNotificationsSeen(Authentication authentication){
        User user = ((UserDetails) authentication.getPrincipal()).getUser();

        List<Notification> notifications = user.getNotificationList();

        boolean seen = true;

        for(Notification notification : notifications){
            notification.setSeen(seen);
        }

        return notifications;
    }

    /**
     * PATCH a specific notification
     *
     * @return A specific notification
     * @endpoint (PATCH) /api/v1/notifications
     * @responseStatus OK
     */
    @RequestMapping(value = "/notifications/{notificationId}", method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.OK)
    public Notification markNotificationSeen(Authentication authentication, @PathVariable Long notificationId){
        User user = ((UserDetails) authentication.getPrincipal()).getUser();

        Notification notify = user.getNotificationList().get(notificationId.intValue());

        notify.setSeen(true);

        return notify;
    }
}
