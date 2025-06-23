package com.example.controlescolar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Environment;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;


public class AlumnosActivity extends AppCompatActivity {

    ImageButton regresar1;

    String idioma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alumnos);

        regresar1 = findViewById(R.id.regresar1);

        idioma = getIntent().getStringExtra("idioma");
        if (idioma == null) idioma = "es"; // por defecto español
        cargarIdiomaAlumnos(idioma);

        regresar1.setOnClickListener(v -> {
            Intent intent = new Intent(AlumnosActivity.this, MainActivity.class);
            intent.putExtra("idioma", idioma);
            startActivity(intent);
        });

        Button botonExportarJSON = findViewById(R.id.botonexportarjson);
        botonExportarJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportarJSON();
            }
        });

        Button botonRegistrar = findViewById(R.id.btnAgregar);

        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText inputNumeroControl = findViewById(R.id.numEdit);
                String NumeroControlTexto = inputNumeroControl.getText().toString().trim();

                EditText inputNombre = findViewById(R.id.nomEdit);
                String NombreTexto = inputNombre.getText().toString().trim();

                EditText inputCarrera = findViewById(R.id.areaEdit);
                String CarreraTexto = inputCarrera.getText().toString().trim();

                if (!NumeroControlTexto.isEmpty() && !NombreTexto.isEmpty() && !CarreraTexto.isEmpty()) {

                    BaseDatosHelper dbHelper = new BaseDatosHelper(AlumnosActivity.this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();


                    String consulta = "SELECT * FROM " + BaseDatosHelper.TABLA_ALUMNOS +
                            " WHERE " + BaseDatosHelper.CAMPO_NUMERO_CONTROL + " = ?";
                    Cursor cursor = db.rawQuery(consulta, new String[]{NumeroControlTexto});

                    if (cursor.moveToFirst()) {
                        Toast.makeText(AlumnosActivity.this, "Este número de control ya está registrado", Toast.LENGTH_SHORT).show();
                    } else {
                        ContentValues valores = new ContentValues();
                        valores.put(BaseDatosHelper.CAMPO_NUMERO_CONTROL, NumeroControlTexto);
                        valores.put(BaseDatosHelper.CAMPO_NOMBRE, NombreTexto);
                        valores.put(BaseDatosHelper.CAMPO_CARRERA, CarreraTexto);

                        long resultado = db.insert(BaseDatosHelper.TABLA_ALUMNOS, null, valores);

                        if (resultado != -1) {
                            Toast.makeText(AlumnosActivity.this, "Alumno registrado correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AlumnosActivity.this, "Error al registrar alumno", Toast.LENGTH_SHORT).show();
                        }
                    }

                    cursor.close();
                    db.close();

                } else {
                    Toast.makeText(AlumnosActivity.this, "Por favor, llene todos los campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });






    }

    //funcion idiomas
    private void cargarIdiomaAlumnos(String idioma) {
        try {
            InputStream is = getAssets().open(idioma + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String jsonStr = new String(buffer, StandardCharsets.UTF_8);
            JSONObject obj = new JSONObject(jsonStr);

            // Aplica los textos a los elementos
            ((TextView) findViewById(R.id.textView5)).setText(obj.getString("titulo"));
            ((TextView) findViewById(R.id.nomText)).setText(obj.getString("nombre"));
            ((TextView) findViewById(R.id.numText)).setText(obj.getString("numero"));
            ((TextView) findViewById(R.id.carreraText)).setText(obj.getString("carrera"));
            ((TextView) findViewById(R.id.semestreText)).setText(obj.getString("semestre"));

            ((EditText) findViewById(R.id.nomEdit)).setHint(obj.getString("nombre"));
            ((EditText) findViewById(R.id.numEdit)).setHint(obj.getString("numero"));
            ((EditText) findViewById(R.id.areaEdit)).setHint(obj.getString("carrera"));
            ((EditText) findViewById(R.id.semestreEdit)).setHint(obj.getString("semestre"));

            ((Button) findViewById(R.id.btnAgregar)).setText(obj.getString("agregar"));
            ((Button) findViewById(R.id.btnEliminar)).setText(obj.getString("eliminar"));
            ((Button) findViewById(R.id.btnConsultar)).setText(obj.getString("consultar"));
            ((Button) findViewById(R.id.btnModificar)).setText(obj.getString("modificar"));

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    //Funcion exportar codigo JSON a la carpeta de descargas del celular (equipo Diego Fregoso)
    private void exportarJSON() {
        BaseDatosHelper dbHelper = new BaseDatosHelper(AlumnosActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        JSONArray jsonArray = new JSONArray();

        Cursor cursor = db.rawQuery("SELECT * FROM " + BaseDatosHelper.TABLA_ALUMNOS, null);

        if (cursor.moveToFirst()) {
            do {
                JSONObject alumno = new JSONObject();
                try {
                    alumno.put("numero_control", cursor.getString(cursor.getColumnIndexOrThrow(BaseDatosHelper.CAMPO_NUMERO_CONTROL)));
                    alumno.put("nombre", cursor.getString(cursor.getColumnIndexOrThrow(BaseDatosHelper.CAMPO_NOMBRE)));
                    alumno.put("carrera", cursor.getString(cursor.getColumnIndexOrThrow(BaseDatosHelper.CAMPO_CARRERA)));
                    jsonArray.put(alumno);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        try {
            String nombreArchivo = "alumnos.json";

            File directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File archivo = new File(directorio, nombreArchivo);

            FileOutputStream fos = new FileOutputStream(archivo);
            fos.write(jsonArray.toString(4).getBytes());
            fos.close();

            Toast.makeText(AlumnosActivity.this, "Archivo guardado en: " + archivo.getAbsolutePath(), Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(AlumnosActivity.this, "Error al exportar", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }





}
