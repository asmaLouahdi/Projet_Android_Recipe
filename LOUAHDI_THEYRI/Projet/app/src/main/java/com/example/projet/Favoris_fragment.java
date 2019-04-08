package com.example.projet;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projet.adapters.FavorisAdapter;
import com.example.projet.adapters.MyrecipeAdapter;
import com.example.projet.adapters.RecipeAdapter;
import com.example.projet.models.Myrecipe_view;
import com.example.projet.models.Recette;
import com.example.projet.utils.DataBaseHelper;
import com.example.projet.utils.UtilsNetwork;

import java.util.ArrayList;

public class Favoris_fragment extends Fragment {
    private View view;
    private Context context;
    private DataBaseHelper dataBaseHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        dataBaseHelper = new DataBaseHelper(context);
        view = inflater.inflate(R.layout.favoris_fragment,null);
        RecyclerView recyclerView= view.findViewById(R.id.list);
        ArrayList<Recette> recettes = new ArrayList<>();
        Cursor favoris=dataBaseHelper.getFavoris();
        while(favoris.moveToNext()){

                recettes.add(new Recette(favoris.getString(1),favoris.getString(2),"",favoris.getString(0), ""));
        }
        FavorisAdapter ra = new FavorisAdapter(recettes, context);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(ra);
        //ra.notifyDataSetChanged();
        return view;
    }

}
