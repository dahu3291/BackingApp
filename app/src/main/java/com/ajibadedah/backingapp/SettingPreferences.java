package com.ajibadedah.backingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ajibade on 5/4/17
 */

public class SettingPreferences {
    public static void setRecipeJson(Context context, String serializedRecipes, String position) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(context.getString(R.string.recipes_json_key) + position, serializedRecipes);
        editor.apply();
    }

    public static String getRecipeJson(Context context, String position) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String key = context.getString(R.string.recipes_json_key) + position;
        String defaultVal = "";

        return sp.getString(key, defaultVal);
    }
}
