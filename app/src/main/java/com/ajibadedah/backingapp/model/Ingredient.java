package com.ajibadedah.backingapp.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by ajibade on 5/4/17
 */

public class Ingredient {

    private String quantity;
    private String measure;
    private String ingredient;

    public Ingredient(){
        //empty constructor
    }

    public Ingredient(String quantity, String measure, String ingredient){
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public static class IngredientTypeAdapter implements JsonDeserializer<Ingredient> {
        @Override
        public Ingredient deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Ingredient ingredient = new Ingredient();
            JsonObject ingredientJson = json.getAsJsonObject();

            ingredient.setIngredient(ingredientJson.get("ingredient").getAsString());
            ingredient.setQuantity(ingredientJson.get("quantity").getAsString());
            ingredient.setMeasure(ingredientJson.get("measure").getAsString());

            return ingredient;
        }
    }
}
