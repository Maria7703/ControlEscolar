package com.example.controlescolar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper {

    public static final String NOMBRE_BD = "registro.db";
    public static final int VERSION = 1;

    public BaseDatos(Context context) {
        super(context, NOMBRE_BD, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE alumnos (numero_control TEXT PRIMARY KEY, nombre TEXT, carrera INTEGER)");
        db.execSQL("CREATE TABLE docentes (numero_docente TEXT PRIMARY KEY, nombre TEXT, academia TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS alumnos");
        db.execSQL("DROP TABLE IF EXISTS docentes");
        onCreate(db);
    }
}
