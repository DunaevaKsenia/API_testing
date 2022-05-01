package ru.yandex.stellarburgers.user;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.stellarburgers.User;
import ru.yandex.stellarburgers.UserClient;
import ru.yandex.stellarburgers.responses.UserRegistrationResponse;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic(value = "Stellar Burgers")
@Feature(value = "User account")
@Story(value = "Login User")
public class UserLoginPositiveTest {
    private User user;
    private UserClient userClient;
    private String accessToken;


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
    @DisplayName("Login valid user with full credentials")
    @Description("Create new random user, registered it and then try to login with name, email, password")
    public void userCanLoginWithValidRandomFullCredentials() {
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().as(UserRegistrationResponse.class).getAccessToken();

        ValidatableResponse responseLogin = userClient.loginUser(user);
        int statusCode = responseLogin.extract().statusCode();

        assertThat("Valid user cannot login", statusCode, equalTo(SC_OK));
        responseLogin.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Login valid user with email and password")
    @Description("Create new random user, registered it and then try to login with email and password")
    public void userCanLoginWithValidRandomLoginPwd() {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", user.getEmail());
        userData.put("password", user.getPassword());

        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().as(UserRegistrationResponse.class).getAccessToken();

        ValidatableResponse responseLogin = userClient.loginUser(userData);
        int statusCode = responseLogin.extract().statusCode();

        assertThat("Valid user cannot login with email and password", statusCode, equalTo(SC_OK));
        responseLogin.assertThat().body("success", equalTo(true));
    }



}
