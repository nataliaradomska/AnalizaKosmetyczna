package com.example.analizakosmetyczna;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ProductListAdapter extends ArrayAdapter<Product> {

    private Context mContext;
    int mResource;

    public ProductListAdapter(Context context, int resource, ArrayList<Product> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName();
        String desc = getItem(position).getDesc();
        int image = getItem(position).getImage();
        String status = getItem(position).getStatus();

        Product product = new Product(name, desc, image, status);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tv_name = convertView.findViewById(R.id.product_title);
        TextView tv_desc = convertView.findViewById(R.id.product_desc);
        ImageView img_product = convertView.findViewById(R.id.product_img);
        /*Button fav_btn = convertView.findViewById(R.id.fav_btn);*/

        tv_name.setText(name);
        tv_desc.setText(desc);
        img_product.setImageResource(image);

        /*fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.equals("0")) {
                    fav_btn.setBackground(Drawable.createFromPath("@drawable/ic_baseline_favorite_border_24"));
                }
                else {
                    fav_btn.setBackground(Drawable.createFromPath("@drawable/ic_baseline_favorite_24"));
        }}});*/


        return convertView;
    }
}
