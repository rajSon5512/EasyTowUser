package com.example.somzzzzz.easytowuser.Activity.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.somzzzzz.easytowuser.Activity.Fragment.ProfileFragment;




public class ProfileActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
    }

    @Override
    public Fragment createFragment() {
        return new ProfileFragment();
    }
}
