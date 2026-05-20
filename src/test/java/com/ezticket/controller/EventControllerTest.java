package com.ezticket.controller;

import com.ezticket.config.RequestIdFilter;
import com.ezticket.dto.response.EventResponse;
import com.ezticket.entity.enums.EventStatus;
import com.ezticket.exception.BusinessException;
import com.ezticket.exception.ErrorCode;
import com.ezticket.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EventController.class)
@Import(RequestIdFilter.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    @Test
    void listPublishedEvents() throws Exception {
        when(eventService.listPublished()).thenReturn(List.of(
                new EventResponse(1L, "Show", "Venue", Instant.parse("2026-12-01T18:00:00Z"),
                        EventStatus.PUBLISHED, Instant.now(), Instant.now())));

        mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Show"))
                .andExpect(jsonPath("$.meta.requestId").exists());
    }

    @Test
    void getPublishedNotFound() throws Exception {
        when(eventService.getPublished(99L)).thenThrow(
                new BusinessException(ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND, "Event not found: 99"));

        mockMvc.perform(get("/api/v1/events/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.code").value("NOT_FOUND"));
    }
}
