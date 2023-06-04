package com.example.babyphone2.bottomNavBar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.babyphone2.R;
import com.example.babyphone2.Strings;
import com.example.babyphone2.dao.FirebaseWorkerImpl;
import com.example.babyphone2.models.Phone;
import com.example.babyphone2.Strings;
import com.example.babyphone2.dao.FirebaseWorkerImpl;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsFragment extends Fragment {
    private BroadcastReceiver broadcastReceiver;
    private double latitude,longitude;
    private Context context;
    private ValueEventListener valueEventListener;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private GoogleMap myMap;
    private FirebaseWorkerImpl firebaseWorker;
    private Strings values;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        values = Strings.getInstance();
        firebaseWorker = new FirebaseWorkerImpl();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef= database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Phones");
        context = getActivity().getApplicationContext();
        super.onCreate(savedInstanceState);
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            myMap = googleMap;
            valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data:snapshot.getChildren()) {
                        Phone phone = data.getValue(Phone.class);
                        if(phone != null){
                            double latitude = phone.getLatitude();
                            double longitude =  phone.getLongitude();
                            LatLng phones= new LatLng(latitude,longitude);

                            myMap.addCircle(new CircleOptions().center(phones).radius(100).strokeColor(Color.RED).fillColor(Color.CYAN)
                            .clickable(true));
                        }
                    }
                    Phone currentPhone  = firebaseWorker.getPhone(values.getPhoneName());
                    LatLng currentPhoneLatLong = new LatLng(currentPhone.getLatitude(),currentPhone.getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentPhoneLatLong,10);
                    myMap.animateCamera(cameraUpdate);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            myRef.addValueEventListener(valueEventListener);
//            if(broadcastReceiver == null){
//                broadcastReceiver = new BroadcastReceiver() {
//                    @Override
//                    public void onReceive(Context context, Intent intent) {
//                        latitude = (double) intent.getExtras().get("latitude");
//                        longitude = (double) intent.getExtras().get("longitude");
//                        String phoneName = (String) intent.getExtras().get("phoneName");
//                        LatLng currentUpdate = new LatLng(latitude,longitude);
//                        MarkerOptions now = new MarkerOptions().position(currentUpdate).title(phoneName);
//                        googleMap.addMarker(now);
//                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentUpdate));
//
//                    }
//                };
//            }registerReceiver(broadcastReceiver);


        }
    };

    private void registerReceiver(BroadcastReceiver broadcastReceiver) {
        context.registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

}