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

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.textfield.TextInputEditText;

import java.io.File;


public class AlumnosActivity extends AppCompatActivity {

    ImageButton regresar1;

    String idioma;

    private RecyclerView alumnosRecyclerView;

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

        Button botonExportarEXCEL = findViewById(R.id.botonexportarExcel);
        botonExportarJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportarAlumnosAExcel();
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

        alumnosRecyclerView = findViewById(R.id.alumnosRecyclerView);

        // Leer datos del JSON
        JSONHelper helper = new JSONHelper(this);
        Data data = helper.readDataFromAssets();

        if (data != null && data.getAlumnos() != null) {
            AlumnosAdapter adapter = new AlumnosAdapter(data.getAlumnos());
            alumnosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            alumnosRecyclerView.setAdapter(adapter);
        }







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

            ((EditText) findViewById(R.id.nomEdit)).setHint(obj.getString("nombre"));
            ((EditText) findViewById(R.id.numEdit)).setHint(obj.getString("numero"));
            ((EditText) findViewById(R.id.areaEdit)).setHint(obj.getString("carrera"));

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

    //Funcion exportar alumnos a Excel (equipo Ramon)
    private void exportarAlumnosAExcel() {
        // Inicializar base de datos
        BaseDatosHelper baseDatosHelper = new BaseDatosHelper(this);
        SQLiteDatabase db = baseDatosHelper.getReadableDatabase();

        // Obtener los datos de los alumnos desde la base de datos
        Cursor cursor = db.rawQuery("SELECT * FROM " + BaseDatosHelper.TABLA_ALUMNOS, null);
        JSONArray alumnosArray = new JSONArray();

        if (cursor.moveToFirst()) {
            do {
                JSONObject alumno = new JSONObject();
                try {
                    alumno.put("numero_control", cursor.getString(cursor.getColumnIndexOrThrow(BaseDatosHelper.CAMPO_NUMERO_CONTROL)));
                    alumno.put("nombre", cursor.getString(cursor.getColumnIndexOrThrow(BaseDatosHelper.CAMPO_NOMBRE)));
                    alumno.put("carrera", cursor.getString(cursor.getColumnIndexOrThrow(BaseDatosHelper.CAMPO_CARRERA)));
                    alumnosArray.put(alumno);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error al procesar un alumno: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // Crear un archivo Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Alumnos");
        Row headerRow = sheet.createRow(0);

        // Encabezados
        String[] columnas = {"No Control", "Nombre", "Carrera"};
        for (int i = 0; i < columnas.length; i++) {
            headerRow.createCell(i).setCellValue(columnas[i]);
        }

        // Llenar los datos
        for (int i = 0; i < alumnosArray.length(); i++) {
            try {
                JSONObject alumno = alumnosArray.getJSONObject(i);
                Row row = sheet.createRow(i + 1);

                row.createCell(0).setCellValue(alumno.optString("numero_control", "No disponible"));
                row.createCell(1).setCellValue(alumno.optString("nombre", "No disponible"));
                row.createCell(2).setCellValue(alumno.optString("carrera", "No disponible"));
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al acceder a los datos de un alumno: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        // Guardar el archivo Excel en la carpeta de Descargas
        try {
            String nombreArchivo = "alumnos.xlsx";
            File directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File archivo = new File(directorio, nombreArchivo);

            FileOutputStream fos = new FileOutputStream(archivo);
            workbook.write(fos);
            fos.close();

            Toast.makeText(this, "Excel guardado en: " + archivo.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al exportar", Toast.LENGTH_SHORT).show();
        }
    }






}
