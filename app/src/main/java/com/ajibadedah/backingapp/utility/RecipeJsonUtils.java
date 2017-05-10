package com.ajibadedah.backingapp.utility;

import android.content.Context;

import com.ajibadedah.backingapp.model.Ingredient;
import com.ajibadedah.backingapp.model.Recipe;
import com.ajibadedah.backingapp.model.Step;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by ajibade on 5/4/17
 */

public class RecipeJsonUtils {

    public static Recipe getRecipe(String jsonStr)
            throws JSONException {

        String recipeName = "";
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ArrayList<Step> steps = new ArrayList<>();

        JSONArray recipeArray = new JSONArray(jsonStr);
        for (int i = 0; i < recipeArray.length(); i++) {

            JSONObject recipeJson = recipeArray.getJSONObject(i);
            recipeName = recipeJson.getString("name");

            JSONArray ingredientArray = recipeJson.getJSONArray("ingredients");
            for (int j = 0; j < ingredientArray.length(); j++) {
                JSONObject ingredientJson = ingredientArray.getJSONObject(i);

                ingredients.add(new Ingredient(ingredientJson.getString("quantity"),
                        ingredientJson.getString("measure"),
                        ingredientJson.getString("ingredient")));
            }

            JSONArray stepArray = recipeJson.getJSONArray("steps");
            for (int k = 0; k < stepArray.length(); k++) {
                JSONObject stepJson = ingredientArray.getJSONObject(i);

                steps.add(new Step(stepJson.getString("shortDescription"),
                        stepJson.getString("shortDescription"),
                        stepJson.getString("videoURL"),
                        stepJson.getString("thumbnailURL")));

            }

        }

        int sum = 0;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Recipe.class, new Recipe.RecipeTypeAdapter())
                .registerTypeAdapter(Ingredient.class, new Ingredient.IngredientTypeAdapter())
                .registerTypeAdapter(Step.class, new Step.StepTypeAdapter())
                .create();

        Recipe recipe;
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(jsonStr).getAsJsonArray();
        for (int i = 0; i < array.size(); i++){
            recipe = gson.fromJson(array.get(i), Recipe.class);
            sum = sum + i;
        }

        return new Recipe(recipeName, ingredients, steps);
    }

    public static Gson getGson(){
        return new GsonBuilder()
                .registerTypeAdapter(Recipe.class, new Recipe.RecipeTypeAdapter())
                .registerTypeAdapter(Ingredient.class, new Ingredient.IngredientTypeAdapter())
                .registerTypeAdapter(Step.class, new Step.StepTypeAdapter())
                .create();
    }

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {

            InputStream is = context.getAssets().open("bakingRecipe.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
