package com.erayo.baking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements JsonProvider.Callback, MainScreenRecyclerViewAdapter.ClickListener{

    static List<Recipe> recipes = new ArrayList<>();

    RecyclerView recyclerView;
    MainScreenRecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.main_recycler_view);

        if (isConnected()) {
            new JsonProvider(this).execute(BuildConfig.BakingAppUrl);
        }
        else {
            Toast.makeText(this,
                    getResources().getString(R.string.connection_required),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResponse(List<Recipe> list) {
        recipes = list;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL));
        recyclerViewAdapter = new MainScreenRecyclerViewAdapter(recipes, this, this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int clickedItemPosition) {
        Toast.makeText(this,
                String.valueOf(clickedItemPosition),
                Toast.LENGTH_LONG).show();
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null)
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
