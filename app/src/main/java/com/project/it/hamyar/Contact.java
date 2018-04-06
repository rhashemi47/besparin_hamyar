package com.project.it.hamyar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Contact extends Activity {
	private String hamyarcode;
	private String guid;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private Button btnCredit;
	private Button btnOrders;
	private Button btnHome;
	private Button btnSendMessage;
	private Button btnCallSupporter;
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);
		btnCredit=(Button)findViewById(R.id.btnCredit);
		btnOrders=(Button)findViewById(R.id.btnOrders);
		btnHome=(Button)findViewById(R.id.btnHome);
		btnSendMessage=(Button)findViewById(R.id.btnSendMessage);
		btnCallSupporter=(Button)findViewById(R.id.btnCallSupporter);
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
		btnCallSupporter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ActivityCompat.checkSelfPermission(Contact.this,
						android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
					if(ActivityCompat.shouldShowRequestPermissionRationale(Contact.this, android.Manifest.permission.CALL_PHONE))
					{

					}
					else
					{
						ActivityCompat.requestPermissions(Contact.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
					}

				}
				db = dbh.getReadableDatabase();
				Cursor cursorPhone = db.rawQuery("SELECT * FROM Supportphone", null);
				if (cursorPhone.getCount() > 0) {
					cursorPhone.moveToNext();
					dialContactPhone(cursorPhone.getString(cursorPhone.getColumnIndex("PhoneNumber")));
				}
				db.close();
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
	    	Contact.this.LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
	    }

	    return super.onKeyDown( keyCode, event );
	}
	public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
		{
			Intent intent = new Intent(getApplicationContext(),Cls);
			intent.putExtra(VariableName, VariableValue);
			intent.putExtra(VariableName2, VariableValue2);
			Contact.this.startActivity(intent);
		}
	public void dialContactPhone(String phoneNumber) {
		//startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:" + phoneNumber));
		if (ActivityCompat.checkSelfPermission(Contact.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		this.startActivity(callIntent);
	}
}
