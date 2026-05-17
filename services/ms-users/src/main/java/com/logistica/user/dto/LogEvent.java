package com.logistica.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogEvent {
    private String serviceName;
    private String level; // INFO, WARN, ERROR
    private String message;
    private String timestamp;
}