package com.example.somzzzzz.easytowuser.Activity.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.somzzzzz.easytowuser.R;

public abstract class SigleFragmentActivity extends AppCompatActivity {

    public abstract Fragment createFragment();

    protected int getLayout(){

        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());

        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.fragment_container);

        if(fragment==null){

            fragment=createFragment();
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();

        }


    }
}
