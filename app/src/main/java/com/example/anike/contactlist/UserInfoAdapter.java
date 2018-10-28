package com.example.anike.contactlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by anike on 24-10-2018.
 */

class UserInfoAdapter extends ArrayAdapter<String>{
    int mResource;
    private Context mcontext;

    public UserInfoAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        mResource=resource;
        mcontext=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        convertView = inflater.inflate(mResource, parent, false);
        TextView RelativeName = convertView.findViewById(R.id.Name_profile);
        RelativeName.setText(name);
        return convertView;
    }
}
