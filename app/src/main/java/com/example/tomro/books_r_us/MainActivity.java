package com.example.tomro.books_r_us;

import android.app.IntentService;
import android.arch.persistence.room.Room;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Console;
import java.util.List;

public class MainActivity extends AppCompatActivity implements searchFragment.OnFragmentInteractionListener{

    private ViewPager mViewPager;
    private BottomNavigationView navigation;
    private ActionBar actionBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();
        actionBar.setSubtitle("Home");

        setupViewPager();




    }

    private void setupViewPager() {
        mViewPager = findViewById(R.id.pager);

        SimpleFragmentPagerAdapter fragmentPagerAdapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(fragmentPagerAdapter);

        mViewPager.setOffscreenPageLimit(2);


        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
    }




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0);
                    actionBar.setSubtitle("Home");
                    return true;
                case R.id.navigation_books:
                    mViewPager.setCurrentItem(1);
                    actionBar.setSubtitle("Books");
                    return true;
                case R.id.navigation_search:
                    mViewPager.setCurrentItem(2);
                    actionBar.setSubtitle("Search");
                    return true;
            }
            return false;
        }
    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        // This method will be invoked when the current page is scrolled
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        // This method will be invoked when a new page becomes selected.
        @Override
        public void onPageSelected(int position) {

            switch (position) {
                case 0:
                    navigation.setSelectedItemId(R.id.navigation_home);
                    actionBar.setSubtitle("Home");
                    break;
                case 1:
                    navigation.setSelectedItemId(R.id.navigation_books);
                    actionBar.setSubtitle("Books");
                    break;
                case 2:
                    navigation.setSelectedItemId(R.id.navigation_search);
                    actionBar.setSubtitle("Search");
                    break;
            }

        }

        // Called when the scroll state changes:
        // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    public void onButtonPressed(int tag) {
        Log.d("test",Integer.toString(tag));
        Toast.makeText(getApplicationContext(), Integer.toString(tag),Toast.LENGTH_SHORT).show();
    }


    public void onFragmentInteraction(){

    }

}

