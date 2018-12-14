package com.besparina.it.hamyar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.IOException;

import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Profile extends AppCompatActivity {
	private String hamyarcode;
	private String guid;
	private Drawer drawer=null;
	private String countMessage;
	private String countVisit;
	private boolean IsActive;
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
	private TextView tvStatuse;
	private EditText etBrithday;
	private EditText etReagentCodeProfile;
	private EditText etEmail;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private Button btnSendProfile;
	private String ReagentCode="0";
	private boolean CheckInputRegentCode=true;
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
		tvStatuse=(TextView)findViewById(R.id.tvStatuse);
		tvTitleNumberPhone=(TextView)findViewById(R.id.tvTitleNumberPhone);
		tvNumberPhone=(TextView)findViewById(R.id.tvNumberPhone);
		tvCodeMoaref=(TextView)findViewById(R.id.tvCodeMoaref);
		etBrithday=(EditText) findViewById(R.id.etBrithday);
		etEmail=(EditText) findViewById(R.id.etEmail);
		etReagentCodeProfile=(EditText)findViewById(R.id.etReagentCodeProfile);
		imgUser=(ImageView) findViewById(R.id.imgUser);
		btnSendProfile=(Button) findViewById(R.id.btnSendProfile);
		//***************************************************************
//		btnCredit=(TextView)findViewById(R.id.btnCredit);
//		btnOrders=(Button)findViewById(R.id.btnOrders);
//		btnHome=(Button)findViewById(R.id.btnHome);
		Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/IRANSans.ttf");//set font for page
//		btnCredit.setTypeface(FontMitra);
//		btnOrders.setTypeface(FontMitra);
//		btnHome.setTypeface(FontMitra);
		//********************************************************
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
		etEmail.setTypeface(FontMitra);
		//********************************************************
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
		etEmail.setTextSize(18);
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

			db.close();
		}
		//****************************************************************************************
		try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
		Cursor coursors = db.rawQuery("SELECT * FROM messages WHERE IsReade='0' AND IsDelete='0'",null);
		if(coursors.getCount()>0)
		{
			countMessage=String.valueOf(coursors.getCount());
		}
		coursors = db.rawQuery("SELECT * FROM BsHamyarSelectServices WHERE Status='5' AND IsDelete='0'",null);
		if(coursors.getCount()>0)
		{
			countVisit=String.valueOf(coursors.getCount());
		}
		IsActive=PublicVariable.IsActive;
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		CreateMenu(toolbar);
		//***************************************************************************************************************************

		Bitmap bmp= BitmapFactory.decodeResource(getResources(),R.drawable.useravatar);

		try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
		coursors = db.rawQuery("SELECT * FROM Profile",null);
		if(coursors.getCount()>0){
			coursors.moveToNext();
			tvUserCode.setText(coursors.getString(coursors.getColumnIndex("Code")));
			tvUserName.setText(coursors.getString(coursors.getColumnIndex("Name")));
			tvUserFName.setText(coursors.getString(coursors.getColumnIndex("Fam")));
			etBrithday.setText(coursors.getString(coursors.getColumnIndex("BthDate")));
			tvNumberPhone.setText(coursors.getString(coursors.getColumnIndex("Mobile")));
			tvStatuse.setText((coursors.getString(coursors.getColumnIndex("Status"))).compareTo("0")==0 ? "غیرفعال" : "فعال");
			etEmail.setText(coursors.getString(coursors.getColumnIndex("Email")));
			bmp=convertToBitmap(coursors.getString(coursors.getColumnIndex("Pic")));
			try{
				if (coursors.getString(coursors.getColumnIndex("ReagentName")).length() > 0) {
					etReagentCodeProfile.setText(coursors.getString(coursors.getColumnIndex("ReagentName")));
					tvCodeMoaref.setText("معرف");
					etReagentCodeProfile.setEnabled(false);
//					btnSendProfile.setVisibility(View.GONE);
					CheckInputRegentCode=false;
				}
			}
			catch (Exception e)
			{

			}
		}

		db.close();
		try
		{
			imgUser.setImageBitmap(getRoundedRectBitmap(bmp, 1000));
		}
		catch (Exception ex)
		{
			bmp = BitmapFactory.decodeResource(getResources(),R.drawable.useravatar);
			imgUser.setImageBitmap(getRoundedRectBitmap(bmp, 1000));
		}

		etBrithday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

