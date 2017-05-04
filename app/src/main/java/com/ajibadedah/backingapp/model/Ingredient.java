package com.ajibadedah.backingapp.model;

/**
 * Created by ajibade on 5/4/17
 */

public class Ingredient {

    private String quantity;
    private String measure;
    private String ingredient;

    public Ingredient(){
        //empty constructor
    }

    public Ingredient(String quantity, String measure, String ingredient){
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }
}
