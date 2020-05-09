package com.ak.bakingtutorial.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.ak.bakingtutorial.JsonFormatter;
import com.ak.bakingtutorial.models.Ingredients;
import com.ak.bakingtutorial.models.Recipe;

import java.util.ArrayList;


public class RecipeWidgetUpdateService extends IntentService {

    public static final String UPDATE_RECIPE_INGREDIENTS =
            "com.ak.bakingtutorial.action.update_recipe";

    private ArrayList<Ingredients> mIngredients;
    private static ArrayList<Recipe> mRecipe;
    private static Context mContext;
    private static int id;
    private static String mRecipeName;

    public RecipeWidgetUpdateService() {
        super("RecipeWidgetUpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (UPDATE_RECIPE_INGREDIENTS.equals(action)) {
                handleActionUpdateRecipe();
            }
        }

    }

    private void handleActionUpdateRecipe() {
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

        String ingredient = ingredientString.toString();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeIngredientProvider.class));
        RecipeIngredientProvider.updateAppWidget(this, appWidgetManager, appWidgetIds, ingredient, mRecipeName, mRecipe, id);
    }

    public static void startActionUpdateRecipe(Context context, int recipeId, String recipeName, ArrayList<Recipe> recipes) {
        mContext = context;
        id = recipeId;
        mRecipeName = recipeName;
        mRecipe = recipes;
        Intent intent = new Intent(context, RecipeWidgetUpdateService.class);
        intent.setAction(UPDATE_RECIPE_INGREDIENTS);
        context.startService(intent);
    }
}
