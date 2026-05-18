package com.logistica.ms_logs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.logistica.ms_logs.model.LogEntity;
import com.logistica.ms_logs.repository.LogRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaLogConsumer {

    private final LogRepository logRepository;
    private final ObjectMapper objectMapper;

    public KafkaLogConsumer(LogRepository logRepository, ObjectMapper objectMapper) {
        this.logRepository = logRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "queue-logs", groupId = "logs-group")
    public void consumeLog(String messageJson) {
        try {
            JsonNode node = objectMapper.readTree(messageJson);

            LogEntity logEntity = new LogEntity();
            logEntity.setServiceName(node.path("serviceName").asText());
            logEntity.setLevel(node.path("level").asText());
            logEntity.setMessage(node.path("message").asText());
            logEntity.setTimestamp(node.path("timestamp").asText());

            logRepository.save(logEntity);

            System.out.println("[Kafka] Log guardado: [" + logEntity.getLevel() + "] " + logEntity.getServiceName() + " - " + logEntity.getMessage());

        } catch (Exception e) {
            System.err.println("[Kafka] Error al procesar log: " + e.getMessage());
        }
    }
}