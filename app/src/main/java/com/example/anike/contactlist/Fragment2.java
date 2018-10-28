package com.example.anike.contactlist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by User on 4/9/2017.
 */

public class Fragment2 extends Fragment {
    private static final String TAG = "Fragment2";
    private ListView listInHome;
    ArrayList<NameAndNumber> nameAndNumbers = new ArrayList<>();
    ArrayList<Integer> clickedPositions = new ArrayList<>();
    public ImageView image;
    public Bitmap imageBitmap;
    static final int REQUEST_IMAGE_CAPTURE =1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final int REQUEST_IMAGE_CAPTURE = 1;

        View view  = inflater.inflate(R.layout.fragment2, container, false);


        listInHome= view.findViewById(R.id.ListVIewInDetail);


        listInHome.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        getListContent();
        updateListInHome();

        image = view.findViewById(R.id.picture);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }


            }
        });

        Log.d(TAG, "checking 3.");
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            File photoFile = null;

            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);
        }
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
        NewUserAdapter adapter = new NewUserAdapter(getActivity(),R.layout.new_user_list,nameAndNumbers);
        listInHome.setAdapter(adapter);
    }
}
