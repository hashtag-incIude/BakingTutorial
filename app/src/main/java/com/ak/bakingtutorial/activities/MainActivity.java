package com.ak.bakingtutorial.activities;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ak.bakingtutorial.R;
import com.ak.bakingtutorial.fragments.RecipeFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecipeFragment();
    }
    private void initRecipeFragment(){
        RecipeFragment rf = new RecipeFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment_container, rf, RecipeFragment.class.getSimpleName());
        ft.commit();
    }
}
