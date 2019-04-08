package com.example.projet;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.projet.Modify_recipe;

import com.example.projet.utils.DataBaseHelper;

import java.util.ArrayList;

public class Display_myrecipe extends Fragment {
    private View view;
    private Context context;
    private DataBaseHelper dataBaseHelper;
    private TextView recipe_title, recipe_category, recipe_time, recipe_description, recipe_id;
    private LinearLayout recipe_ingredient;
    private ImageView image, delete, modify, share;
    private int id;
    String title, category,time,photo;
    ArrayList<String> ingredients;
    String description="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.display_myrecipe,null);
        recipe_title=view.findViewById(R.id.title);
        recipe_category=view.findViewById(R.id.category);
        recipe_time=view.findViewById(R.id.duration);
        share= view.findViewById(R.id.share);
        recipe_description=view.findViewById(R.id.description);
        recipe_ingredient=view.findViewById(R.id.ingredients_list);
        delete = view.findViewById(R.id.delete);
        modify= view.findViewById(R.id.modify);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                delete();
            }
        });
        modify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                modify();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                share();
            }
        });
        dataBaseHelper=new DataBaseHelper(context);
        image = view.findViewById(R.id.image_plat);
        //image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.entre3));
        id=getArguments().getInt("id");
        title=getArguments().getString("title");
        category=getArguments().getString("category");
        time=getArguments().getString("time");
        photo=getArguments().getString("photo");
        if(photo==null) {
            switch (category) {
                case "Starters":
                    image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.starter4));
                    break;
                case "Main dish":
                    image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.main1));
                    break;
                case "Dessert":
                    image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.dessert1));
                    break;
                case "Healthy":
                    image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.healthy1));
                    break;
                case "Vegetarian":
                    image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.veg1));
                    break;
                case "Drinks":
                    image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bois3));
                    break;
            }
        }else{
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(Uri.parse(photo),filePathColumn,null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
           image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
        Cursor cursor = dataBaseHelper.getRecipe(id);
        ingredients= new ArrayList<>();
        while(cursor.moveToNext()){
            Log.d("ingredients", "onCreate: "+cursor.getInt(0));
            ingredients.add(cursor.getString(cursor.getColumnIndex("INGREDIENT")));
            description=cursor.getString(cursor.getColumnIndex("DESCRIPTION"));
        }
        Log.d("ingredients", "onCreate: "+ingredients.size());
        recipe_title.setText(title);
        recipe_description.setText(description);
        recipe_category.setText(category);
        recipe_time.setText(time);
        //recipe_id.setText(""+id);
        addIngredient(ingredients);
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
            recipe_ingredient.addView(ing);
        }
    }

    public void delete() {
        Log.d("mama", "delete: "+id);
        dataBaseHelper.supprimer(id);
        getFragmentManager().popBackStack();
        getFragmentManager().popBackStack();
        Toast.makeText(context,"Recipe deleted",Toast.LENGTH_SHORT).show();

    }
    public void modify(){
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putInt("id",id);
        bundle.putString("category", category);
        bundle.putString("time",time);
        bundle.putString("description", description);
        bundle.putStringArrayList("ingredients",ingredients);

        Modify_recipe fragment = new Modify_recipe();
        fragment.setArguments(bundle);
        ((MainActivity )context).loadFragment(fragment);

    }
    public void share(){
        String msg="";
        msg=msg+"title: "+title+"\n";
        msg=msg+"Category: "+category+"\n";
        msg=msg+"Duration: "+time+"\n";
        msg=msg+"Ingredients: \n";
        for(int i=0;i<ingredients.size();i++)
            msg=msg+ingredients.get(i)+"\n";
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT,"sujet sujet");
        i.putExtra(Intent.EXTRA_TEXT, msg);
        startActivity(Intent.createChooser(i, "Share text via"));

    }
}