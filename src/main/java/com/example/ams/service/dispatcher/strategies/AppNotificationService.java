package com.example.ams.service.dispatcher.strategies;

import com.example.ams.service.dispatcher.DispatcherStrategy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppNotificationService implements DispatcherStrategy {
    @Override
    public void triggerEvent(String message) {
        log.info(message);
    }
}
