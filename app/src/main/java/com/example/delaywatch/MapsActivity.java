package com.example.delaywatch;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    //Button btnGetDirection;
    MarkerOptions start, dest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //btnGetDirection = findViewById(R.id.btnGetDirection);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //get places
        start = new MarkerOptions().position(new LatLng(27.658143, 85.3199503)).title("Location 1");
        dest = new MarkerOptions().position(new LatLng(27.667491, 85.3208583)).title("Location 1");


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

        LatLng s = new LatLng(27.658143, 85.3199503);
        mMap.addMarker(start);
        mMap.addMarker(dest);
        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
        .clickable(true)
        .add(
                new LatLng(27.658143, 85.3199503),
                new LatLng(27.667491, 85.3208583)));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(s));


        //googleMap.setOnPolylineClickListener(this);
        //googleMap.setOnPolygonClickListener(this);
        String url = makeUrl(start.getPosition(), dest.getPosition(), "driving");

        //val downloadTask: DownloadTask = DownloadTask()
           //     downloadTask.execute(url)

        //System.out.println(url);
        //https://maps.googleapis.com/maps/api/directions/json?
        //
        // origin = place_id:ChIJL4nukoTM1IkRfkPuOuvbrQo;
        // &destination = 27.667491, 85.3208583;
        //&key=R.string.google_maps_key;
        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    private String makeUrl(LatLng start, LatLng dest, String directionMode) {
        //Origin string
        String origin_str = "origin=" + start.latitude + start.longitude;
        //Destination string
        String dest_str = "destination=" + dest.latitude + dest.latitude;

        String mode = "mode=" + directionMode;

        String parameters = origin_str + "&" + dest_str + "&" + mode;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "" + getString(R.string.google_maps_key);
        return url;
    }


/*
    @Override
    public void OnPolygonClickListener(GoogleMap googleMap){

    }

    @Override
    public void OnPolylineClickListener(GoogleMap googleMap){

    }
*/
}






