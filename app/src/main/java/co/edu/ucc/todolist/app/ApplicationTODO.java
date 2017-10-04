package co.edu.ucc.todolist.app;

import android.app.Application;

import co.edu.ucc.todolist.repository.AppDB;

/**
 * Created by ADMIN on 03/10/2017.
 */

public class ApplicationTODO extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppDB.init(getApplicationContext());
    }
}
