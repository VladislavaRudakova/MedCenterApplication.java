package com.medCenter.medCenter.dto;

import com.medCenter.medCenter.model.entity.UserCredentials;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Integer id;
    private String username;
    private String email;
    private UserCredentialsDto userCredentials;
    private String role;
    private String state;
}
