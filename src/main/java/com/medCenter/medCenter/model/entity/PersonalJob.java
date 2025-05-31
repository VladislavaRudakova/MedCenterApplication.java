package com.medCenter.medCenter.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PersonalJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "personal_id")
    private Personal personal;
    @Column(name = "job_title")
    private String jobTitle;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    @OneToOne
    private User user;
    private String state;
    @OneToMany
    @JoinColumn(name = "personal_job_id")
    private List<Ticket> tickets;

}
