package com.example.tomro.books_r_us;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by tomro on 06/12/2017.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return homeFragment.newInstance();
        } else if (position == 1){
            return booksFragment.newInstance("books","test");
        } else if (position == 2){
            return searchFragment.newInstance("search","test");
        } else {
            return homeFragment.newInstance();
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 3;
    }

}