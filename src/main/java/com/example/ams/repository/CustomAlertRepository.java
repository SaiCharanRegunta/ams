package com.example.ams.repository;

import com.example.ams.model.alert.AlertStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomAlertRepository {
    void updateAlertStatusByAlertId(String alertId, AlertStatus status);
}
