package com.example.projet.utils;
/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.projet.Display_recipe;
import com.example.projet.MainActivity;
import com.example.projet.R;
import com.example.projet.Search_result_fragment;
import com.example.projet.models.Recette;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * AsyncTask implementation that opens a network connection and
 * query's the Book Service API.
 */
public class GetDetails extends AsyncTask<String, Recette,Void> {


    private Context context;
    private JSONArray ingredients;
    private Recette pair;

    public GetDetails(Context context, Recette pair) {
        this.context = context;
        this.pair=pair;
    }



    @Override
    protected Void doInBackground(String... params) {

        String queryString = params[0];


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String recipeJSONString = null;

        try {
            final String RECIPE_BASE_URL = "https://www.food2fork.com/api/get?key=a9bd819edc557bdca0cc9b5e022f592a";

            final String QUERY_PARAM = "rId"; // Parameter for the search string.

            Uri builtURI = Uri.parse(RECIPE_BASE_URL).
                    buildUpon().appendQueryParameter(QUERY_PARAM, queryString).build();


            URL requestURL = new URL(builtURI.toString());

            // Open the network connection.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

            // Read the response string into a StringBuilder.
            StringBuilder builder = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine()) != null) {

                builder.append(line + "\n");

            }

            if (builder.length() == 0) {
                return null;
            }
            recipeJSONString = builder.toString();

            // Catch errors.
        } catch (IOException e) {
            e.printStackTrace();

            // Close the connections.
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        // Return the raw response.
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(recipeJSONString);
            Log.d("getting Ingredients", "doInBackground: "+jsonObject);
            JSONObject itemsArray = jsonObject.getJSONObject("recipe");
            //int i = 0;
            //while (i < itemsArray.length()) {
            JSONArray recette = itemsArray.getJSONArray("ingredients");
            Log.d("recette", "doInBackground: "+recette.length());
            try {
                ingredients = recette;
            } catch (Exception e) {
                e.printStackTrace();
            }

            //i++;
            //}
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }
    @Override
    protected void onPostExecute(Void val){
        super.onPostExecute(val);

        /*Intent i = new Intent(context,Display.class);*/
        ArrayList<String> tmp = new ArrayList<>();
        for(int j=0;j<ingredients.length();j++){
            String string = null;
            try {
                string = ingredients.getString(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tmp.add(string);
        }

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("ingredients",tmp);
        bundle.putString("title",pair.getTitle());
        bundle.putString("image_url",pair.getImage_url());
        bundle.putString("publisher",pair.getPublisher());
        Display_recipe fragment = new Display_recipe();
        fragment.setArguments(bundle);
        ((MainActivity)context).loadFragment(fragment);
    }

}
