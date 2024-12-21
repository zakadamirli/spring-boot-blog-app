package com.zekademirli.questapp.controllers;

import com.zekademirli.questapp.entities.RefreshToken;
import com.zekademirli.questapp.entities.User;
import com.zekademirli.questapp.requests.RefreshTokenRequest;
import com.zekademirli.questapp.requests.UserRequest;
import com.zekademirli.questapp.responses.AuthResponse;
import com.zekademirli.questapp.security.JwtTokenProvider;
import com.zekademirli.questapp.services.RefreshTokenService;
import com.zekademirli.questapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public AuthResponse loginM(@RequestBody UserRequest userRequest) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userRequest.getUsername(),
                userRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtTokenProvider.generateJwtToken(authentication);
        User user = userService.getOneUserByUserName(userRequest.getUsername());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken("Bearer " + jwtToken);
        authResponse.setRefreshToken(refreshTokenService.createRefreshToken(user));
        authResponse.setUserId(user.getId());
        return authResponse;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserRequest userRequest) {
        AuthResponse authResponse = new AuthResponse();
        if (userService.getOneUserByUserName(userRequest.getUsername()) != null) {
            authResponse.setMessage("Username already exists");
            return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userService.saveOneUser(user);
        authResponse.setMessage("User registered successfully");
        authResponse.setUserId(user.getId());
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        AuthResponse authResponse = new AuthResponse();
        RefreshToken refreshToken = refreshTokenService.getByUser(refreshTokenRequest.getUserId());
        if (refreshToken.getToken().equals(refreshTokenRequest.getRefreshToken())
                && !refreshTokenService.isRefreshExpired(refreshToken)) {
            User user = refreshToken.getUser();
            String jwtToken = jwtTokenProvider.generateJwtTokenByUserId(user.getId());
            authResponse.setAccessToken("Bearer " + jwtToken);
            authResponse.setMessage("Token successfully refreshed");
            authResponse.setUserId(user.getId());
            return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
        } else {
            authResponse.setMessage("Refresh token is not valid");
            return new ResponseEntity<>(authResponse, HttpStatus.UNAUTHORIZED);
        }
    }

}


/*
{
    "message": null,
    "userId": 10,
    "accessToken": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMCIsImlhdCI6MTczMzAxMzkzNSwiZXhwIjoxNzMzMDEzOTM4fQ.b2_gdIo9qFUCVKJY4qvo4rSfuFPUbuCy9jUAlFQJHNU",
    "refreshToken": "Bearer e0b5679d-eb74-4a4c-80ed-e18d880c4b2a"
}


* */