package com.erayo.baking;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.erayo.baking.adapter.MainScreenRecyclerViewAdapter;
import com.erayo.baking.data.JsonProvider;
import com.erayo.baking.model.Ingredients;
import com.erayo.baking.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WidgetActiviy extends AppCompatActivity implements JsonProvider.Callback, MainScreenRecyclerViewAdapter.ClickListener {

    static List<Recipe> recipes = new ArrayList<>();

    @BindView(R.id.widget_rv)
    RecyclerView recyclerView;
    MainScreenRecyclerViewAdapter recyclerViewAdapter;
    RemoteViews remoteViews;
    private AppWidgetManager widgetManager;

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);

        ButterKnife.bind(this);
        remoteViews = new RemoteViews(this.getPackageName(), R.layout.baking_widget_provider);
        widgetManager = AppWidgetManager.getInstance(this);

        if (recipes.size() == 0) {
            if (isConnected()) {
                new JsonProvider(this).execute(BuildConfig.BakingAppUrl);
            } else {
                Toast.makeText(this,
                        getResources().getString(R.string.connection_required),
                        Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            setUpRecyclerView();
        }

        if (isConnected()) {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            if (extras != null) {
                mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
            }
            if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish();
            }
        }
    }

    @Override
    public void onItemClick(int clickedItemPosition) {
        Recipe recipe = recipes.get(clickedItemPosition);
        List<Ingredients> ingredients = recipe.getIngredients();
        StringBuilder builder = new StringBuilder();
        builder.append(recipe.getName()).append("\n\n");
        for (Ingredients ing : ingredients) {
            builder.append(String.valueOf(ing.getQuantity()))
                    .append(" ")
                    .append(ing.getMeasure())
                    .append(" ")
                    .append(ing.getIngredient())
                    .append("\n");
        }
        remoteViews.setTextViewText(R.id.widget_tv, builder.toString());

        widgetManager.updateAppWidget(mAppWidgetId, remoteViews);
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    public void onResponse(List<Recipe> list) {
        recipes = list;
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL));
        recyclerViewAdapter = new MainScreenRecyclerViewAdapter(recipes, this, this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
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
    protected void onPause() {
        super.onPause();
        mBundleRecyclerViewState = new Bundle();
        mListState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mBundleRecyclerViewState != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                    recyclerView.getLayoutManager().onRestoreInstanceState(mListState);
                }
            }, 50);
        }
    }
}
