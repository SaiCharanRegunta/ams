package com.example.ams.model.alert;

import com.example.ams.model.event.EventType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("alerts")
@Builder
@Data
public class Alert {
    @Id
    String id;
    EventType evenType;
    String clientId;
    long startTime;
    long endTime;
    AlertStatus status;


}
