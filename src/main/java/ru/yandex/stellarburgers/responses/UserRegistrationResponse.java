package ru.yandex.stellarburgers.responses;

import lombok.Data;

@Data
public class UserRegistrationResponse {
    private boolean success;
    private UserResponse userResponse;
    private String accessToken;
    private String refreshToken;
}
