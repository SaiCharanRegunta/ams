package com.example.ams.service.monitoring.strategies;

import com.example.ams.model.event.Event;
import com.example.ams.service.AlertService;
import com.example.ams.service.EventService;
import com.example.ams.service.monitoring.AbstractMonitoringStrategy;

import java.util.Calendar;
import java.util.List;

import static com.example.ams.utils.Constants.MILLISFORSEC;

public class ThumblingWindow extends AbstractMonitoringStrategy {
    public ThumblingWindow(EventService eventService, AlertService alertService) {
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

        long timeStamp = event.getTimeStamp();

        // Considering windows start from 00:00 hour of the day
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);

        // Calculate the number of seconds since midnight
        long secondsSinceMidnight = (timeStamp - calendar.getTimeInMillis()) / MILLISFORSEC;

        // Calculate the window index
        long windowIndex = (secondsSinceMidnight / windowSize);

        // Calculate the window start time in seconds
        long windowStartTimeInSeconds = windowIndex * windowSize;

        // Convert the window start time back to epoch milliseconds
        return calendar.getTimeInMillis() + (windowStartTimeInSeconds * MILLISFORSEC);
    }
}







