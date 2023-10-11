package com.example.ams.model.alert;

import com.example.ams.model.event.EventType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("alertConfig")
@Data
@Builder
public class AlertPolicy {
    @Id
    String id;
    String clientId;
    AlertType alertType;
    EventType eventType;
    long createdAt;
    long count;
    long windowSize;
    List<CommunicationInfo> communicationInfoList;
}
