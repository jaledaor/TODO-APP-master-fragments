package co.edu.ucc.todolist.vistas.presenters;

import java.util.List;

import co.edu.ucc.todolist.dominio.ILtarea;
import co.edu.ucc.todolist.dominio.LTarea;
import co.edu.ucc.todolist.modelo.Tarea;
import co.edu.ucc.todolist.vistas.IListView;

/**
 * Created by Jaledaor on 29-Sep-17.
 */

public class ListPresenter implements IListPresenter {

    private IListView view;
    private ILtarea ltarea;

    public ListPresenter(IListView view){
        this.view = view;
        ltarea = new LTarea();
    }

    @Override
    public void addTarea(String descTarea, String fecha) {
        Tarea objTarea = new Tarea();

        objTarea.setNombre(descTarea);
        objTarea.setRealizada(false);
        objTarea.setFecha_task(fecha);

        ltarea.addTarea(objTarea);

        view.refrescarListaTareas(ltarea.getTareas());
    }

    @Override
    public List<Tarea> obtenerTareas() {
        return ltarea.getTareas();
    }

    @Override
    public void itemCambioEstado(int posicion, boolean realizado) {
        Tarea tarea= ltarea.obtenerXID(posicion+1);
        tarea.setRealizada(realizado);
        ltarea.actualizar(tarea);
        view.refrescarTarea(tarea, posicion);
    }
}
