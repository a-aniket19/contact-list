package com.example.anike.contactlist;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "Main2Activity";
    private SectionsStatePagerAdapter mSectionsStatePagerAdapter;
    private ViewPager mViewPager;
    ArrayList<String> relation=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ActivityCompat.requestPermissions(Main2Activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        Log.d(TAG, "onCreate: Started ");
        Log.d(TAG, "checking 1");

        mSectionsStatePagerAdapter=new SectionsStatePagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.containter2);

        setupViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsStatePagerAdapter adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment2(),"Fragment 2");
        Log.d(TAG, "checking2 ");
        viewPager.setAdapter(adapter);
    }





    public void AddContact(View view) {
        relation.clear();
        EditText NameInDetails = findViewById(R.id.NameInDetails);
        EditText NumberInDetail = findViewById(R.id.NumberInDetail);
        String name = NameInDetails.getText().toString();
        String number = NumberInDetail.getText().toString();
        if (name.matches("") || number.matches("")) {
            Toast.makeText(getApplicationContext(), " Enter Name and Number ", Toast.LENGTH_LONG).show();
        } else {

            SQLiteDatabase mydatabase = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);
            mydatabase.execSQL("CREATE TABLE IF NOT EXISTS test ( Name VARCHAR, Number VARCHAR PRIMARY KEY)");
            Cursor test = mydatabase.rawQuery("Select * from test where Number ='" + number + "';", null);
            if (test.moveToNext()) {
                Toast.makeText(getApplicationContext(), "This Number already exists ", Toast.LENGTH_LONG).show();
                mydatabase.execSQL("Delete from RelationList");
                setupViewPager(mViewPager);
            } else {
                ImageView imageView = (ImageView) findViewById(R.id.picture);
                Bitmap bm = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                FileOutputStream out = null;
                File file = new File(Environment.getExternalStorageDirectory().getPath(), "Images");
                Log.d(TAG, "filepath: " + file);
                if (!file.exists()) {
                    file.mkdirs();
                }
                String filename = (file.getAbsolutePath() + "/"
                        + name + ".jpg");
                try {
                    out = new FileOutputStream(filename);
                    bm.compress(Bitmap.CompressFormat.PNG, 100, out);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "AddContact: Name = " + name + "  Number = " + number);


                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS test ( Name VARCHAR, Number VARCHAR PRIMARY KEY)");
                mydatabase.execSQL("Insert into test (Name , Number) " +
                        "Values('" + name + "','" + number + "')");


                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS RelationList ( RelatedTo VARCHAR )");
                Cursor cursor = mydatabase.rawQuery("Select * from RelationList", null);
                int RelatedToIndex = cursor.getColumnIndex("RelatedTo");

                while (cursor.moveToNext()) {
                    Log.d(TAG, "AddContact:  the relationship is with  " + cursor.getString(RelatedToIndex));
                    Log.d(TAG, "AddContact: adding " + cursor.getString(RelatedToIndex));
                    relation.add(cursor.getString(RelatedToIndex));
                }


                mydatabase.execSQL("Delete from RelationList");
                Log.d(TAG, "AddContact: size of relation ArrayList is " + Integer.toString(relation.size()));
                for (int i = 0; i < relation.size(); i++) {
                    mydatabase.execSQL("Create Table if not Exists finalRelation ( ContactName VARCHAR, RelatedTo VARCHAR)");
                    mydatabase.execSQL("Insert into finalRelation(ContactName,RelatedTo)" +
                            "Values('" + name + "','" + relation.get(i) + "')");
                    Log.d(TAG, "AddContact: adding " + name + " is related to " + relation.get(i));
                }

                for (int j = 0; j < relation.size(); j++) {
                    mydatabase.execSQL("Create Table if not Exists finalRelation ( ContactName VARCHAR, RelatedTo VARCHAR)");
                    mydatabase.execSQL("Insert into finalRelation(ContactName,RelatedTo)" +
                            "Values('" + relation.get(j) + "','" + name + "')");
                    Log.d(TAG, "AddContact: adding " + relation.get(j) + " is related to " + name);
                }

                finish();
            }
        }
    }
}
