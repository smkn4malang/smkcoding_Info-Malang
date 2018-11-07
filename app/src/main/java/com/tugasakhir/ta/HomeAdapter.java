package com.tugasakhir.ta;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class HomeAdapter extends FragmentStatePagerAdapter {
    public HomeAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position){
        if (position == 0){
            return new hariIniFragment();
        }else if (position == 1){
            return new terdahuluFragment();
        }
        throw new IllegalStateException("position not valid");
    }

    @Override
    public int getCount(){
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int Position){
        if(Position == 0 ){
            return "NEWS";
        }else if (Position == 1) {
            return "My Posts";
        }
        throw new IllegalStateException("Position not valid");
    }
}