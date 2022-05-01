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
import ru.yandex.stellarburgers.responses.UserRegistrationResponse;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic(value = "Stellar Burgers")
@Feature(value = "User account")
@Story(value = "Create User account")
public class UserRegistrationNegativeTest {

    User user;
    UserClient userClient;

    @Before
    public void setUp() {
        user = new User();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Create user with existing login")
    @Description("Create user with existing login and then trying to register it")
    public void userCannotBeRegisteredWitExistingLogin() {
        user = User.randomUser();
        ValidatableResponse responseFirst = userClient.createUser(user);
        String accessToken = responseFirst.extract().as(UserRegistrationResponse.class).getAccessToken();

        ValidatableResponse responseSecond = userClient.createUser(user);
        int statusCode = responseSecond.extract().statusCode();
        userClient.deleteUser(user, accessToken.substring(7));

        assertThat("User with existing login should not be registered ", statusCode, equalTo(SC_FORBIDDEN));
        responseSecond.assertThat().body("message", equalTo("User already exists"));
        responseSecond.assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Create user with NULL email")
    @Description("Create new user with NULL email and then try try to register it")
    public void userCanNotBeRegisteredWithNullEmail() {
        Faker faker = new Faker();
        user.setName(faker.name().username());
        user.setPassword(faker.internet().password(8, 10));

        ValidatableResponse response = userClient.createUser(user);
        int statusCode = response.extract().statusCode();

        assertThat("User with NULL email should not be registered", statusCode, equalTo(SC_FORBIDDEN));
        response.assertThat().body("message", equalTo("Email, password and name are required fields"));
        response.assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Create user with NULL password")
    @Description("Create new user with NULL password and then try try to register it")
    public void userCanNotBeRegisteredWithNullPassword() {
        Faker faker = new Faker();
        user.setName(faker.name().username());
        user.setEmail(faker.internet().emailAddress());

        ValidatableResponse response = userClient.createUser(user);
        int statusCode = response.extract().statusCode();

        assertThat("User with NULL password should not be registered", statusCode, equalTo(SC_FORBIDDEN));
        response.assertThat().body("message", equalTo("Email, password and name are required fields"));
        response.assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Create user with NULL name")
    @Description("Create new user with empty name and then try try to register it")
    public void userCanNotBeRegisteredWithNullName() {
        Faker faker = new Faker();
        user.setPassword(faker.internet().password(8, 10));
        user.setEmail(faker.internet().emailAddress());

        ValidatableResponse response = userClient.createUser(user);
        int statusCode = response.extract().statusCode();

        assertThat("User with NULL name should not be registered", statusCode, equalTo(SC_FORBIDDEN));
        response.assertThat().body("message", equalTo("Email, password and name are required fields"));
        response.assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Create user without email")
    @Description("Create new user without email and then try try to register it")
    public void userCanNotBeRegisteredWithEmptyEmail() {
        Faker faker = new Faker();
        Map<String, String> userData = new HashMap<>();
        userData.put("password", faker.internet().password(8, 10));
        userData.put("name", faker.name().username());

        ValidatableResponse response = userClient.createUser(userData);
        int statusCode = response.extract().statusCode();

        assertThat("User without email should not be registered", statusCode, equalTo(SC_FORBIDDEN));
        response.assertThat().body("message", equalTo("Email, password and name are required fields"));
        response.assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Create user without password")
    @Description("Create new user without password and then try try to register it")
    public void userCanNotBeRegisteredWithEmptyPassword() {
        Faker faker = new Faker();
        Map<String, String> userData = new HashMap<>();
        userData.put("email", faker.internet().emailAddress());
        userData.put("name", faker.name().username());

        ValidatableResponse response = userClient.createUser(userData);
        int statusCode = response.extract().statusCode();

        assertThat("User without password should not be registered", statusCode, equalTo(SC_FORBIDDEN));
        response.assertThat().body("message", equalTo("Email, password and name are required fields"));
        response.assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Create user without name")
    @Description("Create new user without name and then try try to register it")
    public void userCanNotBeRegisteredWithEmptyName() {
        Faker faker = new Faker();
        Map<String, String> userData = new HashMap<>();
        userData.put("email", faker.internet().emailAddress());
        userData.put("password", faker.internet().password(8, 10));

        ValidatableResponse response = userClient.createUser(userData);
        int statusCode = response.extract().statusCode();

        assertThat("User without name should not be registered", statusCode, equalTo(SC_FORBIDDEN));
        response.assertThat().body("message", equalTo("Email, password and name are required fields"));
        response.assertThat().body("success", equalTo(false));
    }
}
