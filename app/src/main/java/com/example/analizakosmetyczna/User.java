package com.example.analizakosmetyczna;

import android.content.Context;
import android.content.SharedPreferences;

public class User {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    User(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void createLogin(int id, String name,String email) {
        editor.putInt("ID", id);
        editor.putString("NAME", name);
        editor.putString("EMAIL", email);
        editor.putBoolean("LOGGED", true);
        editor.commit();
    }
}
