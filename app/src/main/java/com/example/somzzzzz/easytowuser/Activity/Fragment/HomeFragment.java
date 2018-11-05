package com.example.somzzzzz.easytowuser.Activity.Fragment;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.somzzzzz.easytowuser.Activity.Activity.MainActivity;
import com.example.somzzzzz.easytowuser.R;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class HomeFragment extends Fragment {

    private ViewPager mViewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMenuVisibility(true);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View view=inflater.inflate(R.layout.activity_main,container,false);

       init(view);

        final ActionBar actionBar=getActivity().getActionBar();
      //  actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        //actionBar.setDisplayHomeAsUpEnabled(true);


       if(mViewPager!=null){

           setupViewpager(mViewPager);

       }


       return view;
     }

    private void setupViewpager(ViewPager viewPager) {

        Adapter adapter=new Adapter(getFragmentManager());

        adapter.addFragment(new HistoryFragment(),HistoryFragment.class.getSimpleName());
        adapter.addFragment(new PendingFragment(),PendingFragment.class.getSimpleName());
        viewPager.setAdapter(adapter);
    }

    private void init(View view){

        mViewPager= view.findViewById(R.id.viewpager);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_items,menu);
     }


     static class Adapter extends FragmentPagerAdapter{

        private final List<Fragment> mFragments=new ArrayList<>();
        private final List<String> mFragmentTitle=new ArrayList<>();

         public Adapter(FragmentManager fm) {
             super(fm);
         }

         public void addFragment(Fragment fragment,String title){

             mFragments.add(fragment);
             mFragmentTitle.add(title);
         }

         @Override
         public Fragment getItem(int position) {
             return mFragments.get(position);
         }

         @Override
         public int getCount() {
             return mFragments.size();
         }

         public CharSequence getPageTitle(int position){

             return mFragmentTitle.get(position);
         }


     }


}
