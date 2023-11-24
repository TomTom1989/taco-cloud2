package tacos.data;

import java.util.List;
import java.util.Map;

import tacos.Ingredient;

public class IngredientCollectionDTO {
    private Map<String, List<Ingredient>> _embedded;

    public List<Ingredient> getIngredients() {
        return _embedded != null ? _embedded.get("ingredients") : null;
    }

    // Getters and setters for _embedded
}

