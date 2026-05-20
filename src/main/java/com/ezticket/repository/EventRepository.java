package com.ezticket.repository;

import com.ezticket.entity.Event;
import com.ezticket.entity.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByStatusOrderByStartsAtAsc(EventStatus status);

    Optional<Event> findByIdAndStatus(Long id, EventStatus status);
}
