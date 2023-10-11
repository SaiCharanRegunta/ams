package com.example.ams.service;

import com.example.ams.model.event.Event;
import com.example.ams.model.event.EventType;
import com.example.ams.repository.EventRepository;
import com.example.ams.rest.request.EventRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;


    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createExceptionEvent(EventRequest request) {
        Event event = new Event();
        event.setClientId(request.getClientId());
        event.setEventType(request.getEventType());
        event.setTimeStamp(request.getTimeStamp());
        eventRepository.save(event);
        return event;
    }

    public List<Event> getEventCount(String clientId, EventType eventType, long timeStamp) {
        return eventRepository.getEventCountByCriteria(clientId, eventType, timeStamp);
    }
}
