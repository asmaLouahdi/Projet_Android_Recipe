package com.example.projet.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.projet.Display_myrecipe;
import com.example.projet.MainActivity;
import com.example.projet.R;
import com.example.projet.Search_result_fragment;
import com.example.projet.models.Myrecipe_view;

import java.util.List;

public class MyrecipeAdapter extends RecyclerView.Adapter<MyrecipeAdapter.MyViewHolder> {

    private final List<Myrecipe_view> recettes;
    private  Context context;

    public MyrecipeAdapter(List<Myrecipe_view> recettes, Context context) {
        super();
        this.recettes = recettes;
        this.context=context;
    }

    @Override
    public int getItemCount() {
        return recettes.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Myrecipe_view pair = recettes.get(position);
        holder.title_plat.setText(pair.getTitle());
        holder.category_plat.setText(pair.getCategory());
        holder.duration.setText(pair.getTime());
        if(pair.getImage()==null){
        switch (pair.getCategory()){
            case "Starters":
                holder.image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.starter4));
                break;
            case "Main dish":
                holder.image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.main1));
                break;
            case "Dessert":
                holder.image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.dessert1));
                break;
            case "Healthy":
                holder.image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.healthy1));
                break;
            case "Vegetarian":
                holder.image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.veg1));
                break;
            case "Drinks":
                holder.image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bois3));
                break;
        }}
        else{
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(Uri.parse(pair.getImage()),filePathColumn,null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            holder.image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", pair.getId());
                bundle.putString("title", pair.getTitle());
                bundle.putString("category", pair.getCategory());
                bundle.putString("photo", pair.getImage());
                bundle.putString("time", pair.getTime());
                Display_myrecipe fragment = new Display_myrecipe();
                fragment.setArguments(bundle);
                ((MainActivity)context).loadFragment(fragment);

            }
        });
        //new GetImageFromURL(holder.image_plat).execute(pair.getImage_url());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout parent_layout;
        private TextView category_plat;
        private TextView title_plat;
        private TextView duration;
        private ImageView image;
        //private TextView id;

        public MyViewHolder(final View itemView) {
            super(itemView);
            parent_layout = (RelativeLayout)itemView.findViewById(R.id.parent_layout);
            category_plat = ((TextView) itemView.findViewById(R.id.category));
            title_plat = ((TextView) itemView.findViewById(R.id.title));
            duration = ((TextView) itemView.findViewById(R.id.duration));
            image = itemView.findViewById(R.id.icon);

        }


    }


}