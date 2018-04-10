package com.project.it.hamyar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Profile extends Activity {
	private String hamyarcode;
	private String guid;
	private TextView tvProfileRegentCode;
	private TextView tvUserCode;
	private TextView tvTitleUserCode;
	private TextView tvTitleName;
	private TextView tvUserName;
	private TextView tvTitleFName;
	private TextView tvUserFName;
	private TextView TextViewAge;
	private TextView tvTitleNumberPhone;
	private TextView tvNumberPhone;
	private TextView tvCodeMoaref;
	private EditText etBrithday;
	private EditText etReagentCodeProfile;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
//	private Button btnCredit;
//	private Button btnOrders;
//	private Button btnHome;
	private ImageView imgUser;
	private String yearStr="";
	private String monStr="";
	private String dayStr="";
	private int color;
	private Paint paint;
	private Rect rect;
	private RectF rectF;
	private Bitmap result;
	private Canvas canvas;
	private float roundPx;
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
		tvTitleUserCode=(TextView)findViewById(R.id.tvTitleUserCode);
		tvUserCode=(TextView)findViewById(R.id.tvUserCode);
		tvTitleName=(TextView)findViewById(R.id.tvTitleName);
		tvUserName=(TextView)findViewById(R.id.tvUserName);
		tvTitleFName=(TextView)findViewById(R.id.tvTitleFName);
		tvUserFName=(TextView)findViewById(R.id.tvUserFName);
		TextViewAge=(TextView)findViewById(R.id.TextViewAge);
		tvTitleNumberPhone=(TextView)findViewById(R.id.tvTitleNumberPhone);
		tvNumberPhone=(TextView)findViewById(R.id.tvNumberPhone);
		tvCodeMoaref=(TextView)findViewById(R.id.tvCodeMoaref);
		etBrithday=(EditText) findViewById(R.id.etBrithday);
		etReagentCodeProfile=(EditText)findViewById(R.id.etReagentCodeProfile);
		imgUser=(ImageView) findViewById(R.id.imgUser);
		//***************************************************************
//		btnCredit=(Button)findViewById(R.id.btnCredit);
//		btnOrders=(Button)findViewById(R.id.btnOrders);
//		btnHome=(Button)findViewById(R.id.btnHome);
		tvProfileRegentCode=(TextView)findViewById(R.id.tvCodeMoaref);
		Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");//set font for page
//		btnCredit.setTypeface(FontMitra);
//		btnOrders.setTypeface(FontMitra);
//		btnHome.setTypeface(FontMitra);
		//********************************************************
		tvProfileRegentCode.setTypeface(FontMitra);
		tvUserCode.setTypeface(FontMitra);
		tvTitleUserCode.setTypeface(FontMitra);
		tvTitleName.setTypeface(FontMitra);
		tvUserName.setTypeface(FontMitra);
		tvTitleFName.setTypeface(FontMitra);
		tvUserFName.setTypeface(FontMitra);
		TextViewAge.setTypeface(FontMitra);
		tvTitleNumberPhone.setTypeface(FontMitra);
		tvNumberPhone.setTypeface(FontMitra);
		tvCodeMoaref.setTypeface(FontMitra);
		etBrithday.setTypeface(FontMitra);
		etReagentCodeProfile.setTypeface(FontMitra);
		//********************************************************
		tvProfileRegentCode.setTextSize(18);
		tvUserCode.setTextSize(18);
		tvTitleUserCode.setTextSize(18);
		tvTitleName.setTextSize(18);
		tvUserName.setTextSize(18);
		tvTitleFName.setTextSize(18);
		tvUserFName.setTextSize(18);
		TextViewAge.setTextSize(18);
		tvTitleNumberPhone.setTextSize(18);
		tvNumberPhone.setTextSize(18);
		tvCodeMoaref.setTextSize(18);
		etBrithday.setTextSize(18);
		etReagentCodeProfile.setTextSize(18);
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

		Bitmap bmp= BitmapFactory.decodeResource(getResources(),R.drawable.useravatar);

		db=dbh.getReadableDatabase();
		Cursor coursors = db.rawQuery("SELECT * FROM Profile",null);
		if(coursors.getCount()>0){
			coursors.moveToNext();
			tvUserCode.setText(coursors.getString(coursors.getColumnIndex("Code")));
			tvUserName.setText(coursors.getString(coursors.getColumnIndex("Name")));
			tvUserFName.setText(coursors.getString(coursors.getColumnIndex("Fam")));
			etBrithday.setText(coursors.getString(coursors.getColumnIndex("BthDate")));
			tvNumberPhone.setText(coursors.getString(coursors.getColumnIndex("Mobile")));
			etReagentCodeProfile.setText(coursors.getString(coursors.getColumnIndex("HamyarCodeForReagent")));
			bmp=convertToBitmap(coursors.getString(coursors.getColumnIndex("Pic")));
		}

		db.close();

		imgUser.setImageBitmap(getRoundedRectBitmap(bmp,1000));

		etBrithday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				PersianCalendar now = new PersianCalendar();
				DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
								etBrithday.setText(String.valueOf(year)+"/"+String.valueOf(monthOfYear+1)+"/"+String.valueOf(dayOfMonth));
								yearStr=String.valueOf(year);
								monStr=String.valueOf(monthOfYear+1);
								dayStr=String.valueOf(dayOfMonth);
							}
						}, now.getPersianYear(),
						now.getPersianMonth(),
						now.getPersianDay());
				datePickerDialog.setThemeDark(true);
				datePickerDialog.show(getFragmentManager(), "tpd");

			}

		});
		etBrithday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
				{
					PersianCalendar now = new PersianCalendar();
					DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
							new DatePickerDialog.OnDateSetListener() {
								@Override
								public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
									etBrithday.setText(String.valueOf(year)+"/"+String.valueOf(monthOfYear+1)+"/"+String.valueOf(dayOfMonth));
									yearStr=String.valueOf(year);
									monStr=String.valueOf(monthOfYear+1);
									dayStr=String.valueOf(dayOfMonth);
								}
							}, now.getPersianYear(),
							now.getPersianMonth(),
							now.getPersianDay());
					datePickerDialog.setThemeDark(true);
					datePickerDialog.show(getFragmentManager(), "tpd");
				}
			}
		});
//		btnCredit.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				LoadActivity(Credit.class, "guid",  guid, "hamyarcode", hamyarcode);
//			}
//		});
//		btnOrders.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				LoadActivity(History.class, "guid", guid, "hamyarcode", hamyarcode);
//			}
//		});
//		btnHome.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
//			}
//		});
	}
	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event )  {
	    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
	    	Profile.this.LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
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
	public Bitmap convertToBitmap(String base){
		Bitmap Bmp=null;
		try
		{
			byte[] decodedByte = Base64.decode(base, Base64.DEFAULT);
			Bmp = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
//
			return Bmp;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return Bmp;
		}
	}
	void sharecode(String shareStr)
	{
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = "بسپارینا" + "\n"+"کد معرف: "+shareStr+"\n"+"آدرس سایت: " + PublicVariable.site;
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "عنوان");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "اشتراک گذاری با"));
	}
	public  Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels)
	{
		result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		canvas = new Canvas(result);

		color = 0xff424242;
		paint = new Paint();
		rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		rectF = new RectF(rect);
		roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return result;
	}
}

