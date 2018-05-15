package com.erayo.baking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erayo.baking.R;
import com.erayo.baking.model.Recipe;

import java.util.List;

public class MainScreenRecyclerViewAdapter extends RecyclerView.Adapter<MainScreenRecyclerViewAdapter.ViewHolder>{

    private List<Recipe> recipeList;
    private LayoutInflater mInflater;
    final private ClickListener clickListener;

    public MainScreenRecyclerViewAdapter(List<Recipe> recipeList, ClickListener clickListener, Context context){
        this.recipeList = recipeList;
        this.clickListener = clickListener;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.main_screen_adapter_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        String name = recipe.getName();
        String serves = "Serves " + String.valueOf(recipe.getServings());

        holder.pastryName.setText(name);
        holder.serves.setText(serves);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView pastryName;
        TextView serves;

        private ViewHolder(View itemView) {
            super(itemView);
            pastryName = itemView.findViewById(R.id.tv_pastryName);
            serves = itemView.findViewById(R.id.tv_serves);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            clickListener.onItemClick(clickedPosition);
        }
    }

    public interface ClickListener{
        void onItemClick(int clickedItemPosition);
    }
}
