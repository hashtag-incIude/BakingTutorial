package com.ak.bakingtutorial.adapter;


import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ak.bakingtutorial.fragments.RecipeStepSinglePageFragment;
import com.ak.bakingtutorial.models.Step;

import java.util.ArrayList;
import java.util.List;

public class SingleStepAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private List<Step> mSteps;


    public SingleStepAdapter(FragmentManager fm, List<Step> steps, Context context) {
        super(fm);
        this.mContext = context;
        this.mSteps = new ArrayList<>();
        if (steps != null) {
            Log.d("PagerAdapter", "steps is not null" );
            this.mSteps.addAll(steps);
        }
    }

    @Override
    public Fragment getItem(int position) {
        Step step = mSteps.get(position);
        String videoURL = step.getmVideoURL();
        String description = step.getmDescription();
        String imageUrl = step.getmThumbnailURL();
        Log.d("PagerAdapter", "" + description);
        return RecipeStepSinglePageFragment.newInstance(videoURL, description, imageUrl);
    }

    @Override
    public int getCount() {
        return mSteps.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Step step = mSteps.get(position);
        return "Step: " + step.getId();
    }
}
