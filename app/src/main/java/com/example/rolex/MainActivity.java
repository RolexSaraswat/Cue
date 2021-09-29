package com.example.rolex;


import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {
    private static final int POS_CLOSE=0;
    private static final int POS_DASHBOARD=1;

    private static final int POS_NEARBY_RES=2;
    private static final int POS_SETTINGS=4;
    private static final int POS_ABOUTUS=3;
    private static final int POS_LOGOUT=6;
    private String[] screenTitles;
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;


    boolean homePressed = true, doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withDragDistance(180)
                .withRootViewScale(0.75f)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer_menu)
                .inject();
        screenIcons=loadScreenIcons();
        screenTitles= loadScreenTitles();
        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_CLOSE),
                createItemFor(POS_DASHBOARD).setChecked(true),

                createItemFor(POS_NEARBY_RES),
                createItemFor(POS_ABOUTUS),
                createItemFor(POS_SETTINGS),
                new SpaceItem(248),
                createItemFor(POS_LOGOUT)
        ));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.drawer_list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_DASHBOARD);
    }


    private Draweritem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])

                .withTextTint(color(R.color.teal_200))

                .withSelectedTextTint(color(R.color.colorAccent));
    }
    @ColorInt
    private int color(@ColorRes int res) {

        return ContextCompat.getColor(this, res);
    }
    private String[] loadScreenTitles() {

        return getResources().getStringArray(R.array.id_screenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.id_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @Override
    public void onBackPressed() {


        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        //backStackEntryCount==0 -> no fragments more.. so close the activity with warning
        if (backStackEntryCount == 1){

            if (homePressed) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();

                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
            else {
                homePressed = true;

            }

        }

        //some fragments are there.. so allow the back press action
        else {
            super.onBackPressed();
        }

    }


    @Override
    public void onItemSelected(int position) {
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        if(position==POS_DASHBOARD){
            DashboardFragment dashboardFragment=new DashboardFragment();

            transaction.add(R.id.conta, dashboardFragment);

        }
        else if(position==POS_ABOUTUS){
            AboutusFragment aboutusFragment=new AboutusFragment();
            transaction.add(R.id.conta,aboutusFragment );

        }
        else if(position==POS_NEARBY_RES){

            startActivity(new Intent(getApplicationContext(),MapsActivity.class));



        }
        else if (position == POS_LOGOUT) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),login.class));

        }
        slidingRootNav.closeMenu();
        transaction.addToBackStack(null);

        transaction.commit();

    }











}
