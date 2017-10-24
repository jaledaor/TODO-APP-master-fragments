package co.edu.ucc.todolist.vistas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import co.edu.ucc.todolist.R;
import co.edu.ucc.todolist.vistas.fragmentos.RegistroFragment;

/**
 * Created by usuario on 24/10/2017.
 */

public class RegActivity extends AppCompatActivity implements RegistroFragment.OnRegistroFragmentInteraction {
    @Override
    public void registro() {

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        initFragment();
    }

    private void initFragment() {
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.frameAuthActivity, RegistroFragment.newInstance());
        transaction.commit();
    }

    public void registrar() {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
        finish();
    }

}
