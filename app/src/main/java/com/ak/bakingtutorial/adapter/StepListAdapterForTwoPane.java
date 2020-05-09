package com.ak.bakingtutorial.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ak.bakingtutorial.R;
import com.ak.bakingtutorial.models.Step;

import java.util.ArrayList;
import java.util.List;

public class StepListAdapterForTwoPane extends RecyclerView.Adapter<StepListAdapterForTwoPane.ViewHolder> {

    private List<Step> mSteps;
    private int mStepId;
    private int mRecipeId;
    private Context mContext;
    private String mName;
    private Boolean mTwoPane;
    OnStepClickListener mListener;

    public interface OnStepClickListener{
        void onClickStepSelected(View v, int position, String videoUrl, String description, String imageUrl);
    }

    public StepListAdapterForTwoPane(ArrayList<Step> mSteps, OnStepClickListener listener, String recipeName, int recipeId) {
        this.mSteps = new ArrayList<>();
        this.mSteps.addAll(mSteps);
        this.mListener = listener;
        this.mName = recipeName;
        this.mRecipeId = recipeId;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.step_list, parent, false);

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
                String videoUrl = mSteps.get(position).getmVideoURL();
                String description = mSteps.get(position).getmDescription();
                String imageUrl = mSteps.get(position).getmThumbnailURL();
                mListener.onClickStepSelected(view, position, videoUrl, description, imageUrl);
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
