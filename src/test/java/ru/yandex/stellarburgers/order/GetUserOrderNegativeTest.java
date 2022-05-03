package ru.yandex.stellarburgers.order;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import ru.yandex.stellarburgers.OrderClient;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic(value = "Stellar Burgers")
@Feature(value = "Order Burger")
@Story(value = "Get list of user orders")
public class GetUserOrderNegativeTest {

    @Test
    @DisplayName("Get list of user orders without Authorization")
    @Description("Try to get list of user orders with Authorization")
    public void clientCanNotGetListOfOrdersWithoutAuthorization() {
        OrderClient orderClient = new OrderClient();

        ValidatableResponse getOrder = orderClient.getUserOrder();
        int statusCode = getOrder.extract().statusCode();

        assertThat("Client can get list of user orders with Authorization", statusCode, equalTo(SC_UNAUTHORIZED));
        getOrder.assertThat().body("success", equalTo(false));
        getOrder.assertThat().body("message", equalTo("You should be authorised"));
    }
}
