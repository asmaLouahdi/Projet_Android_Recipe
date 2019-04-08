package com.example.projet;

import android.content.Context;
import com.example.projet.Favoris_fragment;

import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telecom.Conference;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.example.projet.models.Myrecipe_view;
import com.example.projet.utils.DataBaseHelper;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import okhttp3.internal.Util;

import static android.support.v4.view.GravityCompat.START;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private Context context = this;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        dataBaseHelper= new DataBaseHelper(context);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);



        loadFragment(new Home_fragment());
        configureToolbar();
        configureSearchView();
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bundle = new Bundle();
                bundle.putString("keyword", query);

                Search_result_fragment fragment = new Search_result_fragment();
                fragment.setArguments(bundle);
                return loadFragment(fragment);
                // new Util(recyclerView, context).execute(query);
                /*Intent intent= new Intent(context, MainActivity.class);
                intent.putExtra("keyWord",query);
                startActivity(intent);*/

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
        // recyclerView = findViewById(R.id.recycler_recipe_list);
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    public void configureToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));


    }

    public void configureSearchView() {
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(true);
    }

    public boolean loadFragment(Fragment fragment){
        if(fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment,"fragment")
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment=null;
        switch(menuItem.getItemId()){
            case R.id.navigation_home:
                fragment = new Home_fragment();
                break;
            case R.id.navigation_dashboard:
                fragment = new Add_recipe();
                break;
            case R.id.navigation_favoris:
                    fragment = new Favoris_fragment();
                    break;
        }
        return loadFragment(fragment);
    }
    public void getCategorie(View view) {
        //Toast.makeText(context,"id"+view.getTag(), Toast.LENGTH_SHORT).show();
        dataBaseHelper = new DataBaseHelper(context);
        Cursor data = dataBaseHelper.getRecipe_ByCategorie((String) view.getTag());
        ArrayList<String> listData = new ArrayList<>();
        ArrayList<Myrecipe_view> recettes=new ArrayList<>();
        while(data.moveToNext()){
            Log.d("data", "add_recipe: "+data.getString(2));
            listData.add(data.getString(1));
            recettes.add(new Myrecipe_view( data.getInt(data.getColumnIndex("ID")),data.getString(data.getColumnIndex("TITLE")),data.getString(data.getColumnIndex("CATEGORY")),data.getString(data.getColumnIndex("TIME")),data.getString(data.getColumnIndex("PHOTO"))));
        }

        Bundle bundle=new Bundle();
        bundle.putSerializable("recipes",recettes);
        Category_list fragment = new Category_list();
        fragment.setArguments(bundle);
        ((MainActivity)context).loadFragment(fragment);


    }
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){

        }else if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
        }
    }

}
