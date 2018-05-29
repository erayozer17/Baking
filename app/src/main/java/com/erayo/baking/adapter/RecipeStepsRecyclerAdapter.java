package com.erayo.baking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.erayo.baking.R;
import com.erayo.baking.model.Step;

import java.util.List;

public class RecipeStepsRecyclerAdapter extends RecyclerView.Adapter<RecipeStepsRecyclerAdapter.ViewHolder> {

    private List<Step> stepList;
    private LayoutInflater mInflater;
    final private ClickListener clickListener;

    public RecipeStepsRecyclerAdapter(List<Step> stepList, ClickListener clickListener, Context context) {
        this.stepList = stepList;
        this.clickListener = clickListener;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recipe_steps_recyclerview_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Step step = stepList.get(position);
        String stepContent = step.getShortDescription();
        holder.listingNumber.setText(String.valueOf(position + 1));
        holder.stepContent.setText(stepContent);
        if (!step.getVideoUrl().equals("")) {
            holder.videoProvided_iv.setVisibility(View.VISIBLE);
        } else {
            holder.videoProvided_iv.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView listingNumber;
        TextView stepContent;
        ImageView videoProvided_iv;

        private ViewHolder(View itemView) {
            super(itemView);
            listingNumber = itemView.findViewById(R.id.list_number_tv);
            stepContent = itemView.findViewById(R.id.step_content_tv);
            videoProvided_iv = itemView.findViewById(R.id.videoProvided);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            clickListener.onItemClick(clickedPosition);
        }
    }

    public interface ClickListener {
        void onItemClick(int clickedItemPosition);
    }
}
