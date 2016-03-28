package com.example.manwest.laboratorio7;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by FRANZ on 26/03/2016.
 */
public class MisPreferencias extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.mis_preferencias);
        addPreferencesFromResource(R.xml.preferences);
    }
}
