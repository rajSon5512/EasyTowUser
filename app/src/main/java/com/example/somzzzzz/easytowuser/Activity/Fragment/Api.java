package com.example.somzzzzz.easytowuser.Activity.Fragment;

import com.example.somzzzzz.easytowuser.Activity.Model.CheckSum;
import com.example.somzzzzz.easytowuser.Activity.Model.Response;
import com.example.somzzzzz.easytowuser.Activity.Model.Transactions;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    //define url

    //https://us-central1-easytowofficer.cloudfunctions.net/api/create-checksum

    //https://us-central1-easytowofficer.cloudfunctions.net/api/verify-checksum

    String BASE_URL="https://us-central1-easytowofficer.cloudfunctions.net/api/";

    @POST("create-checksum")
    Call<CheckSum> getCheckSum(@Body Transactions transactions);

    @POST("verify-checksum")
    Call<Response> getResponse(@Body CheckSum checkSum);


}
