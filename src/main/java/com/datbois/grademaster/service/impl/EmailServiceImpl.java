package com.datbois.grademaster.service.impl;

import com.datbois.grademaster.model.Email;
import com.datbois.grademaster.service.EmailService;
import com.datbois.grademaster.utils.CssInliner;
import com.datbois.grademaster.utils.MailContentBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.io.File;


@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailContentBuilder mailContentBuilder;

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public void sendEmail(Email email) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(email.getFrom());
            messageHelper.setTo(email.getTo());
            messageHelper.setSubject(email.getSubject());

            String html = mailContentBuilder.build(email);

            File styleTag = resourceLoader.getResource("classpath:/public/node_modules/muicss/dist/email/mui-email-styletag.css").getFile();
            File inline = resourceLoader.getResource("classpath:/public/node_modules/muicss/dist/email/mui-email-inline.css").getFile();

            String htmlWithStyleTags = CssInliner.inlineCss(styleTag, html);
            String htmlWithInlineCss = CssInliner.inlineCss(inline, htmlWithStyleTags);

            messageHelper.setText(htmlWithInlineCss, true);
        };

        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            // runtime exception; compiler will not force you to handle it
            LoggerFactory.getLogger(this.getClass()).info(e.toString());
        }
    }
}
