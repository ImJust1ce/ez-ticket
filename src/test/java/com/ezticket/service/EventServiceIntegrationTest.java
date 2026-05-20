package com.ezticket.service;

import com.ezticket.dto.request.CreateEventRequest;
import com.ezticket.dto.request.UpdateEventRequest;
import com.ezticket.entity.enums.EventStatus;
import com.ezticket.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@SpringBootTest
class EventServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("ticketing")
            .withUsername("ticketing")
            .withPassword("ticketing");

    @DynamicPropertySource
    static void datasourceProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private EventService eventService;

    @Test
    void createDraftNotVisibleToPublicUntilPublished() {
        var created = eventService.create(new CreateEventRequest(
                "Concert A",
                "Hanoi Arena",
                Instant.parse("2026-12-01T18:00:00Z"),
                EventStatus.DRAFT));

        assertThat(eventService.listPublished()).isEmpty();
        assertThatThrownBy(() -> eventService.getPublished(created.id()))
                .isInstanceOf(BusinessException.class);

        eventService.update(created.id(), new UpdateEventRequest(
                "Concert A",
                "Hanoi Arena",
                Instant.parse("2026-12-01T18:00:00Z"),
                EventStatus.PUBLISHED));

        assertThat(eventService.listPublished()).hasSize(1);
        assertThat(eventService.getPublished(created.id()).name()).isEqualTo("Concert A");
    }
}
