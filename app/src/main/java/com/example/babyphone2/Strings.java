package com.example.babyphone2;

public class Strings {
     private String phoneName;
     public static final int LOCATION_SERVICE_ID = 175;
     public static final String ACTION_START_LOCATION_SERVICE = "startLocationService";
     public static final  String ACTION_STOP_LOCATION_SERVICE = "stopLocationService";


    private static final Strings instance = new Strings();

    public Strings() {
    }

    public String getPhoneName() {
        return phoneName;
    }
    public static Strings getInstance(){
        return instance;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }
}
