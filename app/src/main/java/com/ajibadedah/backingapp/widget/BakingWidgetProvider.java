package com.ajibadedah.backingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.ajibadedah.backingapp.MainActivity;
import com.ajibadedah.backingapp.R;
import com.ajibadedah.backingapp.model.Recipe;
import com.ajibadedah.backingapp.sync.RecipeManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {
    private static final String EXTRA_IS_NEXT = "EXTRA_IS_NEXT";
    private static final ArrayList<Recipe> recipeList = new ArrayList<>();
    static int position = 0;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);

        // Create an Intent to launch MainActivity
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget, pendingIntent);

        Intent listIntent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.widget_list, listIntent);

        Intent clickIntentTemplate = new Intent(context, MainActivity.class);
        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(clickIntentTemplate)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list, clickPendingIntentTemplate);
        views.setEmptyView(R.id.widget_list, R.id.widget_empty);



        RecipeManager.getInstance(context).getRecipes(new RecipeManager.RecipeConsumer() {
            @Override
            public void onRecipesAvailable(List<Recipe> recipes) {
                recipeList.addAll(recipes);
            }
        });

        views.setTextViewText(R.id.recipe_name, recipeList.get(position).getName());

        Intent intentSync = new Intent(context, BakingWidgetProvider.class);
        intentSync.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intentSync.putExtra(EXTRA_IS_NEXT,  position + 1);
        PendingIntent pendingSync = PendingIntent.getBroadcast(context, 0, intentSync, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.next_recipe, pendingSync);

        Intent previousIntent = new Intent(context, BakingWidgetProvider.class);
        previousIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        previousIntent.putExtra(EXTRA_IS_NEXT, position - 1);
        PendingIntent pendingPrev = PendingIntent.getBroadcast(context, 1, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.previous_recipe, pendingPrev);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);

        if (intent.hasExtra(EXTRA_IS_NEXT)) {
            position = intent.getIntExtra(EXTRA_IS_NEXT, 0);

            position = position > 3 ? 0 : position;
            position = position < 0 ? recipeList.size() - 1 : position;

            SharedPreferences preferences = context.getSharedPreferences("nextRecipe", Context.MODE_PRIVATE);
            preferences.edit().putInt("moveToNextRecipe", position).apply();

            views.setTextViewText(R.id.recipe_name, recipeList.get(position).getName());

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), BakingWidgetProvider.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

