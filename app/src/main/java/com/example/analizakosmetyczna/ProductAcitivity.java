package com.example.analizakosmetyczna;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductAcitivity extends AppCompatActivity {

    TextView pt_product_name, pt_product_type_rate,pt_product_desc, pt_product_ingredients;
    ImageView product_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_acitivity);
        product_img = findViewById(R.id.product_img);
        pt_product_name = findViewById(R.id.pt_product_name);
        pt_product_type_rate = findViewById(R.id.pt_product_type_rate);
        pt_product_desc = findViewById(R.id.pt_product_desc);
        pt_product_ingredients = findViewById(R.id.pt_product_ingredients);

        Product product = (Product) getIntent().getSerializableExtra("PRODUCT");

        product_img.setImageResource(product.getImage());
        pt_product_name.setText(product.getName());
        pt_product_desc.setText(product.getDesc());
        pt_product_type_rate.setText("Typ: "+product.getType()+"\nOcena: "+product.getRate());
        pt_product_ingredients.setText("Składniki: ");
    }
}