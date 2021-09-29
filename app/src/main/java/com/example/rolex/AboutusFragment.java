package com.example.rolex;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;




import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;



public class AboutusFragment extends Fragment {
    private ActionBar actionBar;

    private ViewPager viewPager;

    private ArrayList<WhyCue> modelArrayList;

    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        ViewGroup root =(ViewGroup) inflater.inflate(R.layout.aboutus_fragment , container, false);

        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        viewPager = root.findViewById(R.id.viewpager);
        loadcards();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                String title = modelArrayList.get(position).getTitle();
                actionBar.setTitle(title);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return root;}
    private void loadcards() {
        modelArrayList = new ArrayList<>();

        modelArrayList.add(new WhyCue(
                "Why CUE?",
                "",
                R.drawable.why_cue));

        modelArrayList.add(new WhyCue(
                "Predict your next date!",
                "",
                R.drawable.predict));

        modelArrayList.add(new WhyCue(
                "Explore other features!",
                "",
                R.drawable.more));


        Predict predict = new Predict(getActivity(), modelArrayList);
        viewPager.setAdapter(predict);
        viewPager.setPadding(100,0,100,0);
    }
}
