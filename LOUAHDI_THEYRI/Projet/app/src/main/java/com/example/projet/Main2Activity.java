package com.example.projet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {
    private static final int SELECTED_PICTURE=1;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int PERMISSION_REQUEST=0;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        image = findViewById(R.id.test);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST);
        }
    }
    public void add(View view){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,RESULT_LOAD_IMAGE);
       /* Intent i= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivityForResult(i, SELECTED_PICTURE);*/
    }
    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
       // Uri uri = data.getData();
        //image.setImageURI(uri);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            Uri uri = data.getData();

            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap yourSelectefdImage = BitmapFactory.decodeFile(filePath);
            Drawable d = new BitmapDrawable(yourSelectefdImage);
            image.setBackground(d);

        }
    }
    */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]permissions,@NonNull int[] grantResults){
        //super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch(requestCode){
            case PERMISSION_REQUEST:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"permission OK",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "not okey",Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
       switch(requestCode){
           case RESULT_LOAD_IMAGE:
               if(resultCode == RESULT_OK) {
                   Uri selectedImage = data.getData();
                   String[] filePathColumn = {MediaStore.Images.Media.DATA};
                   Cursor cursor = getContentResolver().query(selectedImage,filePathColumn,null,null,null);
                   cursor.moveToFirst();
                   int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                   String picturePath = cursor.getString(columnIndex);
                   cursor.close();
                   image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
               }
       }
    }
}
