package by.Shelden.exeptions;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        String detailMessage,
        LocalDateTime errorTime
) {
}
