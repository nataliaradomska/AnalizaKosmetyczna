package com.example.analizakosmetyczna;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ScanProduct extends AppCompatActivity {

    TextView tv_data;
    EditText tv_scanned_ingredients;
    Button take_photo, copy_data;
    Bitmap bitmap;
    Intent i_result;
    private static final int REQUEST_CAMERA_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_product);

        tv_data = findViewById(R.id.text_data);
        tv_scanned_ingredients = findViewById(R.id. tv_scanned_ingredients);
        take_photo = findViewById(R.id.take_photo);
        copy_data = findViewById(R.id.copy_data);

        if (ContextCompat.checkSelfPermission(ScanProduct.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ScanProduct.this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }

        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(ScanProduct.this);
            }
        });

        copy_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String scanned_text = tv_scanned_ingredients.getText().toString();

                copyToClipBoard(scanned_text);
                if (scanned_text.length() <= 2) {
                    Toast.makeText(getApplicationContext(), "Wprowadź dane poprawnie.", Toast.LENGTH_LONG).show();
                } else {
                    ArrayList<String> param = new ArrayList<>();
                    String[] ingre = scanned_text.split(",");
                    for (String s : ingre) {
                        param.add(s.toUpperCase());
                        System.out.println(s);
                    }

                    new Analyse().execute(param);
                }
            }
        });

        i_result = new Intent(this, Results.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    getTextFromImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void getTextFromImage(Bitmap bitmap) {
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if (!recognizer.isOperational()) {
            Toast.makeText(ScanProduct.this, "Error", Toast.LENGTH_SHORT);
        }
        else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < textBlockSparseArray.size(); i++) {
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
            tv_scanned_ingredients.setText(stringBuilder.toString());
            take_photo.setText("Zrób zdjęcie ponownie");
            copy_data.setVisibility(View.VISIBLE);
        }
    }

    private void copyToClipBoard(String text) {
        ClipboardManager clipBoard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Skopiowano", text);
        clipBoard.setPrimaryClip(clip);
        Toast.makeText(ScanProduct.this, "Skopiowano", Toast.LENGTH_SHORT);
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