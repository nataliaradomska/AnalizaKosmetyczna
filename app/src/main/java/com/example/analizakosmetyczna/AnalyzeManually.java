package com.example.analizakosmetyczna;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AnalyzeManually extends AppCompatActivity {

    TextView tv_ingredients;
    TextView btn_analyse;
    Intent i_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze_manually);

        btn_analyse = findViewById(R.id.btn_analyse);
        tv_ingredients = findViewById(R.id.tv_ingredients);
        i_result = new Intent(this, Results.class);

    }

    public void analyze(View view) {

        String ingredients = tv_ingredients.getText().toString();
        if (ingredients.length() <= 2) {
            Toast.makeText(getApplicationContext(), "Wprowadź dane poprawnie.", Toast.LENGTH_LONG).show();
        } else {
            ArrayList<String> param = new ArrayList<>();
            String[] ingre = ingredients.split(",");
            for (String s : ingre) {
                param.add(s);
                System.out.println(s);
            }

            new Analyse().execute(param);
        }
    }

    public String setRate(int rate) {
        if (rate == 2) {
            return "Polecam";
        }
        if (rate == 1) {
            return "Polecam, ale";
        }
        if (rate == 0) {
            return "Nie polecam";
        } else {
            return "";
        }
    }

    class Analyse extends AsyncTask<ArrayList<String>, Integer, Boolean> {
        Connection connection;
        Statement statement;
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ArrayList<Ingredient> result = new ArrayList<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @SuppressLint("WrongThread")
        @Override
        protected final Boolean doInBackground(ArrayList<String>... param) {
            try {
                ArrayList<String> dane = param[0];
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://cometics.xaa.pl/p581392_cosmetics?useSSL=false", "p581392", "eOtI2Yjz7");
                statement = connection.createStatement();

                ResultSet rs = statement.executeQuery("SELECT ingredient_name, description, rate FROM ingredients;");
                while (rs.next()) {
                    ingredients.add(new Ingredient(rs.getString(1), rs.getString(2), setRate(rs.getInt(3))));
                }

                for (String s : dane) {
                    for (Ingredient i : ingredients) {
                        if (i.getName().contains(s)) {
                            result.add(i);
                        }
                    }
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
                Toast.makeText(getApplicationContext(), "Brak składników w bazie spróbuj ponownie.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Analiza składników pomyślna!", Toast.LENGTH_LONG).show();
                i_result.putExtra("INGREDIENTS",result);
                startActivity(i_result);
            }
        }
    }
}