package com.example.babyphone2.models;

public class Phone {
    private String userId, phoneName;
    private String phoneId;
    private double longitude, latitude;
    private boolean isChildPhone;

    public Phone() {
    }
    public Phone(String phoneName, String phoneId, double longitude, double latitude, boolean isChildPhone) {
        this.phoneName = phoneName;
        this.phoneId = phoneId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.isChildPhone = isChildPhone;
    }

    public boolean isChildPhone() {
        return isChildPhone;
    }

    public void setChildPhone(boolean childPhone) {
        isChildPhone = childPhone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
