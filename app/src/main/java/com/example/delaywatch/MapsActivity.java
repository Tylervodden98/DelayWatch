package com.example.delaywatch;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;

import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.DirectionsApiRequest;
//import com.google.maps.PlaceAutocompleteRequest;
//import com.google.maps.PlaceDetailsRequest;
//import com.google.maps.PlacesApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    //Button btnGetDirection;
    MarkerOptions start, dest;
    Marker x;
    Polyline currentPolyline;
    private GeoApiContext mGeoApiContext = null;
    private static final String TAG = "UserListFragment";
    //for user location
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean LocationPermission = false;
    private static int default_zoom = 15;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    //For Timer
    long begin = System.nanoTime();
    long finish = System.nanoTime();
    //make button methods & send this data to firebase
    long timeElapsed = finish - begin;

    //Widgets
    private EditText mSearchText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getLocationPerms();
        //gets input from search
        mSearchText = (EditText) findViewById(R.id.input_search);
        init();
        //btnGetDirection = findViewById(R.id.btnGetDirection);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //For Autocomplete Fragment

        //get places
        start = new MarkerOptions().position(new LatLng(27.658143, 85.3199503)).title("Start");
        dest = new MarkerOptions().position(new LatLng(27.667491, 85.3208583)).title("Destination");
        Log.d(TAG, "time passed: " + timeElapsed);
        //String url = makeUrl(start.getPosition(), dest.getPosition(), "driving");
        //new FetchURL(MapsActivity.this).execute(url, "driving");

    }

    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
    }

    private void getLocationPerms() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationPermission = true;
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        1234);
            }

        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    1234);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        LocationPermission = false;

        switch (requestCode) {
            case 1234: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        LocationPermission = false;
                        return;
                    }
                    LocationPermission = true;
                    //onMapReady();
                    initMap();
                }
            }
        }
    }
/*
    private void getDeviceLocation() {
        //getting location of device
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);

        try {
            if (LocationPermission) {
                final Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            //found location
                            if(mFusedLocationProviderClient.getLastLocation() != null) {
                                Location currentLoc = (Location) task.getResult();
                                //LatLng xyz = new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude());
                                moveCamera(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()), default_zoom, "Your Location");
                            }
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get location", Toast.LENGTH_SHORT).show();
                            //no location found
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: Security Exception: " + e.getMessage());
        }
    }
*/
    private void init() {
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    //execute our search for map
                    geoLocation();
                }
                return false;
            }
        });
        //hideKeyboard();
    }

    private void geoLocation() {
        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            //getting list of address
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocation: IOException" + e.getMessage());
        }
        if (list.size() > 0) {
            Address address = list.get(0);
            Log.d(TAG, "geoLocation: found location" + address.toString());
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), default_zoom, address.getAddressLine(0));
            hideKeyboard();
        }
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

        //getting starting location from user data
/*
        if (LocationPermission = true) {
            getDeviceLocation();
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

        }
*/

        LatLng s = new LatLng(27.667491, 85.3208583);
        x = mMap.addMarker(new MarkerOptions()
                .position(s)
                .title("Destination"));
        x.setTag(0);

       mMap.addMarker(start);
       mMap.addMarker(dest);

        if (mGeoApiContext == null) {
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_maps_key))
                    .build();
        }

        //method to find quickest path
        calculateDirections(x);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(s));
        /*
        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
        .clickable(true)
        .add(
                new LatLng(27.658143, 85.3199503),
                new LatLng(27.667491, 85.3208583)));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(s));


        //googleMap.setOnPolylineClickListener(this);
        //googleMap.setOnPolygonClickListener(this);
        String url = makeUrl(start.getPosition(), dest.getPosition(), "driving");
        */

        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    private void calculateDirections(Marker marker) {
        Log.d(TAG, "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                marker.getPosition().latitude,
                marker.getPosition().longitude
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.alternatives(true);
        directions.origin(
                new com.google.maps.model.LatLng(
                        start.getPosition().latitude,
                        start.getPosition().longitude
                )
        );
        Log.d(TAG, "calculateDirections: destination: " + destination.toString());
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.d(TAG, "onResult: routes: " + result.routes[0].toString());
                Log.d(TAG, "calculateDirections: duration: " + result.routes[0].legs[0].duration);
                Log.d(TAG, "calculateDirections: duration: " + result.routes[0].legs[0].distance);
                Log.d(TAG, "onResult: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());

                addPolylinesToMap(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "onFailure: " + e.getMessage());

            }
        });
    }


        private void addPolylinesToMap(final DirectionsResult result){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: result routes: " + result.routes.length);

                    for(DirectionsRoute route: result.routes){
                        Log.d(TAG, "run: leg: " + route.legs[0].toString());
                        List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                        List<LatLng> newDecodedPath = new ArrayList<>();

                        // This loops through all the LatLng coordinates of ONE polyline.
                        for(com.google.maps.model.LatLng latLng: decodedPath){

//                        Log.d(TAG, "run: latlng: " + latLng.toString());

                            newDecodedPath.add(new LatLng(
                                    latLng.lat,
                                    latLng.lng
                            ));
                        }
                        Polyline polyline = mMap.addPolyline(new PolylineOptions()
                                .addAll(newDecodedPath));
                        polyline.setColor(polyline.getColor() ^ 0xff);
                       // polyline.setColor(ContextCompat.getColor(getActivity(),R.color.dark_teal));
                        polyline.setClickable(true);

                    }
                }
            });
    }
    private void moveCamera(LatLng latLng, float zoom, String title){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(options);
    }

    private void hideKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}






