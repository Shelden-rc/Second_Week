package by.Shelden.directMail;

import by.Shelden.dto.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationSender {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationSender.class);
    private final JavaMailSender mailSender;

    public EmailNotificationSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendDirect(String email, String name, OperationType operation) {
        final String SUBJECT_CREATE = "Аккаунт успешно создан!";
        final String SUBJECT_DELETE = "Ваш аккаунт был удалён";
        final String BODY_TEMPLATE = """
            Здравствуйте, %s!
            
            %s
            """.stripTrailing();
        final String ACTION_TEXT_CREATE = "Ваш аккаунт успешно создан в системе.";
        final String ACTION_TEXT_DELETE = "Ваш аккаунт был удалён из системы.";

        String subject = switch (operation) {
            case CREATE -> SUBJECT_CREATE;
            case DELETE -> SUBJECT_DELETE;
        };
        String actionText = switch (operation) {
            case CREATE -> ACTION_TEXT_CREATE;
            case DELETE -> ACTION_TEXT_DELETE;
        };

        String text = String.format(BODY_TEMPLATE, name, actionText);

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