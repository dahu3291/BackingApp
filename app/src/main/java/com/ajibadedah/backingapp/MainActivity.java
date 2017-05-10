package com.ajibadedah.backingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity
        implements RecipeListFragment.OnFragmentInteractionListener,
        RecipeStepListFragment.OnStepClickedListener{

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Determine if you're creating a two-pane or single-pane display
        if(findViewById(R.id.step_container) != null) {
            mTwoPane = true;

        } else {
            mTwoPane = false;
        }

        RecipeListFragment newFragment = RecipeListFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, newFragment, "RecipeListFragment")
                .addToBackStack("RecipeListFragment")
                .commit();


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

    @Override
    public void onFragmentInteraction(String recipeJson) {
        if (mTwoPane) {
            RecipeStepListFragment newFragment = RecipeStepListFragment.newInstance(recipeJson, true, true);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, newFragment, "RecipeStepListFragment")
                    .addToBackStack("RecipeStepListFragment")
                    .commit();
        } else {
            RecipeStepListFragment newFragment = RecipeStepListFragment.newInstance(recipeJson, false, false);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, newFragment, "RecipeStepListFragment")
                    .addToBackStack("RecipeStepListFragment")
                    .commit();
        }

    }

    @Override
    public void onStepClicked(String json, int position) {
        if (mTwoPane){
            if (position == -1) {
                RecipeStepListFragment secondFragment = RecipeStepListFragment.newInstance(json, true, false);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.step_container, secondFragment, "RecipeStepListFragmentSecond")
                        .addToBackStack("RecipeListFragmentSecond")
                        .commit();
            } else {
                StepViewFragment stepViewFragment = StepViewFragment.newInstance(json, position, true);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.step_container, stepViewFragment, "StepViewFragment")
                        .commit();
            }
        } else {
            Intent intent = new Intent(this, StepViewActivity.class);
            intent.putExtra("steps", json);
            intent.putExtra("position", position);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        boolean shouldCallSuper = true;
        if (!mTwoPane) {
            RecipeStepListFragment newFragment = (RecipeStepListFragment)
                    getSupportFragmentManager().findFragmentByTag("RecipeStepListFragment");

            if (newFragment != null) {
                shouldCallSuper = newFragment.changeView();
            }
        }

        if(shouldCallSuper) super.onBackPressed();
    }

}
