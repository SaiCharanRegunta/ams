package com.example.ams.model.event;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("events")
@Data
public class Event {
    EventType eventType;
    String clientId;
    long timeStamp;
}
