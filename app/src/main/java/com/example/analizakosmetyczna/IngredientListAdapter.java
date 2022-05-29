package com.example.analizakosmetyczna;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class IngredientListAdapter extends ArrayAdapter<Ingredient> {

    private Context mContext;
    int mResource;

    public IngredientListAdapter(Context context, int resource, ArrayList<Ingredient> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName();
        String desc = getItem(position).getDesc();
        String rate = getItem(position).getRate();

        Ingredient ingredient = new Ingredient(name, desc, rate);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tv_name = convertView.findViewById(R.id.ingredient_name);
        TextView tv_desc = convertView.findViewById(R.id.ingredient_desc);
        TextView tv_rate = convertView.findViewById(R.id.ingredient_rate);

        if(rate == "Polecam") {
            tv_rate.setTextColor(Color.rgb(0, 165, 0));
        }
        if(rate == "Polecam, ale") {
            tv_rate.setTextColor(Color.rgb(255, 165, 0));
        }
        if(rate == "Nie polecam") {
            tv_rate.setTextColor(Color.rgb(255, 0, 0));
        }

        tv_name.setText(name);
        tv_desc.setText(desc);
        tv_rate.setText(rate);

        return convertView;
    }
}
