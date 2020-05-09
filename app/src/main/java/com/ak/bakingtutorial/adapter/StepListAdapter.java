package com.ak.bakingtutorial.adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ak.bakingtutorial.R;
import com.ak.bakingtutorial.activities.SingleStepActivity;
import com.ak.bakingtutorial.models.Step;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.ViewHolder> {
    private List<Step> mSteps;
    private int mStepId;
    private int mRecipeId;
    private Context mContext;
    private String mName;
    OnStepListener mListener;

    public interface OnStepListener {
        void onStepSelected(View v, int position);
    }

    public StepListAdapter(ArrayList<Step> mSteps, OnStepListener listener, String recipeName, int recipeId) {
        this.mSteps = new ArrayList<>();
        this.mSteps.addAll(mSteps);
        this.mListener = listener;
        this.mName = recipeName;
        this.mRecipeId = recipeId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        StringBuilder sb = new StringBuilder();
        sb.append(mSteps.get(position).getId());
        sb.append(". ");
        sb.append(mSteps.get(position).getShortDescription());

        holder.stepTextView.setText(sb);

        holder.stepTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStepId = mSteps.get(position).getId();
                Log.d("StepListAdapter", "StepID: " + mStepId + "RecipeID: " + mRecipeId);
                mListener.onStepSelected(view, position);

                    Intent intent = new Intent(mContext, SingleStepActivity.class);
                    Type type = new TypeToken<List<Step>>() {
                    }.getType();
                    String steps = new GsonBuilder().create().toJson(mSteps, type);
                    intent.putExtra(SingleStepActivity.STEPS, steps);
                    intent.putExtra("current_recipe", mName);
                    intent.putExtra("STEP_ID", mStepId);
                    mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView stepTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            stepTextView = itemView.findViewById(R.id.step_text_view);
        }
    }
}
