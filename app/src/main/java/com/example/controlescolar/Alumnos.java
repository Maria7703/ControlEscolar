package com.example.controlescolar;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;

public class Alumnos extends AppCompatActivity {

    EditText campoNumero, campoNombre, campoSemestre, campoCarrera, campoTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alumnos); // tu XML

        campoNumero = findViewById(R.id.numEdit);
        campoNombre = findViewById(R.id.nomEdit);
        campoSemestre = findViewById(R.id.areaEdit);

        findViewById(R.id.btnAgregar).setOnClickListener(v -> registrarAlumno());
        findViewById(R.id.btnConsultar).setOnClickListener(v -> consultarAlumno());
        findViewById(R.id.btnModificar).setOnClickListener(v -> actualizarAlumno());
        findViewById(R.id.btnEliminar).setOnClickListener(v -> eliminarAlumno());
    }

    void registrarAlumno() {
        try {
            JSONObject json = new JSONObject();
            json.put("numero_control", campoNumero.getText().toString());
            json.put("nombre", campoNombre.getText().toString());
            json.put("semestre", Integer.parseInt(campoSemestre.getText().toString()));
            json.put("carrera", campoCarrera.getText().toString());
            json.put("telefono", campoTelefono.getText().toString());

            FuncionesBD.registrarDesdeJSON(this, json, true);
            campoNumero.setText("");
            campoNombre.setText("");
            campoSemestre.setText("");
            campoCarrera.setText("");
            campoTelefono.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void consultarAlumno() {
        String clave = campoNumero.getText().toString();
        JSONObject json = FuncionesBD.consultarRegistro(this, clave, true);
        if (json != null) {
            try {
                campoNombre.setText(json.getString("nombre"));
                campoSemestre.setText(String.valueOf(json.getInt("semestre")));
                campoCarrera.setText(json.getString("carrera"));
                campoTelefono.setText(json.getString("telefono"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Alumno no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    void actualizarAlumno() {
        try {
            JSONObject json = new JSONObject();
            json.put("numero_control", campoNumero.getText().toString());
            json.put("nombre", campoNombre.getText().toString());
            json.put("semestre", Integer.parseInt(campoSemestre.getText().toString()));
            json.put("carrera", campoCarrera.getText().toString());
            json.put("telefono", campoTelefono.getText().toString());

            boolean exito = FuncionesBD.actualizarDesdeJSON(this, json, true);
            Toast.makeText(this, exito ? "Alumno actualizado" : "No se pudo actualizar", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void eliminarAlumno() {
        String clave = campoNumero.getText().toString();
        boolean exito = FuncionesBD.eliminarRegistro(this, clave, true);
        Toast.makeText(this, exito ? "Alumno eliminado" : "No se pudo eliminar", Toast.LENGTH_SHORT).show();
    }
}

