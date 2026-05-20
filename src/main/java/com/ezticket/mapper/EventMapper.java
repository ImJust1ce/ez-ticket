package com.ezticket.mapper;

import com.ezticket.dto.request.CreateEventRequest;
import com.ezticket.dto.request.UpdateEventRequest;
import com.ezticket.dto.response.EventResponse;
import com.ezticket.entity.Event;
import com.ezticket.entity.enums.EventStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventResponse toResponse(Event event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", expression = "java(resolveStatus(request.status()))")
    Event toEntity(CreateEventRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(UpdateEventRequest request, @MappingTarget Event event);

    default EventStatus resolveStatus(EventStatus status) {
        return status != null ? status : EventStatus.DRAFT;
    }
}
