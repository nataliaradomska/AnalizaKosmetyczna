package com.example.analizakosmetyczna;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Results extends AppCompatActivity {

    // globally
    TextView tv_polecam, tv_polecam_ale, tv_nie_polecam;
    int polecam, polecam_ale, nie_polecam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ListView listview = findViewById(R.id.listview);
        ArrayList<Ingredient> ingredient_list = (ArrayList<Ingredient>) getIntent().getSerializableExtra("INGREDIENTS");

        polecam = 0;
        polecam_ale = 0;
        nie_polecam = 0;
        tv_polecam = (TextView) findViewById(R.id.polecam);
        tv_polecam_ale = (TextView) findViewById(R.id.polecam_ale);
        tv_nie_polecam = (TextView) findViewById(R.id.nie_polecam);

//        Ingredient aqua = new Ingredient("Aqua", "Woda - jest to substancja, która jest rozpuszczalnikiem. " +
//                "Po nałożeniu produktu kosmetycznego na skórę, woda pod wpływem temperatury ciała wyparowuje.", "Polecam");
//        Ingredient glycerin = new Ingredient("Glycerin", "Gliceryna - jest to substancja nawilżająca. Ma zdolność przenikania przez warstwę rogową naskórka, " +
//                "pomaga substancjom aktywnym przedostać się w głąb skóry. Posiada bardzo silne działanie higroskopijne.", "Polecam, ale");
//        Ingredient benzyl_sal = new Ingredient("Benzyl salicylate", "Salicylan benzylu - jest to składnik kompozycji zapachowych lub aromatycznych, może wywoływać alergie.",
//                "Nie polecam");
//        Ingredient nylon_12 = new Ingredient("Nylon-12", "Nylon - substancja regulująca lepkość i spęczniająca, środek zmętniający.",
//                "Polecam");
//        Ingredient caprylyl_glycol = new Ingredient("Caprylyl glycol", "Salicylan benzylu - jest to nośnik substancji aktywnej. Jako humekant odpowiada za utrzymanie " +
//                "odpowiedniego poziomu wilgoci produktu. Wykazuje działanie natłuszczające, zmiękczające, nawilżające i wygładzające. " +
//                "Tworzy warstwę okluzyjną na skórze. Jest komedogenna i może wywoływać reakcje alergiczne.",
//                "Polecam");
//
//        ingredient_list.add(aqua);
//        ingredient_list.add(glycerin);
//        ingredient_list.add(benzyl_sal);
//        ingredient_list.add(nylon_12);
//        ingredient_list.add(caprylyl_glycol);

        for (int i = 0; i<ingredient_list.size(); i++) {
            if(ingredient_list.get(i).getRate().equals("Polecam")) polecam++;
            if(ingredient_list.get(i).getRate().equals("Polecam, ale")) polecam_ale++;
            if(ingredient_list.get(i).getRate().equals("Nie polecam")) nie_polecam++;
        }

        tv_polecam.setText(String.valueOf(polecam));
        tv_polecam_ale.setText(String.valueOf(polecam_ale));
        tv_nie_polecam.setText(String.valueOf(nie_polecam));

        IngredientListAdapter adapter = new IngredientListAdapter(this, R.layout.adapter_view_layout, ingredient_list);
        listview.setAdapter(adapter);

    }


}