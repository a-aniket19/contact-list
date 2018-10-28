package com.example.anike.contactlist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by User on 4/9/2017.
 */

public class Fragment1 extends Fragment {
    private static final String TAG = "Fragment1";
    ArrayList<NameAndNumber> nameAndNumbers = new ArrayList<>();
    private Button btnNavFrag1;
    private ListView listInHome;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment1, container, false);
        btnNavFrag1 =view.findViewById(R.id.addNewProfile);
        listInHome= view.findViewById(R.id.ListViewInMain);
        getListContent();
        updateListInHome();

        Log.d(TAG, "onCreateView: started.");
        return view;
    }

    public void onResume(){
        super.onResume();
        nameAndNumbers.clear();
        getListContent();
        updateListInHome();

    }

    private void getListContent() {
        SQLiteDatabase mydataBase = getActivity().openOrCreateDatabase("Users", Context.MODE_PRIVATE,null);
        mydataBase.execSQL("CREATE TABLE IF NOT EXISTS test ( Name VARCHAR, Number VARCHAR PRIMARY KEY)");
        Cursor c = mydataBase.rawQuery("Select * from test",null);
        int nameIndex = c.getColumnIndex("Name");
        int numberIndex= c.getColumnIndex("Number");
        while (c.moveToNext()){
            nameAndNumbers.add(new NameAndNumber(c.getString(nameIndex),c.getString(numberIndex)));
            Log.d(TAG, "getListContent: Name = "+c.getString(nameIndex)+"  Number = "+c.getString(numberIndex));
        }
    }

    private void updateListInHome() {
        homePageAdapter adapter = new homePageAdapter(getActivity(),R.layout.home_list,nameAndNumbers);
        listInHome.setAdapter(adapter);
    }
}
