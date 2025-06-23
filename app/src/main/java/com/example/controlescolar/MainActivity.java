package com.example.controlescolar;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    Button btnAlumnos, btnDocentes, btnIdioma;
    String nuevoIdioma, idiomaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        cargarIdiomaDesdeJSON("es"); // Carga por defecto

        btnAlumnos = findViewById(R.id.btnAlumnos);
        btnDocentes = findViewById(R.id.btnDocentes);

        idiomaActual = getIntent().getStringExtra("idioma");
        if (idiomaActual == null) idiomaActual = "es"; // por defecto español
        cargarIdiomaDesdeJSON(idiomaActual);

        btnAlumnos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AlumnosActivity.class);
            intent.putExtra("idioma", idiomaActual);
            startActivity(intent);
        });

        btnIdioma = findViewById(R.id.btnIdioma);
        btnIdioma.setOnClickListener(v -> {
             nuevoIdioma = idiomaActual.equals("es") ? "en" : "es";
            cargarIdiomaDesdeJSON(nuevoIdioma);
        });



    };

//    private String idiomaActual = "es";

    private void cargarIdiomaDesdeJSON(String idioma) {
        try {
            InputStream is = getAssets().open(idioma + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String jsonStr = new String(buffer, StandardCharsets.UTF_8);
            JSONObject obj = new JSONObject(jsonStr);

            // Asignar textos
            ((TextView) findViewById(R.id.textView5)).setText(obj.getString("titulo"));
            ((Button) findViewById(R.id.btnAlumnos)).setText(obj.getString("alumnos"));
            ((Button) findViewById(R.id.btnDocentes)).setText(obj.getString("docentes"));

            // También actualiza el botón de idioma
            Button btnIdioma = findViewById(R.id.btnIdioma);
            btnIdioma.setText(idioma.equals("es") ? "🌐 EN" : "🌐 ES");

            idiomaActual = idioma;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


};

