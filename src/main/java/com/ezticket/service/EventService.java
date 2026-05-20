package com.ezticket.service;

import com.ezticket.dto.request.CreateEventRequest;
import com.ezticket.dto.request.UpdateEventRequest;
import com.ezticket.dto.response.EventResponse;
import com.ezticket.entity.Event;
import com.ezticket.entity.enums.EventStatus;
import com.ezticket.exception.BusinessException;
import com.ezticket.exception.ErrorCode;
import com.ezticket.mapper.EventMapper;
import com.ezticket.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Transactional(readOnly = true)
    public List<EventResponse> listPublished() {
        return eventRepository.findByStatusOrderByStartsAtAsc(EventStatus.PUBLISHED).stream()
                .map(eventMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EventResponse getPublished(Long id) {
        Event event = eventRepository.findByIdAndStatus(id, EventStatus.PUBLISHED)
                .orElseThrow(() -> notFound(id));
        return eventMapper.toResponse(event);
    }

    @Transactional(readOnly = true)
    public List<EventResponse> listAll() {
        return eventRepository.findAll().stream()
                .map(eventMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EventResponse getById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> notFound(id));
        return eventMapper.toResponse(event);
    }

    @Transactional
    public EventResponse create(CreateEventRequest request) {
        Event event = eventMapper.toEntity(request);
        return eventMapper.toResponse(eventRepository.save(event));
    }

    @Transactional
    public EventResponse update(Long id, UpdateEventRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> notFound(id));
        eventMapper.updateEntity(request, event);
        return eventMapper.toResponse(eventRepository.save(event));
    }

    @Transactional
    public void delete(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> notFound(id));
        eventRepository.delete(event);
    }

    private static BusinessException notFound(Long id) {
        return new BusinessException(
                ErrorCode.NOT_FOUND,
                HttpStatus.NOT_FOUND,
                "Event not found: " + id);
    }
}
