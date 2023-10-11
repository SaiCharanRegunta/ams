package com.example.ams.service;

import com.example.ams.model.event.Event;
import com.example.ams.rest.request.EventRequest;
import com.example.ams.service.monitoring.MonitoringService;
import org.springframework.stereotype.Service;

import static com.example.ams.utils.Constants.SUCCESS_RESPONSE;

@Service
public class EventCaptureService {
    private final MonitoringService monitoringService;
    private final EventService eventService;

    public EventCaptureService(MonitoringService monitoringService, EventService eventService) {
        this.monitoringService = monitoringService;
        this.eventService = eventService;
    }

    public String createExceptionEvent(EventRequest request) {
        Event event = eventService.createExceptionEvent(request);
        monitoringService.monitorAfterEvent(event);
        return SUCCESS_RESPONSE;

    }

}
