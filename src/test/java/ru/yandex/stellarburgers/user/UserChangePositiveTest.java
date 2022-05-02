package ru.yandex.stellarburgers.user;

import com.github.javafaker.Faker;
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
import ru.yandex.stellarburgers.responses.AboutUserResp;
import ru.yandex.stellarburgers.responses.UserRegistrationResp;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

@Epic(value = "Stellar Burgers")
@Feature(value = "User account")
@Story(value = "Change User account data")
public class UserChangePositiveTest {
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
    @DisplayName("Change user name with authorization")
    @Description("Create new random user, registered it and then try to change user name with user authorization")
    public void userCanChangeNameWithAuthorization() {
        ValidatableResponse responseCreate = userClient.createUser(user);
        accessToken = responseCreate.extract().as(UserRegistrationResp.class).getAccessToken();

        Faker faker = new Faker();
        User userChanged = new User();
        userChanged.setName(faker.name().username());

        ValidatableResponse responseChange = userClient.changeUserData(accessToken.substring(7), userChanged);
        AboutUserResp aboutUserResp = responseChange.extract().as(AboutUserResp.class);
        statusCode = responseChange.extract().statusCode();

        assertThat("Authorized user do not change name", statusCode, equalTo(SC_OK));
        responseChange.assertThat().body("success", equalTo(true));
        assertEquals("Authorized user do not change name",userChanged.getName(), aboutUserResp.getUser().getName());
    }

    @Test
    @DisplayName("Change user password with authorization")
    @Description("Create new random user, registered it and then try to change user password with user authorization")
    public void userCanChangePasswordWithAuthorization() {
        ValidatableResponse responseCreate = userClient.createUser(user);
        accessToken = responseCreate.extract().as(UserRegistrationResp.class).getAccessToken();

        Faker faker = new Faker();
        User userChanged = new User();
        userChanged.setPassword(faker.internet().password(8, 10));

        ValidatableResponse responseChange = userClient.changeUserData(accessToken.substring(7), userChanged);
        statusCode = responseChange.extract().statusCode();

        assertThat("Authorized user do not change password", statusCode, equalTo(SC_OK));
        responseChange.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Change user email with authorization")
    @Description("Create new random user, registered it and then try to change user email with user authorization")
    public void userCanChangeEmailWithAuthorization() {
        ValidatableResponse responseCreate = userClient.createUser(user);
        accessToken = responseCreate.extract().as(UserRegistrationResp.class).getAccessToken();

        Faker faker = new Faker();
        User userChanged = new User();
        userChanged.setEmail(faker.internet().emailAddress());

        ValidatableResponse responseChange = userClient.changeUserData(accessToken.substring(7), userChanged);
        AboutUserResp aboutUserResp = responseChange.extract().as(AboutUserResp.class);
        statusCode = responseChange.extract().statusCode();

        assertThat("Authorized user do not change Email", statusCode, equalTo(SC_OK));
        responseChange.assertThat().body("success", equalTo(true));
        assertEquals("Authorized user do not change Email", userChanged.getEmail(), aboutUserResp.getUser().getEmail());
    }

    @Test
    @DisplayName("Change user name, password with authorization")
    @Description("Create new random user, registered it and then try to change user name, password with user authorization")
    public void userCanChangeNamePasswordWithAuthorization() {
        ValidatableResponse responseCreate = userClient.createUser(user);
        accessToken = responseCreate.extract().as(UserRegistrationResp.class).getAccessToken();

        Faker faker = new Faker();
        User userChanged = new User();
        userChanged.setName(faker.name().username());
        userChanged.setPassword(faker.internet().password(8, 10));

        ValidatableResponse responseChange = userClient.changeUserData(accessToken.substring(7), userChanged);
        AboutUserResp aboutUserResp = responseChange.extract().as(AboutUserResp.class);
        statusCode = responseChange.extract().statusCode();

        assertThat("Authorized user do not change name, password", statusCode, equalTo(SC_OK));
        responseChange.assertThat().body("success", equalTo(true));
        assertEquals("Authorized user do not change name",userChanged.getName(), aboutUserResp.getUser().getName());
    }

    @Test
    @DisplayName("Change user password, Email with authorization")
    @Description("Create new random user, registered it and then try to change user password, Email with user authorization")
    public void userCanChangeEmailPasswordWithAuthorization() {
        ValidatableResponse responseCreate = userClient.createUser(user);
        accessToken = responseCreate.extract().as(UserRegistrationResp.class).getAccessToken();

        Faker faker = new Faker();
        User userChanged = new User();
        userChanged.setPassword(faker.internet().password(8, 10));
        userChanged.setEmail(faker.internet().emailAddress());

        ValidatableResponse responseChange = userClient.changeUserData(accessToken.substring(7), userChanged);
        AboutUserResp aboutUserResp = responseChange.extract().as(AboutUserResp.class);
        statusCode = responseChange.extract().statusCode();

        assertThat("Authorized user do not change password, Email", statusCode, equalTo(SC_OK));
        responseChange.assertThat().body("success", equalTo(true));
        assertEquals("Authorized user do not change Email", userChanged.getEmail(), aboutUserResp.getUser().getEmail());
    }

    @Test
    @DisplayName("Change user name, Email with authorization")
    @Description("Create new random user, registered it and then try to change user name, Email with user authorization")
    public void userCanChangeNameEmailWithAuthorization() {
        ValidatableResponse responseCreate = userClient.createUser(user);
        accessToken = responseCreate.extract().as(UserRegistrationResp.class).getAccessToken();

        Faker faker = new Faker();
        User userChanged = new User();
        userChanged.setName(faker.name().username());
        userChanged.setEmail(faker.internet().emailAddress());

        ValidatableResponse responseChange = userClient.changeUserData(accessToken.substring(7), userChanged);
        AboutUserResp aboutUserResp = responseChange.extract().as(AboutUserResp.class);
        statusCode = responseChange.extract().statusCode();

        assertThat("Authorized user do not change name, Email", statusCode, equalTo(SC_OK));
        responseChange.assertThat().body("success", equalTo(true));
        assertEquals("Authorized user do not change name",userChanged.getName(), aboutUserResp.getUser().getName());
        assertEquals("Authorized user do not change Email", userChanged.getEmail(), aboutUserResp.getUser().getEmail());
    }

    @Test
    @DisplayName("Change all user data with authorization")
    @Description("Create new random user, registered it and then try to change all user data with user authorization")
    public void userCanChangeAllDataWithAuthorization() {
        ValidatableResponse responseCreate = userClient.createUser(user);
        accessToken = responseCreate.extract().as(UserRegistrationResp.class).getAccessToken();

        User userRandom = User.randomUser();

        ValidatableResponse responseChange = userClient.changeUserData(accessToken.substring(7), userRandom);
        AboutUserResp aboutUserResp = responseChange.extract().as(AboutUserResp.class);
        statusCode = responseChange.extract().statusCode();

        assertThat("Authorized user do not change data", statusCode, equalTo(SC_OK));
        responseChange.assertThat().body("success", equalTo(true));
        assertEquals("Authorized user do not change name", userRandom.getName(), aboutUserResp.getUser().getName());
        assertEquals("Authorized user do not change Email", userRandom.getEmail(), aboutUserResp.getUser().getEmail());
    }
}
