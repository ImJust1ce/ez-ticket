package com.ezticket.dto.response;

import com.ezticket.entity.enums.EventStatus;

import java.time.Instant;

public record EventResponse(
        Long id,
        String name,
        String venue,
        Instant startsAt,
        EventStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
