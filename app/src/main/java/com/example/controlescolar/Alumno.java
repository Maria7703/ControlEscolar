package com.example.controlescolar;


public class Alumno {
    private String numeroControl;
    private String nombre;
    private String carrera;

    public Alumno(String numeroControl, String nombre, String carrera) {
        this.numeroControl = numeroControl;
        this.nombre = nombre;
        this.carrera = carrera;
    }

    public String getNumeroControl() {
        return numeroControl;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCarrera() {
        return carrera;
    }
}
