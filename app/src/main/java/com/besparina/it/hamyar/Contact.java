package com.besparina.it.hamyar;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Base64;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Contact extends Activity {
	private String hamyarcode;
	private String guid;
	private Drawer drawer=null;
	private String countMessage;
	private String countVisit;
	private boolean IsActive;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private EditText etSendMessage;
	private TextView btnCredit;
	private Button btnDutyToday;
	private Button btnServices_at_the_turn;
	private Button btnHome;
	private Button btnSendMessage;
	private Button btnCallSupporter;
	private int GET_MY_PERMISSION = 2;
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);
		btnCredit=(TextView)findViewById(R.id.btnCredit);
		etSendMessage=(EditText)findViewById(R.id.etSendMessage);
		btnServices_at_the_turn=(Button)findViewById(R.id.btnServices_at_the_turn);
		btnDutyToday=(Button)findViewById(R.id.btnDutyToday);
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
			db.close();
		}
		//****************************************************************************************
		/*TextView tvAmountCredit=(TextView) findViewById(R.id.tvAmountCredit);
		try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
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
		}*/
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
		btnSendMessage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				db = dbh.getReadableDatabase();
				Cursor cursorPhone = db.rawQuery("SELECT * FROM Supportphone", null);
				if (cursorPhone.getCount() > 0) {
					cursorPhone.moveToNext();
					String MessageStr="کد کاربر: "+hamyarcode+"\n"+etSendMessage.getText().toString();
//					SendMessage(MessageStr, cursorPhone.getString(cursorPhone.getColumnIndex("PhoneNumber")));
					SendMessage(MessageStr,"09385064073");
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
		btnDutyToday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				LoadActivity(List_Dutys.class, "guid", guid, "hamyarcode", hamyarcode);
			}
		});
		btnServices_at_the_turn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoadActivity(ListServiceAtTheTurn.class, "guid", guid, "hamyarcode", hamyarcode);
			}
		});
		btnHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
			}
		});
	}
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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
		if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
			drawerGravity= Gravity.START;
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
						new SecondaryDrawerItem().withName(R.string.Invite_friends).withIcon(R.drawable.contact).withSelectable(false).withEnabled(IsActive),
						new SecondaryDrawerItem().withName(R.string.Contact).withIcon(R.drawable.contact).withSelectable(false),
						//new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
						new SecondaryDrawerItem().withName(R.string.action_settings).withIcon(R.drawable.setting).withSelectable(false).withEnabled(IsActive),
						new SecondaryDrawerItem().withName(R.string.Help).withIcon(R.drawable.help).withSelectable(false),
						new SecondaryDrawerItem().withName(R.string.Contact).withIcon(R.drawable.contact).withSelectable(false),
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
											SyncProfile profile = new SyncProfile(Contact.this, c.getString(c.getColumnIndex("guid")), c.getString(c.getColumnIndex("hamyarcode")));
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
									Toast.makeText(Contact.this, "برای استفاده از امکانات بسپارینا باید ثبت نام کنید", Toast.LENGTH_LONG).show();
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
									SyncGetHmFactorService getHmFactorService=new SyncGetHmFactorService(Contact.this,guid,hamyarcode);
									getHmFactorService.AsyncExecute();
									SyncGetHmFactorTools syncGetHmFactorTools=new SyncGetHmFactorTools(Contact.this,guid,hamyarcode);
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
//                            case 12:
////                                Toast.makeText(Contact.this, "خروج از برنامه", Toast.LENGTH_SHORT).show();
//                                ExitApplication();
//                                break;
							case 12:
//                                Toast.makeText(Contact.this, "خروج از کاربری", Toast.LENGTH_SHORT).show();
								Logout();
								break;
							case 14:
								Toast.makeText(Contact.this, "تلگرام", Toast.LENGTH_SHORT).show();
								break;
							case 15:
								Toast.makeText(Contact.this, "اینستاگرام", Toast.LENGTH_SHORT).show();
								break;
						}
						return true;
					}
				})
				.build();
//        drawer.closeDrawer();
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
	    	Contact.this.LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
	    }

	    return super.onKeyDown( keyCode, event );
	}
	public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
		{
			Intent intent = new Intent(getApplicationContext(),Cls);
			intent.putExtra(VariableName, VariableValue);
			intent.putExtra(VariableName2, VariableValue2);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
	public void SendMessage(String message ,String phoneNumber) {
		if (ActivityCompat.checkSelfPermission(Contact.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
//			Toast.makeText(Contact.this,"اجازه دسترسی به ارسال پیام داده نشده است",Toast.LENGTH_LONG).show();
			ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.SEND_SMS},GET_MY_PERMISSION);
			return;
		} else {
			SmsManager smsManager = SmsManager.getDefault();
			String SENT = "SMS_SENT";
			String DELIVERED = "SMS_DELIVERED";

			SmsManager sms = SmsManager.getDefault();
			ArrayList<String> parts = sms.divideMessage(message);
			int messageCount = parts.size();

			ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();
			ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();

			PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
			PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
			for (int j = 0; j < messageCount; j++) {
				sentIntents.add(sentPI);
				deliveryIntents.add(deliveredPI);
			}

			// ---when the SMS has been sent---
			registerReceiver(new BroadcastReceiver() {
				@Override
				public void onReceive(Context arg0, Intent arg1) {
					switch (getResultCode()) {
						case Activity.RESULT_OK:

							Toast.makeText(getBaseContext(), "پیام ارسال شد",
									Toast.LENGTH_SHORT).show();
							break;
						case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
							Toast.makeText(getBaseContext(), "ارسال پیام با خطا مواجه شد",
									Toast.LENGTH_SHORT).show();
							break;
						case SmsManager.RESULT_ERROR_NO_SERVICE:
							Toast.makeText(getBaseContext(), "سرویس ارسال پیامک در دسترس نیست",
									Toast.LENGTH_SHORT).show();
							break;
						case SmsManager.RESULT_ERROR_NULL_PDU:
							Toast.makeText(getBaseContext(), "خظایی رخ داده است",
									Toast.LENGTH_SHORT).show();
							break;
						case SmsManager.RESULT_ERROR_RADIO_OFF:
							Toast.makeText(getBaseContext(), "آنتن ضعیف است",
									Toast.LENGTH_SHORT).show();
							break;
					}
				}
			}, new IntentFilter(SENT));

			// ---when the SMS has been delivered---
			registerReceiver(new BroadcastReceiver() {
				@Override
				public void onReceive(Context arg0, Intent arg1) {
					switch (getResultCode()) {

						case Activity.RESULT_OK:
							Toast.makeText(getBaseContext(), "پیام تحویل شد",
									Toast.LENGTH_SHORT).show();
							break;
						case Activity.RESULT_CANCELED:
							Toast.makeText(getBaseContext(), "پیام تحویل نشد",
									Toast.LENGTH_SHORT).show();
							break;
					}
				}
			}, new IntentFilter(DELIVERED));
			smsManager.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
           /* sms.sendMultipartTextMessage(phoneNumber, null, parts, sentIntents, deliveryIntents); */
		}
	}
}
