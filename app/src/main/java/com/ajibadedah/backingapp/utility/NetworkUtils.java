package com.ajibadedah.backingapp.utility;

import android.content.Context;
import android.net.Uri;

import com.ajibadedah.backingapp.model.Ingredient;
import com.ajibadedah.backingapp.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by ajibade on 5/4/17
 */

public class NetworkUtils {

    private static final String RECIPE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/5907926b_baking/baking.json";
    /**
     * This method returns the entire result from the HTTP response.
     *
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl() throws IOException {
        URL url;
        try {
            url = new URL(RECIPE_URL);

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

    public static void getRecipe(Context context, String jsonStr)
            throws JSONException {
//        JSONObject recipeJson = new JSONObject(jsonStr);
        JSONArray recipeArray = new JSONArray(jsonStr);
        for (int i = 0; i < recipeArray.length(); i++){

            JSONObject recipeJson = recipeArray.getJSONObject(i);

            String recipeName = recipeJson.getString("name");
            ArrayList<Ingredient> ingredients = new ArrayList<>();
            ArrayList<Step> steps = new ArrayList<>();

            JSONArray ingredientArray = recipeJson.getJSONArray("ingredients");
            for (int j = 0; j < ingredientArray.length(); j++){
                JSONObject ingredientJson = ingredientArray.getJSONObject(i);

                ingredients.add(new Ingredient(ingredientJson.getString("quantity"),
                        ingredientJson.getString("measure"),
                        ingredientJson.getString("ingredient")));
            }

            JSONArray stepArray = recipeJson.getJSONArray("steps");
            for (int k = 0; k < ingredientArray.length(); k++){
                JSONObject stepJson = ingredientArray.getJSONObject(i);

                steps.add(new Step(stepJson.getString("shortDescription"),
                        stepJson.getString("shortDescription"),
                        buildUrlWithString(stepJson.getString("videoURL")),
                        buildUrlWithString(stepJson.getString("thumbnailURL"))));

            }

        }
    }

    public static URL buildUrlWithString(String string){
        try {
            return new URL(string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
