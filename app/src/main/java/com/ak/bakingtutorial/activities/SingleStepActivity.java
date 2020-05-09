package com.ak.bakingtutorial.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.ak.bakingtutorial.R;
import com.ak.bakingtutorial.adapter.SingleStepAdapter;
import com.ak.bakingtutorial.models.Step;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SingleStepActivity extends AppCompatActivity {

    public static final String STEPS = "STEPS";

    private List<Step> steps = new ArrayList<>();
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private SingleStepAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_step);
        viewPager = findViewById(R.id.view_pager);
        Intent intent = getIntent();
        int stepId = 0;
        if (intent != null && intent.hasExtra(STEPS)) {
            String mStep = intent.getStringExtra(STEPS);
            Type type = new TypeToken<List<Step>>(){}.getType();
            steps.clear();
            steps = new GsonBuilder().create().fromJson(mStep, type);
            String title = intent.getExtras().getString("current_recipe");
            setTitle(title);
            stepId = intent.getIntExtra("STEP_ID", 1);
        }

        adapter = new SingleStepAdapter(getSupportFragmentManager(), steps, this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(stepId);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Title bar back press triggers onBackPressed()
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Both navigation bar back press and title bar back press will trigger this method
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ) {
            getFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }
}
