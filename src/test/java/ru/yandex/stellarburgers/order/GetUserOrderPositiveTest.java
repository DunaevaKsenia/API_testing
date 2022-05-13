package ru.yandex.stellarburgers.order;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.stellarburgers.OrderClient;
import ru.yandex.stellarburgers.User;
import ru.yandex.stellarburgers.UserClient;
import ru.yandex.stellarburgers.responses.UserRegistrationResp;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic(value = "Stellar Burgers")
@Feature(value = "Order Burger")
@Story(value = "Get list of user orders")
public class GetUserOrderPositiveTest {
    private User user;
    private UserClient userClient;
    private OrderClient orderClient;
    private String accessToken;

    @Before
    public void setUp() {
        user = User.randomUser();
        userClient = new UserClient();
        orderClient = new OrderClient();

        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().as(UserRegistrationResp.class).getAccessToken();
    }

    @After
    public void tearDown() {
        if(accessToken != null) {
            userClient.deleteUser(user, accessToken.substring(7));
        }
    }

    @Test
    @DisplayName("Get list of user orders with Authorization")
    @Description("Create new random user, registered it and then try to get list of user orders with Authorization")
    public void userCanGetListOfOrdersWithAuthorization() {
        ValidatableResponse createOrder = orderClient.getUserOrder(user, accessToken.substring(7));
        int statusCode = createOrder.extract().statusCode();

        assertThat("Cannot create order with ingredient and Authorization", statusCode, equalTo(SC_OK));
        createOrder.assertThat().body("success", equalTo(true));
    }
}
