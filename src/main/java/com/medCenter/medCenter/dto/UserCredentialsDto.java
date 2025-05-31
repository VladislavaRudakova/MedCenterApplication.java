package com.medCenter.medCenter.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCredentialsDto {
    private Integer id;
    private String login;
    private String password;
    private String state;
}
