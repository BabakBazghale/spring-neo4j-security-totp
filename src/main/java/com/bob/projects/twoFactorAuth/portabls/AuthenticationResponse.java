package com.bob.projects.twoFactorAuth.portabls;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    @NonNull
    private String accessToken;
    private Boolean isTwoFactorEnabled;
}