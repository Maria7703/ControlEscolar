package com.example.controlescolar;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import android.database.Cursor;

import org.json.JSONObject;

public class FuncionesBD {

    public static void registrarDesdeJSON(Context context, JSONObject json, boolean esAlumno) {
        BaseDatos helper = new BaseDatos(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues valores = new ContentValues();

        try {
            if (esAlumno) {
                valores.put("numero_control", json.getString("numero_control"));
                valores.put("nombre", json.getString("nombre"));
                valores.put("semestre", json.getInt("semestre"));
                valores.put("carrera", json.getString("carrera"));
                db.insert("alumnos", null, valores);
                Toast.makeText(context, "Alumno registrado", Toast.LENGTH_SHORT).show();
            } else {
                valores.put("numero_docente", json.getString("numero_docente"));
                valores.put("nombre", json.getString("nombre"));
                valores.put("academia", json.getString("academia"));
                db.insert("docentes", null, valores);
                Toast.makeText(context, "Docente registrado", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
    }



    // --- CONSULTAR ---
    public static JSONObject consultarRegistro(Context context, String clave, boolean esAlumno) {
        BaseDatos helper = new BaseDatos(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        JSONObject resultado = null;

        String tabla = esAlumno ? "alumnos" : "docentes";
        String campoClave = esAlumno ? "numero_control" : "numero_docente";

        Cursor cursor = db.query(tabla, null, campoClave + "=?", new String[]{clave}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            resultado = new JSONObject();
            try {
                if (esAlumno) {
                    resultado.put("numero_control", cursor.getString(cursor.getColumnIndexOrThrow("numero_control")));
                    resultado.put("nombre", cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
                    resultado.put("semestre", cursor.getInt(cursor.getColumnIndexOrThrow("semestre")));
                    resultado.put("carrera", cursor.getString(cursor.getColumnIndexOrThrow("carrera")));
                } else {
                    resultado.put("numero_docente", cursor.getString(cursor.getColumnIndexOrThrow("numero_docente")));
                    resultado.put("nombre", cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
                    resultado.put("academia", cursor.getString(cursor.getColumnIndexOrThrow("academia")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        }
        db.close();
        return resultado;
    }

    // --- ELIMINAR ---
    public static boolean eliminarRegistro(Context context, String clave, boolean esAlumno) {
        BaseDatos helper = new BaseDatos(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String tabla = esAlumno ? "alumnos" : "docentes";
        String campoClave = esAlumno ? "numero_control" : "numero_docente";

        int filasAfectadas = db.delete(tabla, campoClave + "=?", new String[]{clave});
        db.close();

        return filasAfectadas > 0;
    }

    // --- ACTUALIZAR ---
    public static boolean actualizarDesdeJSON(Context context, JSONObject json, boolean esAlumno) {
        BaseDatos helper = new BaseDatos(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues valores = new ContentValues();

        try {
            String campoClave;
            String clave;

            if (esAlumno) {
                campoClave = "numero_control";
                clave = json.getString(campoClave);
                valores.put("nombre", json.getString("nombre"));
                valores.put("carrera", json.getString("carrera"));
            } else {
                campoClave = "numero_docente";
                clave = json.getString(campoClave);
                valores.put("nombre", json.getString("nombre"));
                valores.put("academia", json.getString("academia"));
            }

            int filasAfectadas = db.update(esAlumno ? "alumnos" : "docentes", valores, campoClave + "=?", new String[]{clave});
            db.close();

            return filasAfectadas > 0;

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

}
