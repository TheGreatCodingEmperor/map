package com.map2.map2;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.app.Activity;
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


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    void getPosition()
    {
        double latitude,longitude;
        GPSTracker gpsTracker;
        Location location;
        gpsTracker = new GPSTracker(getApplicationContext());
        location = gpsTracker.getLocation();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        // Add a marker in Sydney and move the camera
        LatLng original = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions().position(original).title("WTF").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(original,14));
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

        List<Address> addresses = null;


        getPosition();

        //click button
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {

        //delay 5 seconds
        h.postDelayed(new Runnable(){
            public void run() {

                getPosition();

                h.postDelayed(this, delay);//dalay
            }
        }, delay);
            }
        });

    }

}
