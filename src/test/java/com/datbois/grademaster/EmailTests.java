package com.datbois.grademaster;

import com.datbois.grademaster.mail.SmtpServerRule;
import com.datbois.grademaster.model.Email;
import com.datbois.grademaster.service.EmailService;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class EmailTests extends OAuthTests {

    @Rule
    public SmtpServerRule smtpServer = new SmtpServerRule(2525);

    @Autowired
    private EmailService emailService;

    @Test
    public void canSendEmail() throws IOException, MessagingException {
        Email email = new Email(
                "foo@bar.com",
                "Foo",
                "Bar",
                "http://baz.com/",
                "Bazz"
        );
        email.setFrom("some@mail.com");
        emailService.sendEmail(email);

        MimeMessage[] mailbox = smtpServer.getMessages();
        assertThat(mailbox.length, is(greaterThan(0)));

        MimeMessage received = mailbox[0];

        assertThat(received.getAllRecipients()[0].toString(), is(email.getTo()));
        assertThat(received.getSubject(), is(email.getSubject()));
        assertThat(received.getContent().toString(), containsString(email.getBody()));
        assertThat(received.getContent().toString(), containsString(email.getLink()));
        assertThat(received.getContent().toString(), containsString(email.getLinkText()));
    }
}
