package com.example.anike.contactlist;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    ArrayList<NameAndNumber> nameAndNumbers = new ArrayList<>();
    ArrayList<String> DeleteList = new ArrayList<>();
    public SectionsStatePagerAdapter mSectionsStatePagerAdapter;
    public ViewPager mViewPager;
    public ViewPager pager2;
    ArrayList<String> relation=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started.");
        mSectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            mViewPager = (ViewPager) findViewById(R.id.container);
            //setup the pager
            setupViewPager(mViewPager);
        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            mViewPager = (ViewPager) findViewById(R.id.DisplayContactList);
            pager2 = (ViewPager)findViewById(R.id.containerland);
            //setup the pager
            setupViewPager(mViewPager);
            
        }

        }


    private void updateListInHome() {
        Log.d(TAG, "updateListInHome: started");
        ListView homepage = findViewById(R.id.ListViewInMain);
        homePageAdapter adapter = new homePageAdapter(this,R.layout.home_list,nameAndNumbers);
        homepage.setAdapter(adapter);
    }

    private void getListContent() {
        SQLiteDatabase mydataBase = this.openOrCreateDatabase("Users",MODE_PRIVATE,null);
        nameAndNumbers.clear();
        Log.d(TAG, "getListContent: started");
        mydataBase.execSQL("CREATE TABLE IF NOT EXISTS test ( Name VARCHAR, Number VARCHAR PRIMARY KEY)");
        Cursor c = mydataBase.rawQuery("Select * from test ",null);
        int nameIndex = c.getColumnIndex("Name");
        int numberIndex= c.getColumnIndex("Number");
        while (c.moveToNext()){
            nameAndNumbers.add(new NameAndNumber(c.getString(nameIndex),c.getString(numberIndex)));
            Log.d(TAG, "getListContent: Name = "+c.getString(nameIndex)+"  Number = "+c.getString(numberIndex));
        }
    }

    public void setupViewPager(ViewPager viewPager){
        SectionsStatePagerAdapter adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment1(), "Fragment1");
        viewPager.setAdapter(adapter);
    }
    public void setupViewPager2(ViewPager viewPager){
        SectionsStatePagerAdapter adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new Fragment4(),"Fragment 4");
        adapter.addFragment(new Fragment2(),"Fragment 2");
        adapter.addFragment(new Fragment3(),"Fragment 3");
        viewPager.setAdapter(adapter);
    }


    public void setViewPager(int fragmentNumber)
    {
        mViewPager.setCurrentItem(fragmentNumber);
    }

    public void setViewPager2(int fragmentNumber)
    {
        pager2.setCurrentItem(fragmentNumber);
    }

    public void AddContact(View view) {
        relation.clear();
        EditText NameInDetails = findViewById(R.id.NameInDetails);
        EditText NumberInDetail = findViewById(R.id.NumberInDetail);
        String name = NameInDetails.getText().toString();
        String number = NumberInDetail.getText().toString();
        if (name.matches("") || number.matches("")) {
            Toast.makeText(getApplicationContext(), " Enter Name and Number ", Toast.LENGTH_LONG).show();
        }
        else {

            SQLiteDatabase mydatabase = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);

            mydatabase.execSQL("CREATE TABLE IF NOT EXISTS test ( Name VARCHAR, Number VARCHAR PRIMARY KEY)");

            Cursor test = mydatabase.rawQuery("Select * from test where Number ='" + number + "';", null);

            if (test.moveToNext()) {
                Toast.makeText(getApplicationContext(), "This Number already exists ", Toast.LENGTH_LONG).show();
                mydatabase.execSQL("Delete from RelationList");

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

//                SQLiteDatabase mydatabase = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);


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


                Log.d(TAG, "AddContact: deleting  ");
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


                startActivity(new Intent(this, MainActivity.class));

            }
        }
    }

    public void Add(View view) {
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setupViewPager2(pager2);

            Log.d(TAG, "Add: clicked");


        }
        }


    public void Delete(View view) {

        SQLiteDatabase mydatabase = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);
        mydatabase.execSQL("Delete from RelationList");
        mydatabase.execSQL("Delete from finalRelation");
        mydatabase.execSQL("Delete from test");
        nameAndNumbers.clear();
        Log.d(TAG, "showData: deleted");
        getListContent();
        updateListInHome();
    }



    public void DeleteContact(View view) {

        SQLiteDatabase mydataBase = this.openOrCreateDatabase("Users",MODE_PRIVATE,null);
        DeleteList.clear();
        mydataBase.execSQL("CREATE TABLE IF NOT EXISTS DeleteList ( ContactName VARCHAR )");
        Cursor c = mydataBase.rawQuery("Select * from DeleteList",null);
        int DeleteIndex = c.getColumnIndex("ContactName");
        while(c.moveToNext()){
            Log.d(TAG, "DeleteContact: contact to be deleted is  "+c.getString(DeleteIndex));
            DeleteList.add(c.getString(DeleteIndex));
        }




        mydataBase.execSQL("Delete from DeleteList");
        Log.d(TAG, "DeleteContact: starting to delete");
        for (int i =0; i<DeleteList.size();i++){
            Log.d(TAG, "DeleteContact: the element to be deleted is "+DeleteList.get(i));
            mydataBase.execSQL("Delete from test Where Name ='"+DeleteList.get(i)+"';");
            mydataBase.execSQL("Delete from finalRelation Where ContactName='"+DeleteList.get(i)+"' OR RelatedTo='"+DeleteList.get(i)+"';");
        }



         setupViewPager(mViewPager);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
         setupViewPager2(pager2);
    }
    }
}

