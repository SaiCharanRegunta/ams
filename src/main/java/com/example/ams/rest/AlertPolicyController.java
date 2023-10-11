package com.example.ams.rest;

import com.example.ams.rest.request.AlertPolicyRequest;
import com.example.ams.service.AlertPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class AlertPolicyController {
    private final AlertPolicyService alertPolicyService;

    @PostMapping(value = "/configs")
    public ResponseEntity<String> createAlertConfiguration(
            @RequestBody AlertPolicyRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(alertPolicyService.createAlertConfig(request));
    }
    @PostMapping(value = "/configs/{configId}")
    public ResponseEntity<String> updateAlertConfiguration(
            @PathVariable(value = "configId") String configId,
            @RequestBody AlertPolicyRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(alertPolicyService.updateAlertConfig(configId, request));
    }
}