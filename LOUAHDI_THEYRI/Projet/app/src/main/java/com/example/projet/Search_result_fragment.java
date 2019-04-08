package com.example.projet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.projet.utils.UtilsNetwork;

import okhttp3.internal.Util;

public class Search_result_fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        String keyword = getArguments().getString("keyword");
        View view =inflater.inflate(R.layout.search_result,null);
        RecyclerView recyclerView= view.findViewById(R.id.recycler_recipe_list);
        ProgressBar progressBar= view.findViewById(R.id.progressBar);
        new UtilsNetwork(progressBar, recyclerView, getActivity()).execute(keyword);
        return view;

    }

   /* @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("time_key", mTime);
    }
*/
}
