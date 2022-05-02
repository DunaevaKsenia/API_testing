package ru.yandex.stellarburgers.responses;

import lombok.Data;
import ru.yandex.stellarburgers.User;

@Data
public class AboutUserResp {
    private User user;
    private boolean success;
}
