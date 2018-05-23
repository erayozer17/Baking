package com.erayo.baking;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.erayo.baking.adapter.MainScreenRecyclerViewAdapter;
import com.erayo.baking.data.JsonProvider;
import com.erayo.baking.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements JsonProvider.Callback, MainScreenRecyclerViewAdapter.ClickListener{

    static List<Recipe> recipes = new ArrayList<>();
    private static final String RECIPES_LIST = "recipes_list";

    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;
    MainScreenRecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (savedInstanceState != null)
            recipes = savedInstanceState.getParcelableArrayList(RECIPES_LIST);

        if (recipes.size() == 0){
            if (isConnected()) {
                new JsonProvider(this).execute(BuildConfig.BakingAppUrl);
            }
            else {
                Toast.makeText(this,
                        getResources().getString(R.string.connection_required),
                        Toast.LENGTH_LONG).show();
            }
        } else {
            setUpRecyclerView();
        }

    }

    @Override
    public void onResponse(List<Recipe> list) {
        recipes = list;
        setUpRecyclerView();
    }

    private void setUpRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL));
        recyclerViewAdapter = new MainScreenRecyclerViewAdapter(recipes, this, this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int clickedItemPosition) {
        Recipe recipe = recipes.get(clickedItemPosition);
        Intent i = new Intent(MainActivity.this, RecipeStepsActivity.class);
        i.putExtra("recipe", recipe);
        StepDetailActivity.SDA_TAG = StepDetailActivity.NEGATIVE;
        startActivity(i);
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null)
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPES_LIST, (ArrayList<? extends Parcelable>) recipes);
    }
}
