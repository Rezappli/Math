package com.example.math;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.GnssStatus;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private Button startSimulation;
    private TextInputEditText city;
    private TextInputEditText adress;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startSimulation = findViewById(R.id.buttonStartSimulation);
        city = findViewById(R.id.cityTextInput);
        adress = findViewById(R.id.adressTextInput);

        startSimulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityText = city.getText().toString();
                String adressText = adress.getText().toString();
                LatLng pos = getLocationFromAddress(adressText + "," + cityText);
                if(pos != null){
                    MapsActivity.target = pos;
                    MapsMathActivity.target = pos;
                    startActivity(new Intent(MainActivity.this, MapsActivity.class));
                }else{
                    city.setError("Position introuvable");
                    adress.setError("Position introuvable");
                }
            }
        });

    }

    private LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 10);
            if (address == null) {
                return null;
            }

            if(address.size() > 0) {
                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            }else{
                return null;
            }

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}