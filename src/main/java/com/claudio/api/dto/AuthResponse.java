package com.claudio.api.dto;

import lombok.*;

@Setter
@Getter
public class AuthResponse {
    private String token;

   public AuthResponse() {
    }

    public AuthResponse(String token) {
        this.token = token;
    }

}
