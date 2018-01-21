package com.datbois.grademaster.service.impl;

import com.datbois.grademaster.model.Email;
import com.datbois.grademaster.service.EmailService;
import com.datbois.grademaster.util.CssInliner;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void sendToEmailQueue(Email email) {
        jmsTemplate.convertAndSend(Email.QUEUE, email);
    }

    @Override
    public void sendEmail(Email email) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(email.getFrom());
            messageHelper.setTo(email.getTo());
            messageHelper.setSubject(email.getSubject());

            messageHelper.setText(emailTemplateBuilder(email), true);
        };

        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            // runtime exception; compiler will not force you to handle it
            LoggerFactory.getLogger(this.getClass()).info(e.toString());
        }
    }

    private String emailTemplateBuilder(Email email) throws IOException {
        Context context = new Context();
        context.setVariable("title", email.getSubject());
        context.setVariable("message", email.getBody());
        context.setVariable("buttonLink", email.getLink());
        context.setVariable("buttonText", email.getLinkText());

        String mailTemplate = templateEngine.process("mailTemplate", context);

        mailTemplate = inliner("classpath:/public/node_modules/muicss/dist/email/mui-email-styletag.css", mailTemplate);
        mailTemplate = inliner("classpath:/public/node_modules/muicss/dist/email/mui-email-inline.css", mailTemplate);
        return mailTemplate;
    }

    private String inliner(String cssFilePath, String html) throws IOException {
        File cssFile = resourceLoader.getResource(cssFilePath).getFile();
        return CssInliner.inlineCss(cssFile, html);
    }
}
