package com.zekademirli.questapp.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {
    private Long userId;
    private String refreshToken;
}
