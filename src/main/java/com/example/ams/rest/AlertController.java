package com.example.ams.rest;

import com.example.ams.model.alert.AlertStatus;
import com.example.ams.rest.request.EventRequest;
import com.example.ams.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Validated
public class AlertController {
    private final AlertService alertService;

    @PostMapping(value = "/alerts/{alertId}")
    public ResponseEntity<String> updateAlertStatus(
            @PathVariable(value = "alertId") String alertId,
            @RequestHeader(value = "status") AlertStatus status,
            EventRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(alertService.updateAlertStatusByAlertId(alertId, status));
    }

}
