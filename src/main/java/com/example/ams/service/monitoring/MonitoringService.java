package com.example.ams.service.monitoring;

import com.example.ams.model.alert.AlertPolicy;
import com.example.ams.model.event.Event;
import com.example.ams.model.event.EventType;
import com.example.ams.service.AlertPolicyService;
import com.example.ams.service.dispatcher.DispatcherStrategy;
import com.example.ams.service.dispatcher.DispatcherStrategyProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitoringService {
    private final AlertPolicyService alertPolicyService;
    private final MonitoringStrategyProvider monitoringStrategyProvider;
    private final DispatcherStrategyProvider dispatcherStrategyProvider;

    public MonitoringService(AlertPolicyService alertPolicyService, MonitoringStrategyProvider monitoringStrategyProvider, DispatcherStrategyProvider dispatcherStrategyProvider) {
        this.alertPolicyService = alertPolicyService;
        this.monitoringStrategyProvider = monitoringStrategyProvider;
        this.dispatcherStrategyProvider = dispatcherStrategyProvider;
    }

    public void monitorAfterEvent(Event event) {
        String clientId = event.getClientId();
        EventType eventType = event.getEventType();

        List<AlertPolicy> alertPolicyList = alertPolicyService.getAlertPolicyByClientIdAndEventType(clientId, eventType);
        alertPolicyList.stream().parallel().forEach(
                alertPolicy -> {
                    MonitoringStrategy monitoringStrategy = monitoringStrategyProvider.getServiceByAlertType(alertPolicy.getAlertType());
                    if (monitoringStrategy.shouldWeTriggerAlertAfterEvent(event, alertPolicy.getCount(), alertPolicy.getWindowSize())) {
                        alertPolicy.getCommunicationInfoList()
                                .stream().parallel().forEach(
                                        communicationInfo -> {
                                            DispatcherStrategy dispatcherStrategy = dispatcherStrategyProvider.getServiceByCommunicationMode(communicationInfo.getMode());
                                            dispatcherStrategy.triggerEvent(communicationInfo.getMessage());
                                        });
                    }
                }
        );
    }
}
