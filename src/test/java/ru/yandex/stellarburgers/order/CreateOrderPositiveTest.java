package ru.yandex.stellarburgers.order;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.stellarburgers.Ingredient;
import ru.yandex.stellarburgers.OrderClient;
import ru.yandex.stellarburgers.User;
import ru.yandex.stellarburgers.UserClient;
import ru.yandex.stellarburgers.responses.IngredientListResp;
import ru.yandex.stellarburgers.responses.UserRegistrationResp;
import java.util.*;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
@Epic(value = "Stellar Burgers")
@Feature(value = "Order Burger")
@Story(value = "Create order")
public class CreateOrderPositiveTest {
    private boolean oneIngredient;
    private boolean twoIngredients;

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

    public CreateOrderPositiveTest(boolean oneIngredient, boolean twoIngredients) {
        this.oneIngredient = oneIngredient;
        this.twoIngredients = twoIngredients;
    }

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

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][] {
                {true, false},
                {false, true},
       };
    }

    @Test
    @DisplayName("Create order with ingredient and Authorization")
    @Description("Create new random user, registered it and then try to create order with ingredient")
    public void clientCanCreateOrderWithIngredientAndAuthorization() {
        if (oneIngredient) {
            String[] ingredientsList = {ingredients.get(int_random).get_id()};
            orderData.put("ingredients", ingredientsList);
        }

        if (twoIngredients) {
            String[] ingredientsList = {ingredients.get(int_random).get_id(), ingredients.get(int_randomTwo).get_id()};
            orderData.put("ingredients", ingredientsList);
        }

        ValidatableResponse createOrder = orderClient.createOrder(orderData, accessToken.substring(7));
        int statusCode = createOrder.extract().statusCode();

        assertThat("Cannot create order with ingredient and Authorization", statusCode, equalTo(SC_OK));
        createOrder.assertThat().body("success", equalTo(true));
    }



    @Test
    @DisplayName("Create order with ingredient without Authorization")
    @Description("Try to create order with ingredient without user authorization")
    public void clientCanCreateOrderWithIngredientWithoutAuthorization() {
        if (oneIngredient) {
            String[] ingredientsList = {ingredients.get(int_random).get_id()};
            orderData.put("ingredients", ingredientsList);
        }

        if (twoIngredients) {
            String[] ingredientsList = {ingredients.get(int_random).get_id(), ingredients.get(int_randomTwo).get_id()};
            orderData.put("ingredients", ingredientsList);
        }

        ValidatableResponse createOrder = orderClient.createOrder(orderData);
        int statusCode = createOrder.extract().statusCode();

        assertThat("Cannot create order with ingredient without Authorization", statusCode, equalTo(SC_OK));
        createOrder.assertThat().body("success", equalTo(true));
    }
}
