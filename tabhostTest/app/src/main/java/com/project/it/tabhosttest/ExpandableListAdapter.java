package com.besparina.it.hamyar;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context _context;
    private List<String> _listDataHeader; // header titles.
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    
    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        dbh=new DatabaseHelper(context.getApplicationContext());
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
    }
	
	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
		final String childText = (String) getChild(groupPosition, childPosition);
		  
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_itemstepjob, null);
        }
  
        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItemStepJob);
        RadioButton radioB = (RadioButton) convertView.findViewById(R.id.radio1);
		radioB.setTag(childText);
        txtListChild.setText(childText);
       // chb.setOnCheckedChangeListener(c);
		radioB.setOnClickListener(c);
        return convertView;
	}
	
	OnClickListener c= new OnClickListener() {
		
		@Override
		 public void onClick(View v) {
            //is chkIos checked?
	if (((RadioButton) v).isChecked())
	{

		try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
		Cursor coursors=db.rawQuery("SELECT * FROM servicesdetails WHERE name='"+((RadioButton) v).getTag().toString()+"'", null);
		db=dbh.getWritableDatabase();			
		coursors.moveToNext();
		db.execSQL("INSERT INTO HmFactorService (code) VALUES('"+coursors.getString(coursors.getColumnIndex("code")) +"')");
	}
	else
	{
		//Toast.makeText(_context,"Unchecked)", Toast.LENGTH_LONG).show();
		try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
		Cursor coursors=db.rawQuery("SELECT * FROM exprtise WHERE name='"+((RadioButton) v).getTag().toString()+"'", null);
		db=dbh.getWritableDatabase();			
		coursors.moveToNext();
		db.execSQL("DELETE FROM HmFactorService WHERE code='"+coursors.getString(coursors.getColumnIndex("code")) +"')");
	}

  }
	};

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		 return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_expertise, null);
        }
  
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
  
        return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
