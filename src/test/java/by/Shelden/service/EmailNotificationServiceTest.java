package by.Shelden.service;

import by.Shelden.dto.OperationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.times;
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
class EmailNotificationServiceTest {

    @Autowired
    private EmailNotificationService emailNotificationService;

    @MockitoBean
    private JavaMailSender mailSender;

    @Test
    void shouldSendCreateEmailSuccessfully() {
        String email = "shelden@example.com";
        String name = "Shelden";

        emailNotificationService.sendDirect(email, name, OperationType.CREATE);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));

        verify(mailSender).send(argThat((SimpleMailMessage msg) ->
                "shelden@example.com".equals(msg.getTo()[0]) &&
                        "Аккаунт успешно создан!".equals(msg.getSubject()) &&
                        msg.getText() != null &&
                        msg.getText().contains("Ваш аккаунт успешно создан в системе.") &&
                        msg.getText().contains("Shelden")
        ));
    }

    @Test
    void shouldSendDeleteEmailSuccessfully() {
        String email = "shelden@example.com";
        String name = "Shelden";

        emailNotificationService.sendDirect(email, name, OperationType.DELETE);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));

        verify(mailSender).send(argThat((SimpleMailMessage msg) ->
                "shelden@example.com".equals(msg.getTo()[0]) &&
                        "Ваш аккаунт был удалён".equals(msg.getSubject()) &&
                        msg.getText() != null &&
                        msg.getText().contains("Ваш аккаунт был удалён из системы.") &&
                        msg.getText().contains("Shelden")
        ));
    }

}