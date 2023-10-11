package com.example.ams.repository;

import com.example.ams.model.event.Event;
import com.example.ams.model.event.EventType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
    @Query(value = "{ 'clientId': ?0, 'eventType': ?1, 'timeStamp': { $gt: ?2 } }")
    List<Event> getEventCountByCriteria(String clientId, EventType eventType, long timeStamp);
}