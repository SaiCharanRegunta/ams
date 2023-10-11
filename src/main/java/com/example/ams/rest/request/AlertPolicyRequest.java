package com.example.ams.rest.request;

import com.example.ams.model.alert.AlertConfig;
import com.example.ams.model.alert.CommunicationInfo;
import com.example.ams.model.event.EventType;
import lombok.Getter;

import java.util.List;

@Getter
public class AlertPolicyRequest {

    String clientId;
    EventType evenType;
    AlertConfig alertConfig;
    List<CommunicationInfo> dispatchStrategyList;
}
