package com.example.analizakosmetyczna;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

public class Logowanie extends AppCompatActivity {

    TextView tv_mail, tv_pass, tv_hello;
    Button btn_login;
    String error;
    Intent i_logged;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logowanie);

        error = "";

        tv_mail = findViewById(R.id.tv_mail);
        tv_pass = findViewById(R.id.tv_pass);
        tv_hello = findViewById(R.id.tv_hello);
        btn_login = findViewById(R.id.btn_analyse);

        user = new User(getApplicationContext());
        i_logged = new Intent(this, MainActivity.class);
    }

    public void login(View view) {
        new Login().execute();
    }

    public void goToRegister(View view) {
        startActivity(new Intent(this, Rejestracja.class));
    }

    public boolean checkEmail(String mail) {
        boolean zwroc = true;
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(mail);
        zwroc = matcher.matches();
        if (!zwroc) {
            error = "Błędny adres e-mail!";
        }
        return zwroc;
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    class Login extends AsyncTask<ArrayList<String>, Integer, Boolean> {

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
                if (checkEmail(tv_mail.getText().toString())) {
                    ResultSet rs = statement.executeQuery("SELECT id, name, password, email FROM users WHERE email='" + tv_mail.getText().toString() + "';");
                    if (rs.next()) {
                        if (BCrypt.checkpw(tv_pass.getText().toString(), rs.getString(3))) {
                            user.createLogin(rs.getInt(1), rs.getString(2), rs.getString(4));
                            return true;
                        } else {
                            error = "Błędne hasło!";
                        }
                    } else {
                        error = "Niepoprawny adres e-mail!";
                    }
                } else {
                    error = "Niepoprawny adres e-mail!";
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
                Toast.makeText(getApplicationContext(), "Błąd logowania: " + error, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Logowanie pomyślne!", Toast.LENGTH_LONG).show();
                startActivity(i_logged);
            }
        }
    }
}