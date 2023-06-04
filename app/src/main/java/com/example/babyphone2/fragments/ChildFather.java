package com.example.babyphone2.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.babyphone2.Home;
import com.example.babyphone2.R;
import com.example.babyphone2.Strings;
import com.example.babyphone2.dao.FirebaseWorkerImpl;
import com.example.babyphone2.models.Phone;
import com.example.babyphone2.viewModels.ChildFatherViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ChildFather extends Fragment {

    private FirebaseWorkerImpl firebaseWorker;
    private ChildFatherViewModel mViewModel;
    private Button fatherPhone, childPhone;
    private EditText phoneName;
    private String phoneNameString;
    private Strings values;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double latitude,longitude;
    private Location returnLocation, currentLocation;

    public static ChildFather newInstance() {
        return new ChildFather();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        firebaseWorker = new FirebaseWorkerImpl();
        super.onCreate(savedInstanceState);
    }
    private Location getLocation(){

        if (ActivityCompat.checkSelfPermission((Activity)getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission((Activity) getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener((Activity) getContext(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    returnLocation = location;
                }
            }
        });
        return  returnLocation;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.child_father_fragment, container, false);
        currentLocation  = getLocation();
        fatherPhone = view.findViewById(R.id.parentPhoneBtn);
        childPhone = view.findViewById(R.id.childPhoneBtn);
        phoneName = view.findViewById(R.id.phoneNameEditText);
        values = Strings.getInstance();
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference("Users");
        String userId = mAuth.getCurrentUser().getUid().toString();
        fatherPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNameString = phoneName.getText().toString();
                if (validate(phoneNameString)) {
                    values.setPhoneName(phoneNameString);
                    Phone phone;
                    phone = new Phone(values.getPhoneName(),"1",longitude,latitude,true);
                   // myRef.child(mAuth.getCurrentUser().getUid()).child("Phones").child(values.getPhoneName()).setValue(phone);
                    addPhone(phone);
                    Intent intent = new Intent(getContext(), Home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }else{
                    phoneName.setError("A phone name is required");
                    phoneName.setText("");
                    phoneName.requestFocus();
                }
            }
        });
        childPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNameString = phoneName.getText().toString();
                if(validate(phoneNameString)){
                    values.setPhoneName(phoneNameString);
                    longitude = returnLocation.getLongitude();
                    latitude = returnLocation.getLatitude();
                    Phone phone;
                    phone = new Phone(values.getPhoneName(),"1",longitude,latitude,true);
                    //myRef.child(mAuth.getCurrentUser().getUid()).child("Phones").child(values.getPhoneName()).setValue(phone);
                    addPhone(phone);
                    Intent intent = new Intent(getContext(), Home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }else{
                    phoneName.setError("A phone name is required");
                    phoneName.setText("");
                    phoneName.requestFocus();
                }
            }
        });
        return view;
    }

    private boolean validate(String phoneNameString) {
        return !phoneNameString.isEmpty();
    }
    private void addPhone(Phone phone){
        firebaseWorker.addPhone(phone);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChildFatherViewModel.class);
        // TODO: Use the ViewModel
    }

}