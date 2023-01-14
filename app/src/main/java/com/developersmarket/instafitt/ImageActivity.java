package com.developersmarket.instafitt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.developersmarket.instafitt.adapter.ViewPagerAdapter;
import com.developersmarket.instafitt.model.ImageModel;

import java.util.ArrayList;


public class ImageActivity extends AppCompatActivity {
    ViewPager mViewPager;
    private static final String TAG = "ImageActivity";
    // images array
    ArrayList<String> urls = new ArrayList<>();
    // Creating Object of ViewPagerAdapter
    ViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        // Initializing the ViewPager Object
        mViewPager = (ViewPager) findViewById(R.id.viewPagerMain);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        int loc = intent.getIntExtra("imageLoc", 1);
        ArrayList<ImageModel> object = (ArrayList<ImageModel>) args.getSerializable("ARRAYLIST");
        for (int i = 0; i < object.size(); i++) {
            urls.add(object.get(i).getImgUrl());

        }
        // Initializing the ViewPagerAdapter

        mViewPagerAdapter = new ViewPagerAdapter(ImageActivity.this, object, loc);
        // Adding the Adapter to the ViewPager
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(loc);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e(TAG, "onPageScrolled: " + position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "onPageSelected: " + position);
                MainActivity.Companion.rvScrolltoPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e(TAG, "onPageScrollStateChanged: " + state);
            }
        });

    }
}