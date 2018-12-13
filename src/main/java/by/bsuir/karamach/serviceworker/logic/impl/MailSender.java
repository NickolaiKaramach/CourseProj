package by.bsuir.karamach.serviceworker.logic.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailSender {

    @Value("${spring.mail.username}")
    private String username;

    private JavaMailSender mailSender;

    public MailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String emailTo, String subject, String message) {
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//
//        mailMessage.setFrom(username);
//        mailMessage.setTo(emailTo);
//        mailMessage.setSubject(subject);
//        mailMessage.setText(mailMessage);
//
//        mailSender.send(mailMessage);

        MimeMessage mailMessage = mailSender.createMimeMessage();

        try {
            mailMessage.setSubject(subject);
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(mailMessage, true);
            helper.setFrom(username);
            helper.setTo(emailTo);
            helper.setText(message, true);
            mailSender.send(mailMessage);

        } catch (MessagingException e) {
            //TODO: LOG !
            System.out.println("ERROR DURING MAIL SENDING");
        }
    }
}
