package com.erayo.baking;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.erayo.baking.model.Step;

public class StepDetailActivity extends AppCompatActivity {

    public static final String POSITIVE = "positive";
    public static final String NEGATIVE = "negative";
    public static String SDA_TAG;
    private static final String STEPS = "steps";

    private Step step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        step = getIntent().getExtras().getParcelable("step");
        Bundle b = new Bundle();
        b.putParcelable("step", step);

        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setTitle(step.getShortDescription());

        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setArguments(b);
        FragmentManager fm = getSupportFragmentManager();
        if (savedInstanceState == null)
            fm.beginTransaction().add(R.id.step_detail_frame_layout, stepDetailFragment).commit();
        else
            fm.beginTransaction().replace(R.id.step_detail_frame_layout, stepDetailFragment).commit();
        SDA_TAG = POSITIVE;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STEPS, step);
        super.onSaveInstanceState(outState);
    }
}
