package com.example.projet.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.projet.Display_recipe;
import com.example.projet.MainActivity;
import com.example.projet.R;
import com.example.projet.models.Recette;
//import com.example.recipeapp.utils.GetImageFromURL;
import com.example.projet.utils.DataBaseHelper;
import com.example.projet.utils.GetDetails;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {

    private final List<Recette> recettes;
    private final List<String> favorites;
    private DataBaseHelper dataBaseHelper;
    private  Context context;

    public RecipeAdapter(List<Recette> recettes, Context context) {
        super();
        dataBaseHelper=new DataBaseHelper(context);
        this.recettes = recettes;
        this.context=context;
        favorites=getFavoris();
    }

    @Override
    public int getItemCount() {
        return recettes.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Recette pair = recettes.get(position);
        //holder.publisher.setText(pair.getPublisher());
        holder.title_plat.setText(pair.getTitle());
        holder.id.setText(pair.getId());
        Picasso.get().load(pair.getImage_url()).into(holder.image_plat);
        Log.d("favoris", "onBindViewHolder: "+favorites);
        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetDetails(context,pair).execute(pair.getId());
                Log.d("item", "onClick: "+pair.getId());
            }
        });
        if(favorites.contains(pair.getId()))
            holder.favoris.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite));
        else
            holder.favoris.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_border));
        holder.favoris.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(! favorites.contains(pair.getId())) {
                    dataBaseHelper.add_favoris(pair.getId(), pair.getTitle(), pair.getImage_url());
                    ((ImageView)v).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite));

                }else {
                    dataBaseHelper.delete_favoris(pair.getId());
                    ((ImageView)v).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_border));
                }
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT,"sujet sujet");
                i.putExtra(Intent.EXTRA_TEXT, pair.getSource_url());
                context.startActivity(Intent.createChooser(i, "Share text via"));
            }
        });

        //new GetImageFromURL(holder.image_plat).execute(pair.getImage_url());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView image_plat;
        private TextView title_plat;
        //private TextView publisher;
        private ImageView favoris,share;
        private TextView id;
        private RelativeLayout parent_layout;
        private Recette currentPair;

        public MyViewHolder(final View itemView) {
            super(itemView);
            parent_layout = (RelativeLayout) itemView.findViewById(R.id.parent_layout);
            image_plat = ((ImageView) itemView.findViewById(R.id.image_plat));
            title_plat = ((TextView) itemView.findViewById(R.id.title_plat));
            favoris = itemView.findViewById(R.id.favoris);
            share = itemView.findViewById(R.id.share);
            //publisher = ((TextView) itemView.findViewById(R.id.publisher));
            id=((TextView)itemView.findViewById(R.id.id));

        }

    }
    public List<String> getFavoris(){

        List<String> list = new ArrayList<>();
        Cursor data= dataBaseHelper.getFavoris();
        while(data.moveToNext()){
            list.add(data.getString(0));
        }
        return list;
    }

}