package ru.yandex.stellarburgers.user;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import ru.yandex.stellarburgers.User;
import ru.yandex.stellarburgers.UserClient;
import ru.yandex.stellarburgers.responses.UserRegistrationResp;


import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic(value = "Stellar Burgers")
@Feature(value = "User account")
@Story(value = "Create User account")
public class UserRegistrationNegativeTest {

    @Test
    @DisplayName("Create user with existing login")
    @Description("Create user with existing login and then trying to register it")
    public void userCannotBeRegisteredWithExistingLogin() {
        User user = User.randomUser();
        UserClient userClient = new UserClient();

        ValidatableResponse responseFirst = userClient.createUser(user);
        String accessToken = responseFirst.extract().as(UserRegistrationResp.class).getAccessToken();

        ValidatableResponse responseSecond = userClient.createUser(user);
        int statusCode = responseSecond.extract().statusCode();

        userClient.deleteUser(user,accessToken.substring(7));

        assertThat("User with existing login should not be registered ", statusCode, equalTo(SC_FORBIDDEN));
        responseSecond.assertThat().body("success", equalTo(false));
        responseSecond.assertThat().body("message", equalTo("User already exists"));
    }
}
