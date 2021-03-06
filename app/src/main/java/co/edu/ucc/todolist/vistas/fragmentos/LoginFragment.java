package co.edu.ucc.todolist.vistas.fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.edu.ucc.todolist.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnLoginFragmentInteraction} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    @BindView(R.id.text_frag_email)
    EditText text_frag_email;

    @BindView(R.id.text_frag_password)
    EditText text_frag_password;

    FirebaseAuth firebaseauth = FirebaseAuth.getInstance();

    private OnLoginFragmentInteraction mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
    @OnClick(R.id.bntIngresarLogin)
    public void ClickIngresarLogin(){
        final String email= text_frag_email.getText().toString();
        final String contrasena= text_frag_password.getText().toString();

        firebaseauth.signInWithEmailAndPassword(email, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if (mListener != null) {
                        mListener.ingresar(email);
                    }

                }
            }
        });

    }

    @OnClick(R.id.btn_frag_registrar)
    public void ClickRegistro(){
        // Crea el nuevo fragmento y la transacción.
        Fragment nuevoFragmento = new RegistroFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frameAuthActivity, nuevoFragmento);
        transaction.addToBackStack(null);

        // Commit a la transacción
        transaction.commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentInteraction) {
            mListener = (OnLoginFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
    public interface OnLoginFragmentInteraction {
        // TODO: Update argument type and name
        void ingresar(String email);

    }
}
