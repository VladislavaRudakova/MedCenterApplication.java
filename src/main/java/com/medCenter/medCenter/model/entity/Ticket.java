package com.medCenter.medCenter.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;
    @ManyToOne
    @JoinColumn(name = "client_id")
    @ToString.Exclude private Client client;
    @ManyToOne
    @JoinColumn(name = "personal_job_id")
    @ToString.Exclude private PersonalJob personalJob;
    private Date date;
    private Time time;
    private String state;
    @Column(name = "sub_state")
    private String subState;
}
