package com.ajibadedah.backingapp.utility;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.JsonReader;
import android.widget.Toast;

import com.ajibadedah.backingapp.model.Ingredient;
import com.ajibadedah.backingapp.model.Recipe;
import com.ajibadedah.backingapp.model.Step;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSourceInputStream;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by ajibade on 5/4/17
 */

public class RecipeJsonUtils {
    public static void getRecipe(Context context, String jsonStr)
            throws JSONException{
//        JSONObject recipeJson = new JSONObject(jsonStr);
//        JSONArray recipeArray = new JSONArray(jsonStr);
//        for (int i = 0; i < recipeArray.length(); i++){
//
//            JSONObject recipeJson = recipeArray.getJSONObject(i);
//
//            String recipeName = recipeJson.getString("name");
//            ArrayList<Ingredient> ingredients = new ArrayList<>();
//            ArrayList<Step> steps = new ArrayList<>();
//
//            JSONArray ingredientArray = recipeJson.getJSONArray("ingredients");
//            for (int j = 0; j < ingredientArray.length(); j++){
//                JSONObject ingredientJson = ingredientArray.getJSONObject(i);
//
//                ingredients.add(new Ingredient(ingredientJson.getString("quantity"),
//                        ingredientJson.getString("measure"),
//                        ingredientJson.getString("ingredient")));
//            }
//
//            JSONArray stepArray = recipeJson.getJSONArray("steps");
//            for (int k = 0; k < ingredientArray.length(); k++){
//                JSONObject stepJson = ingredientArray.getJSONObject(i);
//
//                steps.add(new Step(stepJson.getString("shortDescription"),
//                        stepJson.getString("shortDescription"),
//                        buildUrlWithString(stepJson.getString("videoURL")),
//                        buildUrlWithString(stepJson.getString("thumbnailURL"))));
//
//            }
//
//        }

        int sum = 0;
        Gson gson = new Gson();
        Recipe recipe;
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(jsonStr).getAsJsonArray();
        for (int i = 0; i < array.size(); i++){
            array.get(i);
            recipe = gson.fromJson(array.get(i), Recipe.class);
            sum = sum + i;
        }
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

            JSONObject recipeJson = new JSONObject(json);
            JSONArray recipeArray = new JSONArray(json);
            JSONObject recipeJso = recipeArray.getJSONObject(0);

            String recipeName = recipeJso.getString("name");
            size = size + 1;

//            Scanner scanner = new Scanner(is);
//            scanner.useDelimiter("\\A");
//
//            boolean hasInput = scanner.hasNext();
//            String response = null;
//            if (hasInput) {
//                response = scanner.next();
//            }
//            scanner.close();

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;

    }

    public static JsonReader readJSONFile(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();
        String uri = null;

        try {
            for (String asset : assetManager.list("")) {
                if (asset.endsWith(".json")) {
                    uri = "asset:///" + asset;
                }
            }
        } catch (IOException e) {
            Toast.makeText(context, "Cant get assets JSON file", Toast.LENGTH_LONG)
                    .show();
        }

        String userAgent = Util.getUserAgent(context, "BakingApp");
        DataSource dataSource = new DefaultDataSource(context, null, userAgent, false);
        DataSpec dataSpec = new DataSpec(Uri.parse(uri));
        InputStream inputStream = new DataSourceInputStream(dataSource, dataSpec);

        Scanner scanner = new Scanner(inputStream);
        scanner.useDelimiter("\\A");

        boolean hasInput = scanner.hasNext();
        String response = null;
        if (hasInput) {
            response = scanner.next();
        }
        scanner.close();

        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        } finally {
            Util.closeQuietly(dataSource);
        }

        return reader;
    }

    public static void readEntry(JsonReader reader) {
        Integer id = -1;
        String composer = null;
        String title = null;
        String uri = null;
        String albumArtID = null;

        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "name":
                        title = reader.nextString();
                        break;
                    case "id":
                        id = reader.nextInt();
                        break;
                    case "composer":
                        composer = reader.nextString();
                        break;
                    case "uri":
                        uri = reader.nextString();
                        break;
                    case "albumArtID":
                        albumArtID = reader.nextString();
                        break;
                    default:
                        break;
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        title = composer;

    }
}
