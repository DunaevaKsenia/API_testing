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

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic(value = "Stellar Burgers")
@Feature(value = "User account")
@Story(value = "Change User account data")
public class UserChangeNegativeTest {
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
    @DisplayName("Change user email on existing email with authorization ")
    @Description("Create new random user, registered it and then try to change user email on existing email with user authorization")
    public void userCanNotChangeEmailOnExistingEmailWithAuthorization() {
        ValidatableResponse responseCreate = userClient.createUser(user);
        accessToken = responseCreate.extract().as(UserRegistrationResp.class).getAccessToken();

        User userExistingEmail = User.randomUser();
        ValidatableResponse responseCreateExistingEmail = userClient.createUser(userExistingEmail);
        String accessTokenExistingEmail = responseCreateExistingEmail.extract().as(UserRegistrationResp.class).getAccessToken();

        User userChanged = new User();
        userChanged.setEmail(userExistingEmail.getEmail());
        ValidatableResponse responseChange = userClient.changeUserData(accessToken.substring(7), userChanged);
        statusCode = responseChange.extract().statusCode();

        userClient.deleteUser(userExistingEmail, accessTokenExistingEmail.substring(7));

        assertThat("Unauthorized user change all user data", statusCode, equalTo(SC_FORBIDDEN));
        responseChange.assertThat().body("success", equalTo(false));
        responseChange.assertThat().body("message", equalTo("User with such email already exists"));
    }
}
