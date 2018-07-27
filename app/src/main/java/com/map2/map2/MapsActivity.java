package com.map2.map2;


import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.location.Criteria;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Button button = (Button) findViewById(R.id.simpleButton);
        button.setText("空汙終結者 出發!");


        final Handler h = new Handler();
        final int delay = 10 * 1000;
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                //your write code


        h.postDelayed(new Runnable(){
            public void run(){

                GPSTracker gpsTracker;
                Location mLocation;
                double  latitude, longitude;

                gpsTracker = new GPSTracker(getApplicationContext());
                mLocation = gpsTracker.getLocation();
                latitude = mLocation.getLatitude();
                longitude = mLocation.getLongitude();
                // Add a marker in Sydney and move the camera
                LatLng sydney = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(sydney).title("I'm here...").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,14));

                h.postDelayed(this, delay);
            }
        }, delay);
            }
        });

    }

}
