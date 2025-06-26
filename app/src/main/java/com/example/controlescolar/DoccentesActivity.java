package com.example.controlescolar;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.content.Intent;
import android.view.View;
import android.widget.Button;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;



public class DoccentesActivity extends AppCompatActivity {
    EditText campoNumero, campoNombre, campoAcademia;
    String idioma;
    ImageButton regresar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.docentes); // tu XML
        regresar1 = findViewById(R.id.regresar1);

        idioma = getIntent().getStringExtra("idioma");
        if (idioma == null) idioma = "es"; // por defecto español
//        cargarIdiomaAlumnos(idioma);

        regresar1.setOnClickListener(v -> {
            Intent intent = new Intent(DoccentesActivity.this, MainActivity.class);
//            intent.putExtra("idioma", idioma);
            startActivity(intent);
        });

        campoNumero = findViewById(R.id.numEdit);
        campoNombre = findViewById(R.id.nomEdit);
        campoAcademia = findViewById(R.id.areaEdit);

        findViewById(R.id.btnAgregar).setOnClickListener(v -> registrarDocente());
        findViewById(R.id.btnConsultar).setOnClickListener(v -> consultarDocente());
        findViewById(R.id.btnEliminar).setOnClickListener(v -> eliminarDocente());
        findViewById(R.id.btnModificar).setOnClickListener(v -> actualizarDocente());
    }

    void registrarDocente() {
        try {
            JSONObject json = new JSONObject();
            json.put("numero_docente", campoNumero.getText().toString());
            json.put("nombre", campoNombre.getText().toString());
            json.put("academia", campoAcademia.getText().toString());

            FuncionesBD.registrarDesdeJSON(this, json, false);

            campoNumero.setText("");
            campoNombre.setText("");
            campoAcademia.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void consultarDocente() {
        String clave = campoNumero.getText().toString();
        JSONObject json = FuncionesBD.consultarRegistro(this, clave, false);
        if (json != null) {
            try {
                campoNombre.setText(json.getString("nombre"));
                campoAcademia.setText(json.getString("academia"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Docente no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    void eliminarDocente() {
        String clave = campoNumero.getText().toString();
        boolean exito = FuncionesBD.eliminarRegistro(this, clave, false);
        Toast.makeText(this, exito ? "Docente eliminado" : "No se pudo eliminar", Toast.LENGTH_SHORT).show();
    }

    void actualizarDocente() {
        try {
            JSONObject json = new JSONObject();
            json.put("numero_docente", campoNumero.getText().toString());
            json.put("nombre", campoNombre.getText().toString());
            json.put("academia", campoAcademia.getText().toString());

            boolean exito = FuncionesBD.actualizarDesdeJSON(this, json, false);
            Toast.makeText(this, exito ? "Docente actualizado" : "No se pudo actualizar", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
