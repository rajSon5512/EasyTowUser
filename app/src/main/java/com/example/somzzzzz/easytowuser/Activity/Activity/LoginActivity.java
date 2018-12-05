package com.example.somzzzzz.easytowuser.Activity.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.somzzzzz.easytowuser.Activity.Fragment.LoginFragment;
import com.example.somzzzzz.easytowuser.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends SingleFragmentActivity {


    @Override
    public Fragment createFragment() {
        return new LoginFragment();
    }
}