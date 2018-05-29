package com.erayo.baking;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.erayo.baking.model.Step;

import java.util.ArrayList;
import java.util.List;

public class StepDetailActivity extends AppCompatActivity {

    public static final String POSITIVE = "positive";
    public static final String NEGATIVE = "negative";
    public static String SDA_TAG;
    private static final String STEPS = "steps";

    private Step step;

    public List<Step> stepList;
    public int clickedItemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        stepList = getIntent().getExtras().getParcelableArrayList("steps");
        clickedItemPosition = getIntent().getExtras().getInt("clickedItemPosition");
        step = stepList.get(clickedItemPosition);
        Bundle b = new Bundle();
        b.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) stepList);
        b.putInt("clickedItemPosition", clickedItemPosition);
        b.putParcelable("step", step);

        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setArguments(b);
        FragmentManager fm = getSupportFragmentManager();
        if (savedInstanceState == null)
            fm.beginTransaction().add(R.id.step_detail_frame_layout, stepDetailFragment).commit();
        SDA_TAG = POSITIVE;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STEPS, step);
        super.onSaveInstanceState(outState);
    }

    public void setActionBarTitle(Step step) {
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setTitle(step.getShortDescription());
    }
}
