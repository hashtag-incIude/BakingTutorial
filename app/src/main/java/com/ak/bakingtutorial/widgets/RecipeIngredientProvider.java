package com.ak.bakingtutorial.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.ak.bakingtutorial.R;
import com.ak.bakingtutorial.activities.RecipeStepsActivity;
import com.ak.bakingtutorial.models.Recipe;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientProvider extends AppWidgetProvider {

    private static String mIngredients;
    private static String mRecipeName;
    private static ArrayList<Recipe> mRecipe;
    private static int stepId;

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int[] appWidgetId, String ingredients, String recipeName,
                                       ArrayList<Recipe> recipes, int id) {

        mRecipeName = recipeName;
        mIngredients = ingredients;
        mRecipe = recipes;
        stepId = id;
        Intent intent = new Intent(context, RecipeStepsActivity.class);
        intent.putExtra("current_recipe", recipeName);
        intent.putExtra("recipe", new GsonBuilder().create().toJson(mRecipe));
        intent.putExtra("id", stepId);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredient_provider);

        views.setTextViewText(R.id.widget_recipe_name, recipeName);
        views.setTextViewText(R.id.widget_ingredients, ingredients);

        views.setOnClickPendingIntent(R.id.recipe_linear_layout, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, new int[]{appWidgetId}, mIngredients, mRecipeName, mRecipe, stepId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

