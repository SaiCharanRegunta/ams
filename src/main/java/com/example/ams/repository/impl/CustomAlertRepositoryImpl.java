package com.example.ams.repository.impl;

import com.example.ams.model.alert.Alert;
import com.example.ams.model.alert.AlertStatus;
import com.example.ams.repository.CustomAlertRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class CustomAlertRepositoryImpl implements CustomAlertRepository {
    private final MongoTemplate mongoTemplate;

    public CustomAlertRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void updateAlertStatusByAlertId(String alertId, AlertStatus status) {
        Query query = new Query().addCriteria(where("id").is(alertId));
        Update update = new Update().set("status", status);
        mongoTemplate.upsert(query, update, Alert.class);
    }
}
