package com.bob.projects.twoFactorAuth.portabls;

import lombok.Data;

@Data
public class CodeVerifyInput {
    private String username;
    private String code;
}
