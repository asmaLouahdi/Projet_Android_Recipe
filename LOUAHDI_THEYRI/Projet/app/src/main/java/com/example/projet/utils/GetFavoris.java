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

import
        android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.projet.MainActivity;
import com.example.projet.R;
import com.example.projet.adapters.RecipeAdapter;
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * AsyncTask implementation that opens a network connection and
 * query's the Book Service API.
 */
public class GetFavoris extends AsyncTask<String, Recette, String> {

    private List<Recette> recettes = new ArrayList<Recette>();
    private List<Recette> list = new ArrayList<Recette>();
    private List<String> favoris;
    private RecipeAdapter ra;
    private RecyclerView rv;
    private Context context;
    private DataBaseHelper dataBaseHelper;

    public GetFavoris(RecyclerView rv, List<String>favoris, Context context) {
        this.rv = rv;
        this.context = context;
        this.favoris=favoris;
        dataBaseHelper= new DataBaseHelper(context);
    }


    @Override
    protected void onPreExecute() {
        ra = new RecipeAdapter(recettes, context);
        rv.setLayoutManager(
                new LinearLayoutManager(context));
        rv.setHasFixedSize(true);
        rv.setAdapter(ra);
    }

    @Override
    protected String doInBackground(String... params) {

        String queryString = params[0];


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String recipeJSONString = null;

        try {
            final String RECIPE_BASE_URL = "https://www.food2fork.com/api/search?key=0bc64dc052642d1fc9718fd6b9b10850";

            final String QUERY_PARAM = "q"; // Parameter for the search string.

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
            JSONArray itemsArray = jsonObject.getJSONArray("recipes");
            int i = 0;
            String title;
            String image_url;
            String source_url;
            String id;
            String publisher;

            while (i < itemsArray.length()) {
                JSONObject recette = itemsArray.getJSONObject(i);
                Log.d("recette", "doInBackground: "+recette);
                try {
                    publisher = recette.getString("publisher_url");
                    source_url = recette.getString("source_url");
                    image_url = recette.getString("image_url");
                    title = recette.getString("title");
                    id = recette.getString("recipe_id");
                    list.add(new Recette(title, image_url, source_url, id, publisher));
                    //publishProgress();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                i++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }


    @Override
    protected void onProgressUpdate(Recette... values) {
        recettes.add(values[0]);
        ra.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        refreshMyList(list);
    }
    public void refreshMyList(List<Recette> list){
        this.recettes.clear();
        this.recettes.addAll(list);
        this.ra.notifyDataSetChanged();
    }


}
