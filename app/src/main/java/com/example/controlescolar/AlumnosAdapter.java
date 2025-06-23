package com.example.controlescolar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AlumnosAdapter extends RecyclerView.Adapter<AlumnosAdapter.AlumnoViewHolder> {

    private List<Alumno> alumnos;

    public AlumnosAdapter(List<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    @Override
    public AlumnoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alumno, parent, false);
        return new AlumnoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlumnoViewHolder holder, int position) {
        Alumno alumno = alumnos.get(position);
        holder.nombre.setText(alumno.getNombre());
        holder.numeroControl.setText("No. Control: " + alumno.getNumeroControl());
        holder.carrera.setText("Carrera: " + alumno.getCarrera());
    }

    @Override
    public int getItemCount() {
        return alumnos.size();
    }

    public static class AlumnoViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, numeroControl, carrera;

        public AlumnoViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.alumnoNombre);
            numeroControl = itemView.findViewById(R.id.alumnoNumeroControl);
            carrera = itemView.findViewById(R.id.alumnoCarrera);
        }
    }
}
