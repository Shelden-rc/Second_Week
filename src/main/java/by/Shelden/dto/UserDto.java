package by.Shelden.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDate;
/**
 * DTO для представления данных пользователя.
 * Используется для передачи информации о пользователе между сервисами.
 */
public record UserDto(
        /**
         * Уникальный идентификатор пользователя.
         * Должен быть {@code null}, поскольку присваивается автоматически при создании.
         */
        @Null
        Long id,

        /**
         * Имя пользователя.
         * Не может быть пустым.
         */
        @NotBlank
        String name,

        /**
         * Email пользователя.
         * Должен быть корректным email-адресом и не может быть пустым.
         */
        @Email
        @NotBlank
        String email,

        /**
         * Возраст пользователя.
         * Не может быть {@code null}.
         */
        @NotNull
        Integer age,

        /**
         * Дата создания записи о пользователе.
         * Должна быть {@code null}, поскольку система устанавливает её автоматически.
         */
        @Null
        LocalDate createdAt
) {}
