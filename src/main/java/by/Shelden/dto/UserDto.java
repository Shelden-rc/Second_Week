package by.Shelden.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDate;

public record UserDto(

@Null
Long id,

@NotBlank
String name,

@Email
@NotBlank
String email,

@NotNull
Integer age,

@Null
LocalDate createdAt
) {
}
