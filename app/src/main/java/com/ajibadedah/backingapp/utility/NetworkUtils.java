package com.ajibadedah.backingapp.utility;

import android.content.Context;

import com.ajibadedah.backingapp.model.Ingredient;
import com.ajibadedah.backingapp.model.Recipe;
import com.ajibadedah.backingapp.model.Step;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by ajibade on 5/4/17
 */

public class NetworkUtils {

    private static final String OLD_RECIPE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/5907926b_baking/baking.json";

//    https://nspf.github.io/BakingAppJson/data.json
//    private static final String RECIPE_URL =
//            "https://d17h27t6h515a5.cloudfront.net/topher/2017/March/58d1537b_baking/baking.json";

    private static final RecipeApi recipeApi;

    static {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Recipe.class, new Recipe.RecipeTypeAdapter())
                .registerTypeAdapter(Ingredient.class, new Ingredient.IngredientTypeAdapter())
                .registerTypeAdapter(Step.class, new Step.StepTypeAdapter())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/5907926b_baking/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        recipeApi = retrofit.create(RecipeApi.class);
    }

    public static RecipeApi getRecipeApi() {
        return recipeApi;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl() throws IOException {
        URL url;
        try {
            url = new URL(OLD_RECIPE_URL);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

    public static URL buildUrlWithString(String string) {
        try {
            return new URL(string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Recipe getRecipe(Context context, String jsonStr)
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
                JSONObject stepJson = stepArray.getJSONObject(i);

                steps.add(new Step(stepJson.getString("shortDescription"),
                        stepJson.getString("shortDescription"),
                        stepJson.getString("videoURL"),
                        stepJson.getString("thumbnailURL")));

            }

        }

        return new Recipe(recipeName, ingredients, steps);
    }

    public interface RecipeApi {
        @GET("baking.json")
        Call<List<Recipe>> getRecipes();
    }

}
