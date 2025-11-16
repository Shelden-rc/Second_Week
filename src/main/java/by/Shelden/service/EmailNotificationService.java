package by.Shelden.service;

import by.Shelden.dto.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationService.class);
    private final JavaMailSender mailSender;

    public EmailNotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendDirect(String email, String name, OperationType operation) {
        String subject = switch (operation) {
            case CREATE -> "Аккаунт успешно создан!";
            case DELETE -> "Ваш аккаунт был удалён";
        };
        String text = switch (operation) {
            case CREATE -> String.format("""
                    Здравствуйте, %s!
                    
                    Ваш аккаунт успешно создан в системе.
                    """, name);
            case DELETE -> String.format("""
                    Здравствуйте, %s!
                    
                    Ваш аккаунт был удалён из системы.
                    """, name);
        };

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);

        try {
            mailSender.send(message);
            log.info("Прямое письмо отправлено на {}, операция: {}", email, operation);
        }catch (MailException e) {
            log.warn("Не удалось отправить прямое письмо на {}: {}",
                    email, e.getMessage());
        }
    }
}