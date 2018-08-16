package com.besparina.it.hamyar;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Info_Person extends Activity {
	private String exExpertiseString="";
	private String education="";
	private Button btnSendInfo;
	private List<String> labels = new ArrayList<String>();
	private String phonenumber;
	private String Acceptcode;
	private EditText fname;
	private EditText lname;
	private EditText brithday;
	private String year;
	private String mon;
	private String day;
	private Spinner spEducation;
	private	DatabaseHelper dbh;
	private SQLiteDatabase db;
	private ExpandableListView exExpertise;
	private ExpandableListAdapter listAdapter;
    //ExpandableListView expListView;
	private List<String> listDataHeader;
	private HashMap<String, List<String>> listDataChild;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.info_personal);
		super.onCreate(savedInstanceState);		
		
		dbh=new DatabaseHelper(getApplicationContext());
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
   		try
        {
        	phonenumber = getIntent().getStringExtra("phonenumber").toString();
        	
        }
        catch (Exception e) {
//        	phonenumber="0";
//        	acceptcode="0";
//        	guid="0";
		}		
   		try
        {
   			Acceptcode = getIntent().getStringExtra("acceptcode").toString();
        }
        catch (Exception e) {
//        	phonenumber="0";
//        	acceptcode="0";
//        	guid="0";
		}			
		fname=(EditText)findViewById(R.id.etFname);
		lname=(EditText)findViewById(R.id.etLname);
		brithday=(EditText)findViewById(R.id.etBrithday);
		spEducation=(Spinner)findViewById(R.id.spEdudation);
		exExpertise=(ExpandableListView)findViewById(R.id.evExpertise);
		btnSendInfo=(Button)findViewById(R.id.btnSendInfo);
		btnSendInfo.setOnClickListener(new OnClickListener() {
		public void onClick(View arg0) {
			InternetConnection ic=new InternetConnection(getApplicationContext());
			if(ic.isConnectingToInternet()){			
				if(brithday.getText().toString().length()==10)
				{
					String[] date=brithday.getText().toString().split("/");
					year=date[0];
					mon=date[1];
					day=date[2];
					insertHamyar();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "تاریخ وارد شده اشتباه است(1366/05/03)", Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "اتصال به شبکه را چک نمایید.", Toast.LENGTH_LONG).show();
			}
		}
	});	
		//get List Edication for spinner		
		db=dbh.getReadableDatabase();			
		Cursor cursors = db.rawQuery("SELECT * FROM education ",null);
		String str;
		for(int i=0;i<cursors.getCount();i++){
			cursors.moveToNext();
			str=cursors.getString(cursors.getColumnIndex("title"));
		    labels.add(str);
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spEducation.setAdapter(dataAdapter);
		
		//get the listview expandible
         //expListView = (ExpandableListView)findViewById(R.id.evExpertise);
 
        // preparing list data
        prepareListData();
 
        listAdapter = new com.besparina.it.hamyar.ExpandableListAdapter(this, listDataHeader, listDataChild);

 
        // setting list adapter
        exExpertise.setAdapter(listAdapter);
        exExpertise.setOnGroupClickListener(new OnGroupClickListener() {
        	 
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });
 
        // Listview Group expanded listener
        exExpertise.setOnGroupExpandListener(new OnGroupExpandListener() {
 
            @Override
            public void onGroupExpand(int groupPosition) {
               // Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " Expanded",
//                        Toast.LENGTH_SHORT).show();
            }
        });
 
        // Listview Group collasped listener
        exExpertise.setOnGroupCollapseListener(new OnGroupCollapseListener() {
 
            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " Collapsed",
//                        Toast.LENGTH_SHORT).show();
 
            }
        });
 
     // Listview on child click listener
        exExpertise.setOnChildClickListener(new OnChildClickListener() {
 
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
//                Toast.makeText(
//                        getApplicationContext(),
//                        listDataHeader.get(groupPosition)
//                                + " : "
//                                + listDataChild.get(
//                                        listDataHeader.get(groupPosition)).get(
//                                        childPosition), Toast.LENGTH_SHORT)
//                        .show();
                return false;
            }
        });
	}
public void insertHamyar() {
	db=dbh.getReadableDatabase();			
		Cursor coursors = db.rawQuery("SELECT * FROM exprtise ",null);
		for(int i=0;i<coursors.getCount();i++){
			coursors.moveToNext();
			exExpertiseString+=coursors.getString(coursors.getColumnIndex("code"));
			if((i+1)<coursors.getCount())
			{
				exExpertiseString+="##";
			}
		}
		String queryEducation="SELECT * FROM education WHERE title='"+((Spinner)spEducation).getSelectedItem().toString()+"'";
		coursors = db.rawQuery(queryEducation,null);
			coursors.moveToNext();
			education=coursors.getString(coursors.getColumnIndex("key"));
	InsertHamyar insertHamyar=new InsertHamyar(Info_Person.this,phonenumber, Acceptcode,fname.getText().toString(),lname.getText().toString(),education,exExpertiseString,year,mon,day);
	insertHamyar.AsyncExecute();
	
}	 
	/*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        db=dbh.getReadableDatabase();			
		Cursor headers = db.rawQuery("SELECT * FROM services ",null);
		//String head;
		for(int i=0;i<headers.getCount();i++){
			headers.moveToNext();
			//head=headers.getString(headers.getColumnIndex("servicename"));
			listDataHeader.add(headers.getString(headers.getColumnIndex("servicename")));
			//Cursor childs = db.rawQuery("SELECT * FROM servicesdetails WHERE servicename='"+listDataHeader.get(i)+"'",null);
			Cursor childs = db.rawQuery("SELECT * FROM servicesdetails WHERE code='"+headers.getString(headers.getColumnIndex("code"))+"'",null);
			//String child;
			 List<String> childDetails = new ArrayList<String>();
			for(int j=0;j<childs.getCount();j++){
				childs.moveToNext();
				//child=childs.getString(childs.getColumnIndex("name"));
				childDetails.add(childs.getString(childs.getColumnIndex("name")));			}
			listDataChild.put(listDataHeader.get(i), childDetails);
		}
        // Adding child data
//        listDataHeader.add("نظافت و خدمات منزل");
// Adding child data
//        List<String> nezafat = new ArrayList<String>();
//        nezafat.add("نظافت منزل و محل کار");
//        nezafat.add("نظافت مشاعات"); 
//listDataChild.put(listDataHeader.get(0), nezafat); // Header, Child data
//        listDataChild.put(listDataHeader.get(1), nowShowing);
//        listDataChild.put(listDataHeader.get(2), comingSoon);
    }
}
