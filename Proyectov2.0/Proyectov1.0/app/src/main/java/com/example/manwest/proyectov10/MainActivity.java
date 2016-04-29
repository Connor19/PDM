package com.example.manwest.proyectov10;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private JSONParser jParser = new JSONParser(); //variable usada por la lista de ciudades
    private JSONParser jParser2 = new JSONParser(); //variable usada por la lista de tipos de basura
    private Spinner spTipoBasura; //spinner de tipo de basura
    private Spinner spCiudad; //spinner de ciudad
    private List<String> listaTipoBasura; //lista de tipo de basura
    private List<String> listaCiudad; //lista de ciudad que usa mMap
    private List<String> listaCiudad2; //lista de ciudad que usa usa spCiudad
    private List<Double> listaLat; //lista de latitudes que usa mMap
    private List<Double> listaLng; //lista de longitudes que usa mMap
    private ProgressDialog pDialog; //Progresss dialog usado en cada cambio de estado
    private Context c= this; //contenedor
    //direcciones de los php del servidor
    private static String url_get_map = "http://frzasd.esy.es/RecyclingBins/getAllGB.php";
    private static String url_get_map2 = "http://frzasd.esy.es/RecyclingBins/getAllGB2.php";
    private static String url_get_tipo = "http://frzasd.esy.es/RecyclingBins/getTipo.php";
    private static String url_get_tipo2 = "http://frzasd.esy.es/RecyclingBins/getTipo2.php";
    private static String url_update_map = "http://frzasd.esy.es/RecyclingBins/updateMap.php";
    private static String url_update_map2 = "http://frzasd.esy.es/RecyclingBins/updateMap2.php";
    //tags usados por cada estado
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_BASURERO = "basurero";
    private static final String TAG_ID = "id";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_LAT = "latitud";
    private static final String TAG_LONG = "longitud";
    private static final String TAG_TIPO = "tipo";
    private static final String TAG_TIPOBASURERO = "tipobasurero";
    private static final String TAG_CONSULTA = "consulta";

    JSONArray nombres = null; //json usado para actualizar mMap
    JSONArray nombres2 = null; //json usado para actualizar la lista de ciudades
    JSONArray tipo = null; //json usado para actualizar la lista de basura
    JSONArray update = null; //json usado para actualizar mMap

    GoogleMap mMap;
    CameraUpdate mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //primero se crean y cargan los datos
        Datos();
        //luego se centra el foco del mMap
        setUpMapIfNeeded();
        //se hacen verificaciones a los urls
        new UbicationMaps(this).execute();
    }
    //usado por el NavigationView
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //crea la lista del menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    //activdades realizadas por cada item del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //en este caso solo hay 1 item, el cual vuelve a cargar mMap
        if (id == R.id.action_clear) {
            //primero limpia
            mMap.clear();
            listaLat.clear();
            listaLng.clear();
            listaCiudad.clear();
            listaCiudad2.clear();
            //luego agrega los titulos de cada lista desplegable
            listaCiudad.add("Distrito");
            listaCiudad2.add("Distrito");
            listaTipoBasura.clear();
            listaTipoBasura.add("Tipo de Basura");
            //al final vuelve a hacer la consulta al servidor
            new UbicationMaps(c).execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //opciones del Navigation View, el cual todos llaman a Login
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent;
        switch (item.getItemId()){
            case R.id.nav_gallery:
                // Handle the camera action
                intent = new Intent(MainActivity.this,Login.class);
                startActivity(intent);
                break;
            case R.id.nav_slideshow:
                // Handle the camera action
                intent = new Intent(this,Login.class);
                startActivity(intent);
                break;
            case R.id.nav_share:
                intent = new Intent(this,Login.class);
                startActivity(intent);
                break;
            case R.id.nav_send:
                intent = new Intent(this,Login.class);
                startActivity(intent);
                break;
            case R.id.nav_info:
                intent = new Intent(this,Login.class);
                startActivity(intent);
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //creando y cargando los datos del servidor en la app
    private void Datos(){
        spTipoBasura = (Spinner) findViewById(R.id.tipoBasura);
        spCiudad = (Spinner) findViewById(R.id.ciudad);

        listaTipoBasura = new ArrayList<String>();
        listaCiudad = new ArrayList<String>();
        listaCiudad2 = new ArrayList<String>();
        listaLat = new ArrayList<Double>();
        listaLng = new ArrayList<Double>();
        //titulo de la lista Tipo basura
        listaTipoBasura.add("Tipo de Basura");
        //listaTipoBasura.add("Organico");
        //listaTipoBasura.add("Latas Metal");
        //listaTipoBasura.add("Papel y Carton");
        //listaTipoBasura.add("Vidrio");
        //listaTipoBasura.add("Plastico");
        //listaTipoBasura.add("Peligrosos");

        //titulo de la lista de ciudades, la que ira en el mMap y en la lista desplegable
        listaCiudad.add("Distrito");
        listaCiudad2.add("Distrito");
        //listaCiudad.add("Lima");
        //listaCiudad.add("Ate");
        //listaCiudad.add("SMP");

        //ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaTipoBasura);
        //adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spTipoBasura.setAdapter(adapter1);

        //ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaCiudad);
        //adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spCiudad.setAdapter(adapter2);

        //para saber que item se selecciono de la lista desplegable de ciudad,
        //donde despues de escoger algun item se puede reiniciar volviendo a seleecionar el titulo
        spCiudad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean go1 = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(parent.getContext(),"Has seleccionado " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Distrito")) {
                    if(go1 == false) {
                        mMap.clear();
                        listaLat.clear();
                        listaLng.clear();
                        listaCiudad.clear();
                        listaCiudad2.clear();
                        listaCiudad.add("Distrito");
                        listaCiudad2.add("Distrito");
                        listaTipoBasura.clear();
                        listaTipoBasura.add("Tipo de Basura");
                        new UbicationMaps(c).execute();
                        go1 = true;
                    }
                } else {
                    Toast.makeText(parent.getContext(), "Has seleccionado " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                    go1 = false;
                    mMap.clear();
                    listaLat.clear();
                    listaLng.clear();
                    listaCiudad.clear();
                    listaCiudad.add("Distrito");
                    new updateMap(c,parent.getItemAtPosition(position).toString(),0).execute();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //para saber que item se selecciono de la lista desplegable de tipo basura,
        //donde despues de escoger algun item se puede reiniciar volviendo a seleecionar el titulo
        //cuando se escoge algun item, se vuelve a cargar la lista, por si se actualiza en algun momento
        spTipoBasura.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean go2 = true;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(parent.getContext(),"Has seleccionado " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();

                if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Tipo de Basura")) {
                    if(go2 == false) {
                        mMap.clear();
                        listaLat.clear();
                        listaLng.clear();
                        listaCiudad.clear();
                        listaCiudad2.clear();
                        listaCiudad.add("Distrito");
                        listaCiudad2.add("Distrito");
                        listaTipoBasura.clear();
                        listaTipoBasura.add("Tipo de Basura");
                        new UbicationMaps(c).execute();
                        go2 = true;
                    }
                } else {
                    Toast.makeText(parent.getContext(), "Has seleccionado " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                    go2 = false;
                    mMap.clear();
                    listaLat.clear();
                    listaLng.clear();
                    //listaTipoBasura.clear();
                    //listaTipoBasura.add("Tipo de Basura");
                    listaCiudad.clear();
                    listaCiudad.add("Distrito");
                    new updateMap(c,parent.getItemAtPosition(position).toString(),1).execute();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            if (mMap != null) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        //se hace foco a la municipalidad de lima
        mCamera = CameraUpdateFactory.newLatLngZoom(new LatLng(-12.0451952, -77.0321625), 11);
        mMap.animateCamera(mCamera);
    }

    private void setMarker(LatLng position, String title, String info,
                           float opacity, float dimension1, float dimension2) {
        mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(title)
                .snippet(info)
                .alpha(opacity)
                .anchor(dimension1, dimension2));
    }
    //con esta clase se obtendra la ubicacion de todos los centros de reciclados y de las listas despleglables
    public class UbicationMaps extends AsyncTask<String, String, String>{
        Context context;
        public UbicationMaps(Context context){
            this.context = context;
        }
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading Map...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args){
            List params = new ArrayList<>();
            List params1 = new ArrayList<>();
            List params2 = new ArrayList<>();
            JSONObject json = jParser.makeHttpRequest(url_get_map, "POST", params);//para mMap
            JSONObject json1 = jParser.makeHttpRequest(url_get_map2, "POST", params1);//para la lista desplegable
            JSONObject json2 = jParser2.makeHttpRequest(url_get_tipo2, "POST", params2);//para la lista desplegable
            Log.d("All names: ", json.toString());
            try{
                int success = json.getInt(TAG_SUCCESS); //para mMap
                int success1 = json1.getInt(TAG_SUCCESS);//para la lista desplegable
                int success2 = json2.getInt(TAG_SUCCESS);//para la lista desplegable
                //para mMap
                if(success == 1){
                    nombres = json.getJSONArray(TAG_BASURERO);
                    for(int i = 0;i < nombres.length();i++){
                        JSONObject c = nombres.getJSONObject(i);
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NOMBRE);
                        String lat = c.getString(TAG_LAT);
                        String lng = c.getString(TAG_LONG);
                        listaCiudad.add(name);
                        listaLat.add(Double.parseDouble(lat));
                        listaLng.add(Double.parseDouble(lng));
                    }
                }
                //para la lista desplegable
                if(success1 == 1){

                    nombres2 = json1.getJSONArray(TAG_BASURERO);
                    for(int i = 0;i < nombres2.length();i++){
                        JSONObject c = nombres2.getJSONObject(i);
                        String name = c.getString(TAG_NOMBRE);
                        listaCiudad2.add(name);
                    }
                }
                //para la lista desplegable
                if(success2 == 1){
                    tipo = json2.getJSONArray(TAG_TIPOBASURERO);
                    for(int i = 0; i<tipo.length();i++){
                        JSONObject c = tipo.getJSONObject(i);
                        String type = c.getString(TAG_TIPO);
                        listaTipoBasura.add(type);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //para la lista desplegable
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listaTipoBasura);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spTipoBasura.setAdapter(adapter1);
                    //para la lista desplegable
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listaCiudad2);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spCiudad.setAdapter(adapter2);
                    //agreando las coordenadas de los centros en mMap
                    for(int i = 0; i < listaLat.size(); i++){
                        mMap.addMarker(new MarkerOptions().position(new LatLng(listaLat.get(i),listaLng.get(i))).title("Municipalidad de " + listaCiudad.get(i+1)));
                    }
                }
            });
        }
    }
    //con esta clase se obtendra la ubicacion los centros de reciclaje dependiendo de la opcion escogida
    public class updateMap extends AsyncTask<String, String, String>{
        Context context;
        String type;
        int flag;
        //el flag se usa para saber quien esta llamandolo
        //0 si se escogio algun item de la lista de ciudades
        //1 si se escogio algun item de la lista de tipos de basura
        public updateMap(Context context, String type, int flag){
            this.context = context;
            this.type = type;
            this.flag = flag;
        }
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading Places...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args){
            List params = new ArrayList<>();
            //cambia los espacios en blanco por %20
            type = type.replace(" ","%20");
            if(flag == 0){
                JSONObject json = jParser.makeHttpRequest(url_update_map2+"?nombre="+type, "POST", params);
                Log.d("All names: ", json.toString());
                try{
                    int success = json.getInt(TAG_SUCCESS);
                    if(success == 1){

                        nombres = json.getJSONArray(TAG_CONSULTA);
                        for(int i = 0;i < nombres.length();i++){
                            JSONObject c = nombres.getJSONObject(i);
                            String name = c.getString(TAG_NOMBRE);
                            String lat = c.getString(TAG_LAT);
                            String lng = c.getString(TAG_LONG);
                            listaCiudad.add(name);
                            listaCiudad2.add(name);
                            listaLat.add(Double.parseDouble(lat));
                            listaLng.add(Double.parseDouble(lng));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(flag == 1){
                JSONObject json = jParser.makeHttpRequest(url_update_map+"?tipo="+type, "POST", params);
                Log.d("All names: ", json.toString());
                try{
                    int success = json.getInt(TAG_SUCCESS);
                    if(success == 1){

                        nombres = json.getJSONArray(TAG_CONSULTA);
                        for(int i = 0;i < nombres.length();i++){
                            JSONObject c = nombres.getJSONObject(i);
                            String name = c.getString(TAG_NOMBRE);
                            String lat = c.getString(TAG_LAT);
                            String lng = c.getString(TAG_LONG);
                            listaCiudad.add(name);
                            listaLat.add(Double.parseDouble(lat));
                            listaLng.add(Double.parseDouble(lng));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //ya no hace falta volver a cargar las listas en los spiiner, la clase anterior se encarga de eso
                    //vuelve a marcar en mMap
                    for(int i = 0; i < listaLat.size(); i++){
                        mMap.addMarker(new MarkerOptions().position(new LatLng(listaLat.get(i),listaLng.get(i))).title("Municipalidad de " + listaCiudad.get(i+1)));
                    }
                }
            });
        }
    }
}
