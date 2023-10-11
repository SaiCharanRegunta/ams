package com.example.ams.repository;

import com.example.ams.model.alert.Alert;
import com.example.ams.model.alert.AlertStatus;
import com.example.ams.model.event.EventType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlertRepository extends MongoRepository<Alert, String>, CustomAlertRepository {
    @Query(value = "{ 'clientId': ?0, 'eventType': ?1, 'status':  ?2 , 'endTime': { $gt: ?3 } }")
    Optional<Alert> findAlertByCriteria(String clientId, EventType eventType, AlertStatus status, long inputTime);

    List<Alert> findByClientId(String clientId);

}
