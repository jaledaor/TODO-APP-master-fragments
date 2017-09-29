package co.edu.ucc.todolist;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.edu.ucc.todolist.entidades.Espacios;

public class ControlActivity extends AppCompatActivity {


    @BindView(R.id.btn_Bano)
    Button btn_Bano;

    @BindView(R.id.btn_Cocina)
    Button btn_Cocina;

    @BindView(R.id.btn_Habitacion)
    Button btn_Habitacion;

    @BindView(R.id.btn_Sala)
    Button btn_Sala;

    private FirebaseAuth mAuth_control;
    private FirebaseDatabase database_control;
    private DatabaseReference reference_control;
    private DatabaseReference reference_usrname;
    private ValueEventListener eventListener;

    private int estado_bano;
    private int estado_cocina;
    private int estado_alcoba;
    private int estado_sala;

    String UID = "";
    String estado_bano_temp = "";
    String estado_cocina_temp = "";
    String estado_alcoba_temp = "";
    String estado_sala_temp = "";

    String usernameTaken = "";
    Espacios espacio = new Espacios();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String nomUsuario = getIntent().getStringExtra("nomUsuario");

        setTitle("Bienvenido " + nomUsuario);
        setContentView(R.layout.activity_control);
        ButterKnife.bind(this);
        mAuth_control = FirebaseAuth.getInstance();
        database_control = FirebaseDatabase.getInstance();
        reference_control = database_control.getReference("hogar");
        UID = mAuth_control.getCurrentUser().getUid();
        cargarData();
    }

    public void cargarData(){

/*inicio cambio*/
        reference_control.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    estado_bano_temp = snapshot.child(UID).child("estado_bano").getValue().toString();
                    estado_cocina_temp = snapshot.child(UID).child("estado_cocina").getValue().toString();
                    estado_alcoba_temp = snapshot.child(UID).child("estado_habitacion").getValue().toString();
                    estado_sala_temp = snapshot.child(UID).child("estado_sala").getValue().toString();
                    espacio.setEstado_bano(Integer.parseInt(estado_bano_temp));
                    espacio.setEstado_sala(Integer.parseInt(estado_sala_temp));
                    espacio.setEstado_habitacion(Integer.parseInt(estado_alcoba_temp));
                    espacio.setEstado_cocina(Integer.parseInt(estado_cocina_temp));
                    /**/
                    if (estado_bano_temp.equals("1")) {
                        btn_Bano.setBackgroundColor(Color.YELLOW);
                        estado_bano = 1;
                    } else {
                        estado_bano = 0;
                        btn_Bano.setBackgroundColor(Color.RED);
                    }
                    if (estado_cocina_temp.equals("1")) {
                        btn_Cocina.setBackgroundColor(Color.YELLOW);
                        estado_cocina = 1;
                    } else {
                        btn_Cocina.setBackgroundColor(Color.RED);
                        estado_cocina = 0;
                    }

                    if (estado_alcoba_temp.equals("1")) {
                        btn_Habitacion.setBackgroundColor(Color.YELLOW);
                        estado_alcoba = 1;
                    } else {
                        btn_Habitacion.setBackgroundColor(Color.RED);
                        estado_alcoba = 0;
                    }

                    if (estado_sala_temp.equals("1")) {
                        btn_Sala.setBackgroundColor(Color.YELLOW);
                        estado_sala = 1;
                    } else {
                        btn_Sala.setBackgroundColor(Color.RED);
                        estado_sala = 0;
                    }

                    /**/
                } else {
                    Toast.makeText(ControlActivity.this, "no hay datos ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @OnClick(R.id.btn_Bano)
    public void clickBano() {
        UID = mAuth_control.getCurrentUser().getUid();
        if (estado_bano == 0) {
            estado_bano = 1;
            espacio.setEstado_habitacion(espacio.getEstado_habitacion());
            espacio.setEstado_bano(estado_bano);
            espacio.setEstado_cocina(espacio.getEstado_cocina());
            espacio.setEstado_sala(espacio.getEstado_sala());
            btn_Bano.setBackgroundColor(Color.YELLOW);
            reference_control.child(UID).setValue(espacio).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                    } else {
                        Toast.makeText(ControlActivity.this,
                                "Error " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            estado_bano = 0;
            espacio.setEstado_habitacion(espacio.getEstado_habitacion());
            espacio.setEstado_bano(estado_bano);
            espacio.setEstado_cocina(espacio.getEstado_cocina());
            espacio.setEstado_sala(espacio.getEstado_sala());
            btn_Bano.setBackgroundColor(Color.RED);
            reference_control.child(UID).setValue(espacio).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                    } else {
                        Toast.makeText(ControlActivity.this,
                                "Error " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @OnClick(R.id.btn_Cocina)
    public void clickCocina() {
        UID = mAuth_control.getCurrentUser().getUid();
        if (estado_cocina == 0) {
            estado_cocina = 1;
            espacio.setEstado_habitacion(espacio.getEstado_habitacion());
            espacio.setEstado_bano(espacio.getEstado_bano());
            espacio.setEstado_cocina(estado_cocina);
            espacio.setEstado_sala(espacio.getEstado_sala());
            btn_Cocina.setBackgroundColor(Color.YELLOW);
            reference_control.child(UID).setValue(espacio).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                    } else {
                        Toast.makeText(ControlActivity.this,
                                "Error " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            estado_cocina = 0;
            espacio.setEstado_habitacion(espacio.getEstado_habitacion());
            espacio.setEstado_bano(espacio.getEstado_bano());
            espacio.setEstado_cocina(estado_cocina);
            espacio.setEstado_sala(espacio.getEstado_sala());
            btn_Cocina.setBackgroundColor(Color.RED);
            reference_control.child(UID).setValue(espacio).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                    } else {
                        Toast.makeText(ControlActivity.this,
                                "Error " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @OnClick(R.id.btn_Habitacion)
    public void clickHabitacion() {
        UID = mAuth_control.getCurrentUser().getUid();
        if (estado_alcoba == 0) {
            estado_alcoba = 1;
            espacio.setEstado_habitacion(estado_alcoba);
            espacio.setEstado_bano(espacio.getEstado_bano());
            espacio.setEstado_cocina(espacio.getEstado_cocina());
            espacio.setEstado_sala(espacio.getEstado_sala());
            btn_Habitacion.setBackgroundColor(Color.YELLOW);
            reference_control.child(UID).setValue(espacio).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                    } else {
                        Toast.makeText(ControlActivity.this,
                                "Error " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            estado_alcoba = 0;
            espacio.setEstado_habitacion(estado_alcoba);
            espacio.setEstado_bano(espacio.getEstado_bano());
            espacio.setEstado_cocina(espacio.getEstado_cocina());
            espacio.setEstado_sala(espacio.getEstado_sala());
            btn_Habitacion.setBackgroundColor(Color.RED);
            reference_control.child(UID).setValue(espacio).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                    } else {
                        Toast.makeText(ControlActivity.this,
                                "Error " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    @OnClick(R.id.btn_Sala)
    public void clickSala() {
        UID = mAuth_control.getCurrentUser().getUid();
        if (estado_sala == 0) {
            estado_sala = 1;
            espacio.setEstado_habitacion(espacio.getEstado_habitacion());
            espacio.setEstado_bano(espacio.getEstado_bano());
            espacio.setEstado_cocina(espacio.getEstado_cocina());
            espacio.setEstado_sala(estado_sala);
            btn_Sala.setBackgroundColor(Color.YELLOW);
            reference_control.child(UID).setValue(espacio).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                    } else {
                        Toast.makeText(ControlActivity.this,
                                "Error " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            estado_sala = 0;
            espacio.setEstado_habitacion(espacio.getEstado_habitacion());
            espacio.setEstado_bano(espacio.getEstado_bano());
            espacio.setEstado_cocina(espacio.getEstado_cocina());
            espacio.setEstado_sala(estado_sala);
            btn_Sala.setBackgroundColor(Color.RED);
            reference_control.child(UID).setValue(espacio).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                    } else {
                        Toast.makeText(ControlActivity.this,
                                "Error " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


}
