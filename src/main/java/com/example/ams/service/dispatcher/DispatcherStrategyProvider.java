package com.example.ams.service.dispatcher;

import com.example.ams.model.alert.CommunicationMode;
import com.example.ams.service.dispatcher.strategies.AppNotificationService;
import com.example.ams.service.dispatcher.strategies.ConsoleService;
import com.example.ams.service.dispatcher.strategies.EmailService;
import com.example.ams.service.dispatcher.strategies.PhoneCallService;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class DispatcherStrategyProvider {
    private static final Map<CommunicationMode, DispatcherStrategy> strategyTypeServiceMap =
            new EnumMap<>(CommunicationMode.class);

    public DispatcherStrategyProvider() {
        strategyTypeServiceMap.put(CommunicationMode.CONSOLE, new ConsoleService());
        strategyTypeServiceMap.put(CommunicationMode.EMAIL, new EmailService());
        strategyTypeServiceMap.put(CommunicationMode.PHONE, new PhoneCallService());
        strategyTypeServiceMap.put(CommunicationMode.NOTIFICATION, new AppNotificationService());
    }

    public DispatcherStrategy getServiceByCommunicationMode(
            CommunicationMode mode) {
        return strategyTypeServiceMap.get(mode);
    }
}
