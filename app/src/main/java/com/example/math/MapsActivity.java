package com.example.math;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static LatLng target;
    private static List<Satellite> satelliteList;
    private static Handler h;
    private Runnable r;
    private static int speed = 1;
    private Button speed_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        speed_button = findViewById(R.id.speedButton);
        speed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(speed){
                    case 1:
                        speed = 2;
                        break;
                    case 2:
                        speed = 3;
                        break;
                    case 3:
                        speed = 4;
                        break;
                    case 4:
                        speed = 5;
                        break;
                    case 5:
                        speed = 1;
                        break;
                }

                speed_button.setText("Speed " + speed);
            }
        });

        satelliteList = new ArrayList<>();
        h = new Handler();
        r = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                float[] results = new float[3];
                if(satelliteList != null) {
                    for (Satellite s : satelliteList) {
                        Location.distanceBetween(s.getLatLng().latitude+7+0.5*speed, s.getLatLng().longitude, target.latitude, target.longitude, results);
                        if(results[0] < 2500000){
                            mMap.addMarker(new MarkerOptions().title("Votre position").snippet("Latitude : " + target.latitude + " Longitude : " + target.longitude).position(target)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        }else {
                            Log.w("Satellite", "Position : " + s.getLatLng().toString());
                            s.setLatLng(new LatLng(s.getLatLng().latitude + 0.5 * speed, s.getLatLng().longitude));
                            Log.w("Satellite", "Position : " + s.getLatLng().toString());
                        }
                    }
                    updateMap();
                }

                update();
            }
        };

        init();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        update();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void updateMap(){
        Log.w("Map", "Update");
        if(mMap != null){
            mMap.clear();
            if(target != null) {

                float[] results = new float[3];
                for(Satellite s : satelliteList) {
                    LatLng k = new LatLng(s.getLatLng().latitude+7, s.getLatLng().longitude);
                    mMap.addMarker(new MarkerOptions().title(s.getName()).snippet("Latitude : " + s.getLatLng().latitude + " Longitude : " + s.getLatLng().longitude)
                            .position(s.getLatLng()).icon(BitmapDescriptorFactory.fromResource(R.drawable.satellite)));
                    mMap.addCircle(new CircleOptions().center(k).strokeWidth(7).fillColor(Color.TRANSPARENT).radius(2500000).strokeColor(Color.RED));
                    Location.distanceBetween(s.getLatLng().latitude+7+0.5*speed, s.getLatLng().longitude, target.latitude, target.longitude, results);
                    if(results[0] < 2500000){
                        mMap.addMarker(new MarkerOptions().title("Votre position").snippet("Latitude : " + target.latitude + " Longitude : " + target.longitude).position(target)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    }
                }
            }
        }
    }

    public void update(){
        h.postDelayed(r, 500);
    }

    public void init(){
        Satellite s = new Satellite("SAT1", new LatLng(-47, -3));
        satelliteList.add(s);
    }


}