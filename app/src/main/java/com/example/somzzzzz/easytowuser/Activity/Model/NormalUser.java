package com.example.somzzzzz.easytowuser.Activity.Model;

import com.google.firebase.firestore.DocumentSnapshot;

public class NormalUser {

    public static final String COLLECTION_NAME="normalUser";


    public static final String OWNER_NAME="owner_name";
    public static final String MOBILE="mobile";
    public static final String REGISTRATION_DATE="registration_date";
    public static final String VEHICLE_NUMBER="vehicle_number";
    public static final String VEHICLE_TYPE="vehicle_type";

    private String mOwnerName;
    private String mVehicleNumber;
    private String mOwnerMobileNumber;
    private String mVehicleType;


    public NormalUser(DocumentSnapshot documentSnapshot){

        mOwnerName=documentSnapshot.getString(NormalUser.OWNER_NAME);
        mOwnerMobileNumber=documentSnapshot.getString(NormalUser.MOBILE);
        mVehicleNumber=documentSnapshot.getString(NormalUser.VEHICLE_NUMBER);
        mVehicleType=documentSnapshot.getString(NormalUser.VEHICLE_TYPE);

    }


    public String getOwnerName() {
        return mOwnerName;
    }

    public void setOwnerName(String ownerName) {
        mOwnerName = ownerName;
    }

    public String getVehicleNumber() {
        return mVehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        mVehicleNumber = vehicleNumber;
    }

    public String getOwnerMobileNumber() {
        return mOwnerMobileNumber;
    }

    public void setOwnerMobileNumber(String ownerMobileNumber) {
        mOwnerMobileNumber = ownerMobileNumber;
    }

    public String getVehicleType() {
        return mVehicleType;
    }

    public void setVehicleType(String vehicleType) {
        mVehicleType = vehicleType;
    }

}
