package com.bob.projects.twoFactorAuth.portabls;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginInput {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
