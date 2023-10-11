package com.example.ams.service.monitoring;

import com.example.ams.model.event.Event;

public interface MonitoringStrategy {
    boolean shouldWeTriggerAlertAfterEvent(Event event, long count, long windowSize);
}
