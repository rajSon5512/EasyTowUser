package com.example.somzzzzz.easytowuser.Activity.Activity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.somzzzzz.easytowuser.Activity.Fragment.HistoryFragment;
import com.example.somzzzzz.easytowuser.Activity.Fragment.MapFragment;
import com.example.somzzzzz.easytowuser.Activity.Fragment.PaymentDone;
import com.example.somzzzzz.easytowuser.Activity.Fragment.PendingFragment;
import com.example.somzzzzz.easytowuser.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = ""+MainActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_list_viewpager);

        mAuth=FirebaseAuth.getInstance();


        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        final ActionBar ab=getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mViewPager=findViewById(R.id.viewpager);

        if(mViewPager!=null){

            setupViewPager(mViewPager);
        }

        TabLayout mTablayout=findViewById(R.id.tabs);
        mTablayout.setupWithViewPager(mViewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.sign_out){

                Log.d(TAG, "onContextItemSelected: Click");
                mAuth.signOut();
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
       }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {

        Adapter adapter=new Adapter(getSupportFragmentManager());

        adapter.addFragment(new PendingFragment(),"Pending");
        adapter.addFragment(new PaymentDone(),"Notifications");
        adapter.addFragment(new HistoryFragment(),"History");
        adapter.addFragment(new MapFragment(),"Map");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter{

        private final List<Fragment> mFragments=new ArrayList();
        private final List<String> mFragmentTitles=new ArrayList();

        public void addFragment(Fragment fragment,String title){

            mFragments.add(fragment);
            mFragmentTitles.add(title);

        }


        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments.get(i);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        public CharSequence getPageTitle(int position){
            return mFragmentTitles.get(position);
        }

    }


}
