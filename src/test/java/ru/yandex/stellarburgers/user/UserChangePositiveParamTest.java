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
import ru.yandex.stellarburgers.responses.AboutUserResp;
import ru.yandex.stellarburgers.responses.UserRegistrationResp;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
@Epic(value = "Stellar Burgers")
@Feature(value = "User account")
@Story(value = "Change User account data")
public class UserChangePositiveParamTest {
    private boolean email;
    private boolean password;
    private boolean name;

    private User user;
    private UserClient userClient;
    private String accessToken;
    private int statusCode;

    public UserChangePositiveParamTest(boolean email, boolean password, boolean name) {
        this.email = email;
        this.password = password;
        this.name = name;
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
                {false, true, true},
                {true, false, true},
                {true, true, false},
                {false, false, true},
                {false, true, false},
                {true, false, false},
                {false, false, false},
        };
    }

    @Test
    @DisplayName("Change user data with authorization")
    @Description("Create new random user, registered it and then try to change user data with user authorization")
    public void userCanChangeDataWithAuthorization() {
        ValidatableResponse responseCreate = userClient.createUser(user);
        accessToken = responseCreate.extract().as(UserRegistrationResp.class).getAccessToken();

        User userRandom = User.randomUser();

        if (!email) {
            user.setEmail(userRandom.getEmail());
        }

        if (!password) {
            user.setPassword(userRandom.getPassword());
        }

        if (!name) {
            user.setName(userRandom.getName());
        }

        ValidatableResponse responseChange = userClient.changeUserData(accessToken.substring(7), user);
        AboutUserResp aboutUserResp = responseChange.extract().as(AboutUserResp.class);
        statusCode = responseChange.extract().statusCode();

        assertThat("Authorized user do not change data", statusCode, equalTo(SC_OK));
        responseChange.assertThat().body("success", equalTo(true));
        assertEquals("Authorized user do not change name",user.getName(), aboutUserResp.getUser().getName());
        assertEquals("Authorized user do not change Email", user.getEmail(), aboutUserResp.getUser().getEmail());
    }
}
