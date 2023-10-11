package com.example.ams.service.monitoring;

import com.example.ams.model.event.Event;
import com.example.ams.service.AlertService;
import com.example.ams.service.EventService;

import java.util.List;

public abstract class AbstractMonitoringStrategy implements MonitoringStrategy {
    private final EventService eventService;
    private final AlertService alertService;

    protected AbstractMonitoringStrategy(EventService eventService, AlertService alertService) {
        this.eventService = eventService;
        this.alertService = alertService;
    }

    protected long getLatestAckTime(Event event) {
        return alertService.getLatestAcknowledgedTime(event.getClientId(), event.getEventType(), event.getTimeStamp());
    }

    protected List<Event> getEventCountByClientIdAndEventTypeAndSearchFromTime(Event event, long searchFromTime) {
        return eventService.getEventCount(event.getClientId(), event.getEventType(), searchFromTime);
    }

    protected void createAlert(List<Event> eventList) {
        long minTimeStamp = eventList.stream()
                .mapToLong(Event::getTimeStamp)
                .min()
                .orElse(0);
        long maxTimeStamp = eventList.stream()
                .mapToLong(Event::getTimeStamp)
                .max()
                .orElse(0);
        alertService.createAlertByClientIdEventTypeFirstEventTimeAndLastEventTime(
                eventList.get(0).getClientId(), eventList.get(0).getEventType(), minTimeStamp, maxTimeStamp
        );
    }
}

