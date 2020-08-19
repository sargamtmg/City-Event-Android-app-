package com.example.project_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements InfoAdapter.OnEventListener{

    RecyclerView recyclerView;
    RecyclerView.Adapter myadapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<Information> eventlist;
    public DatabaseReference databaseretrive;


    Button btn_host;
    TextView tv_mapshow;
    String id;
    Set<String> set = new HashSet<String>();
    String str;

    Double mylat;
    Double mylng;

    //for todays date to compare with date on database
    Calendar c = Calendar.getInstance();
    int today_year = c.get(Calendar.YEAR);
    int today_month = c.get(Calendar.MONTH);
    int today_day = c.get(Calendar.DAY_OF_MONTH);


    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseretrive = FirebaseDatabase.getInstance().getReference("records");

        btn_host = findViewById(R.id.btn_host);
        btn_host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,host_info.class);
                startActivity(intent);
            }
        });

        tv_mapshow = findViewById(R.id.tv_mapshow);
        tv_mapshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this,ShowInMap.class);
                intent2.putExtra("latitude",mylat);
                intent2.putExtra("longitude",mylng);
                startActivity(intent2);
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        recyclerView = findViewById(R.id.recycleview);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        eventlist = new ArrayList<Information>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        } else {
            askPermission();
        }

        databaseretrive.orderByChild("dist").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventlist.clear();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    id = dataSnapshot1.getKey();
                    Information info = dataSnapshot1.getValue(Information.class);
                    if(set.contains(id))
                    {
                    }
                    else {
                        //to calculate distance
                        float results[] = new float[10];
                        Location.distanceBetween(mylat,mylng,info.getLatitude(),info.getLongitude(),results);
                        info.setDist(results[0]/1000);
                        databaseretrive.child(id).setValue(info);
                        set.add(id);
                    }
                    if(info.getYear()<today_year ||
                            (info.getYear() == today_year && info.getMonth()<today_month) ||
                            (info.getYear()==today_year && info.getMonth()==today_month && info.getDay()<today_day))
                    {
                        databaseretrive.child(id).removeValue();
                    }
                    eventlist.add(info);
                }
                myadapter = new InfoAdapter(MainActivity.this,eventlist,MainActivity.this);
                recyclerView.setAdapter(myadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"retrive fail",Toast.LENGTH_SHORT).show();
            }
        });
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

    private void askPermission() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},104);
            }
            else{
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},104);
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Permission already given",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==104)
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

    @Override
    public void onEventClicked(int position) {
        Information information = eventlist.get(position);
        Intent intent = new Intent(MainActivity.this,com.example.project_test.Display_view.class);
        intent.putExtra("view_info",information);
        //for user location
        intent.putExtra("mylat",mylat);
        intent.putExtra("mylng",mylng);
        startActivity(intent);
        //Toast.makeText(getApplicationContext(),"Event Clicked "+inf.getName(),Toast.LENGTH_SHORT).show();
    }
}