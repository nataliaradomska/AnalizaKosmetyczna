package com.example.analizakosmetyczna;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    User user;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_login = findViewById(R.id.sign_in_btn1);
        user = new User(this);

        ListView menu_list = findViewById(R.id.menu_list);
        List<String> menu = new ArrayList<>();
        menu.add("Zeskanuj produkt");
        menu.add("Analizuj skład manualnie");
        menu.add("Wyszukaj produkt");
        menu.add("Ulubione produkty");

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, menu);
        menu_list.setAdapter(arrayAdapter);

        menu_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0: {
                        startActivity(new Intent(MainActivity.this, ScanProduct.class));
                        break;
                    }
                    case 1: {
                        startActivity(new Intent(MainActivity.this, AnalyzeManually.class));
                        break;
                    }
                    case 2: {
                        startActivity(new Intent(MainActivity.this, SearchProduct.class));
                        break;
                    }
                    case 3: {
                        startActivity(new Intent(MainActivity.this, Products.class));
                        break;
                    }
                    default: {
                        System.out.println("Oops! Coś poszło nie tak.");
                    }

                }
            }
        });

        if(user.preferences.getBoolean("LOGGED", false)) {
            btn_login.setVisibility(View.GONE);
            btn_login.setEnabled(false);
        }
    }

    public void logowanie(View view) {
        startActivity(new Intent(MainActivity.this, Logowanie.class));
    }
}