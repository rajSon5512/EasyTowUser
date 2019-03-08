package com.example.somzzzzz.easytowuser.Activity.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.example.somzzzzz.easytowuser.Activity.Fragment.ProfileFragment;
import com.example.somzzzzz.easytowuser.R;


public class ProfileActivity extends SingleFragmentActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

    }

    @Override
    public Fragment createFragment() {
        return new ProfileFragment();
    }
}
