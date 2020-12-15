package com.example.math;

import com.google.android.gms.maps.model.LatLng;

public class Satellite {

    private String name;
    private LatLng latLng;

    public Satellite(String name, LatLng latLng){
        this.name = name;
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
