package com.example.somzzzzz.easytowuser.Activity.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.somzzzzz.easytowuser.Activity.Activity.MainActivity;
import com.example.somzzzzz.easytowuser.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null){

            Intent intent=new Intent(getActivity(),MainActivity.class);
            startActivity(intent);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.login_activtiy_fragment,container,false);

        return view;
    }
}
