package com.erayo.baking;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.erayo.baking.model.Recipe;

public class RecipeStepsActivity extends AppCompatActivity {

    static Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);

        if (StepDetailActivity.SDA_TAG.equals(StepDetailActivity.NEGATIVE))
            recipe = getIntent().getParcelableExtra("recipe");
        Bundle b = new Bundle();
        b.putParcelable("recipe", recipe);

        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setTitle(recipe.getName());

        RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
        recipeStepsFragment.setArguments(b);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.frame_layout_steps, recipeStepsFragment).commit();
    }
}
