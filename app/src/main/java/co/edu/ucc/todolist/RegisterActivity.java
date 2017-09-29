package co.edu.ucc.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.edu.ucc.todolist.entidades.Espacios;
import co.edu.ucc.todolist.entidades.Usuario;
import co.edu.ucc.todolist.vistas.ListActivity;

public class RegisterActivity extends AppCompatActivity {


    @BindView(R.id.txt_nomCompleto)
    EditText txt_nomCompleto;

    @BindView(R.id.txt_email)
    EditText txt_email;

    @BindView(R.id.txt_Password)
    EditText txt_Password;

    @BindView(R.id.btn_Registrar)
    Button btn_Registrar;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference reference, reference_temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("usuarios");
        reference_temp = database.getReference("hogar");
    }

    @OnClick(R.id.btn_Registrar)
    public void clickRegistrar() {

        final String nombres = txt_nomCompleto.getText().toString();
        String email = txt_email.getText().toString();
        String password = txt_Password.getText().toString();

        btn_Registrar.setEnabled(false);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final String UID = task.getResult().getUser().getUid();
                            task.getResult().getUser().getToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                @Override
                                public void onComplete(@NonNull Task<GetTokenResult> task) {
                                    if (task.isSuccessful()) {
                                        Usuario objUsuario = new Usuario();
                                        objUsuario.setNombres(nombres);
                                        objUsuario.setToken(task.getResult().getToken());

                                        reference.child(UID).setValue(objUsuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Espacios espacio = new Espacios();
                                                    reference_temp.child(UID).setValue(espacio).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Intent listActivity = new Intent(getApplicationContext(),
                                                                        ListActivity.class);
                                                                startActivity(listActivity);
                                                                finish();
                                                                return;
                                                            } else {
                                                                Toast.makeText(RegisterActivity.this,
                                                                        "Error " + task.getException().getMessage(),
                                                                        Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });


                                                } else {
                                                    Toast.makeText(RegisterActivity.this,
                                                            "Error " + task.getException().getMessage(),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(RegisterActivity.this,
                                                "Error " + task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    "Error " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
