package com.zekademirli.questapp.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private String message;
    private Long userId;
    private String accessToken;
    private String refreshToken;
}
