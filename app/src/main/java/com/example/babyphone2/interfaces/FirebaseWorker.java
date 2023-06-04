package com.example.babyphone2.interfaces;

import com.example.babyphone2.models.Phone;
import com.example.babyphone2.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public interface FirebaseWorker {


    public FirebaseAuth getAuth();
    public  boolean addUser(User user);
    public  void addPhone(Phone phone);
    public ArrayList<Phone> getPhones(String userId);
    public FirebaseDatabase getDatabase();
    public Phone getPhone(String phoneName);
    public void updatePhone(Phone phone);
}
