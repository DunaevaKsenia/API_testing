package ru.yandex.stellarburgers;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseSpec {
    @Step("Get Ingredients List")
    public ValidatableResponse getIngredientList() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS)
                .then();
    }

    @Step("Create order for accessToken:{accessToken}")
    public ValidatableResponse createOrder(Map<String, String[]> ingredients, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .when()
                .body(ingredients)
                .when()
                .post(ORDERS)
                .then();
    }

    @Step("Create order")
    public ValidatableResponse createOrder(Map<String, String[]> ingredients) {
        return given()
                .spec(getBaseSpec())
                .when()
                .body(ingredients)
                .when()
                .post(ORDERS)
                .then();
    }

    @Step("Get list of user orders with Name:{user.name}  AccessToken:{accessToken}")
    public ValidatableResponse getUserOrder(User user, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .when()
                .get(ORDERS)
                .then();
    }

    @Step("Get list of user orders without Authorization")
    public ValidatableResponse getUserOrder() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDERS)
                .then();
    }
}
