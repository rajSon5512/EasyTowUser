package com.example.somzzzzz.easytowuser.Activity.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.somzzzzz.easytowuser.Activity.Fragment.LoginFragment;
import com.example.somzzzzz.easytowuser.Activity.Fragment.OtpFragment;

public class OtpActivity extends SingleFragmentActivity {

    private static final String TAG =""+OtpActivity.class.getSimpleName() ;
    private static final String VERIFICATION_ID ="vid";
    private static final String MOBILE_NUMBER = "mobilenumber";
    private String verifiationId,mobilenumber;

    public static Intent getStartIntent(String verificationId, Context context, String mobileNumbern) {

        Intent intent=new Intent(context,OtpActivity.class);
        intent.putExtra(OtpActivity.VERIFICATION_ID,verificationId);
        intent.putExtra(OtpActivity.MOBILE_NUMBER,mobileNumbern);
        return  intent;
    }

    @Override
    public Fragment createFragment() {

        this.verifiationId=getIntent().getStringExtra(OtpActivity.VERIFICATION_ID);
        this.mobilenumber=getIntent().getStringExtra(OtpActivity.MOBILE_NUMBER);
        Log.d(TAG, "khyati: "+this.verifiationId+"Mobile number="+mobilenumber);
        return OtpFragment.newInstance(this.verifiationId,this.mobilenumber);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
