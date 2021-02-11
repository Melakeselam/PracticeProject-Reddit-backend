package com.example.redditclone.service;

import com.example.redditclone.config.MailProperties;
import com.example.redditclone.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.example.redditclone.exception.SpringRedditException;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    MailContentBuilder mailContentBuilder;
    JavaMailSender mailSender;
    MailProperties mailProperties;

    @Async
    public void sendEmail(NotificationEmail notificationEmail) throws SpringRedditException {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom(mailProperties.getFrom());
            mimeMessageHelper.setTo(notificationEmail.getRecepient());
            mimeMessageHelper.setSubject(notificationEmail.getSubject());
            mimeMessageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
        };
        try {
            mailSender.send(mimeMessagePreparator);
            log.info("Activation email sent!!");
        }catch (MailException e){
            throw new SpringRedditException("Exception occured when sending email to " + notificationEmail.getRecepient());
        }

    }
}
