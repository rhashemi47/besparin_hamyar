package com.besparina.it.hamyar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class AdapterListServiceNow extends BaseAdapter {


    private ArrayList<HashMap<String, String>> list;
    private Activity activity;
    private String guid;
    private String hamyarcode;

    public AdapterListServiceNow(Activity activity, ArrayList<HashMap<String, String>> list, String guid, String hamyarcode) {
        super();
        this.activity = activity;
        this.list = list;
        this.guid = guid;
        this.hamyarcode = hamyarcode;
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
        TextView txtContentService;
    }

    // @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        HashMap<String, String> map = list.get(position);
        if (convertView == null) {
            Typeface faceh = Typeface.createFromAsset(activity.getAssets(), "font/BMitra.ttf");
            convertView = inflater.inflate(R.layout.list_item_duty_now, null);
            holder = new ViewHolder();
            holder.txtContentService = (TextView) convertView.findViewById(R.id.txtContentService);
            holder.txtContentService.setTypeface(faceh);
            holder.txtContentService.setTextSize(16);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String ContentService = map.get("ContentService");
        String BsUserServicesID = map.get("BsUserServicesID");
        holder.txtContentService.setText(ContentService);
        holder.txtContentService.setTag(BsUserServicesID);
        holder.txtContentService.setOnClickListener(TextViewItemOnclick);
        return convertView;
    }


    private OnClickListener TextViewItemOnclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String BsUserServicesID="";
            BsUserServicesID = ((TextView)v).getTag().toString();
            Intent intent = new Intent(activity.getApplicationContext(),ViewJob.class);
            intent.putExtra("guid",guid);
            intent.putExtra("hamyarcode",hamyarcode);
            intent.putExtra("BsUserServicesID",BsUserServicesID);
            intent.putExtra("tab","0");
            intent.putExtra("back_activity","MainMenu");
            activity.startActivity(intent);
        }
    };
}

