package by.Shelden.dto;

import java.io.Serializable;

/**
 * Событие, публикуемое при изменении данных пользователя.
 * Используется как payload для сообщений в Apache Kafka топике {@code user-events}
 * и передаётся между сервисами в асинхронном режиме.
 */
public record UserEvent(

        /**
         * Тип операции проводимый над пользователем.
         */
        OperationType operation,

        /**
         * Электронная почта пользователя, которому нужно отправить уведомление.
         */
        String email

) implements Serializable {}
