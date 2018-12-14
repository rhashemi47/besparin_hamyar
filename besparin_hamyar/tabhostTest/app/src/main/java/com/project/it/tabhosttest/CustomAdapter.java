package com.besparina.it.hamyar;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.besparina.it.hamyar.Date.ChangeDate;
import com.besparina.it.hamyar.R;
import com.besparina.it.hamyar.ViewJob;

@SuppressLint("NewApi")
public class CustomAdapter extends BaseAdapter {

    private String guid;
    private String hamyarcode;
    private int check_tab;
    private	DatabaseHelper dbh;
    private SQLiteDatabase db;
    private Cursor coursors;
    public ArrayList<HashMap<String, String>> list;
    Activity activity;

    public CustomAdapter(Activity activity,ArrayList<HashMap<String, String>> list,String guid,String hamyarcode,int checktab) {
        super();
        this.activity = activity;
        this.list = list;
        this.guid = guid;
        this.hamyarcode = hamyarcode;
        this.check_tab=checktab;

    }

    // @Override
    public int getCount() {
        return list.size();
    }

    // @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    // @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView txtValues;
    }

    // @Override
    @SuppressLint("ResourceAsColor")
    public View getView(int position, View convertView, ViewGroup parent) {
        dbh=new DatabaseHelper(activity);
        try {

            dbh.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            dbh.openDataBase();

        } catch (SQLException sqle) {

            throw sqle;
        }
        try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        HashMap<String, String> map = list.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            Typeface faceh = Typeface.createFromAsset(activity.getAssets(), "font/BMitra.ttf");

            if(check_tab==0)
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String currentDateandTime = sdf.format(new Date());
                String Emergency;
                String End;
                String query = "SELECT * FROM BsHamyarSelectServices WHERE id=" + map.get("id");
                long diff = -1;
                Date dateCurrent = new Date();
                String date1=dateCurrent.toString();
                coursors = db.rawQuery(query, null);
                for (int i = 0; i < coursors.getCount(); i++) {
                    coursors.moveToNext();
                    Emergency=coursors.getString(coursors.getColumnIndex("IsEmergency"));
                    End= ChangeDate.changeFarsiToMiladi(coursors.getString(coursors.getColumnIndex("StartDate")));
                    try
                    {
                        Date EndDate = sdf.parse(End);
                        Date StartDate = sdf.parse(currentDateandTime);
                        diff = Math.round((EndDate.getTime()-StartDate.getTime()) / (double) 86400000);
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(Emergency.compareTo("1")==0 || diff<=0 || diff==1)
                    {
                        convertView = inflater.inflate(R.layout.fragment_values2, null);
                    }
                    else
                    {
                        convertView = inflater.inflate(R.layout.fragment_values, null);
                    }
                    holder.txtValues = (TextView) convertView.findViewById(R.id.txtValues);
                    holder.txtValues.setTypeface(faceh);
                    holder.txtValues.setTextSize(24);
                    holder.txtValues.setBackgroundResource(R.drawable.rounded_listview_jobs);
                }


            }
            else
            {

                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String currentDateandTime = sdf.format(new Date());
                String Emergency;
                String End;
                String query = "SELECT * FROM BsUserServices WHERE id=" + map.get("id");
                long diff = -1;
                coursors = db.rawQuery(query, null);
                for (int i = 0; i < coursors.getCount(); i++) {
                    coursors.moveToNext();
                    Emergency=coursors.getString(coursors.getColumnIndex("IsEmergency"));
                    End= ChangeDate.changeFarsiToMiladi(coursors.getString(coursors.getColumnIndex("StartDate")));
                    try
                    {
                        Date EndDate = sdf.parse(End);
                        Date StartDate = sdf.parse(currentDateandTime);
                        diff = Math.round((EndDate.getTime()-StartDate.getTime()) / (double) 86400000);
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(Emergency.compareTo("1")==0 || diff<=0 || diff==1)
                    {
                        convertView = inflater.inflate(R.layout.fragment_values2, null);
                    }
                    else
                    {
                        convertView = inflater.inflate(R.layout.fragment_values, null);
                    }
                    holder.txtValues = (TextView) convertView.findViewById(R.id.txtValues);
                    holder.txtValues.setTypeface(faceh);
                    holder.txtValues.setTextSize(24);
                    holder.txtValues.setBackgroundResource(R.drawable.rounded_listview_jobs);
                }
                holder.txtValues.setBackgroundResource(R.drawable.rounded_listview_dutys);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        String name = map.get("name");
        String code = map.get("id");

        holder.txtValues.setText(name);
        //map.get("title")
        holder.txtValues.setTag(code);
        holder.txtValues.setOnClickListener(TextViewItemOnclick);

        return convertView;
    }


    private OnClickListener TextViewItemOnclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
           // String item = ((TextView)v).getTag().toString();
            String BsUserServicesID="";
            BsUserServicesID = ((TextView)v).getTag().toString();
            Intent intent = new Intent(activity.getApplicationContext(),ViewJob.class);
            intent.putExtra("guid",guid);
            intent.putExtra("hamyarcode",hamyarcode);
            intent.putExtra("BsUserServicesID",BsUserServicesID);
            if(check_tab==0)
            {
                intent.putExtra("tab","0");
            }
            else
            {
                intent.putExtra("tab","1");
            }
            activity.startActivity(intent);
        }
    };

}

