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

public class MainActivity extends AppCompatActivity {

    Button btnAlumnos, btnDocentes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnAlumnos = findViewById(R.id.btnAlumnos);
        btnDocentes = findViewById(R.id.btnDocentes);

        btnAlumnos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AlumnosActivity.class);
            startActivity(intent);
        });


    };
};