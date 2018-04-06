package com.project.it.hamyar;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class AdapterMessage extends BaseAdapter {

    private String guid;
    private String hamyarcode;
    private int check_tab;
    public ArrayList<HashMap<String, String>> list;
    Activity activity;

    public AdapterMessage(Activity activity,ArrayList<HashMap<String, String>> list) {
        super();
        this.activity = activity;
        this.list = list;
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
        TextView txtContent;
        ImageView imgIcon;
    }

    // @Override
    @SuppressLint("ResourceAsColor")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        HashMap<String, String> map = list.get(position);
        if (convertView == null) {
            Typeface faceh = Typeface.createFromAsset(activity.getAssets(), "font/BMitra.ttf");
            convertView = inflater.inflate(R.layout.list_item_message, null);
            holder = new ViewHolder();
            holder.imgIcon = (ImageView)convertView.findViewById(R.id.imgIcon);
            holder.txtValues = (TextView) convertView.findViewById(R.id.txtTitleMail);
            holder.txtContent = (TextView) convertView.findViewById(R.id.txtContentMail);
            holder.txtValues.setTypeface(faceh);
            holder.txtValues.setTextSize(18);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name = map.get("Title");
        String code = map.get("Code");
        String content = map.get("Content");
        String readIcon = map.get("IsReade");
        if(readIcon.compareTo("0")==0)
        {
            holder.txtValues.setTextColor(R.color.md_red_A700);
            holder.imgIcon.setImageResource(R.drawable.munread);
        }
        else
        {
            holder.txtValues.setTextColor(R.color.md_green_500);
            holder.imgIcon.setImageResource(R.drawable.mread);
        }
        holder.imgIcon.setTag(code);
        holder.imgIcon.setOnClickListener(ImageItemOnclick);
        holder.txtValues.setText(name);
        holder.txtContent.setText(content);
        holder.txtValues.setTag(code);
        holder.txtValues.setOnClickListener(TextViewItemOnclick);
        holder.txtContent.setOnClickListener(TextViewItemOnclick);

        return convertView;
    }


    private OnClickListener TextViewItemOnclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String item = ((TextView)v).getTag().toString();
            Intent intent = new Intent(activity.getApplicationContext(),ShowMessage.class);
            intent.putExtra("Code",item);
            activity.startActivity(intent);
        }
    };
    private OnClickListener ImageItemOnclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String item = ((ImageView)v).getTag().toString();
            Intent intent = new Intent(activity.getApplicationContext(),ShowMessage.class);
            intent.putExtra("Code",item);
            activity.startActivity(intent);
        }
    };

}

