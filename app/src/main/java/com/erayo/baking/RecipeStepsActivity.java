package com.erayo.baking;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.erayo.baking.model.Recipe;

public class RecipeStepsActivity extends AppCompatActivity {

    static Recipe recipe;
    Menu mMenu;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_widget, mMenu);
        mMenu = menu;

        mMenu.add(Menu.NONE,
                R.string.add_widget,
                Menu.NONE,
                R.string.add_widget)
                .setVisible(true)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case (R.string.add_widget):
                //////////////////////////////////////////////todo widget buraya basınca eklenecek
                Toast.makeText(getApplicationContext(), "Widget Set", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
