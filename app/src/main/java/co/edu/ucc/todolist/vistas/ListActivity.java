package co.edu.ucc.todolist.vistas;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.edu.ucc.todolist.ControlActivity;
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

    private FirebaseAuth mAuth_control;
    private FirebaseDatabase database_control;
    private DatabaseReference reference_control;
    String UID = "";



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

        mAuth_control = FirebaseAuth.getInstance();
        database_control = FirebaseDatabase.getInstance();
        reference_control = database_control.getReference("tareas");
        UID = mAuth_control.getCurrentUser().getUid();


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
        final TextView fecha_select = (TextView) mView.findViewById(R.id.fecha_elegida);

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

                        Tarea objTarea = new Tarea();
                        objTarea.setNombre(m_Text);
                        objTarea.setRealizada(false);
                        objTarea.setFecha_task(fecha_select.getText().toString());

                        listPresenter.addTarea(m_Text, fecha_select.getText().toString());
                        reference_control.child(UID).push().setValue(objTarea);
                        /*reference_control.child(UID).setValue(objTarea).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                } else {
                                    Toast.makeText(ListActivity.this,
                                            "Error " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });*/
                            dialog.dismiss();

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
