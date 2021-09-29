package com.example.rolex;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class LoginAdapter extends FragmentPagerAdapter {
     Context context;
    int totalTabs;
    public  LoginAdapter(FragmentManager fm, Context context, int totalTabs){
        super(fm);
        this.context=context;
        this.totalTabs=totalTabs;

    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    public Fragment getItem(int position){
        switch (position){
            case 0:
                Logintabfragment logintabfragment = new Logintabfragment();
                return logintabfragment;
            case 1:
                Signuptabfragment signuptabfragment =new Signuptabfragment();
                return signuptabfragment;
            default:return null;
        }
}
}
