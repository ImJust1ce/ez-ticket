package com.ezticket.controller;

import com.ezticket.config.AdminKeyInterceptor;
import com.ezticket.config.RequestIdFilter;
import com.ezticket.config.WebMvcConfig;
import com.ezticket.dto.response.EventResponse;
import com.ezticket.entity.enums.EventStatus;
import com.ezticket.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminEventController.class)
@Import({RequestIdFilter.class, AdminKeyInterceptor.class, WebMvcConfig.class})
@TestPropertySource(properties = "app.admin-key=test-admin-key")
class AdminEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EventService eventService;

    @Test
    void createRequiresValidAdminKey() throws Exception {
        String body = """
                {
                  "name": "Jazz Night",
                  "venue": "Saigon Hall",
                  "startsAt": "2026-11-15T20:00:00Z",
                  "status": "DRAFT"
                }
                """;

        mockMvc.perform(post("/api/admin/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createWithAdminKey() throws Exception {
        when(eventService.create(any())).thenReturn(new EventResponse(
                1L, "Jazz Night", "Saigon Hall", Instant.parse("2026-11-15T20:00:00Z"),
                EventStatus.DRAFT, Instant.now(), Instant.now()));

        String body = """
                {
                  "name": "Jazz Night",
                  "venue": "Saigon Hall",
                  "startsAt": "2026-11-15T20:00:00Z",
                  "status": "DRAFT"
                }
                """;

        mockMvc.perform(post("/api/admin/v1/events")
                        .header("X-Admin-Key", "test-admin-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Jazz Night"));
    }
}
