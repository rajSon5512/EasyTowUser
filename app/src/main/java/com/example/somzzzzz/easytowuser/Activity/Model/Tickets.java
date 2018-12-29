package com.example.somzzzzz.easytowuser.Activity.Model;

import com.google.firebase.firestore.DocumentSnapshot;

public class Tickets {

    public static final String COLLECTION_NAME="tickets";

    public static final String FINE="Fine";
    public static final String CURRENT_STATUS="current_status";
    public static final String DATE="date";
    public static final String RAISED_BY="raised_by";
    public static final String VEHICLE_ID="vehicle_id";

    public static final String DEFAULT_TICKET_STATUS="pending";


    private String mFine;
    private String mCurrentStatus;
    private String mDate;
    private String mRaisedBy;
    private String mVehicleId;

    public Tickets(DocumentSnapshot documentSnapshot){

        mFine=documentSnapshot.get(Tickets.FINE).toString();
        mCurrentStatus=documentSnapshot.getString(Tickets.CURRENT_STATUS);
        mDate=documentSnapshot.get(Tickets.DATE).toString();
        mRaisedBy=documentSnapshot.getString(Tickets.RAISED_BY);
        mVehicleId=documentSnapshot.getString(Tickets.VEHICLE_ID);

    }


    public String getFine() {
        return mFine;
    }

    public void setFine(String fine) {
        mFine = fine;
    }

    public String getCurrentStatus() {
        return mCurrentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        mCurrentStatus = currentStatus;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getRaisedBy() {
        return mRaisedBy;
    }

    public void setRaisedBy(String raisedBy) {
        mRaisedBy = raisedBy;
    }

    public String getVehicleId() {
        return mVehicleId;
    }

    public void setVehicleId(String vehicleId) {
        mVehicleId = vehicleId;
    }



}
