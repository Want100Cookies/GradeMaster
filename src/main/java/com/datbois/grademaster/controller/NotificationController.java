package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.Notification;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.model.UserDetails;
import com.datbois.grademaster.service.NotificationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "/notifications", method = RequestMethod.GET)
    @ApiOperation(value = "Get all notifications for the logged in user")
    public List<Notification> getAllUserNotifications(Authentication authentication) {
        User user = ((UserDetails) authentication.getPrincipal()).getUser();

        return user.getNotificationList();
    }

    @RequestMapping(value = "/notifications", method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(value = "Mark all notifications of this user as read")
    public List<Notification> markAllNotificationsSeen(Authentication authentication) {
        User user = ((UserDetails) authentication.getPrincipal()).getUser();

        List<Notification> notifications = user.getNotificationList();

        for (Notification notification : notifications) {
            notification.setSeen(true);
            notificationService.save(notification);
        }

        return notifications;
    }

    @RequestMapping(value = "/notifications/{notificationId}", method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(value = "Mark a single notification as seen")
    public Notification markNotificationSeen(Authentication authentication, @PathVariable Long notificationId) {
        User user = ((UserDetails) authentication.getPrincipal()).getUser();

        Notification notify = user.getNotificationList().get(notificationId.intValue());

        notify.setSeen(true);

        return notificationService.save(notify);
    }
}
