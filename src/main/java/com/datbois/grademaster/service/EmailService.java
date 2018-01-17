package com.datbois.grademaster.service;

import com.datbois.grademaster.model.Email;

import java.io.IOException;

public interface EmailService {

    void sendToEmailQueue(Email email);

    void sendEmail(Email email) throws IOException;

}
