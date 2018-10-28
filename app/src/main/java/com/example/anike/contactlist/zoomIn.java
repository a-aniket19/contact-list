package com.example.anike.contactlist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by anike on 26-10-2018.
 */

public class zoomIn extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoom_in_image);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width  = dm.widthPixels;
        int height = dm.heightPixels;


        getWindow().setLayout((int) (width*0.5),(int) (height*0.5));

        Intent intent = getIntent();
        String imageName = intent.getExtras().getString("imageName");
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Images");

        String filename = (file.getAbsolutePath() + "/"
                +imageName+ ".jpg");


        ImageView imageView = findViewById(R.id.ZoomedInImage);
        imageView.setImageDrawable(Drawable.createFromPath(filename));


    }
}
