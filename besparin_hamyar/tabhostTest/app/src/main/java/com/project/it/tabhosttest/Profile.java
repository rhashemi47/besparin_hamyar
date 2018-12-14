package com.besparina.it.hamyar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import java.io.IOException;

public class Profile extends Activity {
	private String hamyarcode;
	private String guid;
	private TextView Content;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        Content=(TextView)findViewById(R.id.tvProfileContent);
		Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");//set font for page
		Content.setTypeface(FontMitra);
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
			try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
			Cursor coursors = db.rawQuery("SELECT * FROM login",null);
			for(int i=0;i<coursors.getCount();i++){
				coursors.moveToNext();
				guid=coursors.getString(coursors.getColumnIndex("guid"));
				hamyarcode=coursors.getString(coursors.getColumnIndex("hamyarcode"));
			}
		}

		try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
		Cursor coursors = db.rawQuery("SELECT * FROM Profile",null);
		for(int i=0;i<coursors.getCount();i++){
			coursors.moveToNext();
			String textP="نام: "+coursors.getString(coursors.getColumnIndex("Name"))+"\n"+
					"نام خانوادگی: "+coursors.getString(coursors.getColumnIndex("Fam"))+"\n"+
					"تاریخ تولد: "+coursors.getString(coursors.getColumnIndex("BthDate"))+"\n"+
					"شماره شناسنامه: "+coursors.getString(coursors.getColumnIndex("ShSh"))+"\n"+
					"محل تولد: "+coursors.getString(coursors.getColumnIndex("BirthplaceCode"))+"\n"+
					"صادره: "+coursors.getString(coursors.getColumnIndex("Sader"))+"\n"+
					"تاریخ شروع به کار: "+coursors.getString(coursors.getColumnIndex("StartDate"))+"\n"+
					"آدرس: "+coursors.getString(coursors.getColumnIndex("Address"))+"\n"+
					"شماره تلفن: "+coursors.getString(coursors.getColumnIndex("Tel"))+"\n"+
					"شماره همراه: "+coursors.getString(coursors.getColumnIndex("Mobile"))+"\n"+
					"وضعیت: "+coursors.getString(coursors.getColumnIndex("Status"));
			Content.setText(textP);
		}
	}
	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event )  {
	    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
	    	Profile.this.LoadActivity(MainActivity.class, "guid", guid, "hamyarcode", hamyarcode);
	    }

	    return super.onKeyDown( keyCode, event );
	}
	public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
		{
			Intent intent = new Intent(getApplicationContext(),Cls);
			intent.putExtra(VariableName, VariableValue);
			intent.putExtra(VariableName2, VariableValue2);
			Profile.this.startActivity(intent);
		}
}

