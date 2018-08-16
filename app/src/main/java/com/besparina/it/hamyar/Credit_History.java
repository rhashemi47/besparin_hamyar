package com.besparina.it.hamyar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Credit_History extends Activity {

	private String hamyarcode;
	private String guid;
	final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
	private DatabaseHelper dbh;
	private TextView txtContent;
	private SQLiteDatabase db;
	private ListView lstHistoryCredit;
	private TextView btnCredit;
	private Button btnDutyToday;
	private Button btnServices_at_the_turn;
	private Button btnHome;
	private TextView tvRecentCreditsValue;
	private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.credits_history);
	Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");//set font for page
	btnCredit=(TextView)findViewById(R.id.btnCredit);
	btnServices_at_the_turn=(Button)findViewById(R.id.btnServices_at_the_turn);
	btnDutyToday=(Button)findViewById(R.id.btnDutyToday);
	btnHome=(Button)findViewById(R.id.btnHome);
	tvRecentCreditsValue=(TextView)findViewById(R.id.tvRecentCreditsValue);
	tvRecentCreditsValue.setTypeface(FontMitra);
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
		hamyarcode = getIntent().getStringExtra("hamyarcode").toString();
		guid = getIntent().getStringExtra("guid").toString();
	}
	catch (Exception e)
	{
		db=dbh.getReadableDatabase();
		Cursor coursors = db.rawQuery("SELECT * FROM login",null);
		for(int i=0;i<coursors.getCount();i++){
			coursors.moveToNext();
			guid=coursors.getString(coursors.getColumnIndex("guid"));
			hamyarcode=coursors.getString(coursors.getColumnIndex("hamyarcode"));
		}
		db.close();
	}

	//****************************************************************************************
	TextView tvAmountCredit=(TextView) findViewById(R.id.tvAmountCredit);
	db=dbh.getReadableDatabase();
	Cursor cursor = db.rawQuery("SELECT * FROM AmountCredit", null);
	if (cursor.getCount() > 0) {
		cursor.moveToNext();
		String splitStr[] = cursor.getString(cursor.getColumnIndex("Amount")).toString().split("\\.");
		if(splitStr.length>=2)
		{
			if (splitStr[1].compareTo("00") == 0) {
				tvAmountCredit.setText(PersianDigitConverter.PerisanNumber(splitStr[0]));
			} else
			{
				tvAmountCredit.setText(PersianDigitConverter.PerisanNumber(cursor.getString(cursor.getColumnIndex("Amount"))));
			}
		}
		else
		{
			tvAmountCredit.setText(PersianDigitConverter.PerisanNumber(cursor.getString(cursor.getColumnIndex("Amount"))));
		}
	}
	//****************************************************************************************
	lstHistoryCredit=(ListView) findViewById(R.id.lstHistoryCredit);
	txtContent=(TextView)findViewById(R.id.tvHistoryCredits);
	txtContent.setTypeface(FontMitra);

	try
	{
		 Cursor coursors = db.rawQuery("SELECT * FROM credits", null);
		String Content="";
		for (int i=0;i<coursors.getCount();i++) {
			coursors.moveToNext();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name","مبلغ: " +coursors.getString(coursors.getColumnIndex("Price"))+" ریال " +"\n"
					+"عملیات: " + coursors.getString(coursors.getColumnIndex("TransactionType"))+ "\n"
					+"نوع تراکنش: " + coursors.getString(coursors.getColumnIndex("PaymentMethod"))+ "\n"
					+"تاریخ: " + coursors.getString(coursors.getColumnIndex("TransactionDate"))+ "\n"
					+"شماره سند: " + coursors.getString(coursors.getColumnIndex("DocNumber"))+ "\n"
					+"توضیحات: " + coursors.getString(coursors.getColumnIndex("Description")));
			map.put("Code",coursors.getString(coursors.getColumnIndex("Code")));
			valuse.add(map);
		}
		AdapterCredit dataAdapter=new AdapterCredit(this,valuse,guid,hamyarcode);
		lstHistoryCredit.setAdapter(dataAdapter);
		if(valuse.size()==0){
			lstHistoryCredit.setVisibility(View.GONE);
//			txtContent.setVisibility(View.VISIBLE);
//			txtContent.setText("موردی جهت نمایش وجود ندارد");
		}
		else
		{
			lstHistoryCredit.setVisibility(View.VISIBLE);
//			txtContent.setVisibility(View.GONE);
		}
	}
	catch (Exception ex){
		lstHistoryCredit.setVisibility(View.GONE);
//		txtContent.setVisibility(View.VISIBLE);
		tvRecentCreditsValue.setText("موردی جهت نمایش وجود ندارد");
	}


	btnCredit.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			LoadActivity2(Credit.class, "guid",  guid, "hamyarcode", hamyarcode);
		}
	});
	btnDutyToday.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			LoadActivity2(List_Dutys.class, "guid", guid, "hamyarcode", hamyarcode);
		}
	});
	btnServices_at_the_turn.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			LoadActivity2(History.class, "guid", guid, "hamyarcode", hamyarcode);
		}
	});
	btnHome.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			LoadActivity2(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
		}
	});
}
@Override
public boolean onKeyDown( int keyCode, KeyEvent event )  {
    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
    	LoadActivity2(Credit.class, "guid", guid,"hamyarcode", hamyarcode);
    }

    return super.onKeyDown( keyCode, event );
}
	public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

		Credit_History.this.startActivity(intent);
	}
	public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue
			, String VariableName2, String VariableValue2) {
		Intent intent = new Intent(getApplicationContext(), Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);

		this.startActivity(intent);
	}
	public void dialContactPhone(String phoneNumber) {
		//startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:" + phoneNumber));
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.CALL_PHONE},REQUEST_CODE_ASK_PERMISSIONS);
			return;
		}
		startActivity(callIntent);
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case REQUEST_CODE_ASK_PERMISSIONS:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// Permission Granted
					db = dbh.getReadableDatabase();
					Cursor cursorPhone = db.rawQuery("SELECT * FROM Supportphone", null);
					if (cursorPhone.getCount() > 0) {
						cursorPhone.moveToNext();
						dialContactPhone(cursorPhone.getString(cursorPhone.getColumnIndex("PhoneNumber")));
					}
					db.close();
				} else {
					// Permission Denied
					Toast.makeText(this, "مجوز تماس از طریق برنامه لغو شده برای بر قراری تماس از درون برنامه باید مجوز دسترسی تماس را فعال نمایید.", Toast.LENGTH_LONG)
							.show();
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
}
