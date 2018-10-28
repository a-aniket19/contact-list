package com.example.anike.contactlist;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by anike on 22-10-2018.
 */

public class homePageAdapter extends ArrayAdapter<NameAndNumber>  {
    private static final String TAG = "homePageAdapter";
    int mResource;
    private Context mcontext;


    public homePageAdapter(@NonNull Context context, int resource, @NonNull ArrayList<NameAndNumber> objects) {
        super(context, resource, objects);
        mcontext=context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final NameAndNumber Data = getItem(position);
        String name = getItem(position).getName();
        //String number = getItem(position).getNumber();

        //NameAndNumber data = new NameAndNumber(name, number);

        LayoutInflater inflater = LayoutInflater.from(mcontext);
        convertView=inflater.inflate(mResource,parent,false);
        final TextView nameOnList = convertView.findViewById(R.id.ContactName);
        nameOnList.setText(name);
        if(getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            nameOnList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddCallingContact(nameOnList.getText().toString());
                    if (mcontext instanceof MainActivity) {
                        Intent intent = new Intent(mcontext, Main3Activity.class);
                        mcontext.startActivity(intent);
                    }
                }
            });
        }
        if(getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            nameOnList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddCallingContact(nameOnList.getText().toString());
                    Log.d(TAG, "getView: called");
                    ((MainActivity)mcontext).setupViewPager2(((MainActivity) mcontext).pager2);
                    ((MainActivity)mcontext).setViewPager2(2);
                }
            });


        }
        final CheckBox checkBox = convertView.findViewById(R.id.CheckBoxInHome);
        checkBox.setChecked(Data.getCheckBox());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    Log.d(TAG, "onClick: checked");
                    Data.setCheckBox(true);
                    notifyDataSetChanged();
                    addContactToDelete(nameOnList.getText().toString());
                }
                else if (!checkBox.isChecked()){
                    Log.d(TAG, "onClick: unchecked");
                    Data.setCheckBox(false);
                    notifyDataSetChanged();
                    removeContactToDelete(nameOnList.getText().toString());
                }
            }
        });

        return convertView;
    }

    private void removeContactToDelete(String s) {
        Log.d(TAG, "removeContactToDelete: called ");
        SQLiteDatabase mydatabase =getContext().openOrCreateDatabase("Users",Context.MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS DeleteList ( ContactName VARCHAR )");
        mydatabase.execSQL("Delete from DeleteList where ContactName ='"+s+"';");
        Log.d(TAG, "removeContactToDelete: removed "+s);

    }


    private void addContactToDelete(String s) {
        Log.d(TAG, "addContactToDelete: called ");
        SQLiteDatabase mydatabase = getContext().openOrCreateDatabase("Users",Context.MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS DeleteList ( ContactName VARCHAR )");
        mydatabase.execSQL("insert into DeleteList (ContactName) " +
                "Values('"+s+"')");

        Log.d(TAG, "addContactToDelete: added "+s);

    }

    private void AddCallingContact(String s) {

        Log.d(TAG, "AddCallingContact: clicked Name is  "+s);
        SQLiteDatabase mydatabase = getContext().openOrCreateDatabase("Users",Context.MODE_PRIVATE,null);
        mydatabase.execSQL("Create Table If Not Exists tappedContact (ContactName Varchar)");
        mydatabase.execSQL("Delete from tappedContact");
        mydatabase.execSQL("Insert into tappedContact (ContactName) Values('"+s+"')");
        Log.d(TAG, "AddCallingContact: done adding");

    }
}

