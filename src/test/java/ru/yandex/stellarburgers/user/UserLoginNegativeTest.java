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
import ru.yandex.stellarburgers.responses.UserRegistrationResp;
import java.util.HashMap;
import java.util.Map;

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
    @DisplayName("Login user with invalid login")
    @Description("Create new random user and then try to login it with invalid login")
    public void userCanNotLoginWithInvalidLogin() {
        ValidatableResponse responseLogin = userClient.loginUser(user);
        statusCode = responseLogin.extract().statusCode();

        assertThat("User with invalid login was login", statusCode, equalTo(SC_UNAUTHORIZED));
        responseLogin.assertThat().body("success", equalTo(false));
        responseLogin.assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Login user with invalid password")
    @Description("Create new random user and then try to login it with invalid password")
    public void userCanNotLoginWithInvalidPassword() {
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().as(UserRegistrationResp.class).getAccessToken();

        //у рандомного пользователя пароль минимум 8 символов,
        // задаем пароль из 7 символов, так он точно будет некорректным
        user.setPassword("1234567");

        ValidatableResponse responseLogin = userClient.loginUser(user);
        statusCode = responseLogin.extract().statusCode();

        assertThat("User with invalid password was login", statusCode, equalTo(SC_UNAUTHORIZED));
        responseLogin.assertThat().body("success", equalTo(false));
        responseLogin.assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Login user with invalid Email, password")
    @Description("Create new random user and then try to login it with invalid Email, password")
    public void userCanNotLoginWithInvalidLoginPassword() {
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().as(UserRegistrationResp.class).getAccessToken();

        User userRandom = User.randomUser();

        ValidatableResponse responseLogin = userClient.loginUser(userRandom);
        statusCode = responseLogin.extract().statusCode();

        assertThat("User with invalid Email, password was login", statusCode, equalTo(SC_UNAUTHORIZED));
        responseLogin.assertThat().body("success", equalTo(false));
        responseLogin.assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Login user without login")
    @Description("Create new random user and then try to login it without login")
    public void userCanNotLoginWithoutLogin() {
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().as(UserRegistrationResp.class).getAccessToken();

        Map<String, String> userData = new HashMap<>();
        userData.put("password", user.getPassword());

        ValidatableResponse responseLogin = userClient.loginUser(userData);
        statusCode = responseLogin.extract().statusCode();

        assertThat("User without login was login", statusCode, equalTo(SC_UNAUTHORIZED));
        responseLogin.assertThat().body("success", equalTo(false));
        responseLogin.assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Login user without password")
    @Description("Create new random user and then try to login it without password")
    public void userCanNotLoginWithoutPassword() {
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().as(UserRegistrationResp.class).getAccessToken();

        Map<String, String> userData = new HashMap<>();
        userData.put("email", user.getEmail());

        ValidatableResponse responseLogin = userClient.loginUser(userData);
        statusCode = responseLogin.extract().statusCode();

        assertThat("User without password was login", statusCode, equalTo(SC_UNAUTHORIZED));
        responseLogin.assertThat().body("success", equalTo(false));
        responseLogin.assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Login user without data")
    @Description("Create new empty user and then try to login it without data")
    public void userCanNotLoginWithoutData() {
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().as(UserRegistrationResp.class).getAccessToken();

        User userEmpty = new User();

        ValidatableResponse responseLogin = userClient.loginUser(userEmpty);
        statusCode = responseLogin.extract().statusCode();

        assertThat("User without data was login", statusCode, equalTo(SC_UNAUTHORIZED));
        responseLogin.assertThat().body("success", equalTo(false));
        responseLogin.assertThat().body("message", equalTo("email or password are incorrect"));
    }
}
