package com.map2.map2;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.TextView;

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

import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import static android.Manifest.permission.*;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double latitude,longitude;

    void getPosition(Boolean AddMarker)
    {
        double currentLatitude,currentLongitude;
        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
        Location location = gpsTracker.getLocation();
        if(location!=null)
        {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            // Add a marker in location and move the camera
            latitude = currentLatitude;
            longitude = currentLongitude;
            LatLng position = new LatLng(currentLatitude, currentLongitude);
            setMarkerOnMap(position,AddMarker);
        }
    }
    void setMarkerOnMap(LatLng position,Boolean AddMarker)
    {
        if(AddMarker)
            mMap.addMarker(new MarkerOptions().position(position).title("WTF").icon(BitmapDescriptorFactory.defaultMarker(240.0f)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,20));
    }

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
        final int delay = 3 * 1000;

        Location location;
        while(true)
        {
            GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
            location = gpsTracker.getLocation();
            if(location != null)
                break;
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        getPosition(false);


        //click button
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {

                //delay 5 seconds
                h.postDelayed(new Runnable(){
                    public void run() {

                         getPosition(true);

                         h.postDelayed(this, delay);//dalay
                    }
                }, delay);
            }
        });

    }

}
