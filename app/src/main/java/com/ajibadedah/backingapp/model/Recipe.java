package com.ajibadedah.backingapp.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by ajibade on 5/4/17
 */

public class Recipe {

    private String name;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;

    public Recipe(){
        //empty constructor
    }

    public Recipe(String name, ArrayList<Ingredient> ingredients, ArrayList<Step> steps) {
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    private void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    private void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public static class RecipeTypeAdapter implements JsonDeserializer<Recipe> {

         @Override
         public Recipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
             Recipe recipe = new Recipe();

             JsonObject recipeJson = json.getAsJsonObject();

             recipe.setName(recipeJson.get("name").getAsString());

             ArrayList<Ingredient> ingredients = new ArrayList<>();
             ArrayList<Step> steps = new ArrayList<>();

             JsonArray ingredientArray = recipeJson.get("ingredients").getAsJsonArray();
             for (JsonElement element : ingredientArray) {
                 ingredients.add(context.<Ingredient>deserialize(element, Ingredient.class));
             }

             JsonArray stepArray = recipeJson.get("steps").getAsJsonArray();
             for (JsonElement element : stepArray) {
                 steps.add(context.<Step>deserialize(element, Step.class));
             }

             recipe.setIngredients(ingredients);
             recipe.setSteps(steps);

             return recipe;
         }

     }
}
