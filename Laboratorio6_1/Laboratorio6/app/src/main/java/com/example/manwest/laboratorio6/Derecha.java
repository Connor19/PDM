package com.example.manwest.laboratorio6;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by FRANZ on 27/02/2016.
 */
public class Derecha extends Fragment{
    View rootView;
    TextView txt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView= inflater.inflate (R.layout.derecha, container, false);
        txt = (TextView) rootView.findViewById(R.id.textView2);
        return rootView;
    }

    public void obtenerDatos(String mensaje){
        txt.setText(mensaje);
    }
}
