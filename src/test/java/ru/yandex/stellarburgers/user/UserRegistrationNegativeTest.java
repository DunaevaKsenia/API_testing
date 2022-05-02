package ru.yandex.stellarburgers.user;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.stellarburgers.User;
import ru.yandex.stellarburgers.UserClient;
import ru.yandex.stellarburgers.responses.UserRegistrationResp;
import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic(value = "Stellar Burgers")
@Feature(value = "User account")
@Story(value = "Create User account")
public class UserRegistrationNegativeTest {

    private User user;
    private UserClient userClient;
    private int statusCode;

    /*
          Предварительно создается User с NULL логином, паролем и email-ом.
      */
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
        String accessToken = responseFirst.extract().as(UserRegistrationResp.class).getAccessToken();

        ValidatableResponse responseSecond = userClient.createUser(user);
        statusCode = responseSecond.extract().statusCode();

        userClient.deleteUser(user,accessToken.substring(7));

        assertThat("User with existing login should not be registered ", statusCode, equalTo(SC_FORBIDDEN));
        responseSecond.assertThat().body("success", equalTo(false));
        responseSecond.assertThat().body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Create user with NULL email")
    @Description("Create new user with NULL email and then try try to register it")
    public void userCanNotBeRegisteredWithNullEmail() {
        Faker faker = new Faker();
        user.setName(faker.name().username());
        user.setPassword(faker.internet().password(8, 10));

        ValidatableResponse response = userClient.createUser(user);
        statusCode = response.extract().statusCode();

        assertThat("User with NULL email should not be registered", statusCode, equalTo(SC_FORBIDDEN));
        response.assertThat().body("success", equalTo(false));
        response.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Create user with NULL password")
    @Description("Create new user with NULL password and then try try to register it")
    public void userCanNotBeRegisteredWithNullPassword() {
        Faker faker = new Faker();
        user.setName(faker.name().username());
        user.setEmail(faker.internet().emailAddress());

        ValidatableResponse response = userClient.createUser(user);
        statusCode = response.extract().statusCode();

        assertThat("User with NULL password should not be registered", statusCode, equalTo(SC_FORBIDDEN));
        response.assertThat().body("success", equalTo(false));
        response.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Create user with NULL name")
    @Description("Create new user with empty name and then try try to register it")
    public void userCanNotBeRegisteredWithNullName() {
        Faker faker = new Faker();
        user.setPassword(faker.internet().password(8, 10));
        user.setEmail(faker.internet().emailAddress());

        ValidatableResponse response = userClient.createUser(user);
        statusCode = response.extract().statusCode();

        assertThat("User with NULL name should not be registered", statusCode, equalTo(SC_FORBIDDEN));
        response.assertThat().body("success", equalTo(false));
        response.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Create user with NULL email, password")
    @Description("Create new user with NULL email, password and then try try to register it")
    public void userCanNotBeRegisteredWithNullEmailPassword() {
        Faker faker = new Faker();
        user.setName(faker.name().username());

        ValidatableResponse response = userClient.createUser(user);
        statusCode = response.extract().statusCode();

        assertThat("User with NULL email, password should not be registered", statusCode, equalTo(SC_FORBIDDEN));
        response.assertThat().body("success", equalTo(false));
        response.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Create user with NULL email, name")
    @Description("Create new user with NULL  email, name and then try try to register it")
    public void userCanNotBeRegisteredWithNullEmailName() {
        Faker faker = new Faker();
        user.setPassword(faker.internet().password(8, 10));

        ValidatableResponse response = userClient.createUser(user);
        statusCode = response.extract().statusCode();

        assertThat("User with NULL email, name should not be registered", statusCode, equalTo(SC_FORBIDDEN));
        response.assertThat().body("success", equalTo(false));
        response.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Create user with NULL password, name")
    @Description("Create new user with NULL password, name and then try try to register it")
    public void userCanNotBeRegisteredWithNullPasswordName() {
        Faker faker = new Faker();
        user.setEmail(faker.internet().emailAddress());

        ValidatableResponse response = userClient.createUser(user);
        statusCode = response.extract().statusCode();

        assertThat("User with NULL password, name should not be registered", statusCode, equalTo(SC_FORBIDDEN));
        response.assertThat().body("success", equalTo(false));
        response.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Create user with NULL data")
    @Description("Create new user with NULL data and then try try to register it")
    public void userCanNotBeRegisteredWithNullData() {
        ValidatableResponse response = userClient.createUser(user);
        statusCode = response.extract().statusCode();

        assertThat("User with NULL data should not be registered", statusCode, equalTo(SC_FORBIDDEN));
        response.assertThat().body("success", equalTo(false));
        response.assertThat().body("message", equalTo("Email, password and name are required fields"));
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
        statusCode = response.extract().statusCode();

        assertThat("User without email should not be registered", statusCode, equalTo(SC_FORBIDDEN));
        response.assertThat().body("success", equalTo(false));
        response.assertThat().body("message", equalTo("Email, password and name are required fields"));
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
        statusCode = response.extract().statusCode();

        assertThat("User without password should not be registered", statusCode, equalTo(SC_FORBIDDEN));
        response.assertThat().body("success", equalTo(false));
        response.assertThat().body("message", equalTo("Email, password and name are required fields"));
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
        statusCode = response.extract().statusCode();

        assertThat("User without name should not be registered", statusCode, equalTo(SC_FORBIDDEN));
        response.assertThat().body("success", equalTo(false));
        response.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Create user without email, password")
    @Description("Create new user without email, password and then try try to register it")
    public void userCanNotBeRegisteredWithEmptyEmailPassword() {
        Faker faker = new Faker();
        Map<String, String> userData = new HashMap<>();
        userData.put("name", faker.name().username());

        ValidatableResponse response = userClient.createUser(userData);
        statusCode = response.extract().statusCode();

        assertThat("User without email, password should not be registered", statusCode, equalTo(SC_FORBIDDEN));
        response.assertThat().body("success", equalTo(false));
        response.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }
    @Test
    @DisplayName("Create user without email, name")
    @Description("Create new user without email, name and then try try to register it")
    public void userCanNotBeRegisteredWithEmptyEmailName() {
        Faker faker = new Faker();
        Map<String, String> userData = new HashMap<>();
        userData.put("password", faker.internet().password(8, 10));

        ValidatableResponse response = userClient.createUser(userData);
        statusCode = response.extract().statusCode();

        assertThat("User without email, name should not be registered", statusCode, equalTo(SC_FORBIDDEN));
        response.assertThat().body("success", equalTo(false));
        response.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Create user without password, name")
    @Description("Create new user without password, name and then try try to register it")
    public void userCanNotBeRegisteredWithEmptyPasswordName() {
        Faker faker = new Faker();
        Map<String, String> userData = new HashMap<>();
        userData.put("email", faker.internet().emailAddress());

        ValidatableResponse response = userClient.createUser(userData);
        statusCode = response.extract().statusCode();

        assertThat("User without password, name should not be registered", statusCode, equalTo(SC_FORBIDDEN));
        response.assertThat().body("success", equalTo(false));
        response.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Create user without data")
    @Description("Create new user without data and then try try to register it")
    public void userCanNotBeRegisteredWithEmptyData() {
        Map<String, String> userData = new HashMap<>();

        ValidatableResponse response = userClient.createUser(userData);
        statusCode = response.extract().statusCode();

        assertThat("User without data should not be registered", statusCode, equalTo(SC_FORBIDDEN));
        response.assertThat().body("success", equalTo(false));
        response.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }
}
