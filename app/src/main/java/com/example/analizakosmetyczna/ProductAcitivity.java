package com.example.analizakosmetyczna;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ProductAcitivity extends AppCompatActivity {

    TextView pt_product_name, pt_product_type_rate, pt_product_desc, pt_product_ingredients;
    ImageView product_img;
    Button btn_favourite;
    User user;
    int ID_USER, ID_PRODUCT;
    String status;
    Product product;
    boolean dataset = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_acitivity);
        product_img = findViewById(R.id.product_img);
        pt_product_name = findViewById(R.id.pt_product_name);
        pt_product_type_rate = findViewById(R.id.pt_product_type_rate);
        pt_product_desc = findViewById(R.id.pt_product_desc);
        pt_product_ingredients = findViewById(R.id.pt_product_ingredients);

        product = (Product) getIntent().getSerializableExtra("PRODUCT");
        status = product.getStatus();
        ID_PRODUCT = product.getId();

        System.out.println("STATUS     " + status);
        product_img.setImageResource(product.getImage());
        pt_product_name.setText(product.getName());
        pt_product_desc.setText(product.getDesc());


        btn_favourite = findViewById(R.id.btn_favourite);

        user = new User(this);
        ID_USER = user.preferences.getInt("ID", 0);

        if (status.equals("1")) {
            btn_favourite.setText("Usuń z ulubionych");
        } else {
            btn_favourite.setText("Dodaj do ulubionych");
        }

        new getProductData().execute();
    }

    public void addToFavourite(View view) {
        new FavouriteProducts().execute();
    }

    class FavouriteProducts extends AsyncTask<ArrayList<String>, Integer, Boolean> {
        Connection connection;
        Statement statement;

        @SuppressLint("WrongThread")
        @Override
        protected final Boolean doInBackground(ArrayList<String>... param) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://cometics.xaa.pl/p581392_cosmetics?useSSL=false", "p581392", "eOtI2Yjz7");
                statement = connection.createStatement();

                if (status.equals("1")) {
                    System.out.println("ID PRODUCT    " + ID_PRODUCT);
                    statement.executeUpdate("DELETE FROM favourite_products WHERE id_product = " + ID_PRODUCT + " AND id_user = " + ID_USER);
                } else {
                    statement.executeUpdate("INSERT INTO favourite_products (id_product, id_user) VALUES ('" + ID_PRODUCT + "', '" + ID_USER + "');");
                }

            } catch (Exception e) {
                System.out.println("ERROR:  " + e.getMessage());
            }
            return true;
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
            String returnText = "dodany do ulubionych";
            if (status.equals("1")) {
                product.setStatus("0");
                status = "0";
                btn_favourite.setText("Dodaj do ulubionych");
                returnText = "Usunięty z ulubionych";
            } else {
                product.setStatus("1");
                status = "1";
                btn_favourite.setText("Usuń z ulubionych");
            }
            Toast.makeText(getApplicationContext(), "Produkt " + returnText, Toast.LENGTH_LONG).show();
        }
    }

    class getProductData extends AsyncTask<ArrayList<String>, Integer, Boolean> {
        Connection connection;
        Statement statement;
        String ingredients;
        int iter, suma;

        @SuppressLint("WrongThread")
        @Override
        protected final Boolean doInBackground(ArrayList<String>... param) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://cometics.xaa.pl/p581392_cosmetics?useSSL=false", "p581392", "eOtI2Yjz7");
                statement = connection.createStatement();

                if (!dataset) {
                    ResultSet rs = statement.executeQuery("SELECT ingredients.ingredient_name, ingredients.rate FROM ingredients LEFT JOIN product_ingredients ON ingredients.id = product_ingredients.id_ingredient WHERE product_ingredients.id_product = " + String.valueOf(ID_PRODUCT));
                    ingredients = "";
                    iter = 0;
                    suma = 0;
                    while (rs.next()) {
                        ingredients += rs.getString(1) + ", ";
                        suma += rs.getInt(2);
                        iter++;
                    }
                }

            } catch (Exception e) {
                System.out.println("ERROR:  " + e.getMessage());
            }

            return true;
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
            if (iter == 0) iter = 1;
            pt_product_ingredients.setText("Składniki: " + ingredients);
            product.setRate(suma / iter);
            pt_product_type_rate.setText("Typ: " + product.getType() + "\nOcena: " + product.getRate());
            dataset = true;
        }
    }
}