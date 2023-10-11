package com.example.ams.repository;

import com.example.ams.model.alert.AlertPolicy;
import com.example.ams.model.event.EventType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertPolicyRepository extends MongoRepository<AlertPolicy, String> {


    List<AlertPolicy> findByClientIdAndEventType(String clientId, EventType eventType);
}
