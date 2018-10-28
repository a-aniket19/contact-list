package com.example.anike.contactlist;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by User on 4/9/2017.
 */

public class Fragment3 extends Fragment {
    private static final String TAG = "Fragment3";
    String contactName1,contactName , contactNumber ;
    TextView NameInInfo, NumberInInfo;
    private ListView listInDetail;
    ArrayList<String> RelatedTo = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment3, container, false);
        listInDetail= view.findViewById(R.id.ListViewInInfo);
        Log.d(TAG, "onCreateView: started");
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Images");

        getListContent();
        updateListInHome();
        NameInInfo= view.findViewById(R.id.NameInInfo);
        Log.d(TAG, "onCreateView: " +NameInInfo);
        NumberInInfo=view.findViewById(R.id.NumberInInfo);

        String filename = (file.getAbsolutePath() + "/"
                + contactName+ ".jpg");
        ImageView imageview = view.findViewById(R.id.fragment3picture);
        imageview.setImageDrawable(Drawable.createFromPath(filename));
        NameInInfo.setText(contactName);
        NumberInInfo.setText(contactNumber);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(),zoomIn.class);
                intent.putExtra("imageName",contactName);
                startActivity(intent);

            }
        });
        return view;
    }

    private void updateListInHome() {
        UserInfoAdapter adapter = new UserInfoAdapter(getActivity(),R.layout.info_list,RelatedTo);
        listInDetail.setAdapter(adapter);

    }

    private void getListContent() {
        Log.d(TAG, "getListContent: Started");
        SQLiteDatabase mydatabase = getActivity().openOrCreateDatabase("Users", Context.MODE_PRIVATE, null);
        mydatabase.execSQL("Create Table If Not Exists tappedContact (ContactName Varchar)");
        Cursor c = mydatabase.rawQuery("Select * from tappedContact", null);
        int index = c.getColumnIndex("ContactName");
        c.moveToNext();
        contactName = c.getString(index);


        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS test ( Name VARCHAR, Number VARCHAR PRIMARY KEY)");
        Cursor cursor = mydatabase.rawQuery("Select * from test where Name='"+contactName+"';",null);
        //int nameColumnID = cursor.getColumnIndex("Name");
        int numberColumnID = cursor.getColumnIndex("Number");
        Log.d(TAG, "getListContent: cursor "+Integer.toString(cursor.getColumnIndex("Number")));

        while (cursor.moveToNext()) {
            contactNumber=cursor.getString(numberColumnID);
            Log.d(TAG, "getListContent: number is : "+cursor.getString(numberColumnID));
        }


        mydatabase.execSQL("Create Table if not Exists finalRelation ( ContactName VARCHAR, RelatedTo VARCHAR)");
        Cursor c2 = mydatabase.rawQuery("Select * from finalRelation Where ContactName ='"+contactName+"';",null);
        int relatedColumnId = c2.getColumnIndex("RelatedTo");
        while (c2.moveToNext()){
            RelatedTo.add(c2.getString(relatedColumnId));
            Log.d(TAG, "getListContent: "+c2.getString(relatedColumnId));
        }



    }
}
