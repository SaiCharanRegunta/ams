package com.example.ams.service;

import com.example.ams.model.alert.Alert;
import com.example.ams.model.alert.AlertStatus;
import com.example.ams.model.event.EventType;
import com.example.ams.repository.AlertRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.ams.utils.Constants.SUCCESS_RESPONSE;

@Service
public class AlertService {
    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public void createAlertByClientIdEventTypeFirstEventTimeAndLastEventTime(String clientId, EventType eventType, long startTime, long endTime) {
        Alert alert = Alert.builder()
                .clientId(clientId)
                .evenType(eventType)
                .startTime(startTime)
                .endTime(endTime)
                .status(AlertStatus.TRIGGERED)
                .build();
        alertRepository.save(alert);
    }

    public String updateAlertStatusByAlertId(String alertId, AlertStatus status) {
        alertRepository.updateAlertStatusByAlertId(alertId, status);
        return SUCCESS_RESPONSE;
    }

    public long getLatestAcknowledgedTime(String clientId, EventType eventType, long timeStamp) {
        Optional<Alert> alert = alertRepository.findAlertByCriteria(clientId, eventType, AlertStatus.TRIGGERED, timeStamp);
        return alert.map(Alert::getEndTime).orElse(0L);
    }

}
