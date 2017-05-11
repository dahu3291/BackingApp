package com.ajibadedah.backingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ajibadedah.backingapp.IdlingResource.SimpleIdlingResource;
import com.ajibadedah.backingapp.R;
import com.ajibadedah.backingapp.model.Ingredient;
import com.ajibadedah.backingapp.model.Recipe;
import com.ajibadedah.backingapp.model.Step;
import com.ajibadedah.backingapp.sync.RecipeManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajibade on 5/7/17
 */

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int i = intent.getIntExtra("EXTRA_NEXT", 0);
        return new ListRemoteViewsFactory(this.getApplicationContext(), i);
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    ArrayList<Recipe> recipeList;
    ArrayList<Ingredient> ingredientsList;
    int recipePosition;

    public ListRemoteViewsFactory(Context applicationContext, int recipePosition) {
        mContext = applicationContext;
        this.recipePosition = recipePosition;
    }

    @Override
    public void onCreate() {
        recipeList = new ArrayList<>();
        ingredientsList = new ArrayList<>();
    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences preferences = mContext.getSharedPreferences("nextRecipe", Context.MODE_PRIVATE);
        int recipePosition = preferences.getInt("moveToNextRecipe", 0);
        RecipeManager.getInstance(mContext).getRecipes(new RecipeManager.RecipeConsumer() {
            @Override
            public void onRecipesAvailable(List<Recipe> recipes) {
                recipeList.addAll(recipes);
//                recipeAdapter.notifyDataSetChanged();
            }
        }, new SimpleIdlingResource());

        ingredientsList.clear();
        ingredientsList.addAll(recipeList.get(recipePosition).getIngredients());
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredientsList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.baking_widget_ingredient_list_item);

        views.setTextViewText(R.id.quantity_text, ingredientsList.get(position).getQuantity());
        views.setTextViewText(R.id.measure_text, ingredientsList.get(position).getMeasure());
        views.setTextViewText(R.id.ingredient_text, ingredientsList.get(position).getIngredient());
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
