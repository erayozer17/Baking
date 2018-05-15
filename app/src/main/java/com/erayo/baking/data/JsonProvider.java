package com.erayo.baking.data;

import android.content.Context;
import android.os.AsyncTask;

import com.erayo.baking.model.Ingredients;
import com.erayo.baking.model.Recipe;
import com.erayo.baking.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JsonProvider extends AsyncTask<String, Void, List<Recipe>> {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String INGREDIENTS = "ingredients";
    private static final String INGREDIENT = "ingredient";
    private static final String STEPS = "steps";
    private static final String SERVINGS = "servings";
    private static final String IMAGE = "image";
    private static final String MEASURE = "measure";
    private static final String QUANTITY = "quantity";
    private static final String DESCRIPTION = "description";
    private static final String SHORT_DESCRIPTION = "shortDescription";
    private static final String THUMBNAIL_URL = "thumbnailURL";
    private static final String VIDEO_URL = "videoURL";

    private Callback callback;

    public JsonProvider(Context context) {
        if (context instanceof Callback) {
            this.callback = (Callback) context;
        }
    }

    @Override
    protected List<Recipe> doInBackground(String... urls) {
        List<Recipe> list = new ArrayList<>();
        Recipe recipe;
        if (urls.length == 0) {
            return null;
        }

        URL url = null;
        try {
            url = new URL(urls[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            String plainJsonResponse = null;
            if (url != null) {
                plainJsonResponse = getResponseFromHttpUrl(url);
            }

            JSONArray jsonArray = new JSONArray(plainJsonResponse);

            for (int i = 0 ; i < jsonArray.length() ; i++){
                recipe = new Recipe();
                recipe.setId(jsonArray.getJSONObject(i).optInt(ID));
                recipe.setImage(jsonArray.getJSONObject(i).optString(IMAGE));
                recipe.setServings(jsonArray.getJSONObject(i).optInt(SERVINGS));
                recipe.setName(jsonArray.getJSONObject(i).optString(NAME));
                recipe.setIngredients(prepareIngredients(jsonArray.getJSONObject(i)));
                recipe.setSteps(prepareSteps(jsonArray.getJSONObject(i)));
                list.add(recipe);
            }

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return list;
    }

    @Override
    protected void onPostExecute(List<Recipe> list) {
        callback.onResponse(list);
    }

    private String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    private List<Ingredients> prepareIngredients(JSONObject object) throws JSONException{
        JSONArray jsonArray = object.getJSONArray(INGREDIENTS);
        Ingredients ingredients;
        List<Ingredients> list = new ArrayList<>();
        for(int i = 0 ; i < jsonArray.length() ; i++){
            ingredients = new Ingredients();
            ingredients.setIngredient(jsonArray.getJSONObject(i).optString(INGREDIENT));
            ingredients.setMeasure(jsonArray.getJSONObject(i).optString(MEASURE));
            ingredients.setQuantity(jsonArray.getJSONObject(i).optInt(QUANTITY));
            list.add(ingredients);
        }
        return list;
    }

    private List<Step> prepareSteps(JSONObject object) throws JSONException{
        JSONArray jsonArray = object.getJSONArray(STEPS);
        Step steps;
        List<Step> list = new ArrayList<>();
        for(int i = 0 ; i < jsonArray.length() ; i++){
            steps = new Step();
            steps.setId(jsonArray.getJSONObject(i).optInt(ID));
            steps.setDescription(jsonArray.getJSONObject(i).optString(DESCRIPTION));
            steps.setShortDescription(jsonArray.getJSONObject(i).optString(SHORT_DESCRIPTION));
            steps.setThumbnailUrl(jsonArray.getJSONObject(i).optString(THUMBNAIL_URL));
            steps.setVideoUrl(jsonArray.getJSONObject(i).optString(VIDEO_URL));
            list.add(steps);
        }
        return list;
    }

    public interface Callback {
        void onResponse(List<Recipe> list);
    }
}
