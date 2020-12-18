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
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static LatLng target;
    private static List<Satellite> satelliteList;
    private static Handler h1, h;
    private Runnable r1, r;
    private static int speed = 1;
    private Button suivant;
    private boolean start;
    private int sleep = 0;
    private TextView instructions;
    private int etape;
    private Satellite s, s1, s2;
    private Polyline line;
    private Marker m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        instructions = findViewById(R.id.instruction);

        suivant = findViewById(R.id.suivant);
        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (etape){
                    case 1 : etape1bis();
                        break;
                    case 2 : etape2();
                        break;
                    case 3 : etape3();
                        break;
                    case 4 : etape4();
                        break;
                    case 5 : etape5();
                        break;
                    case 6 : etape6();
                        break;
                    case 7 : etape7();
                        break;
                }


                }

                //speed_button.setText("Speed " + speed);
            });


        suivant.setVisibility(View.INVISIBLE);

        h1 = new Handler();
        r1 = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                if(sleep < 2){
                    sleep++;
                    h1.postDelayed(this,1000);
                }else{
                    suivant.setVisibility(View.VISIBLE);
                }
            }
        };

        h1.postDelayed(r1,0);





        satelliteList = new ArrayList<>();
        h = new Handler();
        r = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                float[] results = new float[3];
                if(satelliteList != null) {
                    for (Satellite s : satelliteList) {
                        if(s.getName().equalsIgnoreCase("SAT1")){
                            Location.distanceBetween(s.getLatLng().latitude+0.1*speed, s.getLatLng().longitude, target.latitude, target.longitude, results);
                            if(results[0] <= 2000000){

                                mMap.addMarker(new MarkerOptions().title("Votre position").snippet("Latitude : " + target.latitude + " Longitude : " + target.longitude).position(target)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                                h.removeCallbacks(r);
                            }else {
                                Log.w("Satellite", "Position : " + s.getLatLng().toString());
                                s.setLatLng(new LatLng(s.getLatLng().latitude + 0.1 * speed, s.getLatLng().longitude));
                                Log.w("Satellite", "Position : " + s.getLatLng().toString());
                            }
                        }
                        if(s.getName().equalsIgnoreCase("SAT2")){
                            Location.distanceBetween(s.getLatLng().latitude, s.getLatLng().longitude+0.1*speed, target.latitude, target.longitude, results);
                            if(results[0] <= 2000000){

                                h.removeCallbacks(r);

                            }else {
                                Log.w("Satellite", "Position : " + s.getLatLng().toString());
                                s.setLatLng(new LatLng(s.getLatLng().latitude, s.getLatLng().longitude  + 0.1 * speed));
                                Log.w("Satellite", "Position : " + s.getLatLng().toString());
                            }
                        }
                        if(s.getName().equalsIgnoreCase("SAT3")){
                            Location.distanceBetween(s.getLatLng().latitude, s.getLatLng().longitude-0.1*speed, target.latitude, target.longitude, results);
                            if(results[0] <= 2000000){
                                mMap.addMarker(new MarkerOptions().title("Votre position").snippet("Latitude : " + target.latitude + " Longitude : " + target.longitude).position(target)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                                h.removeCallbacks(r);
                            }else {
                                Log.w("Satellite", "Position : " + s.getLatLng().toString());
                                s.setLatLng(new LatLng(s.getLatLng().latitude, s.getLatLng().longitude  - 0.1 * speed));
                                Log.w("Satellite", "Position : " + s.getLatLng().toString());
                            }
                        }

                    }
                    updateMap();
                }


            }
        };

        //init();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        init();



    }

    private void etape3() {
        line.remove();
        instructions.setText("L’ensemble des points de l’espace situé à la distance du satellite est la sphère centrée sur le satellite." +
                " On sait donc que le récepteur est situé sur cette sphère.");
        LatLng latlngS = new LatLng(s.getLatLng().latitude+10, s.getLatLng().longitude);
        LatLng latlngU = new LatLng(target.latitude, target.longitude);
        float[] results = new float[3];
        Location.distanceBetween(latlngS.latitude, latlngS.longitude, target.latitude, target.longitude, results);
        double dist = results[0];
        mMap.addCircle(new CircleOptions().center(latlngS).strokeWidth(5).fillColor(Color.TRANSPARENT).radius(dist).strokeColor(Color.RED));
        etape = 4;
    }
    public void etape4(){
        instructions.setText("Cette donnée ne suffit pas à connaître précisément la position du récepteur. " +
                "Le récepteur capte donc le signal d’un deuxième satellite et effectue la même opération");
        ajoutMarker(s1);
        LatLng latlngS = new LatLng(s1.getLatLng().latitude, s1.getLatLng().longitude);
        float[] results = new float[3];
        Location.distanceBetween(latlngS.latitude, latlngS.longitude, target.latitude, target.longitude, results);
        double dist = results[0];
        mMap.addCircle(new CircleOptions().center(latlngS).strokeWidth(5).fillColor(Color.TRANSPARENT).radius(dist).strokeColor(Color.RED));
        etape = 5;
    }

    public void etape5(){

        LatLng latlngS = s.getLatLng();
        LatLng latlngS1 = s1.getLatLng();

        float[] results = new float[3];
        Location.distanceBetween(latlngS.latitude, latlngS.longitude, latlngS1.latitude, latlngS1.longitude, results);
        double middle = results[0];

        LatLng latLngMiddle = new LatLng(0+middle,-15);
        etape = 6;




    }

    public void etape6(){
        instructions.setText("Puis un troisième sattelite");
        ajoutMarker(s2);
        LatLng latlngS = new LatLng(s2.getLatLng().latitude, s2.getLatLng().longitude);
        float[] results = new float[3];
        Location.distanceBetween(latlngS.latitude, latlngS.longitude, target.latitude, target.longitude, results);
        double dist = results[0];
        mMap.addCircle(new CircleOptions().center(latlngS).strokeWidth(5).fillColor(Color.TRANSPARENT).radius(dist).strokeColor(Color.RED));
        etape = 7;

    }

    private void etape7(){
        instructions.setText("On connait maintenant votre position de longitude : "+target.longitude+" et de latitude : "+target.latitude);
        m.remove();
        mMap.addMarker(new MarkerOptions().title("Votre potisition").snippet("Latitude : " + target.latitude + " Longitude : " + target.longitude).position(target)
                .icon(BitmapDescriptorFactory.defaultMarker()));
        etape = 8;
    }

    private void etape2() {

        LatLng latlngS = new LatLng(s.getLatLng().latitude+5, s.getLatLng().longitude);
        LatLng latlngU = new LatLng(target.latitude, target.longitude);
        line = mMap.addPolyline(new PolylineOptions()
                .add(latlngS, latlngU)
                .width(5)
                .color(Color.RED));
        instructions.setText("Le récepteur mesure le temps mis par le " +
                "signal émis par le satellite pour parcourir la distance " +
                "qui le sépare du satellite.");

        etape = 3;

    }

    public void etape1(){
        m = mMap.addMarker(new MarkerOptions().title("Récepteur").snippet("Latitude : " + target.latitude + " Longitude : " + target.longitude).position(target)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.antenne)));
        /*mMap.addMarker(new MarkerOptions().title("Récepteur").snippet("Latitude : " + target.latitude + " Longitude : " + target.longitude).position(target)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.antenne)));*/
        h1.postDelayed(r1,0);
        etape = 1;

    }

    public void etape1bis(){
        instructions.setText("Un signal est émis par un premier sattelite");
        ajoutMarker(s);

        suivant.setVisibility(View.VISIBLE);
        etape = 2;
    }

    private void ajoutMarker(Satellite sat) {
        mMap.addMarker(new MarkerOptions().title(sat.getName()).snippet("Latitude : " + sat.getLatLng().latitude + " Longitude : " + sat.getLatLng().longitude)
                .position(sat.getLatLng()).icon(BitmapDescriptorFactory.fromResource(R.drawable.satellite)));
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
        etape1();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void updateMap(){
        Log.w("Map", "Update");
        if(mMap != null){
            mMap.clear();
            if(target != null) {

                float[] results = new float[3];
                for(Satellite s : satelliteList) {
                    LatLng k = new LatLng(s.getLatLng().latitude, s.getLatLng().longitude);
                    mMap.addMarker(new MarkerOptions().title(s.getName()).snippet("Latitude : " + s.getLatLng().latitude + " Longitude : " + s.getLatLng().longitude)
                            .position(s.getLatLng()).icon(BitmapDescriptorFactory.fromResource(R.drawable.satellite)));
                    mMap.addCircle(new CircleOptions().center(k).strokeWidth(5).fillColor(Color.TRANSPARENT).radius(2000000).strokeColor(Color.RED));
                    Location.distanceBetween(s.getLatLng().latitude+0.1*speed, s.getLatLng().longitude, target.latitude, target.longitude, results);
                    if(results[0] <= 2000000){
                        mMap.addMarker(new MarkerOptions().title("Votre position").snippet("Latitude : " + target.latitude + " Longitude : " + target.longitude).position(target)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    }
                }
            }
        }
    }



    public void init(){
        s = new Satellite("SAT1", new LatLng(10, 0));
        s1 = new Satellite("SAT2", new LatLng(40, -30));
        s2 = new Satellite("SAT3", new LatLng(60, 10));
    }


}