package ru.yandex.stellarburgers.user;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.stellarburgers.*;
import ru.yandex.stellarburgers.responses.*;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic(value = "Stellar Burgers")
@Feature(value = "User account")
@Story(value = "Create User account")
public class UserRegistrationPositiveTest {
    private User user;
    private UserClient userClient;
    private String accessToken;
    private int statusCode;

    @Before
    public void setUp() {
        user = User.randomUser();
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        if(accessToken != null) {
            userClient.deleteUser(user, accessToken.substring(7));
        }
    }

    @Test
    @DisplayName("Create new valid user")
    @Description("Create new user with valid random credentials and then registration it")
    public void userCanBeRegisteredWithValidRandomCredentials() {
        ValidatableResponse response = userClient.createUser(user);
        statusCode = response.extract().statusCode();
        accessToken = response.extract().as(UserRegistrationResp.class).getAccessToken();

        assertThat("Valid user cannot be registered", statusCode, equalTo(SC_OK));
        response.assertThat().body("success", equalTo(true));
    }
}
