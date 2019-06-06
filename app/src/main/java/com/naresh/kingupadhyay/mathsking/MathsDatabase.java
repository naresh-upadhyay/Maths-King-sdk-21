package com.naresh.kingupadhyay.mathsking;

import android.app.Application;
import android.os.StrictMode;

import com.google.firebase.database.FirebaseDatabase;

public class MathsDatabase extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }
}
