package com.ajibadedah.backingapp;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity
        implements RecipeListFragment.OnFragmentInteractionListener,
        RecipeStepListFragment.OnStepClickedListener, FragmentManager.OnBackStackChangedListener{

    private static final String CURRENT_MAIN_FRAGMENT_TAG = "CURRENT_MAIN_FRAGMENT_TAG";
    private static final String CURRENT_DETAIL_FRAGMENT_TAG = "CURRENT_DETAIL_FRAGMENT_TAG";
    private static final String RECIPE_LIST_FRAGMENT_TAG = "RecipeListFragment";
    private static final String RECIPE_STEP_LIST_FRAGMENT_TAG = "RecipeStepListFragment";
    private static final String RECIPE_STEP_LIST_FRAGMENT_SECOND_TAG = "RecipeStepListFragmentSecond";
    private static final String STEP_VIEW_FRAGMENT_TAG = "StepViewFragment";
    private static final String RECIPE_CLICKED_JSON = "recipeClickedJson";
    private static final String STEP_CLICKED_JSON = "stepClickedJson";
    private static final String STEP_CLICKED_POSITION = "stepClickedPosition";
    private boolean mTwoPane;
    private String currentMainFragmentTag;
    private String currentDetailFragmentTag;
    private String recipeClickedJson;
    private String stepClickedJson;
    private int stepClickedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Determine if you're creating a two-pane or single-pane display
        mTwoPane = findViewById(R.id.step_container) != null ;

        if (savedInstanceState != null){
            currentMainFragmentTag = savedInstanceState.getString(CURRENT_MAIN_FRAGMENT_TAG);
            currentDetailFragmentTag = savedInstanceState.getString(CURRENT_DETAIL_FRAGMENT_TAG);
            recipeClickedJson = savedInstanceState.getString(RECIPE_CLICKED_JSON);
            stepClickedJson = savedInstanceState.getString(STEP_CLICKED_JSON);
            stepClickedPosition = savedInstanceState.getInt(STEP_CLICKED_POSITION);
        } else {
            getSupportFragmentManager().addOnBackStackChangedListener(this);
            RecipeListFragment newFragment = RecipeListFragment.newInstance(mTwoPane);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, newFragment, RECIPE_LIST_FRAGMENT_TAG)
                    .addToBackStack(RECIPE_LIST_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        Fragment mainFragment;
        switch (currentMainFragmentTag){
            case RECIPE_LIST_FRAGMENT_TAG:
                mainFragment = RecipeListFragment.newInstance(mTwoPane);
                break;
            case RECIPE_STEP_LIST_FRAGMENT_TAG:
                mainFragment = mTwoPane ?
                        RecipeStepListFragment.newInstance(recipeClickedJson, true, true):
                        RecipeStepListFragment.newInstance(recipeClickedJson, false, false);
                break;
            default:throw new UnsupportedOperationException(currentMainFragmentTag +
                    "Should not be in main_container");
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, mainFragment, currentMainFragmentTag)
                .addToBackStack(currentMainFragmentTag)
                .commit();

        Fragment detailFragment;
        if (mTwoPane && currentDetailFragmentTag != null){
            switch (currentDetailFragmentTag){
                case RECIPE_STEP_LIST_FRAGMENT_SECOND_TAG:
                    detailFragment = RecipeStepListFragment.newInstance(stepClickedJson, true, false);
                    break;
                case STEP_VIEW_FRAGMENT_TAG:
                    detailFragment = StepViewFragment.newInstance(stepClickedJson, stepClickedPosition, true);
                    break;
                default:throw new UnsupportedOperationException(currentDetailFragmentTag +
                        "Should not be in step_container");
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_container, detailFragment, currentDetailFragmentTag)
                    .addToBackStack(currentDetailFragmentTag)
                    .commit();
        }
    }

    @Override
    public void onBackStackChanged() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
        if (fragment != null ){
            currentMainFragmentTag = fragment.getTag();

            if (mTwoPane) {
                if (currentMainFragmentTag.equals(RECIPE_LIST_FRAGMENT_TAG)) {
                    ConstraintSet set = new ConstraintSet();
                    ConstraintLayout mConstraintLayout = (ConstraintLayout) findViewById(R.id.activity_main);
                    set.clone(mConstraintLayout);
                    set.setGuidelinePercent(R.id.divider, 1f);
                    set.applyTo(mConstraintLayout);
                } else {
                    ConstraintSet set = new ConstraintSet();
                    ConstraintLayout mConstraintLayout = (ConstraintLayout) findViewById(R.id.activity_main);
                    set.clone(mConstraintLayout);
                    set.setGuidelinePercent(R.id.divider, 0.4f);
                    set.applyTo(mConstraintLayout);
                }
            }
        }

        Fragment detailFragment = getSupportFragmentManager().findFragmentById(R.id.step_container);
        if (detailFragment != null && mTwoPane){
            currentDetailFragmentTag = detailFragment.getTag();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(CURRENT_MAIN_FRAGMENT_TAG, currentMainFragmentTag);
        outState.putString(CURRENT_DETAIL_FRAGMENT_TAG, currentDetailFragmentTag);
        outState.putString(RECIPE_CLICKED_JSON, recipeClickedJson);
        outState.putString(STEP_CLICKED_JSON, stepClickedJson);
        outState.putInt(STEP_CLICKED_POSITION, stepClickedPosition);
    }

    @Override
    public void onRecipeClicked(String recipeJson) {
        recipeClickedJson = recipeJson;
        if (mTwoPane) {
            RecipeStepListFragment newFragment = RecipeStepListFragment.newInstance(recipeJson, true, true);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, newFragment, RECIPE_STEP_LIST_FRAGMENT_TAG)
                    .addToBackStack(RECIPE_STEP_LIST_FRAGMENT_TAG)
                    .commit();
            Fragment emptyFragment = new Fragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_container, emptyFragment)
                    .commit();
        } else {
            RecipeStepListFragment newFragment = RecipeStepListFragment.newInstance(recipeJson, false, false);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, newFragment, RECIPE_STEP_LIST_FRAGMENT_TAG)
                    .addToBackStack(RECIPE_STEP_LIST_FRAGMENT_TAG)
                    .commit();
        }

    }

    @Override
    public void onStepClicked(String json, int position) {
        stepClickedJson = json;
        stepClickedPosition = position;
        if (mTwoPane){
            if (position == -1) {
                RecipeStepListFragment secondFragment = RecipeStepListFragment.newInstance(json, true, false);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.step_container, secondFragment, RECIPE_STEP_LIST_FRAGMENT_SECOND_TAG)
                        .addToBackStack(RECIPE_STEP_LIST_FRAGMENT_SECOND_TAG)
                        .commit();
            } else {
                StepViewFragment stepViewFragment = StepViewFragment.newInstance(json, position, true);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.step_container, stepViewFragment, STEP_VIEW_FRAGMENT_TAG)
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
                    getSupportFragmentManager().findFragmentByTag(RECIPE_STEP_LIST_FRAGMENT_TAG);

            if (newFragment != null) {
                shouldCallSuper = newFragment.changeView();
            }
        } else {
//            if(currentMainFragmentTag.equals(RECIPE_LIST_FRAGMENT_TAG)) finish();

            if(currentMainFragmentTag.equals(RECIPE_STEP_LIST_FRAGMENT_TAG)){
                shouldCallSuper = false;
                RecipeListFragment newFragment = RecipeListFragment.newInstance(mTwoPane);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_container, newFragment, RECIPE_LIST_FRAGMENT_TAG)
                        .addToBackStack(RECIPE_LIST_FRAGMENT_TAG)
                        .commit();

            }
        }

        if(shouldCallSuper) super.onBackPressed();
    }

}
