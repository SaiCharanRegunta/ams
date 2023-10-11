package com.example.ams.service.monitoring;

import com.example.ams.model.alert.AlertType;
import com.example.ams.service.AlertService;
import com.example.ams.service.EventService;
import com.example.ams.service.monitoring.strategies.SimpleCount;
import com.example.ams.service.monitoring.strategies.SlidingWindow;
import com.example.ams.service.monitoring.strategies.ThumblingWindow;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class MonitoringStrategyProvider {
    private static final Map<AlertType, MonitoringStrategy> strategyTypeServiceMap =
            new EnumMap<>(AlertType.class);

    public MonitoringStrategyProvider(EventService eventService, AlertService alertService) {
        strategyTypeServiceMap.put(AlertType.SIMPLE_COUNT, new SimpleCount(eventService, alertService));
        strategyTypeServiceMap.put(AlertType.TUMBLING_WINDOW, new ThumblingWindow(eventService, alertService));
        strategyTypeServiceMap.put(AlertType.SLIDING_WINDOW, new SlidingWindow(eventService, alertService));
    }

    protected MonitoringStrategy getServiceByAlertType(AlertType alertType) {
        return strategyTypeServiceMap.get(alertType);
    }
}
