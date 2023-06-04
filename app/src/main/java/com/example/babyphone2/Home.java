package com.example.babyphone2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.babyphone2.dao.FirebaseWorkerImpl;
import com.example.babyphone2.databinding.ActivityHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseWorkerImpl firebaseWorker;
    private @NonNull ActivityHomeBinding binding;
    private FloatingActionButton signOut, openMaps;
    private Strings values;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseWorker = new FirebaseWorkerImpl();
        mAuth = firebaseWorker.getAuth();
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        signOut = binding.signOutBtn;
        values = Strings.getInstance();
        String testPhoneName = values.getPhoneName();
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser() !=null){
                    mAuth.signOut();
                    Intent intent  = new Intent(Home.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    //stopLocationService();
                    startActivity(intent);
                    Log.d("Log Out", "Logged Out");
                }else{
                    Toast.makeText(Home.this,"Unable to log out",Toast.LENGTH_LONG);
                }
            }
        });
//        if(!isLocationServiceRunning()){
//            startLocationService();
//        }
//        openMaps = binding.openMapsFragment;
//        openMaps.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                firebaseWorker.getPhone("testt");
//            }
//        });


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_map, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

//    private boolean isLocationServiceRunning(){
//        ActivityManager activityManager= (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        if(activityManager != null){
//            for(ActivityManager.RunningServiceInfo services : activityManager.getRunningServices(Integer.MAX_VALUE)){
//                if(LocationService.class.getName().equals(services.service.getClassName())){
//                    if(services.foreground){
//                        return true;
//                    }
//                }
//            }
//            return false;
//        }
//        return  false;
//    }
//    private void startLocationService(){
//        if(!isLocationServiceRunning()){
//            Intent intent = new Intent(getApplicationContext(), LocationService.class);
//            intent.setAction(Strings.ACTION_START_LOCATION_SERVICE);
//            startService(intent);
//            Toast.makeText(this, "Location Service started", Toast.LENGTH_LONG).show();
//
//        }
//    }
//    private void stopLocationService(){
//        if(isLocationServiceRunning()){
//            Intent intent = new Intent(getApplicationContext(), LocationService.class);
//            intent.setAction(Strings.ACTION_STOP_LOCATION_SERVICE);
//            startService(intent);
//            Toast.makeText(this,"Location Service Stopped", Toast.LENGTH_LONG).show();
//        }
//    }

}