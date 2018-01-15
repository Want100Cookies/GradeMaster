package com.datbois.grademaster.utils;

import com.datbois.grademaster.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailContentBuilder {

    @Autowired
    private TemplateEngine templateEngine;

    public String build(Email email) {
        Context context = new Context();
        context.setVariable("title", email.getSubject());
        context.setVariable("message", email.getBody());
        return templateEngine.process("mailTemplate", context);
    }
}
