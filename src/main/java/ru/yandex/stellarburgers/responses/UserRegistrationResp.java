package ru.yandex.stellarburgers.responses;

import lombok.Data;
import ru.yandex.stellarburgers.User;

@Data
public class UserRegistrationResp {
    private boolean success;
    private User user;
    private String accessToken;
    private String refreshToken;
}
