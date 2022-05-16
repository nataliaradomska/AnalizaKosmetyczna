package com.example.analizakosmetyczna;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rejestracja extends AppCompatActivity {

    String error;
    TextView tv_mail, tv_name, tv_pass, tv_passr;
    Intent to_login;
    ArrayList<String> emails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracja);

        error = "";
        tv_mail = findViewById(R.id.tv_mail);
        tv_name = findViewById(R.id.tv_ingredients);
        tv_pass = findViewById(R.id.tv_password);
        tv_passr = findViewById(R.id.tv_passwordr);
        emails = new ArrayList<String>();

        to_login = new Intent(this, MainActivity.class);
    }

    public void toLogin(View view) {
        startActivity(new Intent(Rejestracja.this, Logowanie.class));
    }

    public void register(View view) {
        new Register().execute();
    }

    public boolean sprawdzHaslo(String h1, String h2) {
        boolean zwroc = false;
        boolean duza_litera = true;
        boolean mala_litera = true;
        for (int i = 0; i < h1.length(); i++) {
            if (Character.isUpperCase(h1.charAt(i))) duza_litera = false;
            if (Character.isLowerCase(h1.charAt(i))) mala_litera = false;
        }
        if (h1.length() < 8) {
            error = "Hasło powinno posiadać minimum 8 znaków!";
        } else if (duza_litera || mala_litera) {
            error = "Użyj dużych i małych liter w haśle!";
        } else {
            if (h1.equals(h2)) {
                zwroc = true;
            } else {
                error = "Podane hasła nie są takie same!";
                zwroc = false;
            }
        }
        return zwroc;
    }

    public boolean sprwadzMail(String mail) {
        boolean zwroc = true;
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(mail);
        zwroc = matcher.matches();
        if (!zwroc) {
            error = "Błędny adres e-mail!";
        } else {
            for (String s : emails) {
                if (mail.equals(s)) {
                    zwroc = false;
                    error = "Adres e-mail jest już użyty!";
                    break;
                }
            }
        }
        return zwroc;
    }

    public boolean sprawdzImie(String imie) {

        boolean zwroc = true;
        if (imie.length() < 3) {
            zwroc = false;
            error = "Imię jest za krótkie!";
        } else if (!Character.isUpperCase(imie.charAt(0))) {
            zwroc = false;
            error = "Pierwsza litera powinna być duża!";
        } else {
            for (int i = 0; i < imie.length(); i++) {
                if (!Character.isLetter(imie.charAt(i))) {
                    zwroc = false;
                    error = "Imię składa się tylko z liter!";
                }
            }
        }
        return zwroc;
    }

    public String hash(String haslo) {
        return BCrypt.hashpw(haslo, BCrypt.gensalt(10));
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    class Register extends AsyncTask<ArrayList<String>, Integer, Boolean> {

        Connection connection;
        Statement statement;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            closeKeyboard();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        @SuppressLint("WrongThread")
        @Override
        protected final Boolean doInBackground(ArrayList<String>... param) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://cometics.xaa.pl/p581392_cosmetics?useSSL=false", "p581392", "eOtI2Yjz7");
                statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT email FROM users");
                while (rs.next()) {
                    emails.add(rs.getString(1));
                }

                if (sprwadzMail(tv_mail.getText().toString()) && sprawdzImie(tv_name.getText().toString()) && sprawdzHaslo(tv_pass.getText().toString(), tv_passr.getText().toString())) {
                    error = "";
                    statement.executeUpdate("INSERT INTO users (name, email, password) VALUES ('" + tv_name.getText().toString() + "', '" + tv_mail.getText().toString() + "', '" + hash(tv_pass.getText().toString()) + "')");
                    return true;
                }
            } catch (Exception e) {
                error = e.getMessage();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean pass) {
            super.onPostExecute(pass);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            try {
                connection.close();
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if (!pass) {
                Toast.makeText(getApplicationContext(), "Błąd rejestracji: " + error, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Rejestracja pomyślna!", Toast.LENGTH_LONG).show();
                startActivity(to_login);
            }
        }
    }
}