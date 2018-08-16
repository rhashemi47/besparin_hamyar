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
public class AdapterServices extends BaseAdapter {


    private ArrayList<HashMap<String, String>> list;
    private Activity activity;
    private String guid;
    private String hamyarcode;

    public AdapterServices(Activity activity, ArrayList<HashMap<String, String>> list, String guid, String hamyarcode) {
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
        TextView txtLocationService;
        TextView txtDate;
        TextView txtTime;
        TextView txtDescription;
        LinearLayout LinearList;
        LinearLayout LinearTitle;
//        Button btnNumberPhone;
    }

    // @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        HashMap<String, String> map = list.get(position);
        if (convertView == null) {
            Typeface faceh = Typeface.createFromAsset(activity.getAssets(), "font/BMitra.ttf");
            convertView = inflater.inflate(R.layout.list_item_service, null);
            holder = new ViewHolder();
            holder.LinearList = (LinearLayout) convertView.findViewById(R.id.LinearList);
            holder.LinearTitle = (LinearLayout) convertView.findViewById(R.id.LinearTitle);
//            holder.btnNumberPhone = (Button) convertView.findViewById(R.id.btnCallFromList);
//            holder.btnNumberPhone.setTypeface(faceh);
//            holder.btnNumberPhone.setTextSize(16);
            holder.txtLocationService = (TextView) convertView.findViewById(R.id.txtLocationService);
            holder.txtLocationService.setTypeface(faceh);
            holder.txtLocationService.setTextSize(16);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            holder.txtDate.setTypeface(faceh);
            holder.txtDate.setTextSize(16);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
            holder.txtTime.setTypeface(faceh);
            holder.txtTime.setTextSize(16);
            holder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
            holder.txtDescription.setTypeface(faceh);
            holder.txtDescription.setTextSize(16);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String LocationService = map.get("LocationService");
        String Date = map.get("Date");
        String Time = map.get("Time");
        String code = map.get("Code");
        String Emergency = map.get("Emergency");
        String Description = map.get("Description");
//        String UserPhone = map.get("UserPhone");
        if(Emergency.compareTo("عادی")==0)
        {
            holder.LinearTitle.setBackgroundColor(Color.parseColor("#95d1db"));
        }
        else
        {
            holder.LinearTitle.setBackgroundColor(Color.parseColor("#db7c76"));
        }
        holder.txtLocationService.setText(LocationService);
        holder.txtDate.setText(Date);
        holder.txtTime.setText(Time);
        holder.txtDescription.setText(Description);
//        holder.btnNumberPhone.setTag(UserPhone);
        holder.LinearList.setTag(code);
        holder.LinearList.setOnClickListener(TextViewItemOnclick);
//        holder.btnNumberPhone.setOnClickListener(ButtonItemOnClick);

        return convertView;
    }


    private OnClickListener TextViewItemOnclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String BsUserServicesID="";
            BsUserServicesID = ((LinearLayout)v).getTag().toString();
            Intent intent = new Intent(activity.getApplicationContext(),ViewJob.class);
            intent.putExtra("guid",guid);
            intent.putExtra("hamyarcode",hamyarcode);
            intent.putExtra("BsUserServicesID",BsUserServicesID);
            intent.putExtra("tab","1");
            activity.startActivity(intent);
        }
    };
    private OnClickListener ButtonItemOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String UserPhone="";
            UserPhone = ((Button)v).getTag().toString();
            if (ActivityCompat.checkSelfPermission(activity,
                    android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if(ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.CALL_PHONE))
                {
                    //do nothing
                }
                else{

                    ActivityCompat.requestPermissions(activity,new String[]{android.Manifest.permission.CALL_PHONE},2);
                }

            }
            dialContactPhone(UserPhone);
        }
    };
    public void dialContactPhone(String phoneNumber) {
        //startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        activity.startActivity(callIntent);
    }
}

