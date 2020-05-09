package com.ak.bakingtutorial.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ak.bakingtutorial.R;
import com.ak.bakingtutorial.RecipeLoader;
import com.ak.bakingtutorial.activities.RecipeStepsActivity;
import com.ak.bakingtutorial.adapter.RecipeListAdapter;
import com.ak.bakingtutorial.models.Recipe;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RecipeFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Recipe>> {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.recipe_list_progress_bar)
    ProgressBar progressBar;

    private static final int RECIPE_LOADER_ID = 1;
    private static final String JSON_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private List<Recipe> mRecipe;
    private Context mContext;
    private RecyclerView.LayoutManager layoutManager;
    private boolean mTwoPane;
    private DisplayMetrics displayMetrics;
    private WindowManager windowmanager;
    private SharedPreferences preferences;

    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        progressBar.setVisibility(View.VISIBLE);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());


        displayMetrics = new DisplayMetrics();
        windowmanager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        float scaleFactor = displayMetrics.density;
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;
        float widthDp = deviceWidth / scaleFactor;
        float heightDp = deviceHeight / scaleFactor;
        float smallestWidth = Math.min(widthDp, heightDp);

        mRecyclerView.setHasFixedSize(true);
        if (smallestWidth >= 600 || this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(getContext(), numberOfColumns());
        } else {
            mTwoPane = false;
            layoutManager = new LinearLayoutManager(mContext);
        }
        mRecyclerView.setLayoutManager(layoutManager);

        if (isNetworkAvailable()) {
            getLoaderManager().initLoader(RECIPE_LOADER_ID, null, this);
        }
        return view;
    }

    private int numberOfColumns() {
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(JSON_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();


        return new RecipeLoader(getContext(), uriBuilder.toString(), preferences);
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> recipes) {
        progressBar.setVisibility(View.GONE);
        mRecipe = recipes;
        RecipeListAdapter listAdapter = new RecipeListAdapter((ArrayList<Recipe>) mRecipe, mContext, new RecipeListAdapter.OnStepClickListener() {
            @Override
            public void onStepSelected(View v, int position, int id) {
                Intent intent = new Intent(mContext, RecipeStepsActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("current_recipe", mRecipe.get(position).getName());
                intent.putExtra("recipe", new GsonBuilder().create().toJson(mRecipe));
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(listAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {

    }
}
