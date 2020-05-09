package com.ak.bakingtutorial;


import android.content.Context;
import android.content.SharedPreferences;

import androidx.loader.content.AsyncTaskLoader;

public class RecipeLoader extends AsyncTaskLoader {

    private final String mUrl;
    private SharedPreferences mPreference;

    public RecipeLoader(Context context, String url, SharedPreferences preferences) {
        super(context);
        mUrl = url;
        mPreference = preferences;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Object loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        return JsonFormatter.fetchRecipeData(mUrl, mPreference);
    }
}
