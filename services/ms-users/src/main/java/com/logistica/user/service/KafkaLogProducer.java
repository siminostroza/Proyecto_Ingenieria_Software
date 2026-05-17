package com.logistica.user.service;

import com.logistica.user.dto.LogEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class KafkaLogProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper; // Para convertir el objeto a JSON String
    private final String TOPIC = "queue-logs";

    public KafkaLogProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendLog(String level, String message) {
        try {
            LogEvent log = new LogEvent("ms-auth", level, message, Instant.now().toString());
            String jsonLog = objectMapper.writeValueAsString(log);

            // Enviamos el mensaje de forma totalmente asíncrona
            kafkaTemplate.send(TOPIC, jsonLog);
        } catch (Exception e) {
            // Log local en caso de que falle el envío al broker para no romper el flujo
            // principal
            System.err.println("Error enviando log a Kafka: " + e.getMessage());
        }
    }
}