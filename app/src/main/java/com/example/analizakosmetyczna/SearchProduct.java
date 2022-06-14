package com.example.analizakosmetyczna;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SearchProduct extends AppCompatActivity {

    TextView tv_search;
    Context c;
    ListView listview;
    Intent i_product;
    User user;
    int USER_ID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        listview = findViewById(R.id.ls_search_result);
        c = this;
        i_product = new Intent(this, ProductAcitivity.class);

        user = new User(this);
        USER_ID = user.preferences.getInt("ID",0);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "Działa", Toast.LENGTH_LONG).show();
                System.out.println("DZdssadadsdadasd");
            }
        });

    }


    public void search(View view) {

        tv_search = findViewById(R.id.tv_search);
        String search_items = tv_search.getText().toString();
        if (search_items.length() <= 2) {
            Toast.makeText(getApplicationContext(), "Błędne hasło wyszukiwania!", Toast.LENGTH_LONG).show();
        } else {
            ArrayList<String> param = new ArrayList<>();
            String[] keywords = search_items.split(" ");
            for (String s : keywords) {
                param.add(s);
                System.out.println(s);
            }

            new Search().execute(param);
        }

    }

    class Search extends AsyncTask<ArrayList<String>, Integer, Boolean> {
        Connection connection;
        Statement statement;
        ArrayList<Product> result = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressLint("WrongThread")
        @Override
        protected final Boolean doInBackground(ArrayList<String>... param) {
            try {
                ArrayList<Integer> images = new ArrayList<>();
                ArrayList<Integer> favouriteproducts = new ArrayList<>();

                images.add(R.drawable.product1);
                images.add(R.drawable.product2);
                images.add(R.drawable.product3);
                images.add(R.drawable.product4);
                images.add(R.drawable.product5);
                images.add(R.drawable.product6);
                int i = 0;
                ArrayList<String> dane = param[0];
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://cometics.xaa.pl/p581392_cosmetics?useSSL=false", "p581392", "eOtI2Yjz7");
                statement = connection.createStatement();
                String query = "";
                for (String s : dane) {
                    System.out.println("dane: " + s);
                    query += s + "|";
                }
                query = query.substring(0, query.length() - 1);
                System.out.println("USER ID  "+USER_ID);
                ResultSet fp = statement.executeQuery("SELECT id_product FROM favourite_products WHERE id_user = " + USER_ID);
                while (fp.next()) {
                    System.out.println("FP   :"+fp.getInt(1));
                    favouriteproducts.add(fp.getInt(1));
                }

                ResultSet rs = statement.executeQuery("SELECT p.name, p.description, pt.product_type, p.rate, p.id FROM products AS p LEFT JOIN product_types AS pt ON p.type=pt.id WHERE name  REGEXP '" + query + "'");
                while (rs.next()) {
                    System.out.println("RS   :"+rs.getInt(5));
                    if (favouriteproducts.contains(rs.getInt(5))) {
                        result.add(new Product(rs.getString(1), rs.getString(2), rs.getString(3), images.get(i), "1", rs.getFloat(4), rs.getInt(5)));
                    } else {
                        result.add(new Product(rs.getString(1), rs.getString(2), rs.getString(3), images.get(i), "0", rs.getFloat(4), rs.getInt(5)));
                    }
                    i++;
                }

                if (result.size() > 0) {
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
                Toast.makeText(getApplicationContext(), "Brak wników wyszukiwania", Toast.LENGTH_LONG).show();
            } else {
                int i = result.size();
                Toast.makeText(getApplicationContext(), "Znalezionych produktów: " + i, Toast.LENGTH_LONG).show();
                ProductListAdapter adapter = new ProductListAdapter(c, R.layout.product_adapter_view_layout, result);
                listview.setAdapter(adapter);
            }
        }
    }
}
