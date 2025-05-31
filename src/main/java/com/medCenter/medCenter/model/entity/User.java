package com.medCenter.medCenter.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String username;
    private String email;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_credentials")
    private UserCredentials userCredentials;
    private String role;
    private String state;
}
