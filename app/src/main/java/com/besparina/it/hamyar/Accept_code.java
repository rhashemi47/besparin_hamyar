package com.besparina.it.hamyar;


import android.app.Activity;
import android.app.MediaRouteButton;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Accept_code extends Activity {
	private String phonenumber;
	private EditText acceptcode;
	private Button btnSendAcceptcode;
	private Button btnRefreshAcceptcode;
	private String check_load;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private TextView tvTimer;
	private IntentFilter intentFilter;
	private Handler mHandler;
	private boolean continue_or_stop = true;
	private boolean createthread=true;
	private int counter=59;
	private BroadcastReceiver intentReciever=new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			acceptcode.setText(intent.getExtras().getString("sms"));
		}
	};

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accept_code);
		try
		{
			check_load=getIntent().getStringExtra("check_load");
		}
		catch (Exception ex)
		{
			check_load="0";
		}
		intentFilter=new IntentFilter();
		intentFilter.addAction("SMS_RECEIVED_ACTION");
		int GET_MY_PERMISSION = 1;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//For Read SMS In Android 8 and Last
				if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
					if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.RECEIVE_SMS)) {
						//do nothing
					} else {

						ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECEIVE_SMS}, GET_MY_PERMISSION);
					}
				}
			} else {
				if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
					if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_SMS)) {
						//do nothing
					} else {

						ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_SMS}, GET_MY_PERMISSION);
					}
				}
			}
		}
		registerReceiver(intentReciever,intentFilter);
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
		Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/IRANSans.ttf");//set font for page
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remive page title
		acceptcode=(EditText)findViewById(R.id.etAcceptcode);
		btnSendAcceptcode=(Button)findViewById(R.id.btnSendAcceptCode);
		btnRefreshAcceptcode=(Button)findViewById(R.id.btnRefreshAcceptCode);
		tvTimer=(TextView) findViewById(R.id.tvTimer);
		//set font for element
		acceptcode.setTypeface(FontMitra);
		btnSendAcceptcode.setTypeface(FontMitra);
		btnRefreshAcceptcode.setTypeface(FontMitra);
		startCountAnimation();
		btnSendAcceptcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				InternetConnection ic=new InternetConnection(getApplicationContext());
				if(ic.isConnectingToInternet()){
					if(checkInsert().compareTo("0")!=0)//کنترل وارد کردن کد تاییدیه در تکست باکس
					{
						Send_AcceptCode();
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
				try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
				query="SELECT * FROM Profile";
				Cursor coursors = db.rawQuery(query,null);
				if(coursors.getCount()>0)
				{
					coursors.moveToNext();
					String Mobile;
					Mobile=coursors.getString(coursors.getColumnIndex("Mobile"));
					acceptcode.setText("");
					SendAcceptCode sendCode=new SendAcceptCode(Accept_code.this,Mobile,"0");
					sendCode.AsyncExecute();
				}

				try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}

			}
		});
		SMSReseiver.bindListener(new SmsListener() {
			@Override
			public void onMessageReceived(String messageText) {
				Log.e("Text",messageText);
				acceptcode.setText("");
				acceptcode.setText(messageText);
				Send_AcceptCode();
			}
		});
	}

	@Override
	public  void onResume() {

		super.onResume();
		registerReceiver(intentReciever,intentFilter);
	}
	@Override
	public void onPause() {

		super.onPause();
		unregisterReceiver(intentReciever);
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
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
	public void Send_AcceptCode()
	{
		phonenumber = getIntent().getStringExtra("phonenumber").toString();
		String query="UPDATE login SET Phone ='"+phonenumber+"', AcceptCode='"+acceptcode.getText().toString()+"'";
		try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
		db.execSQL(query);
		HmLogin hm=new HmLogin(Accept_code.this, phonenumber, acceptcode.getText().toString(),check_load);
		hm.AsyncExecute();

		try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
	}
	public void startCountAnimation() {
		continue_or_stop=true;
		if(createthread) {
			mHandler = new Handler();
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (continue_or_stop) {
						try {
							Thread.sleep(1000); // every 60 seconds
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									if(counter!=0)
									{
										counter-=1;
										tvTimer.setText(String.valueOf(counter)+ " ثانیه");
										btnRefreshAcceptcode.setVisibility(View.GONE);
									}
									else
									{
										continue_or_stop = false;
										btnRefreshAcceptcode.setVisibility(View.VISIBLE);
									}
								}
							});
						} catch (Exception e) {
						}
					}
				}
			}).start();

			createthread = false;

		}
	}
}
