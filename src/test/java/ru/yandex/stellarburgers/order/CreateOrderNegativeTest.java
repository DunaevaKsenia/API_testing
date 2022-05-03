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
import ru.yandex.stellarburgers.Ingredient;
import ru.yandex.stellarburgers.OrderClient;
import ru.yandex.stellarburgers.User;
import ru.yandex.stellarburgers.UserClient;
import ru.yandex.stellarburgers.responses.IngredientListResp;
import ru.yandex.stellarburgers.responses.UserRegistrationResp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic(value = "Stellar Burgers")
@Feature(value = "Order Burger")
@Story(value = "Create order")
public class CreateOrderNegativeTest {
    private User user;
    private UserClient userClient;
    private OrderClient orderClient;
    private String accessToken;
    private List<Ingredient> ingredients;
    private int ingredientsCount;
    private Map<String, String[]> orderData;
    private Random rand;
    private int int_random;
    private int int_randomTwo;

    @Before
    public void setUp() {
        user = User.randomUser();
        userClient = new UserClient();
        orderClient = new OrderClient();
        orderData = new HashMap<>();
        rand = new Random();

        ingredients = orderClient.getIngredientList().extract().as(IngredientListResp.class).getData();
        ingredientsCount = ingredients.size();
        int_random = rand.nextInt(ingredientsCount - 1);
        int_randomTwo = rand.nextInt(ingredientsCount - 1);

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
    @DisplayName("Create order with incorrect ingredient and Authorization")
    @Description("Create new random user, registered it and then try to create order with incorrect ingredient")
    public void clientCanNotCreateOrderWithIncorrectIngredientWithAuthorization() {
        String[] ingredientsList = {ingredients.get(int_random).get_id().substring(1)};
        orderData.put("ingredients", ingredientsList);

        ValidatableResponse createOrder = orderClient.createOrder(orderData, accessToken.substring(7));
        int statusCode = createOrder.extract().statusCode();
        assertThat("Order with incorrect ingredient and Authorization was created", statusCode, equalTo(SC_INTERNAL_SERVER_ERROR));
    }

    @Test
    @DisplayName("Create order with incorrect ingredient without Authorization")
    @Description("Try to create order with incorrect ingredient without Authorization")
    public void clientCanNotCreateOrderWithIncorrectIngredientWithoutAuthorization() {
        String[] ingredientsList = {ingredients.get(int_random).get_id().substring(1)};
        orderData.put("ingredients", ingredientsList);

        ValidatableResponse createOrder = orderClient.createOrder(orderData);
        int statusCode = createOrder.extract().statusCode();
        assertThat("Order with incorrect ingredient without Authorization was created", statusCode, equalTo(SC_INTERNAL_SERVER_ERROR));
    }

    @Test
    @DisplayName("Create order without ingredient with Authorization")
    @Description("Create new random user, registered it and then try to create order without ingredient with Authorization")
    public void clientCanNotCreateOrderWithoutIngredientWithAuthorization() {
        String[] ingredientsList = new String[0];
        orderData.put("ingredients", ingredientsList);

        ValidatableResponse createOrder = orderClient.createOrder(orderData, accessToken.substring(7));
        int statusCode = createOrder.extract().statusCode();

        assertThat("Order without ingredient with Authorization was created", statusCode, equalTo(SC_BAD_REQUEST));
        createOrder.assertThat().body("success", equalTo(false));
        createOrder.assertThat().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Create order without ingredient without Authorization")
    @Description("Try to create order without ingredient without Authorization")
    public void clientCanNotCreateOrderWithoutIngredientWithoutAuthorization() {
        String[] ingredientsList = new String[0];
        orderData.put("ingredients", ingredientsList);

        ValidatableResponse createOrder = orderClient.createOrder(orderData);
        int statusCode = createOrder.extract().statusCode();

        assertThat("Order without ingredient without Authorization was created", statusCode, equalTo(SC_BAD_REQUEST));
        createOrder.assertThat().body("success", equalTo(false));
        createOrder.assertThat().body("message", equalTo("Ingredient ids must be provided"));
    }
}
