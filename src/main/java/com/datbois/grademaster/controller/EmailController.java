package com.datbois.grademaster.controller;

import com.datbois.grademaster.service.EmailService;
import com.datbois.grademaster.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/emails")
    public void mail(){
        Email mail = new Email();

        mail.setFrom("datboisgrademaster@gmail.com");
        mail.setBody("Hallo dit is een test bericht.");
        mail.setSubject("Test-Email");
        mail.setTo("DannyHooyer@gmail.com");
        emailService.sendEmail(mail);
    }
}
