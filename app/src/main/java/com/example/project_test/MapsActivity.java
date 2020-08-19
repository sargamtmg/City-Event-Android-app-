package com.example.project_test;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    EditText et_place;
    Button btn_search;
    Button btn_result;
    Geocoder mygeo;
    Address loc;
    LatLng latLng;
    Double mylat;
    Double mylng;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        et_place = findViewById(R.id.et_place);
        btn_search = findViewById(R.id.btn_search);
        btn_result = findViewById(R.id.btn_result);
        mygeo = new Geocoder(this);

        latLng = new LatLng(0,0);  //just initializing
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

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationDisplay();
            }
        });

        btn_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResult();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        } else {
            askPermission();
        }
    }

    private void askPermission() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},102);
            }
            else{
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},102);
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Permission already given",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==102)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                getLastLocation();
            }
            else{
                Toast.makeText(getApplicationContext(),"permission not given",Toast.LENGTH_SHORT).show();

            }
        }
    }

    void getLastLocation() {
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
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();

        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null)
                {
                    mylat=location.getLatitude();
                    mylng = location.getLongitude();
                    latLng = new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title("My Location user"));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,13);
                    mMap.animateCamera(cameraUpdate);
                }
            }
        });

        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"failure",Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void sendResult() {
        Intent intent = new Intent();
        float results[] = new float[10];
        Location.distanceBetween(mylat,mylng,loc.getLatitude(),loc.getLongitude(),results);
        intent.putExtra("lat",loc.getLatitude());
        intent.putExtra("lng",loc.getLongitude());
        intent.putExtra("locality",loc.getLocality());
        intent.putExtra("address",loc.getAddressLine(0));
        intent.putExtra("distance",results[0]);
        setResult(RESULT_OK,intent);
        MapsActivity.this.finish();
    }

    private void locationDisplay() {
        try {
            List<Address> addresses = mygeo.getFromLocationName(et_place.getText().toString(),1);
            if(addresses.size()>0)
            {
                loc = addresses.get(0);
                latLng = new LatLng(loc.getLatitude(),loc.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("searched place"));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,16);
                mMap.animateCamera(cameraUpdate);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"exception occur",Toast.LENGTH_SHORT).show();
        }
    }

}