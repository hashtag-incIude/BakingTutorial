package com.ak.bakingtutorial.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ak.bakingtutorial.JsonFormatter;
import com.ak.bakingtutorial.R;
import com.ak.bakingtutorial.adapter.StepListAdapter;
import com.ak.bakingtutorial.adapter.StepListAdapterForTwoPane;
import com.ak.bakingtutorial.models.Ingredients;
import com.ak.bakingtutorial.models.Step;

import java.util.ArrayList;

public class StepsListFragment extends Fragment implements StepListAdapter.OnStepListener, StepListAdapterForTwoPane.OnStepClickListener {

    private static final String STRING_RECIPE = "recipe";
    private static final String STEPS_POSITION = "STEPS_POSITION";
    private static String TWOPANE = "two_pane";
    private int stepsPosition = -1;

    private ArrayList<Ingredients> mIngredients;
    private ArrayList<Step> mSteps;
    private int id = 0;
    private TextView ingredientTextView;
    private RecyclerView mStepRecyclerView;
    private String recipeName = null;
    private String sRecipe;
    private boolean mTwoPane;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(STRING_RECIPE)) {
            sRecipe = getArguments().getString(STRING_RECIPE);
            mTwoPane = getArguments().getBoolean(TWOPANE);
        }
    }

    public static StepsListFragment newInstance(String sRecipe, boolean twoPane){
        StepsListFragment fragment = new StepsListFragment();
        Bundle args = new Bundle();
        args.putString(STRING_RECIPE, sRecipe);
        args.putBoolean(TWOPANE, twoPane);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STEPS_POSITION, stepsPosition);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        Intent intent = getActivity().getIntent();
        final Context mContext = getContext();
        if (savedInstanceState != null){
            stepsPosition = savedInstanceState.getInt(STEPS_POSITION);
        }
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
            recipeName = intent.getStringExtra("current_recipe");
        }


        StringBuilder ingredientString = new StringBuilder();
        int quantity;
        String ingredientName;
        String measure;
        mIngredients = JsonFormatter.extractIngredientsFromJson(id);
        for (int i = 0; i < mIngredients.size(); i++) {
            ingredientName = mIngredients.get(i).getmIngredient();
            quantity = mIngredients.get(i).getmQuantity();
            measure = mIngredients.get(i).getmMeasure();
            ingredientString.append("\u25CF ");
            ingredientString.append(ingredientName);
            ingredientString.append(" (");
            ingredientString.append(quantity);
            ingredientString.append(" ");
            ingredientString.append(measure);
            ingredientString.append(")");
            ingredientString.append("\n");
        }
        ingredientTextView = view.findViewById(R.id.ingredients);
        ingredientTextView.setText(ingredientString.toString());

        mSteps = JsonFormatter.extractStepsFromJson(id);
        mStepRecyclerView = view.findViewById(R.id.step_recycler_view);
        mStepRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mStepRecyclerView.setLayoutManager(layoutManager);
        mStepRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        if (mTwoPane){
            StepListAdapterForTwoPane listAdapter = new StepListAdapterForTwoPane(mSteps,this,recipeName, id);
            mStepRecyclerView.setAdapter(listAdapter);
        }else {
            StepListAdapter listAdapter = new StepListAdapter(mSteps, this, recipeName, id);
            mStepRecyclerView.setAdapter(listAdapter);
        }


        if (stepsPosition != -1)
            mStepRecyclerView.smoothScrollToPosition(stepsPosition);

        return view;
    }

    @Override
    public void onStepSelected(View v, int position) {
        stepsPosition = position;
    }

    @Override
    public void onClickStepSelected(View v, int position, String videoUrl, String description, String imageUrl) {
        RecipeStepSinglePageFragment singlePageFragment =
                RecipeStepSinglePageFragment.newInstance(videoUrl, description, imageUrl, mTwoPane);

        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container2, singlePageFragment, RecipeStepSinglePageFragment.class.getSimpleName());
        ft.commit();
    }


}
