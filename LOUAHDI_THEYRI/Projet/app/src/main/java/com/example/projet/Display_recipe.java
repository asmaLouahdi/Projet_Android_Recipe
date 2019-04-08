package com.example.projet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Display_recipe extends Fragment {
    private TextView title, publisher;
    private Context context;
    private LinearLayout ingredient;
    private ImageView image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.display_recipe,null);
        context = getActivity();
        title = (TextView) view.findViewById(R.id.title_plat);
        image = (ImageView) view.findViewById(R.id.image_plat);
        publisher = (TextView) view.findViewById(R.id.publisher);
        ingredient = (LinearLayout) view.findViewById(R.id.ingredients_list);


        String title_recette = getArguments().getString("title");
        String image_url = getArguments().getString("image_url");
        String publisher_url = getArguments().getString("publisher");
        ArrayList<String> ingredients = getArguments().getStringArrayList("ingredients");
        addIngredient(ingredients);
        title.setText(title_recette);
        publisher.setText(publisher_url);


        Picasso.get().load(image_url).into(image);


        return view;
    }
    public void addIngredient(ArrayList<String> ingredients) {
        for (int j = 0; j < ingredients.size(); j++) {
            TextView ing = new TextView(context);
            TextViewCompat.setTextAppearance(ing, R.style.Chips);
            ing.setText(ingredients.get(j));
            ing.setPadding(0, 32, 0, 32);
            ing.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(
                    context, R.drawable.bullet), null, null, null);
            ing.setBackground(ContextCompat.getDrawable(context, R.drawable.dashed_line));
            ing.setCompoundDrawablePadding(32);
            ingredient.addView(ing);
        }
    }
}
