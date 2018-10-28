package com.example.anike.contactlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.zip.Inflater;


/**
 * Created by anike on 26-10-2018.
 */

public class Fragment4 extends Fragment {

    private static final String TAG="Fragment4";

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: called fragment 4 ");
        View view = inflater.inflate(R.layout.fragment4, container , false);
        ((MainActivity)getContext()).setViewPager2(1);
        return view;
    }
}
