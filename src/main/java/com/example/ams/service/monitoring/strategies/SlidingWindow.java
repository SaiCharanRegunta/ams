package com.example.ams.service.monitoring.strategies;

import com.example.ams.model.event.Event;
import com.example.ams.service.AlertService;
import com.example.ams.service.EventService;
import com.example.ams.service.monitoring.AbstractMonitoringStrategy;

import java.util.List;

import static com.example.ams.utils.Constants.MILLISFORSEC;

public class SlidingWindow extends AbstractMonitoringStrategy {
    public SlidingWindow(EventService eventService, AlertService alertService) {
        super(eventService, alertService);
    }

    @Override
    public boolean shouldWeTriggerAlertAfterEvent(Event event, long count, long windowSize) {
        long latestAckTime = getLatestAckTime(event);
        long windowStartTime = getWindowStartTime(event, windowSize);
        long searchFromTime = Math.max(latestAckTime, windowStartTime);
        List<Event> eventList = getEventCountByClientIdAndEventTypeAndSearchFromTime(event, searchFromTime);
        if (eventList.size() >= count) {
            createAlert(eventList);
            return true;
        }
        return false;
    }

    private long getWindowStartTime(Event event, long windowSize) {
        return event.getTimeStamp() - (windowSize * MILLISFORSEC);
    }
}
