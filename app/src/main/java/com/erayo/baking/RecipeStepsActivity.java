package com.erayo.baking;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.erayo.baking.model.Recipe;

public class RecipeStepsActivity extends AppCompatActivity implements IOnStepPressListener {

    static Recipe recipe;
    private static final String RECIPE_LIST = "recipe_list";

    public static boolean isTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);

        isTwoPane = findViewById(R.id.mt_dt_step_detail_frame) != null;

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
        if (savedInstanceState == null) {
            fm.beginTransaction().add(R.id.frame_layout_steps, recipeStepsFragment).commit();
        } else {
            fm.beginTransaction().replace(R.id.frame_layout_steps, recipeStepsFragment).commit();
        }

        if (isTwoPane) {
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setArguments(b);
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepDetailFragment.TAG = StepDetailFragment.EXTERNAL;
            if (savedInstanceState != null) {
                fragmentManager.beginTransaction().add(R.id.mt_dt_step_detail_frame, stepDetailFragment).commit();
            } else {
                fragmentManager.beginTransaction().replace(R.id.mt_dt_step_detail_frame, stepDetailFragment).commit();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RECIPE_LIST, recipe);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStepPressListener(int clickedItemPosition) {
        if (isTwoPane) {
            StepDetailFragment stepDetailFragment =
                    (StepDetailFragment) getSupportFragmentManager().findFragmentById(R.id.mt_dt_step_detail_frame);
            stepDetailFragment.adjustUIForTwoPane(clickedItemPosition);
        }
    }
}
