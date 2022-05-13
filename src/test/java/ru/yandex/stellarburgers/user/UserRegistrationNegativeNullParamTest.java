package ru.yandex.stellarburgers.user;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.stellarburgers.User;
import ru.yandex.stellarburgers.UserClient;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
@Epic(value = "Stellar Burgers")
@Feature(value = "User account")
@Story(value = "Create User account")
public class UserRegistrationNegativeNullParamTest {
    private boolean email;
    private boolean password;
    private boolean name;

    private User user;
    private UserClient userClient;

    /*
          Предварительно создается User с NULL логином, паролем и email-ом.
      */

    public UserRegistrationNegativeNullParamTest(boolean email, boolean password, boolean name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Before
    public void setUp() {
        user = new User();
        userClient = new UserClient();
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
    @DisplayName("Create user with NULL data")
    @Description("Create new user with NULL data and then try try to register it")
    public void userCanNotBeRegisteredWithNullData() {
        User userRandom = User.randomUser();

        if (email) {
            user.setEmail(userRandom.getEmail());
        }

        if (password) {
            user.setPassword(userRandom.getPassword());
        }

        if (name) {
            user.setName(userRandom.getName());
        }

        ValidatableResponse response = userClient.createUser(user);
        int statusCode = response.extract().statusCode();

        assertThat("User with NULL data should not be registered", statusCode, equalTo(SC_FORBIDDEN));
        response.assertThat().body("success", equalTo(false));
        response.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }
}
