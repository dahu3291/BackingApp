package com.ajibadedah.backingapp.sync;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.ajibadedah.backingapp.IdlingResource.SimpleIdlingResource;
import com.ajibadedah.backingapp.model.Ingredient;
import com.ajibadedah.backingapp.model.Recipe;
import com.ajibadedah.backingapp.model.Step;
import com.ajibadedah.backingapp.utility.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ajibade on 5/6/17
 */

public class RecipeManager {

    private static final String RECIPE_MANAGER = "recipeManager";
    private static final String RECIPES = "recipes";
    @SuppressLint("StaticFieldLeak")
    private static RecipeManager instance;
    private final Gson gson;
    private final Context appContext;
    private final List<Recipe> recipes;
    private final List<RecipeConsumer> recipeConsumers;
    @Nullable  SimpleIdlingResource idlingResource;
    private final Callback<List<Recipe>> apiCallback = new Callback<List<Recipe>>() {
        @Override
        public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
            recipes.addAll(response.body());

            SharedPreferences preferences = appContext.getSharedPreferences(RECIPE_MANAGER, Context.MODE_PRIVATE);
            String serializedRecipes = gson.toJson(recipes);
            preferences.edit().putString(RECIPES, serializedRecipes).apply();

            for (RecipeConsumer recipeConsumer : recipeConsumers) {
                recipeConsumer.onRecipesAvailable(recipes);
            }
            if (idlingResource != null) {
                idlingResource.setIdleState(true);
            }
            recipeConsumers.clear();
        }

        @Override
        public void onFailure(Call<List<Recipe>> call, Throwable t) {
            recipeConsumers.clear();
        }
    };
    private boolean hasCalledApi;

    private RecipeManager(Context context) {
        appContext = context.getApplicationContext();
        gson = new GsonBuilder()
                .registerTypeAdapter(Recipe.class, new Recipe.RecipeTypeAdapter())
                .registerTypeAdapter(Ingredient.class, new Ingredient.IngredientTypeAdapter())
                .registerTypeAdapter(Step.class, new Step.StepTypeAdapter())
                .create();
        recipes = new ArrayList<>();
        recipeConsumers = new ArrayList<>();

        SharedPreferences preferences = appContext.getSharedPreferences(RECIPE_MANAGER, Context.MODE_PRIVATE);

        String serializedRecipes = preferences.getString(RECIPES, "");
        Recipe[] deserializedRecipes = gson.fromJson(serializedRecipes, Recipe[].class);

        if (deserializedRecipes != null) {
            recipes.addAll(Arrays.asList(deserializedRecipes));
        }
    }

    public static RecipeManager getInstance(Context context) {
        if (instance == null) instance = new RecipeManager(context);
        return instance;
    }

    public void getRecipes(final RecipeConsumer recipeConsumer, @Nullable final SimpleIdlingResource idlingRes) {
        idlingResource = idlingRes;
        /**
         * The IdlingResource is null in production as set by the @Nullable annotation which means
         * the value is allowed to be null.
         *
         * If the idle state is true, Espresso can perform the next action.
         * If the idle state is false, Espresso will wait until it is true before
         * performing the next action.
         */
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        if (!recipes.isEmpty()) {
            recipeConsumer.onRecipesAvailable(recipes);
            if (idlingResource != null) {
                idlingResource.setIdleState(true);
            }
            return;
        }

        if (!recipeConsumers.contains(recipeConsumer)) {
            recipeConsumers.add(recipeConsumer);
        }

        if (!hasCalledApi) {
            NetworkUtils.getRecipeApi().getRecipes().enqueue(apiCallback);
            hasCalledApi = true;
        }
    }

    public interface RecipeConsumer {
        void onRecipesAvailable(List<Recipe> recipes);
    }
}
