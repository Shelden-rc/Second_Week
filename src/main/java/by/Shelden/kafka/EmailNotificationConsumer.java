package by.Shelden.kafka;

import by.Shelden.dto.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationConsumer.class);
    private final JavaMailSender mailSender;

    public EmailNotificationConsumer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-service-group")
    public void consume(UserEvent event) {
        String text = switch (event.operation()) {
            case CREATE -> "Здравствуйте! Ваш аккаунт успешно создан.";
            case DELETE -> "Здравствуйте! Ваш аккаунт был удалён.";
        };

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.email());
        message.setSubject("Уведомление об аккаунте");
        message.setText(text);
        log.info("Попытка отправить сообщение на {}, операция: {}", event.email(), event.operation());
        mailSender.send(message);
    }
}