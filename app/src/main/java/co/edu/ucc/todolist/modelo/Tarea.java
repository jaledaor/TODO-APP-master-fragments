package co.edu.ucc.todolist.modelo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

/**
 * Created by Jaledaor on 29-Sep-17.
 */
@Entity
public class Tarea {

    @ColumnInfo(name="nombre")
    private String nombre;
    @ColumnInfo(name="fecha")
    private String fecha_task;
    @ColumnInfo(name="realizada")
    private boolean realizada;



    public Tarea() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isRealizada() {
        return realizada;
    }

    public void setRealizada(boolean realizada) {
        this.realizada = realizada;
    }

    public String getFecha_task() {return fecha_task;}

    public void setFecha_task(String fecha_task) {this.fecha_task = fecha_task;}
}
