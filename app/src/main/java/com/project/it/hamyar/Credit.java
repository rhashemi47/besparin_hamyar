package com.project.it.hamyar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Credit extends Activity {
	private String hamyarcode;
	private String guid;
	private DatabaseHelper dbh;
	private TextView txtContent;
	private TextView tvRecentCreditsValue;
	private SQLiteDatabase db;
	private Button btnIncreseCredit;
	private ListView lstHistoryCredit;
	private Button btnCredit;
	private Button btnOrders;
	private Button btnHome;
	private EditText etCurrencyInsertCredit;
	private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.credits);
	btnCredit=(Button)findViewById(R.id.btnCredit);
	btnOrders=(Button)findViewById(R.id.btnOrders);
	btnHome=(Button)findViewById(R.id.btnHome);
	etCurrencyInsertCredit=(EditText)findViewById(R.id.etCurrencyInsertCredit);
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
		Cursor coursors = db.rawQuery("SELECT * FROM login",null);
		for(int i=0;i<coursors.getCount();i++){
			coursors.moveToNext();
			guid=coursors.getString(coursors.getColumnIndex("guid"));
			hamyarcode=coursors.getString(coursors.getColumnIndex("hamyarcode"));
		}
	}

	btnIncreseCredit=(Button)findViewById(R.id.btnIncresCredit);
	lstHistoryCredit=(ListView) findViewById(R.id.lstHistoryCredit);
	Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");//set font for page
	txtContent=(TextView)findViewById(R.id.tvHistoryCredits);
	txtContent.setTypeface(FontMitra);
	tvRecentCreditsValue=(TextView)findViewById(R.id.tvRecentCreditsValue);
	tvRecentCreditsValue.setTypeface(FontMitra);
	String Query="UPDATE UpdateApp SET Status='1'";
	db=dbh.getWritableDatabase();
	db.execSQL(Query);
	try
	{
		String Content="";
		Cursor coursors = db.rawQuery("SELECT * FROM credits ORDER BY Code DESC", null);
		if (coursors.getCount() > 0) {
			coursors.moveToNext();
			Content+=coursors.getString(coursors.getColumnIndex("Price"));
		}
		else
		{
			lstHistoryCredit.setVisibility(View.GONE);
		}
		if(Content.compareTo("")==0){
			tvRecentCreditsValue.setText("0"+" ریال");
		}
		else {
			tvRecentCreditsValue.setText(Content+" ریال");
		}
	}
	catch (Exception ex){
		tvRecentCreditsValue.setText("0"+" ریال");
		lstHistoryCredit.setVisibility(View.GONE);
	}
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
			txtContent.setVisibility(View.VISIBLE);
			txtContent.setText("موردی جهت نمایش وجود ندارد");
		}
		else
		{
			lstHistoryCredit.setVisibility(View.VISIBLE);
			txtContent.setVisibility(View.GONE);
		}
	}
	catch (Exception ex){
		lstHistoryCredit.setVisibility(View.GONE);
		txtContent.setVisibility(View.VISIBLE);
		tvRecentCreditsValue.setText("موردی جهت نمایش وجود ندارد");
	}
	btnIncreseCredit.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(etCurrencyInsertCredit.getText().length()>0) {
				SyncInsertHamyarCredit syncInsertHamyarCredit = new SyncInsertHamyarCredit(Credit.this,etCurrencyInsertCredit.getText().toString() , hamyarcode, "1", "10004", "تست");
				syncInsertHamyarCredit.AsyncExecute();
			}
			else
			{
				Toast.makeText(Credit.this, "لطفا مبلغ مورد نظر خود را به ریال وارد نمایید", Toast.LENGTH_SHORT).show();
			}
		}
	});
	btnCredit.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			LoadActivity(Credit.class, "guid",  guid, "hamyarcode", hamyarcode);
		}
	});
	btnOrders.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			LoadActivity(History.class, "guid", guid, "hamyarcode", hamyarcode);
		}
	});
	btnHome.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
		}
	});
}
@Override
public boolean onKeyDown( int keyCode, KeyEvent event )  {
    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
    	Credit.this.LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
    }

    return super.onKeyDown( keyCode, event );
}
public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);
		Credit.this.startActivity(intent);
	}
}
