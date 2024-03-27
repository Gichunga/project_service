package com.gichungasoftwares.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long Id;

    private String password;
    private String email;
    private String fullName;
    private String mobile;
    private String role;
}
