package com.example.somzzzzz.easytowuser.Activity.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.somzzzzz.easytowuser.Activity.Activity.LoginActivity;
import com.example.somzzzzz.easytowuser.Activity.Activity.MainActivity;
import com.example.somzzzzz.easytowuser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import static android.support.constraint.Constraints.TAG;

public class OtpFragment extends Fragment implements View.OnClickListener {

    private EditText mOtpnumber;
    private Button mResendButton,mNextButton;
    public static final String keyforverificationId="vid";
    public static final String keyforobilenumber="mobilenumber";
    private String mobileNumber,verificationId;
    private FirebaseAuth mAuth;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.otp_activtiy_fragment,container,false);

         mobileNumber=getArguments().getString(keyforobilenumber);
         verificationId=getArguments().getString(keyforverificationId);

        Log.d(TAG, "onCreateView: "+mobileNumber+"verification id"+verificationId);

        init(view);

        return view;
    }

    private void init(View view) {

        mOtpnumber=view.findViewById(R.id.otp_number);
        mResendButton=view.findViewById(R.id.resend_button);
        mNextButton=view.findViewById(R.id.next_button);
        mResendButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mAuth=FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.next_button:

                    String otpCode=mOtpnumber.getText().toString();
                
                    if(TextUtils.isEmpty(otpCode)){

                        Toast.makeText(getActivity(),R.string.otpEmpty,Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Log.d(TAG, "onClick: nextClick");
                    verifyPhoneNumberWithCode(verificationId,otpCode);
                    break;

            case R.id. _button:
                    break;

        }


    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {

        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,code);

        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

             //   Toast.makeText(getActivity(),"Done",Toast.LENGTH_SHORT).show();

                if(task.isSuccessful()){

                    Intent intent=new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                }else{

                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){

                        Toast.makeText(getActivity(), R.string.otpWrong, Toast.LENGTH_SHORT).show();

                    }

                }


            }
        }).addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d(TAG, "onFailure: "+e.getMessage());

            }
        });


    }

    public static OtpFragment newInstance(String verificationId,String mobilenumber) {

        Bundle args = new Bundle();
        Log.d(TAG, "newInstance: "+verificationId+"mobile Number="+mobilenumber);
        args.putString(keyforverificationId,verificationId);
        args.putString(keyforobilenumber,mobilenumber);
        OtpFragment fragment = new OtpFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
