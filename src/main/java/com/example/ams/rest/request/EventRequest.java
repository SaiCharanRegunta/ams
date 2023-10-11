package com.example.ams.rest.request;

import com.example.ams.model.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {
    String clientId;
    EventType eventType;
    long timeStamp;

}
