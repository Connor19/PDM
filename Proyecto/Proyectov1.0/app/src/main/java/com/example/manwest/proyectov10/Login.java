package com.example.manwest.proyectov10;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by FRANZ on 26/03/2016.
 */
public class Login extends AppCompatActivity {
    private EditText txtUsuario;
    private EditText txtPassword;
    private Button btnIngresar;
    private Button btnCrear;
    private Button btnRegresar;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        txtUsuario = (EditText)findViewById(R.id.txtUsuario);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnIngresar = (Button) findViewById(R.id.btnIngresar);
        btnCrear = (Button) findViewById(R.id.btnCrear);
        btnRegresar = (Button) findViewById(R.id.btnRegresar);
        asignarEventos();
    }
    private void asignarEventos(){
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        btnCrear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
