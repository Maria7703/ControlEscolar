package com.example.controlescolar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatosHelper extends SQLiteOpenHelper {

    private static final String NOMBRE_BD = "ControlEscolar.db";
    private static final int VERSION_BD = 1;


    public static final String TABLA_ALUMNOS = "alumnos";
    public static final String CAMPO_NUMERO_CONTROL = "numEdit";
    public static final String CAMPO_NOMBRE = "nomEdit";
    public static final String CAMPO_CARRERA = "areaEdit";


    public static final String TABLA_DOCENTES = "docentes";
    public static final String CAMPO_ID_DOCENTE = "id_docente";
    public static final String CAMPO_NOMBRE_DOCENTE = "nombre";
    public static final String CAMPO_CARRERA_DOCENTE = "carrera";

    public BaseDatosHelper(Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String crearTablaAlumnos = "CREATE TABLE " + TABLA_ALUMNOS + " (" +
                CAMPO_NUMERO_CONTROL + " TEXT PRIMARY KEY, " +
                CAMPO_NOMBRE + " TEXT, " +
                CAMPO_CARRERA + " TEXT)";
        db.execSQL(crearTablaAlumnos);


        String crearTablaDocentes = "CREATE TABLE " + TABLA_DOCENTES + " (" +
                CAMPO_ID_DOCENTE + " TEXT PRIMARY KEY, " +
                CAMPO_NOMBRE_DOCENTE + " TEXT, " +
                CAMPO_CARRERA_DOCENTE + " TEXT)";
        db.execSQL(crearTablaDocentes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_ALUMNOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_DOCENTES);
        onCreate(db);
    }
}
