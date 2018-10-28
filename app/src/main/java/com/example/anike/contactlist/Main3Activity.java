package com.example.anike.contactlist;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Main3Activity extends AppCompatActivity {


    private static final String TAG = "Main3Activity";

    private SectionsStatePagerAdapter mSectionsStatePagerAdapter;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Log.d(TAG, "onCreate: Started.");

        mSectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        mViewPager=findViewById(R.id.container3);

        setupViewPager(mViewPager);

    }

    public void setupViewPager(ViewPager viewPager) {
        Log.d(TAG, "setupViewPager: started");
        SectionsStatePagerAdapter adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment3(),"Fragment 3");
        viewPager.setAdapter(adapter);
    }

}
