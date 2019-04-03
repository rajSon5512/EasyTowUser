package com.example.somzzzzz.easytowuser.Activity.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.somzzzzz.easytowuser.Activity.Activity.MainActivity;
import com.example.somzzzzz.easytowuser.Activity.Activity.OtpActivity;
import com.example.somzzzzz.easytowuser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginFragment extends Fragment{

    private static final String TAG = "PhoneAuthActivity";


    private boolean mVerificationProgress=false;
    private String mVerificationId;

    private EditText mMobileNumber;
    private Button mLoginButton;
    private FirebaseAuth mAuth;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    private Callback mCallbackInterface;
    
    public interface Callback{
        void onOtpVerfication(String verficationId,String mobilenumber);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null){

            Intent intent=new Intent(getContext(),MainActivity.class);
            startActivity(intent);
            getActivity().finish();

        }

        mCallBacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                Log.d(TAG, "onVerificationCompleted: "+phoneAuthCredential);

                mVerificationProgress=false;

                 signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.d(TAG, "onVerificationFailed: "+e);

            }

            public void onCodeSent(String verificationId,PhoneAuthProvider.ForceResendingToken token){

                Log.d(TAG, "onCodeSent: "+verificationId);

                mVerificationId=verificationId;

                String mobileNumbern=mMobileNumber.getEditableText().toString();

                Intent intent=OtpActivity.getStartIntent(mVerificationId,getContext(),mobileNumbern);
                startActivity(intent);
                getActivity().finish();
            }

        };


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {

        Log.d(TAG, "signInWithPhoneAuthCredential: "+phoneAuthCredential);

        mAuth.signInWithCredential(phoneAuthCredential).addOnSuccessListener(getActivity(), new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Log.d(TAG, "onSuccess:success ");

            }
        }).addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d(TAG, "onFailure: Fail :"+e.getMessage());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.login_activtiy_fragment,container,false);

        init(view);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobilenumber=mMobileNumber.getText().toString();

                if(mobilenumber.length()!=10){

                    Toast.makeText(getContext(),"Please Enter Correct number.",Toast.LENGTH_SHORT).show();

                }else{

                    startPhoneNumberVerficaition(mobilenumber);

                }


            }
        });


        return view;
    }

    private void init(View view) {

        mMobileNumber=view.findViewById(R.id.mobile_number);
        mLoginButton=view.findViewById(R.id.login_button);
        mAuth= FirebaseAuth.getInstance();
    }

    private void startPhoneNumberVerficaition(String phoneNumber){

        Log.d(TAG, "startPhoneNumberVerficaition: "+phoneNumber);

       PhoneAuthProvider.getInstance().verifyPhoneNumber("+91".concat(phoneNumber),60,TimeUnit.SECONDS,getActivity(),mCallBacks);

       mVerificationProgress=true;
    }

}




