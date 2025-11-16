package by.Shelden.kafka;

import by.Shelden.dto.OperationType;
import by.Shelden.dto.UserEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(
        partitions = 1,
        topics = { "user-events" },
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9095",
                "port=9095"
        }
)
class EmailNotificationConsumerTest {

    @Autowired
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    @MockitoBean
    private JavaMailSender mailSender;

    @Test
    void shouldSendEmailOnCreateEvent() {
        kafkaTemplate.send("user-events", new UserEvent(OperationType.CREATE, "shelden@example.com"));

        verify(mailSender, timeout(10000)).send(any(SimpleMailMessage.class));

        verify(mailSender).send(argThat((SimpleMailMessage msg) ->
                "Уведомление об аккаунте".equals(msg.getSubject()) &&
                        msg.getText() != null &&
                        msg.getText().contains("успешно создан") &&
                        "shelden@example.com".equals(msg.getTo()[0])
        ));
    }

    @Test
    void shouldSendEmailOnDeleteEvent() {
        kafkaTemplate.send("user-events", new UserEvent(OperationType.DELETE, "shelden@example.com"));

        verify(mailSender, timeout(10000)).send(any(SimpleMailMessage.class));

        verify(mailSender).send(argThat((SimpleMailMessage msg) ->
                msg.getText() != null &&
                        msg.getText().contains("удалён") &&
                        "shelden@example.com".equals(msg.getTo()[0])
        ));
    }
}