package co.edu.ucc.todolist.vistas.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import co.edu.ucc.todolist.R;
import co.edu.ucc.todolist.RegisterActivity;
import co.edu.ucc.todolist.entidades.Usuario;
import co.edu.ucc.todolist.vistas.ListActivity;

import static co.edu.ucc.todolist.R.string.email;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistroFragment.OnRegistroFragmentInteraction} interface
 * to handle interaction events.
 * Use the {@link RegistroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistroFragment extends Fragment {
    @BindView(R.id.text_register_email)
    EditText text_register_email;

    @BindView(R.id.text_register_password)
    EditText text_register_password;

    @BindView(R.id.text_fragment_nombres)
    EditText text_fragment_nombres;

    FirebaseAuth firebaseauth = FirebaseAuth.getInstance();

    private OnRegistroFragmentInteraction mListener;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    public RegistroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment RegistroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistroFragment newInstance() {
        RegistroFragment fragment = new RegistroFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_registro, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegistroFragmentInteraction) {
            mListener = (OnRegistroFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRegistroFragmentInteraction");
        }
    }

    @OnClick(R.id.btnFragRegistro)
    public void ClickRegistrarUsuario(){

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("usuarios");
        final String email= text_register_email.getText().toString();
        final String contrasena= text_register_password.getText().toString();
        final String nombres = text_fragment_nombres.getText().toString();

        firebaseauth.createUserWithEmailAndPassword(email, contrasena)
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

                                                    Toast.makeText(getContext(),
                                                            "Registro Exitoso " ,
                                                            Toast.LENGTH_SHORT).show();
                                                    mListener.registro();
                                                    return;

                                                } else {
                                                    Toast.makeText(getContext(),
                                                            "Error " + task.getException().getMessage(),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getContext(),
                                                "Error " + task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getContext(),
                                    "Error " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRegistroFragmentInteraction {
        // TODO: Update argument type and name
        void registro();
    }
}
