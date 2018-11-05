package com.example.somzzzzz.easytowuser.Activity.Activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;

import com.example.somzzzzz.easytowuser.Activity.Fragment.HomeFragment;

public class MainActivity extends SigleFragmentActivity {


    @Override
    public Fragment createFragment() {
        return new HomeFragment();
    }


}
