package com.ajibadedah.backingapp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajibadedah.backingapp.model.Ingredient;
import com.ajibadedah.backingapp.model.Recipe;
import com.ajibadedah.backingapp.model.Step;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ajibade on 5/4/17
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_RECIPE_LIST = 0;
    private static final int VIEW_TYPE_INGREDIENT_STEPS = 1;
    private static final int VIEW_TYPE_STEPS = 2;
    private static final int VIEW_TYPE_INGREDIENT = 3;
    private OnRecipeClickListener listener;
    private List<Object> arrayList;

    public RecipeAdapter(List<Object> arrayList){
        this.arrayList = arrayList;
    }

    public RecipeAdapter(List<Object> arrayList, OnRecipeClickListener listener){
        this.arrayList = arrayList;
        this.listener =listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new RecipeViewHolder(itemView);
        View itemView;
        RecyclerView.ViewHolder holder;
        switch (viewType){
            case VIEW_TYPE_RECIPE_LIST:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recipe_list_item, parent, false);
                holder = new RecipeViewHolder(itemView);
                break;

            case VIEW_TYPE_STEPS:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recipe_list_item, parent, false);
                holder = new StepsViewHolder(itemView);
                break;

            case VIEW_TYPE_INGREDIENT:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recipe_list_item, parent, false);
                holder = new IngredientViewHolder(itemView);
                break;
            default:
                throw new UnsupportedOperationException("Unknown view type:");
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){
            case VIEW_TYPE_RECIPE_LIST:
                RecipeViewHolder recipeViewHolder = (RecipeViewHolder) holder;
                recipeViewHolder.bindView(position);
                break;

            case VIEW_TYPE_STEPS:
                StepsViewHolder stepViewHolder = (StepsViewHolder) holder;
                stepViewHolder.bindView(position);
                break;

            case VIEW_TYPE_INGREDIENT:
                IngredientViewHolder ingredientViewHolder = (IngredientViewHolder) holder;
                ingredientViewHolder.bindView(position);
                break;
            default:
                throw new UnsupportedOperationException("Unknown view type:");
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position) instanceof Recipe) {
            return VIEW_TYPE_RECIPE_LIST;
        } else if (arrayList.get(position) instanceof Step ||
                arrayList.get(position) instanceof String) {
            return VIEW_TYPE_STEPS;
        } else if (arrayList.get(position) instanceof Ingredient) {
            return VIEW_TYPE_INGREDIENT;
        }
//        else if (arrayList.get(position) instanceof String) {
//            return VIEW_TYPE_INGREDIENT_STEPS;
//        }
        return -1;
    }

    public interface OnRecipeClickListener {
        void onRecipeClicked(int position);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            recipeName = (TextView) itemView.findViewById(R.id.recipe_text);

        }

        void bindView(final int position){
            Recipe recipe = (Recipe) arrayList.get(position);
            String text = recipe.getName();
            recipeName.setText(text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRecipeClicked(position);
                }
            });
        }
    }


    public class StepsViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName;
        ImageView recipeImage;

        public StepsViewHolder(View itemView) {
            super(itemView);
            recipeName = (TextView) itemView.findViewById(R.id.recipe_text);
            recipeImage = (ImageView) itemView.findViewById(R.id.recipe_photo);
        }

        void bindView(final int position){
            String text;
            if (position == 0){
                text = (String) arrayList.get(position);
            } else {
                Step step = (Step) arrayList.get(position);
                text = step.getShortDescription();
                if (!step.getThumbnailURL().equals(""))
                Picasso.with(itemView.getContext()).load(step.getThumbnailURL()).into(recipeImage);
            }
            recipeName.setText(text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRecipeClicked(position);
                }
            });

        }
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView quantityView;
        TextView measureView;
        TextView ingredentView;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            quantityView = (TextView) itemView.findViewById(R.id.quantity_text);
            measureView = (TextView) itemView.findViewById(R.id.measure_text);
            ingredentView = (TextView) itemView.findViewById(R.id.ingredient_text);
            quantityView.setVisibility(View.VISIBLE);
            measureView.setVisibility(View.VISIBLE);
            ingredentView.setVisibility(View.VISIBLE);

            CardView cardView = (CardView) itemView.findViewById(R.id.card_view);
            cardView.setVisibility(View.GONE);
        }

        void bindView(final int position){
            String text;
            Ingredient ingredient = (Ingredient) arrayList.get(position);
            text = ingredient.getQuantity();
            quantityView.setText(text);
            text = ingredient.getMeasure();
            measureView.setText(text);
            text = ingredient.getIngredient();
            ingredentView.setText(text);
        }
    }


}
