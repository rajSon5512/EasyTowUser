package com.example.somzzzzz.easytowuser.Activity.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.somzzzzz.easytowuser.Activity.Activity.MainActivity;
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

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;


    private boolean mVerificationProgress=false;
    private String mVerificationId;

    private EditText mEditText;
    private Button mLoginButton;
    private FirebaseAuth mAuth;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();


        if(user!=null){

            Intent intent=new Intent(getContext(),MainActivity.class);
            startActivity(intent);
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

                if(e instanceof FirebaseAuthInvalidCredentialsException){

                }else if(e instanceof FirebaseTooManyRequestsException){

                }


            }

            public void onCodeSent(String verificationId,PhoneAuthProvider.ForceResendingToken token){

                Log.d(TAG, "onCodeSent: "+verificationId);

                mVerificationId=verificationId;

            }

        };


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {

        Log.d(TAG, "signInWithPhoneAuthCredential: "+phoneAuthCredential);

        mAuth.signInWithCredential(phoneAuthCredential).addOnSuccessListener(getActivity(), new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Log.d(TAG, "onSuccess: done ");
                

                
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

                String mobilenumber=mEditText.getText().toString();

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

        mEditText=view.findViewById(R.id.mobile_number);
        mLoginButton=view.findViewById(R.id.login_button);
        mAuth= FirebaseAuth.getInstance();
    }

    private void startPhoneNumberVerficaition(String phoneNumber){

        Log.d(TAG, "startPhoneNumberVerficaition: "+phoneNumber);

       PhoneAuthProvider.getInstance().verifyPhoneNumber("+91".concat(phoneNumber),60,TimeUnit.SECONDS,getActivity(),mCallBacks);

       mVerificationProgress=true;
    }

}




