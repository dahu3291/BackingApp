package com.ajibadedah.backingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StepViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        String jsonSteps = getIntent().getStringExtra("steps");
        int position = getIntent().getIntExtra("position", 0);
        StepViewFragment stepViewFragment = StepViewFragment.newInstance(jsonSteps, position, false);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_container, stepViewFragment, "StepViewFragment")
                .commit();
    }
}
