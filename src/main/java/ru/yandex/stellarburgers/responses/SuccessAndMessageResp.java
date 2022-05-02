package ru.yandex.stellarburgers.responses;

import lombok.Data;

@Data
public class SuccessAndMessageResp {
    private boolean success;
    private String message;
}
