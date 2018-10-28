package com.example.anike.contactlist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by anike on 23-10-2018.
 */

public class NewUserAdapter extends ArrayAdapter<NameAndNumber> {



    int mResource;
    private Context mcontext;
    private CompoundButton.OnCheckedChangeListener checkedListener;
    int i=1;
    public NewUserAdapter(@NonNull Context context, int resource, @NonNull ArrayList<NameAndNumber> objects) {
        super(context, resource, objects);
        mResource=resource;
        mcontext=context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final NameAndNumber nameAndNumber = getItem(position);
        final String name = nameAndNumber.getName();
        String number = getItem(position).getNumber();
//        NameAndNumber data = new NameAndNumber(name, number);

        LayoutInflater inflater = LayoutInflater.from(mcontext);
        convertView=inflater.inflate(mResource,parent,false);
        final TextView nameOnList = convertView.findViewById(R.id.ContactNameInNew);
        nameOnList.setText(name);
        final CheckBox checkBox = convertView.findViewById(R.id.CheckBoxInNew);
        checkBox.setChecked(nameAndNumber.getCheckBox());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: called");
                if (checkBox.isChecked()){

                    Log.d(TAG, "onClick: checked : "+name);
                    nameAndNumber.setCheckBox(true);
                    notifyDataSetChanged();
                    addRelation(nameOnList.getText().toString());
                }
                else if (!checkBox.isChecked()){

                    Log.d(TAG, "onClick: unchecked :"+name);
                    nameAndNumber.setCheckBox(false);
                    notifyDataSetChanged();
                    RemoveRelation(nameOnList.getText().toString());
                }
            }
        });

        return convertView;
    }

    private void RemoveRelation(String s) {
        Log.d(TAG, "RemoveRelation: called");
        SQLiteDatabase mydatabase =getContext().openOrCreateDatabase("Users",Context.MODE_PRIVATE,null);
        mydatabase.execSQL("Delete from RelationList Where RelatedTo='"+s+"';");

    }

    private void addRelation(String relateTo) {
        Log.d(TAG, "addRelation: called");
        SQLiteDatabase mydatabase = getContext().openOrCreateDatabase("Users",Context.MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS RelationList ( RelatedTo VARCHAR )");
        mydatabase.execSQL("Insert into RelationList (RelatedTo) " +
                "Values('"+relateTo+"')");
        Log.d(TAG, "addRelation: "+relateTo);
        i++;

    }


}
