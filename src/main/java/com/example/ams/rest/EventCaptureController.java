package com.example.ams.rest;

import com.example.ams.rest.request.EventRequest;
import com.example.ams.service.EventCaptureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EventCaptureController {
    private final EventCaptureService eventCaptureService;

    @PostMapping(value = "/events")
    public ResponseEntity<String> createExceptionEvent(
            @RequestBody EventRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventCaptureService.createExceptionEvent(request));
    }

}
