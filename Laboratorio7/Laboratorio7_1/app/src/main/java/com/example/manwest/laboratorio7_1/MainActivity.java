package com.example.manwest.laboratorio7_1;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    public void crearFicheroInterno(View view){
        try{
            OutputStreamWriter fout = new OutputStreamWriter(openFileOutput("prueba_int.txt", Context.MODE_PRIVATE));
            fout.write("Texto de prueba.");
            fout.close();
            Toast.makeText(this, "Se creo el fichero interno", Toast.LENGTH_LONG).show();
        }
        catch (Exception ex){
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }
    }
    public void leerFicheroInterno(View view){
        try{
            BufferedReader fin =new BufferedReader(new InputStreamReader(openFileInput("prueba_int.txt")));
            String texto = fin.readLine();
            Toast.makeText(this, texto, Toast.LENGTH_LONG).show();
            fin.close();
            Toast.makeText(this, "Se ha leido el fichero interno", Toast.LENGTH_LONG).show();
        }
        catch (Exception ex){
            Log.e("Ficheros", "Error al leer desde memoria interna");
        }

    }
    public void crearFicheroExterno(View view){
        try
        {
            File ruta_sd_global = Environment.getExternalStorageDirectory();
            File f = new File(ruta_sd_global.getAbsolutePath(), "prueba_sd.txt");
            OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(f));
            fout.write("Texto de prueba Externa.");
            fout.close();
            Toast.makeText(this,"Se creo fichero externo", Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
        }

    }
    public void leerFicheroExterno(View view){
        try{
            File ruta_sd = Environment.getExternalStorageDirectory();
            File f = new File(ruta_sd.getAbsolutePath(), "prueba_sd.txt");
            BufferedReader fin = new BufferedReader(new
                    InputStreamReader(new FileInputStream(f)));
            String texto = fin.readLine();
            fin.close();
            Toast.makeText(this, texto, Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Se ha leido el fichero externo", Toast.LENGTH_LONG).show();

        }
        catch (Exception ex){
            Log.e("Ficheros", "Error al leer fichero desde tarjeta SD");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
