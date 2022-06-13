package com.example.analizakosmetyczna;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Products extends AppCompatActivity {

    ListView listview;
    TextView tv_product_title, tv_product_desc;
    ImageView iv_product_img;
    String prefix = "product";
    Context c;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        listview = findViewById(R.id.listview_product);
        c = this;
        iv_product_img = (ImageView) findViewById(R.id.product_img);
        tv_product_title = (TextView) findViewById(R.id.product_title);
        tv_product_desc = (TextView) findViewById(R.id.product_desc);
        user = new User(this);

        new AddProducts().execute();
    }

    class AddProducts extends AsyncTask<ArrayList<String>, Integer, Boolean> {
        Connection connection;
        Statement statement;
        ArrayList<Product> products_list = new ArrayList<>();
        int USER_ID = user.preferences.getInt("ID", 0);
        @SuppressLint("WrongThread")
        @Override
        protected final Boolean doInBackground(ArrayList<String>... param) {
            ArrayList<Integer> images = new ArrayList<>();
            images.add(R.drawable.product1);
            images.add(R.drawable.product2);
            images.add(R.drawable.product3);
            images.add(R.drawable.product4);
            images.add(R.drawable.product5);
            images.add(R.drawable.product6);
            int i = 0;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://cometics.xaa.pl/p581392_cosmetics?useSSL=false", "p581392", "eOtI2Yjz7");
                statement = connection.createStatement();

                ResultSet rs = statement.executeQuery("SELECT p.name, p.description, pt.product_type, p.rate, p.id FROM products AS p LEFT JOIN product_types AS pt ON p.type=pt.id LEFT JOIN favourite_products AS fp ON p.id=fp.id_product WHERE fp.id_user = "+String.valueOf(USER_ID));
                while (rs.next()) {
                    System.out.println("GET INT        "+rs.getInt(5));
                    products_list.add(new Product(rs.getString(1), rs.getString(2), rs.getString(3), images.get(i), "1", rs.getFloat(4), rs.getInt(5)));
                    i++;
                }
                if (products_list.size() > 0) {
                    return true;
                }

            } catch (Exception e) {
                System.out.println("ERROR:  " + e.getMessage());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean pass) {
            super.onPostExecute(pass);
            try {
                connection.close();
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if (!pass) {
                Toast.makeText(getApplicationContext(), "Błąd pobierania produktów.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Produkty załadowane pomyślnie", Toast.LENGTH_LONG).show();

                ProductListAdapter adapter = new ProductListAdapter(c, R.layout.product_adapter_view_layout, products_list);
                listview.setAdapter(adapter);
            }
        }
    }
}

