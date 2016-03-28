package com.example.manwest.laboratorio6_4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    Login ctlLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctlLogin = (Login)findViewById(R.id.CtlLogin);
        ctlLogin.setOnLoginListener(new OnLoginListener(){
            @Override
            public void onLogin(String usuario, String password){
                if (usuario.equals("demo") && password.equals("demo"))
                    ctlLogin.setMensaje("Login correcto!");
                else
                    ctlLogin.setMensaje("Vuelva a intentarlo.");
            }
        });
    }
}
