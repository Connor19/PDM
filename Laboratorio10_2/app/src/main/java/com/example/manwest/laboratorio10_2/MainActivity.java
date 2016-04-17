package com.example.manwest.laboratorio10_2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ZoomButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends AppCompatActivity {
    private GoogleMap mMap;
    private CameraUpdate mCamera;
    private PolylineOptions poligono;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpMapIfNeeded();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setMarker(new LatLng(-12.017128, -77.050748), "Cafetería",
                "El mejor café", 0.9F, 0.1F, 0.1F, R.drawable.cafeteria);
        setMarker(new LatLng(-12.017124, -77.050744), "Restaurante",
                "Ají de gallina buenaso", 0.5F, 0.5F, 0.5F, R.drawable.restaurante);
        //agregando un poligono
        poligono = new PolylineOptions()
                .add(new LatLng(-12.0168396,-77.0515987))
                .add(new LatLng(-12.0164067, -77.0498499))
                .add(new LatLng(-12.018339,-77.0488819))
                .add(new LatLng(-12.0185124,-77.0520373))
                .add(new LatLng(-12.0168396, -77.0515987));
        Polyline p = mMap.addPolyline(poligono);
        //modificando las vistas de la camara, centrando a otro punto (Ej el pabellon central de la UNI)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(-12.0240254, -77.0487977), 14));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        CameraPosition cam = new CameraPosition.Builder()
                .target(new LatLng(-12.0202266, -77.0476005))//cerro de la UNI
                .zoom(15)//zoom de 15
                .bearing(45)//orientacion de 45 NOR OESTE
                .tilt(45)//angulo de camara 45
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cam));

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
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                setUpMap();
            }
        }
    }
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(-12.017124,-77.050744)).title("Facultad de Ciencias"));
        //mCamera = CameraUpdateFactory.newLatLngZoom(new LatLng(-12.017124, -77.050744), 0);
        //mMap.animateCamera(mCamera);
        //mMap.addMarker(new MarkerOptions().position(new LatLng(-12.017124, -77.050744))
        //        .title("Facultad de Ciencias")
                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
        //        .icon(BitmapDescriptorFactory.fromResource(R.drawable.cafeteria))
        //        .snippet("The beast School"));

        mCamera = CameraUpdateFactory.newLatLngZoom(new LatLng(-12.017124, -77.050744), 14);
        mMap.animateCamera(mCamera);
    }

    private void setMarker(LatLng position, String title, String info,
                           float opacity, float dimension1, float dimension2, int icon) {
        mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(title)
                .snippet(info)
                .alpha(opacity)
                .anchor(dimension1, dimension2)
                .icon(BitmapDescriptorFactory.fromResource(icon)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MenuOpcion1:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.MenuOpcion2:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.MenuOpcion3:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            case R.id.MenuOpcion4:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
