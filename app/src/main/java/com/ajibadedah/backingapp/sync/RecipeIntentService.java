package com.ajibadedah.backingapp.sync;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.ajibadedah.backingapp.utility.NetworkUtils;
import com.ajibadedah.backingapp.utility.RecipeJsonUtils;

import org.json.JSONException;

import java.io.IOException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RecipeIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_RECIPE_SYNC = "com.ajibadedah.backingapp.sync.action.SYNC";
    private static final String ACTION_BAZ = "com.ajibadedah.backingapp.sync.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.ajibadedah.backingapp.sync.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.ajibadedah.backingapp.sync.extra.PARAM2";

    public RecipeIntentService() {
        super("RecipeIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionRecipe(Context context) {
        Intent intent = new Intent(context, RecipeIntentService.class);
        intent.setAction(ACTION_RECIPE_SYNC);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, RecipeIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_RECIPE_SYNC.equals(action)) {
                handleActionRecipeSync();
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionRecipeSync() {
        String json;
        try {
            json = NetworkUtils.getResponseFromHttpUrl();
            RecipeJsonUtils.getRecipe(this, json);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
