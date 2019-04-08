package com.example.projet.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="My_RECIPES";
    private static final String TABLE_NAME1 ="recipe";
    private static final String TABLE_NAME2 ="ingredient";
    private static final String TABLE_NAME3= "favoris";

    private static final String id="ID";
    private static final String title="TITLE";
    private static final String category="CATEGORY";
    private static final String time="TIME";
    private static final String description="DESCRIPTION";
    private static final String recipe_photo="PHOTO";


    private static final String ingredient="INGREDIENT";
    private static final String recipe_id="RECIPE_ID";
    private  static  final String url_recipe = "RECIPE_URL";




    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String recipe_table = "CREATE TABLE " + TABLE_NAME1 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                title +" TEXT, "+category+" TEXT, "+time+" TEXT, "+description+" TEXT, "+recipe_photo+" TEXT )";

        String ingredient_table = "CREATE TABLE " + TABLE_NAME2 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ingredient +" TEXT,"+recipe_id+" INTEGER, FOREIGN KEY("+recipe_id+") REFERENCES recipe("+id+"))";
        String favoris_table = "CREATE TABLE " + TABLE_NAME3 + " (ID TEXT UNIQUE,"+ title +" TEXT, "+url_recipe+" TEXT )";

        db.execSQL(recipe_table);
        db.execSQL(ingredient_table);
        db.execSQL(favoris_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
        onCreate(db);
    }
    public boolean addRecipe(String title, String category, String time, String description, ArrayList<String> ingredients, String photo) {

        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues contentValues = new ContentValues();
        contentValues.put(this.title, title);
        contentValues.put(this.category, category);
        contentValues.put(this.time, time);
        contentValues.put(this.description, description);
        contentValues.put(this.recipe_photo, photo);

        Log.d(TAG, "addData: Adding " + photo + " to " + TABLE_NAME1);

        long result = db.insert(TABLE_NAME1, null, contentValues);
        if (result == -1) {
            return false;
        } else {

            try{
                for (int i = 0; i < ingredients.size() ; i++){
                    ContentValues cv = new ContentValues();
                    cv.put(this.ingredient, ingredients.get(i));
                    cv.put(this.recipe_id, result);
                    Log.d("Added ",""+ cv);
                    db.insertOrThrow(TABLE_NAME2, null, cv);
                }
                db.close();
            }catch (Exception e){
                Log.e("Problem", e + " ");
            }

        }
        return true;
    }
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME1;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getRecipe_ByCategorie(String category){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME1+" WHERE "+ this.category+" = '"+category+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getRecipe(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME1+" JOIN "+TABLE_NAME2+" WHERE "+TABLE_NAME1+"."+this.id+"="+TABLE_NAME2+"."+recipe_id+" AND "+ TABLE_NAME1+"."+this.id+" = "+id;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public boolean supprimer(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME2, recipe_id + "=" + id, null);

        return db.delete(TABLE_NAME1, this.id + "=" + id, null) > 0;

    }
    public void modifier(int id, String title, String category, String time, String description, ArrayList<String> ingredients){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.title, title);
        contentValues.put(this.category, category);
        contentValues.put(this.time, time);
        contentValues.put(this.description, description);

        db.delete(TABLE_NAME2, recipe_id + "=" + id, null);
        try{
            for (int i = 0; i < ingredients.size() ; i++){
                ContentValues cv = new ContentValues();
                cv.put(this.ingredient, ingredients.get(i));
                cv.put(this.recipe_id, id);
                Log.d("Added ",""+ cv);
                db.insertOrThrow(TABLE_NAME2, null, cv);
            }
        }catch (Exception e){
            Log.e("Problem", e + " ");
        }
        db.update(TABLE_NAME1, contentValues, this.id+" = "+id, null);
        db.close();

    }
    public boolean add_favoris(String id, String title, String url){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.id, id);
        contentValues.put(this.title, title);
        contentValues.put(this.url_recipe, url);
        long result = db.insert(TABLE_NAME3, null, contentValues);
        db.close();
        if (result == -1) {
            return false;
        } else {
            Log.d(TAG, "add_favoris: "+result);
            return true;
        }
    }
    public Cursor getFavoris(){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME3;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public void delete_favoris(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME3, this.id + " = " + "'"+id+"'", null);
        db.close();
    }
}