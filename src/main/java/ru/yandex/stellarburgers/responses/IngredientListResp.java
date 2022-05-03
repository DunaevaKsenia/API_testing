package ru.yandex.stellarburgers.responses;

import lombok.Data;
import ru.yandex.stellarburgers.Ingredient;
import java.util.List;

@Data
public class IngredientListResp {
    private boolean success;
    private List<Ingredient> data;

    public IngredientListResp(boolean success, List<Ingredient> data) {
        this.success = success;
        this.data = data;
    }

    public IngredientListResp() {
    }
}
