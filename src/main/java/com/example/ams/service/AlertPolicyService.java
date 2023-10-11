package com.example.ams.service;

import com.example.ams.model.alert.AlertPolicy;
import com.example.ams.model.event.EventType;
import com.example.ams.repository.AlertPolicyRepository;
import com.example.ams.rest.request.AlertPolicyRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.ams.utils.Constants.SUCCESS_RESPONSE;

@Service
public class AlertPolicyService {
    private final AlertPolicyRepository alertPolicyRepository;

    public AlertPolicyService(AlertPolicyRepository alertPolicyRepository) {
        this.alertPolicyRepository = alertPolicyRepository;
    }

    public String createAlertConfig(AlertPolicyRequest request) {
        AlertPolicy alertPolicy = AlertPolicy.builder()
                .createdAt(System.currentTimeMillis())
                .eventType(request.getEvenType())
                .clientId(request.getClientId())
                .alertType(request.getAlertConfig().getType())
                .count(request.getAlertConfig().getCount())
                .windowSize(request.getAlertConfig().getWindowSize())
                .communicationInfoList(request.getDispatchStrategyList())
                .build();
        alertPolicyRepository.save(alertPolicy);
        return SUCCESS_RESPONSE + "with id : " + alertPolicy.getId();
    }

    public List<AlertPolicy> getAlertPolicyByClientIdAndEventType(String clientId, EventType eventType) {
        return alertPolicyRepository.findByClientIdAndEventType(clientId, eventType);
    }

    public String updateAlertConfig(String configId, AlertPolicyRequest request) {
        AlertPolicy alertPolicy = AlertPolicy.builder()
                .id(configId)
                .createdAt(System.currentTimeMillis())
                .eventType(request.getEvenType())
                .clientId(request.getClientId())
                .alertType(request.getAlertConfig().getType())
                .count(request.getAlertConfig().getCount())
                .windowSize(request.getAlertConfig().getWindowSize())
                .communicationInfoList(request.getDispatchStrategyList())
                .build();
        alertPolicyRepository.save(alertPolicy);
        return SUCCESS_RESPONSE;
    }
}
