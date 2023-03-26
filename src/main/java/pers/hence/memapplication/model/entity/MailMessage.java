package pers.hence.memapplication.model.entity;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

/**
 * 对SimpleMailMessage进行一层封装
 * @author https://github.com/Mr-Jacks520
 */
@Component
@NoArgsConstructor
public class MailMessage {

    @Value("${spring.mail.username}")
    private String fromMail;

    public SimpleMailMessage create(String toMail, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromMail);
        message.setTo(toMail);
        message.setSubject(subject);
        message.setText(text);
        return message;
    }
}
