package com.example.analizakosmetyczna;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Products extends AppCompatActivity {

    TextView tv_product_title, tv_product_desc;
    ImageView iv_product_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        ListView listview = findViewById(R.id.listview_product);
        ArrayList<Product> products_list = new ArrayList<>();

        iv_product_img = (ImageView) findViewById(R.id.product_img);
        tv_product_title = (TextView) findViewById(R.id.product_title);
        tv_product_desc = (TextView) findViewById(R.id.product_desc);

        products_list.add(new Product("Make Me Bio - Garden Roses","Nawilżający krem dla " +
                "skóry suchej i wrażliwej",R.drawable.product1,"1"));
        products_list.add(new Product("Hipp Mamasanft Oil", "Olejek na rozstępy dla " +
                "kobiet w ciąży",R.drawable.product2,"1"));
        products_list.add(new Product("Cetaphil EM, emulsja micelarna do mycia, 500 ml",
                "Specjalistyczny dermokosmetyk w postaci emulsji przeznaczony " +
                "do mycia twarzy oraz ciała, w tym w szczególności skóry suchej lub wrażliwej.",
                R.drawable.product3, "0"));
        products_list.add(new Product("Holika Holika Aloe 99% Soothing Gel", "Aloesowy " +
                "kojący żel nawilżający",R.drawable.product4, "1"));
        products_list.add(new Product("Yope - Naturalne Masło do Ciała - Kokos i Sól Morska - " +
                "200ml", "Kokosowe masło do ciała",R.drawable.product5, "0"));
        products_list.add(new Product("Peeling 3 enzymy - Tołpa Dermo Face Sebio",
                "Peeling pozostawi Twoją cerę odnowioną, bez zrogowaceń, " +
                "widocznych zaskórników oraz doskonale wygładzoną i matową.",R.drawable.product6, "0"));


        ProductListAdapter adapter = new ProductListAdapter(this, R.layout.product_adapter_view_layout, products_list);
        listview.setAdapter(adapter);
    }
}

