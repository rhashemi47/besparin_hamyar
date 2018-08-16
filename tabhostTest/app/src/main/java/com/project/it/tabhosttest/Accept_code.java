package com.besparina.it.hamyar;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class Accept_code extends Activity {
	String phonenumber;
	EditText acceptcode;
	Button btnSendAcceptcode;
	Button btnRefreshAcceptcode;
	DatabaseHelper dbh;
	SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accept_code);
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
		Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");//set font for page
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remive page title
		acceptcode=(EditText)findViewById(R.id.etAcceptcode);
		btnSendAcceptcode=(Button)findViewById(R.id.btnSendAcceptCode);
		btnRefreshAcceptcode=(Button)findViewById(R.id.btnRefreshAcceptCode);
		//set font for element
		acceptcode.setTypeface(FontMitra);
		btnSendAcceptcode.setTypeface(FontMitra);        
		btnSendAcceptcode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				InternetConnection ic=new InternetConnection(getApplicationContext());
				if(ic.isConnectingToInternet()){
					if(checkInsert().compareTo("0")!=0)
					{
						phonenumber = getIntent().getStringExtra("phonenumber").toString();
						HmLogin hm=new HmLogin(Accept_code.this, phonenumber, acceptcode.getText().toString());
						hm.AsyncExecute();
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(), "اتصال به شبکه را چک نمایید.", Toast.LENGTH_LONG).show();
				}
			}
		});
		btnRefreshAcceptcode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String query=null;
				db=dbh.getReadableDatabase();
				query="SELECT * FROM Profile";
				Cursor coursors = db.rawQuery(query,null);
				if(coursors.getCount()>0)
				{
					coursors.moveToNext();
					String Mobile;
					Mobile=coursors.getString(coursors.getColumnIndex("Mobile"));
					SendAcceptCode sendCode=new SendAcceptCode(Accept_code.this,Mobile,0);
					sendCode.AsyncExecute();
				}

			}
		});
	}


	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event )  {
		if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
			LoadActivity(Login.class,"hamyarcode","0","guid","0");
		}
		return super.onKeyDown( keyCode, event );
	}
	public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);
		Accept_code.this.startActivity(intent);
	}
	public String checkInsert()
	{
		String Acceptcode = acceptcode.getText().toString();
		if(Acceptcode.compareTo("")==0) {
			Toast.makeText(getApplicationContext(), "لطفا کد تایید را وارد نمایید.", Toast.LENGTH_LONG).show();
			return  "0";
		}
		else
		{
			return Acceptcode;
		}
	}
}
