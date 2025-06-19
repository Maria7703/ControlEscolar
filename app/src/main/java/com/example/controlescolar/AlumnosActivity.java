package com.example.controlescolar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class AlumnosActivity extends AppCompatActivity {

    ImageButton regresar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alumnos);

        regresar1 = findViewById(R.id.regresar1);

        regresar1.setOnClickListener(v -> {
            Intent intent = new Intent(AlumnosActivity.this, MainActivity.class);
            startActivity(intent);
        });


    }


}
