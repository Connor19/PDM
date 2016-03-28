package com.example.manwest.laboratorio6;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by FRANZ on 27/02/2016.
 */
@SuppressWarnings("ConstantConditions")
public class Izquierda extends Fragment {
    View rootView;
    Button boton;
    EditText campo;
    enviarMensaje EM;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView= inflater.inflate (R.layout.izquierda, container, false);
        campo = (EditText) rootView.findViewById(R.id.editText);
        boton= (Button) rootView.findViewById(R.id.btnizq);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje;
                mensaje = campo.getText().toString();
                EM.enviarDatos(mensaje);
            }
        });

        return rootView;

    }
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            EM = (enviarMensaje)context;
        } catch (ClassCastException e) {
            throw new ClassCastException ("Necesitas implementar mensaje");
        }
    }

}
