package com.besparina.it.hamyar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class AdapterHistory extends BaseAdapter {


    private ArrayList<HashMap<String, String>> list;
    private Activity activity;
    private String guid;
    private String hamyarcode;

    public AdapterHistory(Activity activity, ArrayList<HashMap<String, String>> list, String guid, String hamyarcode) {
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
        TextView txtTitleService;
        TextView txtNumberService;
        TextView txtNameCustomer;
        TextView txtDate;
        TextView txtTime;
        TextView txtPeriod;
        TextView txtEmergency;
        TextView txtCountHamyar;
        TextView txtDescription;
        TextView txtAddres;
    }
    // @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        HashMap<String, String> map = list.get(position);
        if (convertView == null) {
            Typeface faceh = Typeface.createFromAsset(activity.getAssets(), "font/IRANSans.ttf");
            convertView = inflater.inflate(R.layout.list_item_history, null);
            holder = new ViewHolder();
            holder.txtTitleService = (TextView) convertView.findViewById(R.id.txtTitleService);
            holder.txtTitleService.setTypeface(faceh);
            holder.txtTitleService.setTextSize(20);
            holder.txtNumberService = (TextView) convertView.findViewById(R.id.txtNumberService);
            holder.txtNumberService.setTypeface(faceh);
            holder.txtNumberService.setTextSize(20);
            holder.txtNameCustomer = (TextView) convertView.findViewById(R.id.txtNameCustomer);
            holder.txtNameCustomer.setTypeface(faceh);
            holder.txtNameCustomer.setTextSize(20);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            holder.txtDate.setTypeface(faceh);
            holder.txtDate.setTextSize(20);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
            holder.txtTime.setTypeface(faceh);
            holder.txtTime.setTextSize(20);
            holder.txtPeriod = (TextView) convertView.findViewById(R.id.txtPeriod);
            holder.txtPeriod.setTypeface(faceh);
            holder.txtPeriod.setTextSize(20);
            holder.txtEmergency = (TextView) convertView.findViewById(R.id.txtEmergency);
            holder.txtEmergency.setTypeface(faceh);
            holder.txtEmergency.setTextSize(20);
            holder.txtCountHamyar = (TextView) convertView.findViewById(R.id.txtCountHamyar);
            holder.txtCountHamyar.setTypeface(faceh);
            holder.txtCountHamyar.setTextSize(20);
            holder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
            holder.txtDescription.setTypeface(faceh);
            holder.txtDescription.setTextSize(20);
            holder.txtAddres = (TextView) convertView.findViewById(R.id.txtAddres);
            holder.txtAddres.setTypeface(faceh);
            holder.txtAddres.setTextSize(20);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String NumberService = map.get("NumberService");
        String TitleService = map.get("TitleService");
        String NameCustomer = map.get("NameCustomer");
        String Date = map.get("Date");
        String Time = map.get("Time");
        String Period = map.get("Period");
        String CountHamyar = map.get("CountHamyar");
        String Description = map.get("Description");
        String Addres = map.get("Addres");
        String Emergency = map.get("Emergency");
        holder.txtTitleService.setText(TitleService);
        holder.txtNumberService.setText(NumberService);
        holder.txtNameCustomer.setText(NameCustomer);
        holder.txtDate.setText(Date);
        holder.txtTime.setText(Time);
        holder.txtPeriod.setText(Period);
        holder.txtEmergency.setText(Emergency);
        holder.txtCountHamyar.setText(CountHamyar);
        holder.txtDescription.setText(Description);
        holder.txtAddres.setText(Addres);
        return convertView;
    }
}

