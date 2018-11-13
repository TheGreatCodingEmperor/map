package com.map2.map2;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import android.location.Location;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double latitude,longitude;
    private float pollution = 1;
    private double xVector,yVector;
    private double tmp;

    public String PM;

    private String[] wind = new String[480];
    private String[] webName = new String[480];

    private TextView text;
    boolean stop = true;


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
        pollution = 0;
        mMap = googleMap;

        final Button button =  findViewById(R.id.simpleButton);
        button.setText("空汙終結者 出發!");
        text =findViewById(R.id.Address);

        final Handler h = new Handler();
        final int delay = 1 * 1000;

        Location location;

        Wind data = new Wind();
        data.getWebsite();
        wind = data.getImg();
        webName = data.getTexts();

        while(true)
        {
            GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
            location = gpsTracker.getLocation();
            if(location != null)
                break;
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        getPosition("nothing",false);


        //click button
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                stop = !stop;
                //delay 5 seconds
                h.postDelayed(new Runnable(){
                    public void run() {
                        getAddress();
                        fetchData process = new fetchData();
                        try {
                            process.execute().get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        PM = process.getPM();
                        if(!stop)
                        {
                            button.setText("聽我諭令，凍結時空!");
                            h.postDelayed(this, delay);//dalay
                            PM = process.getPM();
                        }
                        else
                            button.setText("空汙終結者 出發!");
                    }
                }, delay);
            }
        });

    }
    //pre : name is address name,AddMarker is if AddMarker on map or not
    //post : global latitude and longitude will both update to current location state
    Location getPosition(String address,Boolean whetherAddMarker)
    {
        double currentLatitude,currentLongitude;
        float angle;
        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
        Location location = gpsTracker.getLocation();
        if(location!=null)
        {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            double xDistance=(currentLatitude-latitude)*111000;
            double yDistance=(currentLongitude-longitude)*101000;

            setMarkerOnMap(address,whetherAddMarker);
            if(yDistance==0)
            {
                if(xDistance==0)angle = (float)tmp;
                if(xDistance>0)angle=0;
                else angle=180;
            }
            else
            {
                xVector = 0.9*xVector + pollution*xDistance;
                yVector = 0.9*yVector + pollution*yDistance;
                angle=(float)Math.toDegrees(Math.atan(xVector/yVector));
            }

            PointInDirection((float)angle);

            latitude = currentLatitude;
            longitude = currentLongitude;
        }
        return location;
    }
    //pre : name is address name , AddMarker is add marker on map or not
    //post : will add marker on map and move camera to the marker
    void setMarkerOnMap(String name,Boolean AddMarker)
    {
        LatLng position =new LatLng(latitude,longitude);
        if(AddMarker)
            mMap.addMarker(new MarkerOptions().position(position).title(name).icon(BitmapDescriptorFactory.defaultMarker(260)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,20));
    }
    //pre : angle is the angle that arrow point into source of pollution
    //post : rotate the arrow to point in source of pollution
    void PointInDirection(float angle)
    {
        ImageView Arrow =  findViewById(R.id.Arrow);
        Arrow.setBackgroundResource(R.color.transparent);
        Arrow.setRotation((float) angle);
    }
    //post : get address in text
    public void getAddress()
    {
        String name = "no address";
        Geocoder geocoder = new Geocoder(this);
        try {
            String w = "nothing";
            Address address = geocoder.getFromLocation(latitude, longitude, 1).get(0);
            name = address.getAddressLine(0);
            String city = address.getLocality();
            String state = address.getAdminArea();
            String country = address.getCountryName();
            String postalCode = address.getPostalCode();
            String knownName = address.getFeatureName();
            int b = city.length();
            for(int i=0;i<=469;i++)
            {
                int a = webName[i].length();
                if(a<b)
                {
                    if (strcmp(webName[i],city)){w=wind[i];break;}
                }
            }
            tmp = positionAngle(w);
            String allAddress = (String)("City :"+ city  + "\nState: "+ state + "\nCountry: " + country + "\n"+ w + "\n" + PM);

            text.setText(allAddress);

        } catch (IOException e) {
            e.printStackTrace();
        }
        getPosition(name,true);
    }
    public Boolean strcmp(String shortS,String longS)
    {
        int l = shortS.length();
        return shortS.substring(0,l-1).equals(longS.substring(0,l-1));
    }
    public double positionAngle(String str)
    {
        double x = 22.5;
        if (str.equals("北"))return 0;
        else if ( str.equals("北北東") )return x;
        else  if ( str.equals("東北") )return 2*x;
        else  if ( str.equals("東北東") )return 3*x;
        else  if ( str.equals("東") )return 4*x;
        else  if ( str.equals("東南東") )return 5*x;
        else  if ( str.equals("東南") )return 6*x;
        else  if ( str.equals("南南東") )return 7*x;
        else  if ( str.equals("南") )return 8*x;
        else  if ( str.equals("南南西") )return 9*x;
        else  if ( str.equals("西南") )return 10*x;
        else  if ( str.equals("西南西") )return 11*x;
        else  if ( str.equals("西") )return 12*x;
        else  if ( str.equals("西北西") )return 13*x;
        else  if ( str.equals("西北") )return 14*x;
        else  if ( str.equals("北北西") )return 15*x;
        else return -1;
    }
}
