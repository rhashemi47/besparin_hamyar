package com.project.it.hamyar;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
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

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

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
	private EditText etReagentCode;
	private String yearStr="";
	private String monStr="";
	private String dayStr="";
	private Spinner spEducation;
	private	DatabaseHelper dbh;
	private SQLiteDatabase db;
	private ExpandableListView exExpertise;
	private ExpandableListAdapter listAdapter;
    //ExpandableListView expListView;
	private List<String> listDataHeader;
	private HashMap<String, List<String>> listDataChild;
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
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
		}		
   		try
        {
   			Acceptcode = getIntent().getStringExtra("acceptcode").toString();
        }
        catch (Exception e) {
		}			
		fname=(EditText)findViewById(R.id.etFname);
		lname=(EditText)findViewById(R.id.etLname);
		etReagentCode=(EditText)findViewById(R.id.etReagentCode);
		brithday=(EditText)findViewById(R.id.etBrithday);
		spEducation=(Spinner)findViewById(R.id.spEdudation);
		exExpertise=(ExpandableListView)findViewById(R.id.evExpertise);
		btnSendInfo=(Button)findViewById(R.id.btnSendInfo);
		btnSendInfo.setOnClickListener(new OnClickListener() {
		public void onClick(View arg0) {
			InternetConnection ic=new InternetConnection(getApplicationContext());
			if(ic.isConnectingToInternet())
			{
				String ReagentCode="";
				ReagentCode=etReagentCode.getText().toString().trim();
				ReagentCode=ReagentCode.replace(" ","");
				if(ReagentCode.length()>0 && ReagentCode.length()<=5)
				{
					Toast.makeText(getApplicationContext(), "کد معرف به درستی وارد نشده!", Toast.LENGTH_LONG).show();
				}
				else
				{
					insertHamyar();
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
		db.close();
		spEducation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				education=spEducation.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
        prepareListData();
 
        listAdapter = new CustomeExpandableListAdapter(this, listDataHeader, listDataChild);

 
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
        brithday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

						PersianCalendar now = new PersianCalendar();
						DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
								new DatePickerDialog.OnDateSetListener() {
									@Override
									public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
										brithday.setText(String.valueOf(year)+"/"+String.valueOf(monthOfYear+1)+"/"+String.valueOf(dayOfMonth));
										yearStr=String.valueOf(year);
										monStr=String.valueOf(monthOfYear);
										dayStr=String.valueOf(dayOfMonth);
									}
								}, now.getPersianYear(),
								now.getPersianMonth(),
								now.getPersianDay());
						datePickerDialog.setThemeDark(true);
						datePickerDialog.show(getFragmentManager(), "tpd");

					}

		});
        brithday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				PersianCalendar now = new PersianCalendar();
				DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
								brithday.setText(String.valueOf(year)+"/"+String.valueOf(monthOfYear+1)+"/"+String.valueOf(dayOfMonth));
								yearStr=String.valueOf(year);
								monStr=String.valueOf(monthOfYear);
								dayStr=String.valueOf(dayOfMonth);
							}
						}, now.getPersianYear(),
						now.getPersianMonth(),
						now.getPersianDay());
				datePickerDialog.setThemeDark(true);
				datePickerDialog.show(getFragmentManager(), "tpd");
			}
		});
	}
public void insertHamyar() {
	db=dbh.getReadableDatabase();
	String errorStr="";
	if(fname.getText().toString().compareTo("")==0){
		errorStr="لطفا نام خود راوارد نمایید\n";
	}
	if(lname.getText().toString().compareTo("")==0){
		errorStr="لطفا نام خانوادگی خود راوارد نمایید\n";
	}
	if(education.compareTo("")==0){
		errorStr="لطفا سطح تحصیلات خود راوارد نمایید\n";
	}
	if(yearStr.compareTo("")==0 || monStr.compareTo("")==0 || dayStr.compareTo("")==0){
		errorStr="لطفا تاریخ تولد را وارد نمایید\n";
	}
	if(errorStr.compareTo("")==0) {
		Cursor coursors = db.rawQuery("SELECT * FROM exprtise ", null);
		if (coursors.getCount() > 0) {
			for (int i = 0; i < coursors.getCount(); i++) {
				coursors.moveToNext();
				exExpertiseString += coursors.getString(coursors.getColumnIndex("code"));
				if ((i + 1) < coursors.getCount()) {
					exExpertiseString += "##";
				}
			}
			String queryEducation = "SELECT * FROM education WHERE title='" + ((Spinner) spEducation).getSelectedItem().toString() + "'";
			coursors = db.rawQuery(queryEducation, null);
			coursors.moveToNext();
			education = coursors.getString(coursors.getColumnIndex("key"));
			String ReagentCode=etReagentCode.getText().toString();
			ReagentCode=ReagentCode.trim();
			if(ReagentCode.compareTo("")==0)
			{
				ReagentCode="0";
			}
			InsertHamyar insertHamyar = new InsertHamyar(Info_Person.this, phonenumber, Acceptcode, fname.getText().toString(), lname.getText().toString(), education, exExpertiseString, yearStr, monStr, dayStr,ReagentCode);
			insertHamyar.AsyncExecute();
		}
		else
		{
			Toast.makeText(getApplicationContext(), "لطفا تخصص خود را اعلام انتخاب فرمایید", Toast.LENGTH_SHORT).show();
		}
		db.close();
	}
	else
	{
		Toast.makeText(getApplicationContext(), errorStr, Toast.LENGTH_SHORT).show();
	}
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
			listDataHeader.add(headers.getString(headers.getColumnIndex("servicename")));
			Cursor childs = db.rawQuery("SELECT * FROM servicesdetails WHERE code='"+headers.getString(headers.getColumnIndex("code"))+"'",null);
			//String child;
			 List<String> childDetails = new ArrayList<String>();
			for(int j=0;j<childs.getCount();j++)
			{
				childs.moveToNext();
				childDetails.add(childs.getString(childs.getColumnIndex("name")));
			}

			listDataChild.put(listDataHeader.get(i), childDetails);
		}
		db.close();
    }
}
