package nordgym.configuration;

import nordgym.domain.models.binding.ContactFormBindingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

@Component
public class MailComponent {

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
            System.err.println(e.getMessage());
            return false;
        }
    }
}
