package com.example.ams.service.monitoring.strategies;

import com.example.ams.model.event.Event;
import com.example.ams.service.AlertService;
import com.example.ams.service.EventService;
import com.example.ams.service.monitoring.AbstractMonitoringStrategy;

import java.util.List;

public class SimpleCount extends AbstractMonitoringStrategy {

    public SimpleCount(EventService eventService, AlertService alertService) {
        super(eventService, alertService);
    }

    @Override
    public boolean shouldWeTriggerAlertAfterEvent(Event event, long count, long windowSize) {
        long latestAckTime = getLatestAckTime(event);
        List<Event> eventList = getEventCountByClientIdAndEventTypeAndSearchFromTime(event, latestAckTime);
        if (eventList.size() >= count) {
            createAlert(eventList);
            return true;
        }
        return false;
    }
}
