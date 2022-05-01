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

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic(value = "Stellar Burgers")
@Feature(value = "User account")
@Story(value = "Login User")
public class UserLoginNegativeTest {
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
    @DisplayName("Login user with invalid login")
    @Description("Create new random user and then try to login it with invalid login")
    public void userCanNotLoginWithInvalidLogin() {
        ValidatableResponse responseLogin = userClient.loginUser(user);
        int statusCode = responseLogin.extract().statusCode();

        assertThat("User with invalid login was login", statusCode, equalTo(SC_UNAUTHORIZED));
        responseLogin.assertThat().body("success", equalTo(false));
        responseLogin.assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Login user with invalid password")
    @Description("Create new random user and then try to login it with invalid password")
    public void userCanNotLoginWithInvalidPassword() {
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().as(UserRegistrationResponse.class).getAccessToken();

        //у рандомного пользователя пароль минимум 8 символов,
        // задаем пароль из 7 символов, так он точно будет некорректным
        user.setPassword("1234567");

        ValidatableResponse responseLogin = userClient.loginUser(user);
        int statusCode = responseLogin.extract().statusCode();

        assertThat("User with invalid password was login", statusCode, equalTo(SC_UNAUTHORIZED));
        responseLogin.assertThat().body("success", equalTo(false));
        responseLogin.assertThat().body("message", equalTo("email or password are incorrect"));
    }
}
