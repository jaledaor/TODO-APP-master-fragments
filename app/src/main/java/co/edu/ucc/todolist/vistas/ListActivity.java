package co.edu.ucc.todolist.vistas;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.edu.ucc.todolist.LoginActivity;
import co.edu.ucc.todolist.R;
import co.edu.ucc.todolist.modelo.Tarea;
import co.edu.ucc.todolist.vistas.adaptadores.TodoListAdapter;
import co.edu.ucc.todolist.vistas.presenters.IListPresenter;
import co.edu.ucc.todolist.vistas.presenters.ListPresenter;


public class ListActivity extends AppCompatActivity implements
        IListView, TodoListAdapter.OnListenerItemCheck {

    private IListPresenter listPresenter;

    private Toast mToast;

    @BindView(R.id.rvListTODO)
    RecyclerView rvListTODO;

    @BindView(R.id.txtTarea)
    EditText txtTarea;

    public String m_Text = "";
    public String fecha_picker = "";
    public int dayOfMonth;
    public int month;
    public int year;
    DatePickerDialog datePickerDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ButterKnife.bind(this);

        listPresenter = new ListPresenter(this);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        rvListTODO.setLayoutManager(llm);


        List<Tarea> lsTarea = listPresenter.obtenerTareas();

        rvListTODO.setAdapter(new TodoListAdapter(lsTarea, this));

    }
    /*private void showToast(String message) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }

        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }*/


    @OnClick(R.id.btnEnviarTarea)
    @Override
    public void clickAddTarea() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_datepicker, null);
        final EditText input = (EditText) mView.findViewById(R.id.text_input);
        final Button btn_fecha = (Button) mView.findViewById(R.id.btn_cargar_date);
        final EditText fecha_select = (EditText) mView.findViewById(R.id.fecha_elegida);

        btn_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar myCalendar = Calendar.getInstance();
                dayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH);
                month = myCalendar.get(Calendar.MONTH);
                year = myCalendar.get(Calendar.YEAR);
                datePickerDialog = new DatePickerDialog(ListActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                fecha_select.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        builder.setView(mView)
                .setTitle("Adicionar Tarea")
                .setPositiveButton("OK", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                            Toast.makeText(ListActivity.this,
                                    R.string.success_login_msg,
                                    Toast.LENGTH_SHORT).show();
                            m_Text = input.getText().toString();
                            dialog.dismiss();
                            listPresenter.addTarea(m_Text, fecha_select.getText().toString());

                    }
                })
                .setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


        final AlertDialog dialog = builder.create();

        dialog.show();

        /*String descTarea = input.getText().toString();*/

    }


    @Override
    public void refrescarListaTareas() {
        rvListTODO.getAdapter().notifyDataSetChanged();

        rvListTODO.scrollToPosition(
                rvListTODO.getAdapter().getItemCount() - 1);

        txtTarea.setText("");
    }

    @Override
    public void refrescarTarea(int posicion) {
        rvListTODO.getAdapter().notifyItemChanged(posicion);

    }


    @Override
    public void itemCambioEstado(int posicion, boolean realizada) {
        listPresenter.itemCambioEstado(posicion, realizada);
    }

}
