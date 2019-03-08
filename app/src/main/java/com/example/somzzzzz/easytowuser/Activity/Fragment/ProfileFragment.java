package com.example.somzzzzz.easytowuser.Activity.Fragment;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toolbar;

import com.example.somzzzzz.easytowuser.Activity.Model.NormalUser;
import com.example.somzzzzz.easytowuser.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.support.constraint.Constraints.TAG;

public class ProfileFragment extends Fragment {

    private CollectionReference mCollectionReference;
    private FirebaseUser mUser;
    private EditText mProfileName;
    private EditText mMobileNumber;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mUser=FirebaseAuth.getInstance().getCurrentUser();
        mCollectionReference=FirebaseFirestore.getInstance().collection(NormalUser.COLLECTION_NAME);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragmentprofile,container,false);

        init(view);

        fetchInfo();

        return view;
    }

    private void fetchInfo() {

        mCollectionReference.document(mUser.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        NormalUser normalUser=new NormalUser(documentSnapshot);

                        mProfileName.setText(normalUser.getOwnerName());
                        mMobileNumber.setText(normalUser.getOwnerMobileNumber());

                        mProfileName.setEnabled(false);
                        mMobileNumber.setEnabled(false);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG, "onFailure: "+e.getMessage());

                    }
                });


    }

    private void init(View view) {

        mProfileName=view.findViewById(R.id.profile_name);
        mMobileNumber=view.findViewById(R.id.mobile_number);

    }




}
