package com.ajibadedah.backingapp.model;

import java.util.ArrayList;

/**
 * Created by ajibade on 5/4/17
 */

public class Recipe {

    private String name;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;

    public Recipe(){
        //empty constructor
    }

    public String getName() {
        return name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }
}
