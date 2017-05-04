package com.ajibadedah.backingapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;

import com.ajibadedah.backingapp.sync.RecipeIntentService;
import com.ajibadedah.backingapp.utility.NetworkUtils;
import com.ajibadedah.backingapp.utility.RecipeJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecipeIntentService.startActionRecipe(this);

//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.main_container, newFragment)
//                .commit();

//        String json = RecipeJsonUtils.loadJSONFromAsset(this);
//        try {
//            RecipeJsonUtils.getRecipe(this, json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        try {
//
//            JsonReader reader;
//            ArrayList<Integer> sampleIDs = new ArrayList<>();
//
//                reader = RecipeJsonUtils.readJSONFile(this);
//                reader.beginArray();
//                while (reader.hasNext()) {
//                    RecipeJsonUtils.readEntry(reader);
//                }
//                reader.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
