package com.example.projet;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.projet.utils.DataBaseHelper;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class Add_recipe extends Fragment {
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int PERMISSION_REQUEST=0;
    private DataBaseHelper dataBaseHelper;
    private EditText title;
    private EditText description;
    private LinearLayout linearLayout;
    private Spinner spinner, hours, minutes;
    private ImageView add,image;
    private  View view;
    private Context context;
    private Button button;
    private ImageButton addPhoto;
    private String recipe_photo;
    private ArrayList<EditText> myingredients;
    private String titre="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    context = getActivity();
        view =inflater.inflate(R.layout.add_recipe,null);
        title=view.findViewById(R.id.title);
        title.setText(titre);
        linearLayout = view.findViewById(R.id.ingredients);

        myingredients = new ArrayList<>();
        for(int index=0; index<((ViewGroup)linearLayout).getChildCount(); ++index) {
            EditText nextChild = (EditText)((ViewGroup)linearLayout).getChildAt(index);
            if(!nextChild.getText().toString().isEmpty()){
                myingredients.add(nextChild);
            }
        }

    dataBaseHelper = new DataBaseHelper(context);
       button = view.findViewById(R.id.save);
       addPhoto = view.findViewById(R.id.add_photo);
       image = view.findViewById(R.id.image);
       add = view.findViewById(R.id.add);
        configureSpinners();
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            add_recipe();
            }
        });
        addPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,RESULT_LOAD_IMAGE);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                add_field();
            }
        });
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST);
        }
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]permissions,@NonNull int[] grantResults){
        //super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch(requestCode){
            case PERMISSION_REQUEST:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(context,"permission OK",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "not okey",Toast.LENGTH_SHORT).show();
                }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case RESULT_LOAD_IMAGE:
                if(resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    recipe_photo = selectedImage.toString();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = context.getContentResolver().query(selectedImage,filePathColumn,null,null,null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }
        }
    }

    public void configureSpinners(){
        spinner = (Spinner) view.findViewById(R.id.category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.categories, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        hours = (Spinner) view.findViewById(R.id.hours);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.hours,  R.layout.spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hours.setAdapter(adapter1);
        minutes = (Spinner) view.findViewById(R.id.minutes);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.minutes,  R.layout.spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minutes.setAdapter(adapter2);
    }
    public void add_field() {
        LinearLayout layout = view.findViewById(R.id.ingredients);

        EditText text = new EditText(getActivity());
        text.setPadding(0,0,0,0);
        text.setLayoutParams(new ViewGroup.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        text.setTextSize(13f);
        text.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_grey_600));
        text.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edittext_bottom_border));
        myingredients.add(text);
        layout.addView(text);
    }
    public void add_recipe() {
        String time ;
        String category;
        ArrayList<String> ingredients_list =new ArrayList<String>();

        description = view.findViewById(R.id.step);
        time=hours.getSelectedItem().toString()+"h "+minutes.getSelectedItem().toString()+"mn";
        category = spinner.getSelectedItem().toString();
        for(int index=0; index<((ViewGroup)linearLayout).getChildCount(); ++index) {
            EditText nextChild = (EditText)((ViewGroup)linearLayout).getChildAt(index);
            if(!nextChild.getText().toString().isEmpty()){
                ingredients_list.add(nextChild.getText().toString());
            }
        }
        if(ingredients_list.size() == 0){
            Toast.makeText(context, "Fill at least one ingredient, please",Toast.LENGTH_SHORT).show();
            return;
        }
        if(title.getText().toString().isEmpty()) {
            Toast.makeText(context, "Fill the title, please", Toast.LENGTH_SHORT).show();
            return;
        }
        if(hours.getSelectedItem().toString().equals("00") && minutes.getSelectedItem().toString().equals("00")) {
            Toast.makeText(context, "Fill cook duration, please", Toast.LENGTH_SHORT).show();
            return;
        }
        dataBaseHelper.addRecipe(title.getText().toString(),category,time,description.getText().toString(),ingredients_list, recipe_photo);
        Toast.makeText(context, "Recipe added", Toast.LENGTH_LONG).show();

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            titre = "";
        }else {
            titre = savedInstanceState.getString("item");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        EditText item = (EditText) view.findViewById(R.id.title);
        //EditText amount = (EditText) view.findViewById(R.id.itemAmount);
        outState.putString("item", item.getText().toString());
        //outState.putString("amount", amount.getText().toString());
    }
}
