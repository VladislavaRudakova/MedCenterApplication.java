package com.medCenter.medCenter.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class MedCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne
    @JoinColumn(name = "personal_job_id")
    private PersonalJob personalJob;
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;
    private String complaints;
    private Date date;
    private String diagnosis;
    private String examinationResult;
    private String appointment;
    private String state;
}
