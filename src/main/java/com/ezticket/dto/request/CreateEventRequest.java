package com.ezticket.dto.request;

import com.ezticket.entity.enums.EventStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record CreateEventRequest(
        @NotBlank @Size(max = 255) String name,
        @NotBlank @Size(max = 255) String venue,
        @NotNull Instant startsAt,
        EventStatus status
) {
}
