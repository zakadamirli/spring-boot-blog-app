package com.zekademirli.questapp.services;

import com.zekademirli.questapp.entities.RefreshToken;
import com.zekademirli.questapp.entities.User;
import com.zekademirli.questapp.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${refresh.token.expires.in}")
    private Long refreshTokenExpiresSeconds;

    public boolean isRefreshExpired(RefreshToken refreshToken) {
        return refreshToken.getExpiryDate().before(new Date());
    }

    public String createRefreshToken(User user) {
        RefreshToken token = refreshTokenRepository.findByUserId(user.getId());
        if (token == null) {
            token = new RefreshToken();
            token.setUser(user);
        }
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Date.from(Instant.now().plusSeconds(refreshTokenExpiresSeconds)));
        refreshTokenRepository.save(token);
        return token.getToken();
    }

    public RefreshToken getByUser(Long userId) {
        return refreshTokenRepository.findByUserId(userId);
    }
}