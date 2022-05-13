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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.stellarburgers.User;
import ru.yandex.stellarburgers.UserClient;
import ru.yandex.stellarburgers.responses.UserRegistrationResp;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
@Epic(value = "Stellar Burgers")
@Feature(value = "User account")
@Story(value = "Login User")
public class UserLoginNegativeParamTest {
    private boolean email;
    private boolean password;

    private User user;
    private UserClient userClient;
    private String accessToken;
    private int statusCode;

    public UserLoginNegativeParamTest(boolean email, boolean password) {
        this.email = email;
        this.password = password;
    }

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

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][] {
                {false, true},
                {true, false},
                {false, false},
        };
    }

    @Test
    @DisplayName("Login user with invalid data")
    @Description("Create new random user, registered it  and then try to login it with invalid data")
    public void serCanNotLoginWithInvalidData() {
        String incorrectLogin = user.getEmail().substring(1);
        String incorrectPassword = user.getPassword().substring(1);

        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().as(UserRegistrationResp.class).getAccessToken();

        if (!email) {
            user.setEmail(incorrectLogin);
        }

        if (!password) {
            user.setPassword(incorrectPassword);
        }

        ValidatableResponse responseLogin = userClient.loginUser(user);
        statusCode = responseLogin.extract().statusCode();

        assertThat("User with invalid data was login", statusCode, equalTo(SC_UNAUTHORIZED));
        responseLogin.assertThat().body("success", equalTo(false));
        responseLogin.assertThat().body("message", equalTo("email or password are incorrect"));
    }


    @Test
    @DisplayName("Login user without data")
    @Description("Create new random user, registered it  and then try to login it without data")
    public void serCanNotLoginWithoutData() {
        Map<String, String> userData = new HashMap<>();

        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().as(UserRegistrationResp.class).getAccessToken();

        if (email) {
            userData.put("email", user.getEmail());
        }

        if (password) {
            userData.put("password", user.getPassword());
        }

        ValidatableResponse responseLogin = userClient.loginUser(userData);
        statusCode = responseLogin.extract().statusCode();

        assertThat("User without data was login", statusCode, equalTo(SC_UNAUTHORIZED));
        responseLogin.assertThat().body("success", equalTo(false));
        responseLogin.assertThat().body("message", equalTo("email or password are incorrect"));
    }
}
