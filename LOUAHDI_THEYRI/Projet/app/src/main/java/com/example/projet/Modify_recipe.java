package com.example.projet;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.projet.models.Myrecipe_view;
import com.example.projet.utils.DataBaseHelper;

import java.util.ArrayList;

public class Modify_recipe extends Fragment {
    private View view;
    private Context context;
    private DataBaseHelper dataBaseHelper;
    private int id;
    ImageView add;
    String title, category,time;
    ArrayList<String> ingredients;
    String description;
    private EditText title_recipe;
    private EditText description_recipe;
    private Spinner spinner, hours, minutes;
    private Button modify;
    LinearLayout linearLayout;
    ArrayAdapter<CharSequence> adapter, adapter1,adapter2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        dataBaseHelper = new DataBaseHelper(context);
        view = inflater.inflate(R.layout.modify_recipe,null);
        id=getArguments().getInt("id");
        add = view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                add_ingr();
            }
        });
        title=getArguments().getString("title");
        category=getArguments().getString("category");
        time=getArguments().getString("time");
        description=getArguments().getString("description");
        ingredients = getArguments().getStringArrayList("ingredients");
        title_recipe=view.findViewById(R.id.title);
        modify = view.findViewById(R.id.modify);
        modify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                modify_recipe();
            }
        });
        description_recipe = view.findViewById(R.id.step);
        linearLayout = view.findViewById(R.id.ingredients);
        configureSpinners();

        title_recipe.setText(title);
        String hour = time.split("h")[0];
        String min = time.split(" ")[1].split("mn")[0];
        description_recipe.setText(description);
        spinner.setSelection(adapter.getPosition(category));
        hours.setSelection(adapter1.getPosition(hour));
        minutes.setSelection(adapter2.getPosition(min));
        add_field();
        return view;
    }
    public void configureSpinners(){
        spinner = (Spinner) view.findViewById(R.id.category);
        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.categories, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        hours = (Spinner) view.findViewById(R.id.hours);
        adapter1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.hours,  R.layout.spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hours.setAdapter(adapter1);
        minutes = (Spinner) view.findViewById(R.id.minutes);
        adapter2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.minutes,  R.layout.spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minutes.setAdapter(adapter2);
    }
    public void add_field() {
        LinearLayout layout = view.findViewById(R.id.ingredients);
        for(int i=0;i<ingredients.size();i++) {
            EditText text = new EditText(getActivity());
            text.setPadding(0, 0, 0, 0);
            text.setText(ingredients.get(i));
            text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            text.setTextSize(13f);
            text.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_grey_600));
            text.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edittext_bottom_border));
            layout.addView(text);
        }
    }
    public void add_ingr(){
        LinearLayout layout = view.findViewById(R.id.ingredients);
        EditText text = new EditText(getActivity());
            text.setPadding(0, 0, 0, 0);
            text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            text.setTextSize(13f);
            text.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_grey_600));
            text.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edittext_bottom_border));
            layout.addView(text);

    }
    public void modify_recipe(){
        String duration = hours.getSelectedItem().toString()+"h "+hours.getSelectedItem().toString()+"mn";
        ArrayList<String>ingredients_list=new ArrayList<>();
        for(int index=0; index<((ViewGroup)linearLayout).getChildCount(); ++index) {
            EditText nextChild = (EditText)((ViewGroup)linearLayout).getChildAt(index);
            if(!nextChild.getText().toString().isEmpty()){
                ingredients_list.add(nextChild.getText().toString());
            }
        }
dataBaseHelper.modifier(id,title_recipe.getText().toString(),spinner.getSelectedItem().toString(),duration,description_recipe.getText().toString(),ingredients_list);
        Toast.makeText(context,"Recipe updated",Toast.LENGTH_SHORT).show();
    }
}
