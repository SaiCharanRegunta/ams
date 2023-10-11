package com.example.ams.model.alert;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommunicationInfo {
    CommunicationMode mode;
    String message;
}
