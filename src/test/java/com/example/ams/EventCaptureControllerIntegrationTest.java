package com.example.ams;

import com.example.ams.model.alert.Alert;
import com.example.ams.model.alert.AlertPolicy;
import com.example.ams.model.alert.CommunicationInfo;
import com.example.ams.model.alert.CommunicationMode;
import com.example.ams.model.event.Event;
import com.example.ams.repository.AlertPolicyRepository;
import com.example.ams.repository.AlertRepository;
import com.example.ams.repository.EventRepository;
import com.example.ams.rest.request.EventRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.example.ams.model.alert.AlertType.SIMPLE_COUNT;
import static com.example.ams.model.alert.AlertType.SLIDING_WINDOW;
import static com.example.ams.model.event.EventType.PAYMENT_EXCEPTION;
import static com.example.ams.model.event.EventType.USER_SERVICE_EXCEPTION;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureMockMvc
@AutoConfigureDataMongo
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
@TestPropertySource(properties = {"de.flapdoodle.mongodb.embedded.version=4.0.2"})
class EventCaptureControllerIntegrationTest {
    protected final MongoTemplate mongoTemplate;
    protected final MongoClient mongoClient;
    protected final MockMvc mvc;

    private final EventRepository eventRepository;
    private final AlertRepository alertRepository;
    private final AlertPolicyRepository alertPolicyRepository;


    @Autowired
    EventCaptureControllerIntegrationTest(MongoTemplate mongoTemplate, MongoClient mongoClient, MockMvc mvc,
                                          EventRepository eventRepository, AlertRepository alertRepository,
                                          AlertPolicyRepository alertPolicyRepository) {
        this.mongoTemplate = mongoTemplate;
        this.mongoClient = mongoClient;
        this.mvc = mvc;
        this.eventRepository = eventRepository;
        this.alertRepository = alertRepository;
        this.alertPolicyRepository = alertPolicyRepository;
    }

    @BeforeAll
    void setUp() throws IOException {
        mongoClient.getDatabase(mongoTemplate.getDb().getName()).drop();
        mongoClient.getDatabase(mongoTemplate.getDb().getName()).listCollections();

        List<Alert> alertList =
                List.of();
        alertRepository.saveAll(alertList);

        List<AlertPolicy> alertPolicies =
                List.of(AlertPolicy.builder()
                                .alertType(SIMPLE_COUNT)
                                .eventType(PAYMENT_EXCEPTION)
                                .clientId("client-b")
                                .count(1)
                                .communicationInfoList(List.of(
                                        CommunicationInfo.builder().mode(CommunicationMode.NOTIFICATION)
                                                .message("Issue in Payment Service")
                                                .build()))
                                .build(),
                        AlertPolicy.builder()
                                .alertType(SLIDING_WINDOW)
                                .eventType(USER_SERVICE_EXCEPTION)
                                .clientId("client-c")
                                .count(2)
                                .windowSize(2)
                                .communicationInfoList(List.of(
                                        CommunicationInfo.builder().mode(CommunicationMode.CONSOLE)
                                                .message("Issue in Payment Service")
                                                .build()))
                                .build());
        alertPolicyRepository.saveAll(alertPolicies);


        File file1 = new File("src/test/resources/data/event_request-3.json");
        File file2 = new File("src/test/resources/data/event_request-4.json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Event event1 = mapper.readValue(file1, Event.class);
        Event event2 = mapper.readValue(file2, Event.class);
        List<Event> eventList =
                List.of(event1, event2);
        eventRepository.saveAll(eventList);
    }


    @Test
    void shouldProcessEvents() throws Exception {
        String fileName = "data/event_request-1.json";
        File file = new File("src/test/resources/" + fileName);
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        EventRequest event = mapper.readValue(file, EventRequest.class);
        String jsonPayload = mapper.writeValueAsString(event);
        MockHttpServletResponse response =
                mvc.perform(
                                post("/events")
                                        .accept(MediaType.APPLICATION_JSON)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(jsonPayload))
                        .andReturn()
                        .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void shouldTriggerAlertBySimpleCount() throws Exception {
        String fileName = "data/event_request-2.json";
        File file = new File("src/test/resources/" + fileName);
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        EventRequest event = mapper.readValue(file, EventRequest.class);
        String jsonPayload = mapper.writeValueAsString(event);
        mvc.perform(
                        post("/events")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPayload))
                .andReturn()
                .getResponse();
        assertThat(alertRepository.findByClientId("client-b").size()).isEqualTo(1);
    }

    @Test
    void shouldTriggerAlertBySlidingWindow() throws Exception {
        String fileName = "data/event_request-5.json";
        File file = new File("src/test/resources/" + fileName);
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        EventRequest event = mapper.readValue(file, EventRequest.class);
        String jsonPayload = mapper.writeValueAsString(event);
        mvc.perform(
                        post("/events")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPayload))
                .andReturn()
                .getResponse();
        assertThat(alertRepository.findByClientId("client-c").size()).isEqualTo(1);
    }
}

