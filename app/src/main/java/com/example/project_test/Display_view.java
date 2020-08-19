package com.example.project_test;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

public class Display_view extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    ImageView iv_displayimg;
    TextView tv_displayname,tv_displaydate,tv_displaydist,tv_displayloc,tv_displayphn,tv_displayemail,tv_displaydes;
    LatLng latlng;
    String title;
    LinearLayout ll_clkloc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        iv_displayimg=findViewById(R.id.iv_displayimg);
        tv_displayname=findViewById(R.id.tv_displayname);
        tv_displaydate=findViewById(R.id.tv_displaydate);
        tv_displaydist=findViewById(R.id.tv_displaydist);
        tv_displayloc=findViewById(R.id.tv_displayloc);
//        tv_displayphn=findViewById(R.id.tv_displayphn);
//        tv_displayemail=findViewById(R.id.tv_displayemail);
        tv_displaydes=findViewById(R.id.tv_displaydes);
        ll_clkloc=findViewById(R.id.ll_clkloc);

        Information info =getIntent().getParcelableExtra("view_info");
        String[] smonth={"Jan","Feb","March","April","May","Jun","July","August","Sept","Oct","Nov","Dec"};
        Picasso.with(getApplicationContext()).load(info.getImage()).fit().centerCrop().into(iv_displayimg);
        tv_displayname.setText(info.getName());
        tv_displaydate.setText(String.valueOf(info.getYear())+" "+smonth[info.getMonth()]+" "+String.valueOf(info.getDay()));
        tv_displaydist.setText(String.valueOf(info.getDist()));
        tv_displayloc.setText(info.getAddressline());
      //  tv_displayphn.setText(info.getPhn());
      //  tv_displayemail.setText(info.getEmail());
        tv_displaydes.setText(info.getDes());

        latlng = new LatLng(info.getLatitude(),info.getLongitude());
        title = info.getName();


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
        mMap.addMarker(new MarkerOptions().position(latlng).title(title)).showInfoWindow();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng,15);
        mMap.animateCamera(cameraUpdate);

        mMap.addMarker(new MarkerOptions().position(new LatLng(getIntent().getDoubleExtra("mylat",0),getIntent().getDoubleExtra("mylng",0))).title("your location"));

        ll_clkloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to do
                //make intent to go to new map activity with user and event location
            }
        });
    }
}