package com.example.babyphone2.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.babyphone2.interfaces.FirebaseWorker;
import com.example.babyphone2.models.Phone;
import com.example.babyphone2.models.User;
import com.example.babyphone2.models.Phone;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseWorkerImpl implements FirebaseWorker {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference phonesReference, usersReference;
    private User user;
    private Phone phone;
    private String userId;

    public FirebaseWorkerImpl() {

    }

    @Override
    public FirebaseAuth getAuth() {
        if(mAuth == null)
            mAuth = FirebaseAuth.getInstance();
        return mAuth;
    }


    @Override
    public  void addPhone(Phone phone) {
        phonesReference = getDatabase().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Phones");
        phonesReference.child(phone.getPhoneName()).setValue(phone).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("Success Added Phone ", phone.getPhoneName());
                }
            }
        });
    }

    @Override
    public ArrayList<Phone> getPhones(String userId) {
        ArrayList<Phone> phones = new ArrayList<>();
        phonesReference = getDatabase().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Phones");
        phonesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    phones.add(snapshot.getValue(Phone.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return phones;
    }

    @Override
    public FirebaseDatabase getDatabase() {
        if(database ==  null){
            database = FirebaseDatabase.getInstance();
        }
        return database;
    }

    public Phone getPhone(String phoneName) {
        phonesReference = getDatabase().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Phones");
        phonesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                   if(phoneName.equals(snapshot1.getValue(Phone.class).getPhoneName())){
                       phone = snapshot1.getValue(Phone.class);
                   }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return phone;
    }

    @Override
    public void updatePhone(Phone phone) {

    }

    @Override
    public boolean addUser(User user) {
        final boolean[] result = {false};
        FirebaseDatabase data1 = getDatabase();
        DatabaseReference myRef = data1.getReference("Users");
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef.child(id)
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                           result[0] =   true;
                        }else {
                            result[0] = false;
                            Log.d("Add User Error", task.getException().getMessage());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FailedToAddUser", e.getMessage());
            }
        });
        return result[0];
    }
}


