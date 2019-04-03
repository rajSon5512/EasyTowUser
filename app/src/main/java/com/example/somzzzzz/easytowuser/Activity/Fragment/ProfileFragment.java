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
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.somzzzzz.easytowuser.Activity.Model.NormalUser;
import com.example.somzzzzz.easytowuser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.support.constraint.Constraints.TAG;

public class ProfileFragment extends Fragment {

    private EditText mMobileNumber,mVehicleNumber,mDateRegistration;
    private TextView mVehicleOwnerName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

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

        String userID=FirebaseAuth.getInstance().getUid();

        FirebaseFirestore.getInstance().collection(NormalUser.COLLECTION_NAME)
                    .document(userID)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    DocumentSnapshot documentSnapshot=task.getResult();

                    NormalUser normalUser=new NormalUser(documentSnapshot);

                    setDatails(normalUser);


                }


            }
        });

    }

    private void setDatails(NormalUser normalUser) {

        mVehicleOwnerName.setText(normalUser.getOwnerName());
        mDateRegistration.setText(normalUser.getRegistrationDate());
        mVehicleNumber.setText(normalUser.getVehicleNumber());
        mMobileNumber.setText(normalUser.getOwnerMobileNumber());
    }


    private void init(View view) {

        mDateRegistration=view.findViewById(R.id.registrationDate_profile);
        mVehicleNumber=view.findViewById(R.id.vehicle_number_profile);
        mMobileNumber=view.findViewById(R.id.owner_phoneNumber);
        mVehicleOwnerName=view.findViewById(R.id.vehicle_owner_textview);

    }

}
