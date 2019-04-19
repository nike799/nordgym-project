package com.nordgym.configuration;

import com.nordgym.domain.models.binding.ContactFormBindingModel;
import com.nordgym.web.controllers.HomeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

@Component
public class MailComponent {

    private static Logger logger = LoggerFactory.getLogger(MailComponent.class);

    @Autowired
    MailSender mailSender;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    TemplateEngine templateEngine;

    public boolean sendSimpleMail(ContactFormBindingModel contact) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(contact.getEmail());
        mailMessage.setSubject(contact.getSubject());
        mailMessage.setText(contact.getMessage());
        mailMessage.setTo("nagrozdanov@gmail.com");

        try {
            mailSender.send(mailMessage);
            return true;
        } catch (MailException e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}
