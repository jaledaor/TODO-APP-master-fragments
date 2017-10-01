package co.edu.ucc.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.edu.ucc.todolist.vistas.ListActivity;

public class LoginActivity extends AppCompatActivity {
    private CallbackManager callbackManager;

    public static final String TAG = "MainActivity";

    @BindView(R.id.txt_nomUsuario)
    EditText txt_nomUsuario;

    @BindView(R.id.txt_Password)
    EditText txt_Password;

    @BindView(R.id.btn_Ingresar)
    Button btn_Ingresar;

    @BindView(R.id.login_button)
    Button login_button;

    private FirebaseAuth mAuth;
    LoginButton loginButton;
    private FirebaseDatabase database_control;
    private DatabaseReference reference_control;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String UID = "";
    String estado_sala_temp = "0";
    String estado_bano_temp = "0";
    String estado_cocina_temp = "0";
    String estado_alcoba_temp = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginManager.getInstance().logOut();
        setContentView(R.layout.activity_login);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();


        ButterKnife.bind(this);
        database_control = FirebaseDatabase.getInstance();
        //reference_control = database_control.getReference("hogar");
        // Initialize your instance of callbackManager//

        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager,

                // If the login attempt is successful, then call onSuccess and pass the LoginResult//
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // Print the user’s ID and the Auth Token to Android Studio’s Logcat Monitor//
                        Log.d(TAG, "User ID: " +
                                loginResult.getAccessToken().getUserId() + "\n" +
                                "Auth Token: " + loginResult.getAccessToken().getToken());
                        handleFacebookAccessToken(loginResult.getAccessToken());
                        Intent ingresar = new Intent(getApplicationContext(), ListActivity.class);
                        startActivity(ingresar);
                        finish();
                        return;
                    }

                    // If the user cancels the login, then call onCancel//
                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }

                    // If an error occurs, then call onError//
                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG, "facebook:onError",exception);
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Toast.makeText(LoginActivity.this, "" + user.getDisplayName(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "something went wrong", Toast.LENGTH_LONG).show();
                }


            }
        };

    }


    @OnClick(R.id.login_button)
    public void faceLogin() {
        // Register your callback//
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            btn_Ingresar.setEnabled(false);

                            Intent ingresar = new Intent(getApplicationContext(), ListActivity.class);
                            startActivity(ingresar);
                            finish();
                            return;


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }

    public void signOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
    }



    @OnClick(R.id.btn_Ingresar)
    public void clickIngressar() {
        String email = txt_nomUsuario.getText().toString();
        String password = txt_Password.getText().toString();
        /*UID = mAuth.getCurrentUser().getUid();
        reference_control.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    estado_bano_temp = snapshot.child(UID).child("estado_bano").getValue().toString();
                    estado_cocina_temp = snapshot.child(UID).child("estado_cocina").getValue().toString();
                    estado_alcoba_temp = snapshot.child(UID).child("estado_habitacion").getValue().toString();
                    estado_sala_temp = snapshot.child(UID).child("estado_sala").getValue().toString();
                } else {
                    Toast.makeText(LoginActivity.this, "no hay datos", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });*/

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                    /*inicio cambio*/



/*inicio cambio*/

                    /*fin cambio*/
                            btn_Ingresar.setEnabled(false);

                            Intent ingresar = new Intent(getApplicationContext(), ListActivity.class);
                            /*ingresar.putExtra("bano", estado_bano_temp);
                            ingresar.putExtra("cocina", estado_cocina_temp);
                            ingresar.putExtra("alcoba", estado_alcoba_temp);
                            ingresar.putExtra("sala", estado_sala_temp);*/
                            startActivity(ingresar);
                            finish();
                            return;
                        } else {
                            Toast.makeText(getApplicationContext(), "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @OnClick(R.id.btn_Registrar)
    public void clickRegistrar() {
        Intent registerActivity = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(registerActivity);
        finish();
        return;
    }


}
