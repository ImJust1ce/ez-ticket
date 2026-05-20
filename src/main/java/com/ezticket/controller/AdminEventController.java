package com.ezticket.controller;

import com.ezticket.config.RequestIdFilter;
import com.ezticket.dto.request.CreateEventRequest;
import com.ezticket.dto.request.UpdateEventRequest;
import com.ezticket.dto.response.ApiResponse;
import com.ezticket.dto.response.EventResponse;
import com.ezticket.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/v1/events")
@RequiredArgsConstructor
@Tag(name = "Admin Events", description = "Event management (requires X-Admin-Key)")
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    @Operation(summary = "List all events (any status)")
    public ApiResponse<List<EventResponse>> list(HttpServletRequest request) {
        return wrap(eventService.listAll(), request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get event by id")
    public ApiResponse<EventResponse> get(@PathVariable Long id, HttpServletRequest request) {
        return wrap(eventService.getById(id), request);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create event")
    public ApiResponse<EventResponse> create(
            @Valid @RequestBody CreateEventRequest body,
            HttpServletRequest request) {
        return wrap(eventService.create(body), request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update event")
    public ApiResponse<EventResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEventRequest body,
            HttpServletRequest request) {
        return wrap(eventService.update(id, body), request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete event")
    public void delete(@PathVariable Long id) {
        eventService.delete(id);
    }

    private static <T> ApiResponse<T> wrap(T data, HttpServletRequest request) {
        String requestId = (String) request.getAttribute(RequestIdFilter.REQUEST_ID_ATTR);
        return ApiResponse.of(data, requestId);
    }
}
