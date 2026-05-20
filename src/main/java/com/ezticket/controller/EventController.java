package com.ezticket.controller;

import com.ezticket.config.RequestIdFilter;
import com.ezticket.dto.response.ApiResponse;
import com.ezticket.dto.response.EventResponse;
import com.ezticket.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Tag(name = "Events", description = "Public event catalog (published only)")
public class EventController {

    private final EventService eventService;

    @GetMapping
    @Operation(summary = "List published events")
    public ApiResponse<List<EventResponse>> list(HttpServletRequest request) {
        return wrap(eventService.listPublished(), request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get published event by id")
    public ApiResponse<EventResponse> get(@PathVariable Long id, HttpServletRequest request) {
        return wrap(eventService.getPublished(id), request);
    }

    private static <T> ApiResponse<T> wrap(T data, HttpServletRequest request) {
        String requestId = (String) request.getAttribute(RequestIdFilter.REQUEST_ID_ATTR);
        return ApiResponse.of(data, requestId);
    }
}
