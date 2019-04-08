package com.example.projet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projet.adapters.MyrecipeAdapter;
import com.example.projet.models.Myrecipe_view;
import com.example.projet.utils.DataBaseHelper;

import java.util.ArrayList;

public class Category_list extends Fragment {
    private RecyclerView recyclerView;
    private MyrecipeAdapter ra;
    private ArrayList<Myrecipe_view> recettes = new ArrayList<>();
    private View view;
    private Context context;
    private DataBaseHelper dataBaseHelper;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.category_list,null);
        recyclerView = view.findViewById(R.id.list);

        recettes=(ArrayList<Myrecipe_view>) getArguments().getSerializable("recipes");
        ra = new MyrecipeAdapter(recettes, context);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(ra);
        return view;
    }
}
