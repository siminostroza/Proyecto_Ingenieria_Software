package com.logistica.ms_logs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "service_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName;
    private String level;

    @Column(length = 1000)
    private String message;

    @Column(length = 50)
    private String timestamp;
}