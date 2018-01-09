package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.Notification;
import com.datbois.grademaster.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "/notifications/{notificationId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.OK)
    public Notification notification(@PathVariable long notificationId){
        return notificationService.findById(notificationId);
    }

}
