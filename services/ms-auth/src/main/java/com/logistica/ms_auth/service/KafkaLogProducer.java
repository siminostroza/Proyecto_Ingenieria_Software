package com.logistica.ms_auth.service;

import com.logistica.ms_auth.dto.LogEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class KafkaLogProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "queue-logs";

    public KafkaLogProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        // Registramos JavaTimeModule para que Jackson pueda serializar Instant correctamente
        this.objectMapper = objectMapper
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void sendLog(String level, String message) {
        try {
            LogEvent log = new LogEvent("ms-auth", level, message, Instant.now());
            String jsonLog = objectMapper.writeValueAsString(log);
            kafkaTemplate.send(TOPIC, jsonLog);
        } catch (Exception e) {
            System.err.println("[ms-auth] Error enviando log a Kafka: " + e.getMessage());
        }
    }
}