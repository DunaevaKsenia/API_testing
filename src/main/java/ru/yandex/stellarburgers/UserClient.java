package ru.yandex.stellarburgers;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_ACCEPTED;

public class UserClient extends BaseSpec {
    @Step("Create new user with Name:{user.name}  Email:{user.email}  Password:{user.password} ")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(CREATE_USER)
                .then();
    }

    @Step("Create new user {userData}")
    public ValidatableResponse createUser(Map<String, String> userData) {
        return given()
                .spec(getBaseSpec())
                .body(userData)
                .when()
                .post(CREATE_USER)
                .then();
    }

    @Step("Delete user with Name:{user.name}  AccessToken:{accessToken}")
    public void deleteUser(User user, String accessToken) {
        given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .when()
                .delete(INFO_USER)
                .then()
                .assertThat()
                .statusCode(SC_ACCEPTED);
    }

    @Step("User login with Name:{user.name}  Email:{user.email}  Password:{user.password}")
    public ValidatableResponse loginUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_LOGIN)
                .then();
    }

    @Step("User login with {userData}")
    public ValidatableResponse loginUser(Map<String, String> userData) {
        return given()
                .spec(getBaseSpec())
                .body(userData)
                .when()
                .post(USER_LOGIN)
                .then();
    }

    @Step("Changing user data for accessToken:{accessToken}   set Name:{user.name}  Email:{user.email}  Password:{user.password}")
    public ValidatableResponse changeUserData(String accessToken, User user) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .and()
                .body(user)
                .when()
                .patch(INFO_USER)
                .then();
    }
    @Step("Changing user data set Name:{user.name}  Email:{user.email}  Password:{user.password}")
    public ValidatableResponse changeUserData(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .patch(INFO_USER)
                .then();
    }
}