//				PersianCalendar now = new PersianCalendar();
//				DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
//						new DatePickerDialog.OnDateSetListener() {
//							@Override
//							public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//								etBrithday.setText(String.valueOf(year)+"/"+String.valueOf(monthOfYear+1)+"/"+String.valueOf(dayOfMonth));
//								yearStr=String.valueOf(year);
//								monStr=String.valueOf(monthOfYear+1);
//								dayStr=String.valueOf(dayOfMonth);
//							}
//						}, now.getPersianYear(),
//						now.getPersianMonth(),
//						now.getPersianDay());
//				datePickerDialog.setThemeDark(true);
//				datePickerDialog.show(getFragmentManager(), "tpd");
				DatePicker();

			}

		});
		etBrithday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
				{
//					PersianCalendar now = new PersianCalendar();
//					DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
//							new DatePickerDialog.OnDateSetListener() {
//								@Override
//								public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//									etBrithday.setText(String.valueOf(year)+"/"+String.valueOf(monthOfYear+1)+"/"+String.valueOf(dayOfMonth));
//									yearStr=String.valueOf(year);
//									monStr=String.valueOf(monthOfYear+1);
//									dayStr=String.valueOf(dayOfMonth);
//								}
//							}, now.getPersianYear(),
//							now.getPersianMonth(),
//							now.getPersianDay());
//					datePickerDialog.setThemeDark(true);
//					datePickerDialog.show(getFragmentManager(), "tpd");
                    DatePicker();
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
//				LoadActivity(ListServiceAtTheTurn.class, "guid", guid, "hamyarcode", hamyarcode);
//			}
//		});
//		btnHome.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
//			}
//		});
		btnSendProfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InternetConnection ic=new InternetConnection(getApplicationContext());
				if(ic.isConnectingToInternet())
				{
					if(CheckInputRegentCode) {
						ReagentCode = etReagentCodeProfile.getText().toString();
						if (ReagentCode.length() > 0 && ReagentCode.length() <= 5) {
							Toast.makeText(getApplicationContext(), "کد معرف به درستی وارد نشده!", Toast.LENGTH_LONG).show();
						}
						else
						{
							SyncUpdateProfile syncUpdateProfile=new SyncUpdateProfile(Profile.this,guid,hamyarcode,PersianDigitConverter.EnglishNumber(ReagentCode),etEmail.getText().toString());
							syncUpdateProfile.AsyncExecute();
						}
					}
					else
					{
						SyncUpdateProfile syncUpdateProfile=new SyncUpdateProfile(Profile.this,guid,hamyarcode,"0",etEmail.getText().toString());
						syncUpdateProfile.AsyncExecute();
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(), "اتصال به شبکه را چک نمایید.", Toast.LENGTH_LONG).show();
				}
				db.close();
			}
		});
	}

    private void DatePicker() {
        PersianDatePickerDialog picker = new PersianDatePickerDialog(this);
        picker.setPositiveButtonString("تایید");
        picker.setNegativeButton("انصراف");
        picker.setTodayButton("امروز");
        picker.setTodayButtonVisible(true);
        //  picker.setInitDate(initDate);
        picker.setMaxYear(PersianDatePickerDialog.THIS_YEAR);
        picker.setMinYear(1300);
        picker.setActionTextColor(Color.GRAY);
        //picker.setTypeFace(FontMitra);
        picker.setListener(new Listener() {

            @Override
            public void onDateSelected(ir.hamsaa.persiandatepicker.util.PersianCalendar persianCalendar) {
                etBrithday.setText(persianCalendar.getPersianYear()+"/"+persianCalendar.getPersianMonth()+"/"+persianCalendar.getPersianDay());
                yearStr=String.valueOf(persianCalendar.getPersianYear());
                monStr=String.valueOf(persianCalendar.getPersianMonth());
                dayStr=String.valueOf(persianCalendar.getPersianDay());
            }

            @Override
            public void onDismissed() {

            }
        });
        picker.show();
    }

	private void CreateMenu(Toolbar toolbar){
		Bitmap bmp= BitmapFactory.decodeResource(getResources(),R.drawable.useravatar);
		String name="";
		String family="";
		String status="";
		db = dbh.getReadableDatabase();
		Cursor coursors = db.rawQuery("SELECT * FROM Profile", null);
		if (coursors.getCount() > 0) {
			coursors.moveToNext();
			try
			{
				if(coursors.getString(coursors.getColumnIndex("Status")).compareTo("null")!=0){
					status = coursors.getString(coursors.getColumnIndex("Status"));
					if(status.compareTo("0")==0)
					{
						status="غیرفعال";
					}
					else
					{
						status="فعال";
					}
				}
				else
				{
					status = "غیرفعال";
				}

			}
			catch (Exception ex){
				status = "غیرفعال";
			}
			try
			{
				if(coursors.getString(coursors.getColumnIndex("Name")).compareTo("null")!=0){
					name = coursors.getString(coursors.getColumnIndex("Name"));
				}
				else
				{
					name = "کاربر";
				}

			}
			catch (Exception ex){
				name = "کاربر";
			}
			try
			{
				if(coursors.getString(coursors.getColumnIndex("Fam")).compareTo("null")!=0){
					family = coursors.getString(coursors.getColumnIndex("Fam"));
				}
				else
				{
					family = "مهمان";
				}

			}
			catch (Exception ex){
				family = "مهمان";
			}
			try
			{
				if(coursors.getString(coursors.getColumnIndex("Pic")).compareTo("null")!=0){
					bmp = convertToBitmap(coursors.getString(coursors.getColumnIndex("Pic")));
				}
				else
				{
					bmp = BitmapFactory.decodeResource(getResources(), R.drawable.useravatar);
				}

			}
			catch (Exception ex){
				bmp = BitmapFactory.decodeResource(getResources(), R.drawable.useravatar);
			}
		}
		else
		{
			name = "کاربر";
			family = "مهمان";
		}

		int drawerGravity= Gravity.END;
		Configuration config = getResources().getConfiguration();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                drawerGravity= Gravity.START;
            }
		}


		String HeaderStr=name+" "+family+" - "+"وضعیت: "+status;
		// Create the AccountHeader
		AccountHeader headerResult = new AccountHeaderBuilder()
				.withActivity(this)
				.withHeaderBackground(R.drawable.menu_header)
				.addProfiles(
						new ProfileDrawerItem().withName(HeaderStr).withIcon(bmp)// withIcon(getResources().getDrawable(R.drawable.personpic))
				).withSelectionListEnabledForSingleProfile(false)
				.withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
					@Override
					public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
						return false;
					}
				})
				.build();
		drawer= new DrawerBuilder()
				.withActivity(this)
				.withToolbar(toolbar)
				.withAccountHeader(headerResult)
				.withDrawerGravity(drawerGravity)
				.withShowDrawerOnFirstLaunch(true)
				.addDrawerItems(
						new SecondaryDrawerItem().withName(R.string.Profile).withIcon(R.drawable.profile).withSelectable(true),
						new SecondaryDrawerItem().withName(R.string.ListVisit).withIcon(R.drawable.job).withBadge(countVisit).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withSelectable(false).withEnabled(IsActive),
						// new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
						new SecondaryDrawerItem().withName(R.string.Yourcommitment).withIcon(R.drawable.yourcommitment).withSelectable(false),
						new SecondaryDrawerItem().withName(R.string.Ourcommitment).withIcon(R.drawable.ourcommitment).withSelectable(false),
						//new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
						new SecondaryDrawerItem().withName(R.string.Messages).withIcon(R.drawable.messages).withBadge(countMessage).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withSelectable(false).withEnabled(IsActive),
						new SecondaryDrawerItem().withName(R.string.GiftBank).withIcon(R.drawable.gift).withSelectable(false).withEnabled(IsActive),
						new SecondaryDrawerItem().withName(R.string.Invite_friends).withIcon(R.drawable.about).withSelectable(false).withEnabled(IsActive),
						new SecondaryDrawerItem().withName(R.string.Contact).withIcon(R.drawable.contact).withSelectable(false),
						//new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
						new SecondaryDrawerItem().withName(R.string.action_settings).withIcon(R.drawable.setting).withSelectable(false).withEnabled(IsActive),
						new SecondaryDrawerItem().withName(R.string.Help).withIcon(R.drawable.help).withSelectable(false),
						new SecondaryDrawerItem().withName(R.string.About).withIcon(R.drawable.about).withSelectable(false),                        new SecondaryDrawerItem().withName(R.string.History).withIcon(R.drawable.history).withSelectable(false),
						//new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
						//new SecondaryDrawerItem().withName(R.string.Exit).withIcon(R.drawable.exit).withSelectable(false),
						new SecondaryDrawerItem().withName(R.string.Logout).withIcon(R.drawable.logout).withSelectable(false)
				)//.addStickyDrawerItems(new PrimaryDrawerItem().withName(R.string.RelateUs).withSelectable(false).withEnabled(false),se),
						//new PrimaryDrawerItem().withName(R.string.telegram).withIcon(R.drawable.telegram).withSelectable(false),
						//new PrimaryDrawerItem().withName(R.string.instagram).withIcon(R.drawable.instagram).withSelectable(false))
				.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
					@Override
					public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
						switch (position){
							case 1://Profile
								try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
								Cursor coursors = db.rawQuery("SELECT * FROM Profile",null);
								if(coursors.getCount()>0)
								{
									coursors.moveToNext();
									String Status_check=coursors.getString(coursors.getColumnIndex("Status"));
									if(Status_check.compareTo("0")==0)
									{
										Cursor c = db.rawQuery("SELECT * FROM login",null);
										if(c.getCount()>0)
										{
											c.moveToNext();
											SyncProfile profile = new SyncProfile(Profile.this, c.getString(c.getColumnIndex("guid")), c.getString(c.getColumnIndex("hamyarcode")));
											profile.AsyncExecute();
										}
									}
									else
									{
										LoadActivity(Profile.class, "guid", guid,"hamyarcode",hamyarcode);
									}
								}
								else
								{
									Toast.makeText(Profile.this, "برای استفاده از امکانات بسپارینا باید ثبت نام کنید", Toast.LENGTH_LONG).show();
									LoadActivity(Login.class,"guid",guid,"hamyarcode",hamyarcode);
								}

								db.close();
								break;
							case 2:
								db = dbh.getReadableDatabase();
								Cursor c = db.rawQuery("SELECT * FROM login",null);
								if(c.getCount()>0) {
									c.moveToNext();
									LoadActivity(List_Visits.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
								}
								db.close();
								break;
							case 3:
//                                db = dbh.getReadableDatabase();
//                                c = db.rawQuery("SELECT * FROM login",null);
//                                if(c.getCount()>0) {
//                                    c.moveToNext();
//
//                                    LoadActivity(YourCommitment.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
//                                }
//                                db.close();
								openWebPage("http://besparina.ir/?page_id=178");
								break;
								case 4:
							openWebPage("http://besparina.ir/?page_id=164");
								break;
							case 5:
								db = dbh.getReadableDatabase();
								c = db.rawQuery("SELECT * FROM login",null);
								if(c.getCount()>0) {
									c.moveToNext();

									LoadActivity(List_Messages.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
								}
								db.close();
								break;
							case 6:
								db = dbh.getReadableDatabase();
								c = db.rawQuery("SELECT * FROM login",null);
								if(c.getCount()>0) {
									c.moveToNext();

									LoadActivity(GiftBank.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
								}
								db.close();
								break;
							case 7:
								db = dbh.getReadableDatabase();
								c = db.rawQuery("SELECT * FROM Profile", null);
								if (c.getCount() > 0) {
									c.moveToNext();
									sharecode(c.getString(c.getColumnIndex("HamyarCodeForReagent")));
									// LoadActivity(GiftBank.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
								}
								db.close();
								break;
							case 8:
								db = dbh.getReadableDatabase();
								c = db.rawQuery("SELECT * FROM login",null);
								if(c.getCount()>0) {
									c.moveToNext();
									LoadActivity(Contact.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
								}
								break;
							case 9:
//                                Toast.makeText(MainMenu.this, "تنظیمات", Toast.LENGTH_SHORT).show();
//                                AlertDialog.Builder alertbox = new AlertDialog.Builder(MainMenu.this);
//                                // set the message to display
//                                alertbox.setMessage("تنظیمات پیش فاکتور");
//
//                                // set a negative/no button and create a listener
//                                alertbox.setPositiveButton("مراحل کاری", new DialogInterface.OnClickListener() {
//                                    // do something when the button is clicked
//                                    public void onClick(DialogInterface arg0, int arg1) {
								try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
								c = db.rawQuery("SELECT * FROM login",null);
								if(c.getCount()>0)
								{
									c.moveToNext();
									SyncGetHmFactorService getHmFactorService=new SyncGetHmFactorService(Profile.this,guid,hamyarcode);
									getHmFactorService.AsyncExecute();
									SyncGetHmFactorTools syncGetHmFactorTools=new SyncGetHmFactorTools(Profile.this,guid,hamyarcode);
									syncGetHmFactorTools.AsyncExecute();
									LoadActivity(Setting.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
								}
								db.close();
//                                        arg0.dismiss();
//                                    }
//                                });

//                                // set a positive/yes button and create a listener
//                                alertbox.setNegativeButton("ملزومات کاری", new DialogInterface.OnClickListener() {
//                                    // do something when the button is clicked
//                                    public void onClick(DialogInterface arg0, int arg1) {
//                                        //Declare Object From Get Internet Connection Status For Check Internet Status
//                                        try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
//                                        Cursor  c = db.rawQuery("SELECT * FROM login",null);
//                                        if(c.getCount()>0) {
//                                            c.moveToNext();
//                                            SyncGetHmFactorTools syncGetHmFactorTools=new SyncGetHmFactorTools(MainMenu.this,guid,hamyarcode);
//                                            syncGetHmFactorTools.AsyncExecute();
//                                            LoadActivity(StepJobDetaile.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
//                                        }
//
//                                        db.close();
//                                        arg0.dismiss();
//
//                                    }
//                                });
//
//                                alertbox.show();
								break;
							case 10:
								openWebPage("http://besparina.ir/?page_id=377&preview=true");
								break;
							case 11:
								openWebPage("http://besparina.ir/?page_id=194");
								break;
							case 14:
								Toast.makeText(Profile.this, "تلگرام", Toast.LENGTH_SHORT).show();
								break;
							case 15:
								Toast.makeText(Profile.this, "اینستاگرام", Toast.LENGTH_SHORT).show();
								break;
						}
						return true;
					}
				})
				.build();
//        drawer.closeDrawer();
	}
	public void openWebPage(String url) {
		Uri webpage = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
		if (intent.resolveActivity(getPackageManager()) != null) {
			startActivity(intent);
		}
	}
	public void Logout() {
		//Exit All Activity And Kill Application
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		// set the message to display
		alertbox.setMessage("آیا می خواهید از کاربری خارج شوید ؟");

		// set a negative/no button and create a listener
		alertbox.setPositiveButton("خیر", new DialogInterface.OnClickListener() {
			// do something when the button is clicked
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		});

		// set a positive/yes button and create a listener
		alertbox.setNegativeButton("بله", new DialogInterface.OnClickListener() {
			// do something when the button is clicked
			public void onClick(DialogInterface arg0, int arg1) {
				//Declare Object From Get Internet Connection Status For Check Internet Status
				stopService(new Intent(getBaseContext(), ServiceGetLocation.class));
				stopService(new Intent(getBaseContext(), ServiceGetNewJob.class));
				stopService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));
				stopService(new Intent(getBaseContext(), ServiceGetSliderPic.class));
				stopService(new Intent(getBaseContext(), ServiceSyncProfile.class));
				stopService(new Intent(getBaseContext(), ServiceSyncServiceSelected.class));
				try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
				db.execSQL("DELETE FROM AmountCredit");
				db.execSQL("DELETE FROM android_metadata");
				db.execSQL("DELETE FROM BsHamyarSelectServices");
				db.execSQL("DELETE FROM BsUserServices");
				db.execSQL("DELETE FROM credits");
				db.execSQL("DELETE FROM DateTB");
				db.execSQL("DELETE FROM education");
				db.execSQL("DELETE FROM exprtise");
				db.execSQL("DELETE FROM FaktorUserDetailes");
				db.execSQL("DELETE FROM HeadFactor");
				db.execSQL("DELETE FROM HmFactorService");
				db.execSQL("DELETE FROM HmFactorTools");
				db.execSQL("DELETE FROM HmFactorTools_List");
				db.execSQL("DELETE FROM InsertFaktorUserDetailes");
				db.execSQL("DELETE FROM login");
				db.execSQL("DELETE FROM messages");
				db.execSQL("DELETE FROM Profile");
				db.execSQL("DELETE FROM services");
				db.execSQL("DELETE FROM servicesdetails");
				db.execSQL("DELETE FROM Slider");
				db.execSQL("DELETE FROM sqlite_sequence");
				db.execSQL("DELETE FROM Supportphone");
				db.execSQL("DELETE FROM Unit");
				db.execSQL("DELETE FROM UpdateApp");
				db.close();
				Intent startMain = new Intent(Intent.ACTION_MAIN);


				startMain.addCategory(Intent.CATEGORY_HOME);

//                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

				startActivity(startMain);

				finish();
				arg0.dismiss();

			}
		});

		alertbox.show();
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
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

