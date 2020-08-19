package com.example.project_test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Calendar;

public class host_info extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText et_name,et_des,et_phn,et_email;
    Button btn_map,btn_submit,btn_date,btn_choose;
    ProgressBar progressBar;
    ImageView iv_pic;
    double lat=0.000;
    double lng=0.000;
    float dist;
    float d=0;
    String addressline;
    String temp="to be filled";
    String timeKey;
    int year=0;
    int month=0;
    int day=0;

    Uri imageUri;

    public DatabaseReference databaseEvent;
    public StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_info);

        databaseEvent = FirebaseDatabase.getInstance().getReference("records");
        storageReference = FirebaseStorage.getInstance().getReference("records");


        et_name = findViewById(R.id.et_name);
        et_des = findViewById(R.id.et_des);
        et_phn = findViewById(R.id.et_phn);
        et_email=findViewById(R.id.et_email);
        btn_map = findViewById(R.id.btn_map);
        btn_submit = findViewById(R.id.btn_submit);
        btn_date = findViewById(R.id.btn_date);
        btn_choose = findViewById(R.id.btn_choose);
        iv_pic = findViewById(R.id.iv_pic);
        progressBar = findViewById(R.id.progressbar);

        //for date choosing
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataPickerFragment datePickerFragment = new DataPickerFragment();
                datePickerFragment.show(getSupportFragmentManager(),"Date Picker");
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String name = et_name.getText().toString();
//                String des = et_des.getText().toString();
//                String phn = et_phn.getText().toString();
//                String email = et_email.getText().toString();

                timeKey = String.valueOf(System.currentTimeMillis());
//                storageReference.child(time).putFile(imageUri);


                storageReference.child(timeKey).putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                                //today coment

                                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                while(!uri.isComplete());
                                Uri url = uri.getResult();

                                temp = url.toString();
                                //today end

                                String name = et_name.getText().toString();
                                String des = et_des.getText().toString();
                                String phn = et_phn.getText().toString();
                                String email = et_email.getText().toString();

                                String id = databaseEvent.push().getKey();
                                Information info =new Information(name,des,phn,email,lat,lng,addressline,d,temp,year,month,day);
                                databaseEvent.child(id).setValue(info);

                                Intent intent = new Intent(host_info.this,MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                //Toast.makeText(getApplicationContext(),imageUri.toString(),Toast.LENGTH_SHORT).show();
                                double progress = (taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount())*100;
                                progressBar.setProgress((int)progress);
                            }
                        });

//                String id = databaseEvent.push().getKey();
//                Information info =new Information(name,des,phn,email,lat,lng,addressline,d,temp);
//                databaseEvent.child(id).setValue(info);

//                Intent intent = new Intent(host_info.this,MainActivity.class);
//                startActivity(intent);
            }
        });

        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(host_info.this,MapsActivity.class);
                startActivityForResult(intent,123);
            }
        });

        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent();
                intent2.setType("image/*");
                intent2.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent2,400);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123)
        {
            if(resultCode==RESULT_OK)
            {
                lat=data.getDoubleExtra("lat",0.000);
                lng=data.getDoubleExtra("lng",0.000);
                dist = data.getFloatExtra("distance",0);
                dist = dist/1000;
                addressline = data.getStringExtra("address");
                String str= "Locality : "+addressline;
                btn_map.setText(str);
            }
        }

        //for image selection
        if(requestCode==400)
        {
            if(resultCode==RESULT_OK)
            {
                   imageUri = data.getData();
                   iv_pic.setImageURI(imageUri);
            }
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        year=i;
        month=i1;
        day=i2;
        String[] smonth={"Jan","Feb","March","April","May","Jun","July","August","Sept","Oct","Nov","Dec"};
        btn_date.setText(String.valueOf(i)+" "+smonth[i1]+" "+String.valueOf(i2));
    }
}