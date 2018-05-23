package com.erayo.baking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.erayo.baking.adapter.RecipeStepsRecyclerAdapter;
import com.erayo.baking.model.Ingredients;
import com.erayo.baking.model.Recipe;
import com.erayo.baking.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsFragment extends Fragment {

    @BindView(R.id.recipe_steps_rv)
    RecyclerView recyclerView;
    @BindView(R.id.ingredients_tv)
    TextView tv_ingredients;
    List<Step> steps;
    public RecipeStepsFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_steps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        List<Ingredients> ingredients;
        Recipe recipe = getArguments().getParcelable("recipe");
        steps = recipe.getSteps();
        initRecyclerView();
        
        ingredients = recipe.getIngredients();
        String ingredientsAppended = "INGREDIENTS" + "\n\n";
        if (ingredients == null){
            ingredientsAppended = "Not Available";
        } else {
            for (int a = 0 ; a < ingredients.size() ; a++) {
                Ingredients i = ingredients.get(a);
                ingredientsAppended += String.valueOf(i.getQuantity()) + " " +
                        i.getMeasure() + " " +
                        i.getIngredient();
                if (a+1 != ingredients.size()){
                    ingredientsAppended += '\n';
                }
            }
        }
        tv_ingredients.setText(ingredientsAppended);
        if(savedInstanceState != null){
            recyclerView.scrollToPosition(savedInstanceState.getInt("position"));
        }
        StepDetailFragment.mVideoPosition = 0;
        StepDetailFragment.isAlreadyPlaying = false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("position", recyclerView.getVerticalScrollbarPosition());
        super.onSaveInstanceState(outState);
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        RecipeStepsRecyclerAdapter recipeStepsRecyclerAdapter =
                new RecipeStepsRecyclerAdapter(steps, new RecipeStepsRecyclerAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int clickedItemPosition) {
                        Intent intentToStepDetail = new Intent(getActivity(), StepDetailActivity.class);
                        Step step = steps.get(clickedItemPosition);
                        intentToStepDetail.putExtra("step", step);
                        startActivity(intentToStepDetail);
                    }
                }, getContext());
        recyclerView.setAdapter(recipeStepsRecyclerAdapter);
        recipeStepsRecyclerAdapter.notifyDataSetChanged();
    }
}
