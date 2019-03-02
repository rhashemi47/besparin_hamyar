package com.besparina.it.hamyar;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.besparina.it.hamyar.Date.ChangeDate;
import com.besparina.it.hamyar.Date.ShamsiCalendar;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainMenu extends AppCompatActivity {
    private String hamyarcode;
    private String guid;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private Drawer drawer = null;
    private String countMessage;
    private String countVisit;
    private Button btnDuty;
    private Button btnServices;
    private TextView btnCredit;
    private Button btnDutyToday;
    private Button btnServices_at_the_turn;
    private Button btnHome;
    private boolean doubleBackToExitPressedOnce = false;
    private boolean IsActive;
    private ListView ListDutyNow;
    private LinearLayout LinearTextServiceNow;
    ArrayList<String> slides;
    ImageView imageView;
    Custom_ViewFlipper viewFlipper;
    GestureDetector mGestureDetector;
    private String AppVersion;
    private ArrayList<HashMap<String, String>> valuse = new ArrayList<HashMap<String, String>>();
    private Handler mHandler;
    private boolean continue_or_stop = true;
//    private JobScheduler jobScheduler_SchaduleServiceGetNewJob = null;
//    private JobScheduler jobScheduler_SchaduleServiceGetFactorAccept = null;
//    private JobScheduler jobScheduler_SchaduleServiceGetLocation = null;
//    private JobScheduler jobScheduler_SchaduleServiceGetSliderPic = null;
//    private JobScheduler jobScheduler_SchaduleServiceSyncProfile = null;
//    private JobScheduler jobScheduler_SchaduleServiceSyncServiceSelected = null;
//    private JobScheduler jobScheduler_SchaduleServiceGetJobUpdate = null;
//    private JobScheduler jobScheduler_SchaduleServiceDeleteJob = null;
//    private JobScheduler jobScheduler_SchaduleServiceGetUserServiceStartDate = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/IRANSans.ttf");//set font for page
        btnDuty = (Button) findViewById(R.id.btnDuty);
        btnServices = (Button) findViewById(R.id.btnServices);
        ListDutyNow = (ListView) findViewById(R.id.ListDutyNow);
        LinearTextServiceNow = (LinearLayout) findViewById(R.id.LinearTextServiceNow);
        //****************************************************************
        btnDuty.setTypeface(FontMitra);
        btnServices.setTypeface(FontMitra);
        //****************************************************************
        btnDuty.setTextSize(18);
        btnServices.setTextSize(18);
        //****************************************************************
        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        if (version.length() > 0) {
            AppVersion = version;
            WsDownLoadUpdate wsDownLoadUpdate = new WsDownLoadUpdate(MainMenu.this, AppVersion, PublicVariable.LinkFileTextCheckVersion, PublicVariable.DownloadAppUpdateLinkAPK);
            wsDownLoadUpdate.AsyncExecute();
        }


        btnCredit = (TextView) findViewById(R.id.btnCredit);
        btnServices_at_the_turn = (Button) findViewById(R.id.btnServices_at_the_turn);
        btnDutyToday = (Button) findViewById(R.id.btnDutyToday);
        btnHome = (Button) findViewById(R.id.btnHome);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(MainMenu.this));
        dbh = new DatabaseHelper(getApplicationContext());
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
        //*******************************************First Stop Services***************************************
        this.stopService(new Intent(this, ServiceDeleteJob.class));
        this.stopService(new Intent(this, ServiceGetFactorAccept.class));
        this.stopService(new Intent(this, ServiceGetJobUpdate.class));
        this.stopService(new Intent(this, ServiceGetLocation.class));
        this.stopService(new Intent(this, ServiceGetNewJob.class));
        this.stopService(new Intent(this, ServiceGetSliderPic.class));
        this.stopService(new Intent(this, ServiceGetUserServiceStartDate.class));
        this.stopService(new Intent(this, ServiceSyncProfile.class));
        this.stopService(new Intent(this, ServiceSyncServiceSelected.class));
        //**************************************************Config Active Service******************************
//        try {
//            if (!db.isOpen()) {
//                db = dbh.getReadableDatabase();
//            }
//        } catch (Exception ex) {
//            db = dbh.getReadableDatabase();
//        }
//        Cursor coursors = db.rawQuery("SELECT * FROM ActiceBackgroundService", null);
//        if (coursors.getCount() > 0) {
//            coursors.moveToNext();
//            if (coursors.getString(coursors.getColumnIndex("Service_DeleteJob")).compareTo("1") == 0) {
//                PublicVariable.Active_Service_DeleteJob = true;
//            }
//            else {
//                PublicVariable.Active_Service_DeleteJob = false;
//            }
//            if (coursors.getString(coursors.getColumnIndex("Service_GetFactorAccept")).compareTo("1") == 0) {
//                PublicVariable.Active_Service_GetFactorAccept = true;
//            }
//            else {
//                PublicVariable.Active_Service_GetFactorAccept = false;
//            }
//            if (coursors.getString(coursors.getColumnIndex("Service_GetJobUpdate")).compareTo("1") == 0) {
//                PublicVariable.Active_Service_GetJobUpdate = true;
//            }
//            else {
//                PublicVariable.Active_Service_GetJobUpdate = false;
//            }
//            if (coursors.getString(coursors.getColumnIndex("Service_GetLocation")).compareTo("1") == 0) {
//                PublicVariable.Active_Service_GetLocation = true;
//            }
//            else {
//                PublicVariable.Active_Service_GetLocation = false;
//            }
//            if (coursors.getString(coursors.getColumnIndex("Service_GetNewJob")).compareTo("1") == 0) {
//                PublicVariable.Active_Service_GetNewJob = true;
//            }
//            else {
//                PublicVariable.Active_Service_GetNewJob = false;
//            }
//            if (coursors.getString(coursors.getColumnIndex("Service_GetSliderPic")).compareTo("1") == 0) {
//                PublicVariable.Active_Service_GetSliderPic = true;
//            }
//            else {
//                PublicVariable.Active_Service_GetSliderPic = false;
//            }
//            if (coursors.getString(coursors.getColumnIndex("Service_GetUserServiceStartDate")).compareTo("1") == 0) {
//                PublicVariable.Active_Service_GetUserServiceStartDate = true;
//            }
//            else {
//                PublicVariable.Active_Service_GetUserServiceStartDate = false;
//            }
//            if (coursors.getString(coursors.getColumnIndex("Service_Profile")).compareTo("1") == 0) {
//                PublicVariable.Active_Service_Profile = true;
//            }
//            else {
//                PublicVariable.Active_Service_Profile = false;
//            }
//            if (coursors.getString(coursors.getColumnIndex("Service_ServiceSelected")).compareTo("1") == 0) {
//                PublicVariable.Active_Service_ServiceSelected = true;
//            }
//            else {
//                PublicVariable.Active_Service_ServiceSelected = false;
//            }
//        }
//        if (db.isOpen()) {
//            db.close();
//        }
        //****************************************************ShowCountServices********************************
        count_service();
        mHandler = new Handler();
        continue_or_stop = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (continue_or_stop) {
                    try {
                        Thread.sleep(5000); // every 5 seconds
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                count_service();
                            }
                        });
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        }).start();
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
        }
        db.close();*/


        //******************************************* COUNT SERVICES *****************************************
//        try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
//        Cursor cursorService = db.rawQuery("SELECT BsUserServices.*,Servicesdetails.name FROM BsUserServices " +
//                "LEFT JOIN " +
//                "Servicesdetails ON " +
//                "Servicesdetails.code=BsUserServices.ServiceDetaileCode WHERE 1=1 ",null);
//        if(cursorService.getCount()>0)
//        {
//            btnServices.setText(String.valueOf(cursorService.getCount()));
//        }
//        else
//        {
//            btnServices.setText("0");
//        }

        try {
            if (!db.isOpen()) {
                db = dbh.getReadableDatabase();
            }
        } catch (Exception ex) {
            db = dbh.getReadableDatabase();
        }
       Cursor coursors = db.rawQuery("SELECT * FROM messages WHERE IsReade='0' AND IsDelete='0'", null);
        if (coursors.getCount() > 0) {
            countMessage = String.valueOf(coursors.getCount());
        }
        coursors = db.rawQuery("SELECT * FROM BsHamyarSelectServices WHERE Status='5' AND IsDelete='0'", null);
        if (coursors.getCount() > 0) {
            countVisit = String.valueOf(coursors.getCount());
        }
        try {
            String status = "0";
            db = dbh.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM Profile", null);
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                try {
                    if (cursor.getString(cursor.getColumnIndex("Status")).compareTo("null") != 0) {
                        status = cursor.getString(cursor.getColumnIndex("Status"));
                        if (status.compareTo("0") == 0) {
                            status = "غیرفعال";
                            PublicVariable.IsActive = false;
                            IsActive = false;
                        } else {
                            status = "فعال";
                            PublicVariable.IsActive = true;
                            IsActive = true;
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                //*****************************************SchaduleServiceGetNewJob******************************************
//                                ComponentName serviceComponent_SchaduleServiceGetNewJob = new ComponentName(getApplicationContext(), SchaduleServiceGetNewJob.class);
//                                JobInfo.Builder builder_SchaduleServiceGetNewJob = null;
//                                builder_SchaduleServiceGetNewJob = new JobInfo.Builder(0, serviceComponent_SchaduleServiceGetNewJob);
//                                builder_SchaduleServiceGetNewJob.setMinimumLatency(5 * 1000); // wait at least
//                                builder_SchaduleServiceGetNewJob.setOverrideDeadline(50 * 1000); // maximum delay
//                                builder_SchaduleServiceGetNewJob.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//                                builder_SchaduleServiceGetNewJob.setRequiresDeviceIdle(false); // device should be idle
//                                builder_SchaduleServiceGetNewJob.setRequiresCharging(false); // we don't care if the device is charging or not
//                                JobScheduler jobScheduler_SchaduleServiceGetNewJob = null;
//                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                                    jobScheduler_SchaduleServiceGetNewJob = getApplicationContext().getSystemService(JobScheduler.class);
//                                }
//                                jobScheduler_SchaduleServiceGetNewJob.schedule(builder_SchaduleServiceGetNewJob.build());
//                                //*****************************************SchaduleServiceGetFactorAccept******************************************
//                                ComponentName serviceComponent_SchaduleServiceGetFactorAccept = new ComponentName(getApplicationContext(), SchaduleServiceGetFactorAccept.class);
//                                JobInfo.Builder builder_SchaduleServiceGetFactorAccept = null;
//                                builder_SchaduleServiceGetFactorAccept = new JobInfo.Builder(1, serviceComponent_SchaduleServiceGetFactorAccept);
//                                builder_SchaduleServiceGetFactorAccept.setMinimumLatency(5 * 1000); // wait at least
//                                builder_SchaduleServiceGetFactorAccept.setOverrideDeadline(50 * 1000); // maximum delay
//                                builder_SchaduleServiceGetFactorAccept.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//                                builder_SchaduleServiceGetFactorAccept.setRequiresDeviceIdle(false); // device should be idle
//                                builder_SchaduleServiceGetFactorAccept.setRequiresCharging(false); // we don't care if the device is charging or not
//                                JobScheduler jobScheduler_SchaduleServiceGetFactorAccept = null;
//                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                                    jobScheduler_SchaduleServiceGetFactorAccept = getApplicationContext().getSystemService(JobScheduler.class);
//                                }
//                                jobScheduler_SchaduleServiceGetFactorAccept.schedule(builder_SchaduleServiceGetFactorAccept.build());
//
//                                //*****************************************SchaduleServiceGetLocation************************************************
//                                ComponentName serviceComponent_SchaduleServiceGetLocation = new ComponentName(getApplicationContext(), SchaduleServiceGetLocation.class);
//                                JobInfo.Builder builder_SchaduleServiceGetLocation = null;
//                                builder_SchaduleServiceGetLocation = new JobInfo.Builder(2, serviceComponent_SchaduleServiceGetLocation);
//                                builder_SchaduleServiceGetLocation.setMinimumLatency(5 * 1000); // wait at least
//                                builder_SchaduleServiceGetLocation.setOverrideDeadline(50 * 1000); // maximum delay
//                                builder_SchaduleServiceGetLocation.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//                                builder_SchaduleServiceGetLocation.setRequiresDeviceIdle(false); // device should be idle
//                                builder_SchaduleServiceGetLocation.setRequiresCharging(false); // we don't care if the device is charging or not
//                                JobScheduler jobScheduler_SchaduleServiceGetLocation = null;
//                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                                    jobScheduler_SchaduleServiceGetLocation = getApplicationContext().getSystemService(JobScheduler.class);
//                                }
//                                jobScheduler_SchaduleServiceGetLocation.schedule(builder_SchaduleServiceGetLocation.build());
//
//                                //*****************************************SchaduleServiceGetSliderPic******************************************
//                                ComponentName serviceComponent_SchaduleServiceGetSliderPic = new ComponentName(getApplicationContext(), SchaduleServiceGetSliderPic.class);
//                                JobInfo.Builder builder_SchaduleServiceGetSliderPic = null;
//                                builder_SchaduleServiceGetSliderPic = new JobInfo.Builder(4, serviceComponent_SchaduleServiceGetSliderPic);
//                                builder_SchaduleServiceGetSliderPic.setMinimumLatency(5 * 1000); // wait at least
//                                builder_SchaduleServiceGetSliderPic.setOverrideDeadline(50 * 1000); // maximum delay
//                                builder_SchaduleServiceGetSliderPic.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//                                builder_SchaduleServiceGetSliderPic.setRequiresDeviceIdle(false); // device should be idle
//                                builder_SchaduleServiceGetSliderPic.setRequiresCharging(false); // we don't care if the device is charging or not
//                                JobScheduler jobScheduler_SchaduleServiceGetSliderPic = null;
//                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                                    jobScheduler_SchaduleServiceGetSliderPic = getApplicationContext().getSystemService(JobScheduler.class);
//                                }
//                                jobScheduler_SchaduleServiceGetSliderPic.schedule(builder_SchaduleServiceGetSliderPic.build());
//
//                                //*****************************************SchaduleServiceSyncProfile******************************************
//                                ComponentName serviceComponent_SchaduleServiceSyncProfile = new ComponentName(getApplicationContext(), SchaduleServiceSyncProfile.class);
//                                JobInfo.Builder builder_SchaduleServiceSyncProfile = null;
//                                builder_SchaduleServiceSyncProfile = new JobInfo.Builder(5, serviceComponent_SchaduleServiceSyncProfile);
//                                builder_SchaduleServiceSyncProfile.setMinimumLatency(5 * 1000); // wait at least
//                                builder_SchaduleServiceSyncProfile.setOverrideDeadline(50 * 1000); // maximum delay
//                                builder_SchaduleServiceSyncProfile.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//                                builder_SchaduleServiceSyncProfile.setRequiresDeviceIdle(false); // device should be idle
//                                builder_SchaduleServiceSyncProfile.setRequiresCharging(false); // we don't care if the device is charging or not
//                                JobScheduler jobScheduler_SchaduleServiceSyncProfile = null;
//                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                                    jobScheduler_SchaduleServiceSyncProfile = getApplicationContext().getSystemService(JobScheduler.class);
//                                }
//                                jobScheduler_SchaduleServiceSyncProfile.schedule(builder_SchaduleServiceSyncProfile.build());
//
//                                //*****************************************SchaduleServiceSyncServiceSelected******************************************
//                                ComponentName serviceComponent_SchaduleServiceSyncServiceSelected = new ComponentName(getApplicationContext(), SchaduleServiceSyncServiceSelected.class);
//                                JobInfo.Builder builder_SchaduleServiceSyncServiceSelected = null;
//                                builder_SchaduleServiceSyncServiceSelected = new JobInfo.Builder(6, serviceComponent_SchaduleServiceSyncServiceSelected);
//                                builder_SchaduleServiceSyncServiceSelected.setMinimumLatency(5 * 1000); // wait at least
//                                builder_SchaduleServiceSyncServiceSelected.setOverrideDeadline(50 * 1000); // maximum delay
//                                builder_SchaduleServiceSyncServiceSelected.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//                                builder_SchaduleServiceSyncServiceSelected.setRequiresDeviceIdle(false); // device should be idle
//                                builder_SchaduleServiceSyncServiceSelected.setRequiresCharging(false); // we don't care if the device is charging or not
//                                JobScheduler jobScheduler_SchaduleServiceSyncServiceSelected = null;
//                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                                    jobScheduler_SchaduleServiceSyncServiceSelected = getApplicationContext().getSystemService(JobScheduler.class);
//                                }
//                                jobScheduler_SchaduleServiceSyncServiceSelected.schedule(builder_SchaduleServiceSyncServiceSelected.build());
//
//                                //*****************************************SchaduleServiceGetJobUpdate******************************************
//                                ComponentName serviceComponent_SchaduleServiceGetJobUpdate = new ComponentName(getApplicationContext(), SchaduleServiceGetJobUpdate.class);
//                                JobInfo.Builder builder_SchaduleServiceGetJobUpdate = null;
//                                builder_SchaduleServiceGetJobUpdate = new JobInfo.Builder(7, serviceComponent_SchaduleServiceGetJobUpdate);
//                                builder_SchaduleServiceGetJobUpdate.setMinimumLatency(5 * 1000); // wait at least
//                                builder_SchaduleServiceGetJobUpdate.setOverrideDeadline(50 * 1000); // maximum delay
//                                builder_SchaduleServiceGetJobUpdate.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//                                builder_SchaduleServiceGetJobUpdate.setRequiresDeviceIdle(false); // device should be idle
//                                builder_SchaduleServiceGetJobUpdate.setRequiresCharging(false); // we don't care if the device is charging or not
//                                JobScheduler jobScheduler_SchaduleServiceGetJobUpdate = null;
//                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                                    jobScheduler_SchaduleServiceGetJobUpdate = getApplicationContext().getSystemService(JobScheduler.class);
//                                }
//                                jobScheduler_SchaduleServiceGetJobUpdate.schedule(builder_SchaduleServiceGetJobUpdate.build());
//
//                                //*****************************************SchaduleServiceDeleteJob******************************************
//                                ComponentName serviceComponent_SchaduleServiceDeleteJob = new ComponentName(getApplicationContext(), SchaduleServiceDeleteJob.class);
//                                JobInfo.Builder builder_SchaduleServiceDeleteJob = null;
//                                builder_SchaduleServiceDeleteJob = new JobInfo.Builder(7, serviceComponent_SchaduleServiceDeleteJob);
//                                builder_SchaduleServiceDeleteJob.setMinimumLatency(5 * 1000); // wait at least
//                                builder_SchaduleServiceDeleteJob.setOverrideDeadline(50 * 1000); // maximum delay
//                                builder_SchaduleServiceDeleteJob.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//                                builder_SchaduleServiceDeleteJob.setRequiresDeviceIdle(false); // device should be idle
//                                builder_SchaduleServiceDeleteJob.setRequiresCharging(false); // we don't care if the device is charging or not
//                                JobScheduler jobScheduler_SchaduleServiceDeleteJob = null;
//                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                                    jobScheduler_SchaduleServiceDeleteJob = getApplicationContext().getSystemService(JobScheduler.class);
//                                }
//                                jobScheduler_SchaduleServiceDeleteJob.schedule(builder_SchaduleServiceDeleteJob.build());
//
//                                //*****************************************SchaduleServiceGetUserServiceStartDate******************************************
//                                ComponentName serviceComponent_SchaduleServiceGetUserServiceStartDate = new ComponentName(getApplicationContext(), SchaduleServiceGetUserServiceStartDate.class);
//                                JobInfo.Builder builder_SchaduleServiceGetUserServiceStartDate = null;
//                                builder_SchaduleServiceGetUserServiceStartDate = new JobInfo.Builder(7, serviceComponent_SchaduleServiceGetUserServiceStartDate);
//                                builder_SchaduleServiceGetUserServiceStartDate.setMinimumLatency(5 * 1000); // wait at least
//                                builder_SchaduleServiceGetUserServiceStartDate.setOverrideDeadline(50 * 1000); // maximum delay
//                                builder_SchaduleServiceGetUserServiceStartDate.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//                                builder_SchaduleServiceGetUserServiceStartDate.setRequiresDeviceIdle(false); // device should be idle
//                                builder_SchaduleServiceGetUserServiceStartDate.setRequiresCharging(false); // we don't care if the device is charging or not
//                                JobScheduler jobScheduler_SchaduleServiceGetUserServiceStartDate = null;
//                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                                    jobScheduler_SchaduleServiceGetUserServiceStartDate = getApplicationContext().getSystemService(JobScheduler.class);
//                                }
//                                jobScheduler_SchaduleServiceGetUserServiceStartDate.schedule(builder_SchaduleServiceGetUserServiceStartDate.build());
//
//                                if (PublicVariable.Active_Service_DeleteJob) {
//                                    this.stopService(new Intent(this, ServiceDeleteJob.class));
//                                    this.startForegroundService(new Intent(this, ServiceDeleteJob.class));
//                                }
//                                if (PublicVariable.Active_Service_GetFactorAccept) {
//                                    this.stopService(new Intent(this, ServiceGetFactorAccept.class));
//                                    this.startForegroundService(new Intent(this, ServiceGetFactorAccept.class));
//                                }
//                                if (PublicVariable.Active_Service_GetJobUpdate) {
//                                    this.stopService(new Intent(this, ServiceGetJobUpdate.class));
//                                    this.startForegroundService(new Intent(this, ServiceGetJobUpdate.class));
//                                }
//                                if (PublicVariable.Active_Service_GetLocation) {
//                                    this.stopService(new Intent(this, ServiceGetLocation.class));
//                                    this.startForegroundService(new Intent(this, ServiceGetLocation.class));
//                                }
//                                if (PublicVariable.Active_Service_GetNewJob) {
//                                    this.stopService(new Intent(this, ServiceGetNewJob.class));
//                                    this.startForegroundService(new Intent(this, ServiceGetNewJob.class));
//                                }
//                                if (PublicVariable.Active_Service_GetSliderPic) {
//                                    this.stopService(new Intent(this, ServiceGetSliderPic.class));
//                                    this.startForegroundService(new Intent(this, ServiceGetSliderPic.class));
//                                }
//                                if (PublicVariable.Active_Service_GetUserServiceStartDate) {
//                                    this.stopService(new Intent(this, ServiceGetUserServiceStartDate.class));
//                                    this.startForegroundService(new Intent(this, ServiceGetUserServiceStartDate.class));
//                                }
//                                if (PublicVariable.Active_Service_Profile) {
//                                    this.stopService(new Intent(this, ServiceSyncProfile.class));
//                                    this.startForegroundService(new Intent(this, ServiceSyncProfile.class));
//                                }
//                                if (PublicVariable.Active_Service_ServiceSelected) {
//                                    this.stopService(new Intent(this, ServiceSyncServiceSelected.class));
//                                    this.startForegroundService(new Intent(this, ServiceSyncServiceSelected.class));
//                                }
//                            } else {
                                startService(new Intent(getBaseContext(), ServiceGetNewJob.class));
                                startService(new Intent(getBaseContext(), ServiceGetFactorAccept.class));
                                startService(new Intent(getBaseContext(), ServiceSyncProfile.class));
                                startService(new Intent(getBaseContext(), ServiceGetLocation.class));
                                startService(new Intent(getBaseContext(), ServiceGetSliderPic.class));
                                startService(new Intent(getBaseContext(), ServiceSyncServiceSelected.class));
                                startService(new Intent(getBaseContext(), ServiceGetJobUpdate.class));
                                startService(new Intent(getBaseContext(), ServiceDeleteJob.class));
                                startService(new Intent(getBaseContext(), ServiceGetUserServiceStartDate.class));
//                            }
                        }
                    } else {
                        status = "غیرفعال";
                        PublicVariable.IsActive = false;
                        IsActive = false;
                    }

                } catch (Exception ex) {
                    status = "غیرفعال";
                    PublicVariable.IsActive = false;
                    IsActive = false;
                }
            }
            hamyarcode = getIntent().getStringExtra("hamyarcode");
            guid = getIntent().getStringExtra("guid");
            Check_Login(hamyarcode, guid, status);
        } catch (Exception e) {
            throw new Error("Error Opne Activity");
        }
        //****************************************************************************************
        if (guid == null || hamyarcode == null) {
            try {
                if (!db.isOpen()) {
                    db = dbh.getReadableDatabase();
                }
            } catch (Exception ex) {
                db = dbh.getReadableDatabase();
            }
            Cursor c = db.rawQuery("SELECT * FROM login", null);
            if (c.getCount() > 0) {
                c.moveToNext();
                guid = c.getString(c.getColumnIndex("guid"));
                hamyarcode = c.getString(c.getColumnIndex("hamyarcode"));
            }
            if (db.isOpen()) {
                db.close();
            }
            c.close();
        }
//        ir.hamsaa.persiandatepicker.util.PersianCalendar calNow=new ir.hamsaa.persiandatepicker.util.PersianCalendar();
//        try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
//        String year,mon,day,query;
//        year=String.valueOf(calNow.getPersianYear());
//        if(calNow.getPersianMonth()<10)
//        {
//            mon="0"+String.valueOf(calNow.getPersianMonth());
//        }
//        else
//        {
//            mon=String.valueOf(calNow.getPersianMonth());
//        }
//        if(calNow.getPersianDay()<10)
//        {
//            day="0"+String.valueOf(calNow.getPersianDay());
//        }
//        else
//        {
//            day=String.valueOf(calNow.getPersianDay());
//        }
//        query="SELECT BsHamyarSelectServices.*,Servicesdetails.name FROM BsHamyarSelectServices " +
//                "LEFT JOIN " +
//                "Servicesdetails ON " +
//                "Servicesdetails.code=BsHamyarSelectServices.ServiceDetaileCode WHERE IsDelete='0' AND " +
//                "Status='1'"+
//                " AND StartDate='"+year+"/"+mon+"/"+day+"'";
//        Cursor cursorDuty = db.rawQuery(query,null);
//        if(cursorDuty.getCount()>0)
//        {
//            btnDuty.setText(String.valueOf(cursorDuty.getCount()));
//        }
//        else
//        {
//            btnDuty.setText("0");
//        }

        try {
            if (!db.isOpen()) {
                db = dbh.getReadableDatabase();
            }
        } catch (Exception ex) {
            db = dbh.getReadableDatabase();
        }
        String query = "SELECT BsHamyarSelectServices.*,Servicesdetails.name FROM BsHamyarSelectServices " +
                "LEFT JOIN " +
                "Servicesdetails ON " +
                "Servicesdetails.code=BsHamyarSelectServices.ServiceDetaileCode WHERE IsDelete='0' AND " +
                "Status='2'";
//                "StartDate='"+year+"/"+mon+"/"+day+"'";
        Cursor cursorServiceNow = db.rawQuery(query, null);
        for (int i = 0; i < cursorServiceNow.getCount(); i++) {
            cursorServiceNow.moveToNext();
            String DateTimeStartStr = ChangeDate.changeFarsiToMiladi(cursorServiceNow.getString(cursorServiceNow.getColumnIndex("StartDate"))) + " " + cursorServiceNow.getString(cursorServiceNow.getColumnIndex("StartTime")) + ":00";
            String DateTimeEndStr = ChangeDate.changeFarsiToMiladi(cursorServiceNow.getString(cursorServiceNow.getColumnIndex("EndDate"))) + " " + cursorServiceNow.getString(cursorServiceNow.getColumnIndex("EndTime")) + ":00";
            String GetDateTime = "Select Cast ((JulianDay('" + faToEn(DateTimeEndStr.replace("/", "-")) + "') - JulianDay('" + faToEn(DateTimeStartStr.replace("/", "-")) + "'))" +
                    " * 24 As Integer) time";
            Cursor Ctime = db.rawQuery(GetDateTime, null);
            if (Ctime.getCount() > 0) {
                HashMap<String, String> map = new HashMap<String, String>();
                Ctime.moveToNext();
                if (!db.isOpen()) {
                    db = dbh.getReadableDatabase();
                }
                Cursor cursor_StartDate = db.rawQuery("SELECT * FROM StartDateService A WHERE A.BsUserServiceCode='"
                        +cursorServiceNow.getString(cursorServiceNow.getColumnIndex("Code"))+"'", null);
                if (cursor_StartDate.getCount()>0) {
                    cursor_StartDate.moveToNext();
                    map.put("BsUserServicesID", cursorServiceNow.getString(cursorServiceNow.getColumnIndex("Code")));
                    map.put("ContentService", "در: " + cursor_StartDate.getString(cursor_StartDate.getColumnIndex("UserConfirmDate")) + " - " +
                            "شروع: " + cursor_StartDate.getString(cursor_StartDate.getColumnIndex("UserConfirmTime")) + " - " +
                            "به مدت: " + Ctime.getString(Ctime.getColumnIndex("time")) + " ساعت " +
                            "به نام: " + cursorServiceNow.getString(cursorServiceNow.getColumnIndex("UserName")) + " " + cursorServiceNow.getString(cursorServiceNow.getColumnIndex("UserFamily")));
                }
                else {
                    map.put("BsUserServicesID", cursorServiceNow.getString(cursorServiceNow.getColumnIndex("Code")));
                    map.put("ContentService", "در: " + cursorServiceNow.getString(cursorServiceNow.getColumnIndex("StartDate")) + " - " +
                            "شروع: " + cursorServiceNow.getString(cursorServiceNow.getColumnIndex("StartTime")) + " - " +
                            "به مدت: " + Ctime.getString(Ctime.getColumnIndex("time")) + " ساعت " +
                            "به نام: " + cursorServiceNow.getString(cursorServiceNow.getColumnIndex("UserName")) + " " + cursorServiceNow.getString(cursorServiceNow.getColumnIndex("UserFamily")));
                }
                valuse.add(map);
            }
            if (!Ctime.isClosed()) {
                Ctime.close();
            }
        }
        if (!cursorServiceNow.isClosed()) {
            cursorServiceNow.close();
        }
        if (db.isOpen()) {
            db.close();
        }
        if (valuse.size() > 0) {
            LinearTextServiceNow.setVisibility(View.VISIBLE);
            AdapterListServiceNow dataAdapter = new AdapterListServiceNow(this, valuse, guid, hamyarcode);
            ListDutyNow.setAdapter(dataAdapter);
        } else {
            LinearTextServiceNow.setVisibility(View.GONE);
        }
        //**************************************************************************
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        viewFlipper = (Custom_ViewFlipper) findViewById(R.id.vf);
        try {
            if (!db.isOpen()) {
                db = dbh.getReadableDatabase();
            }
        } catch (Exception ex) {
            db = dbh.getReadableDatabase();
        }
        coursors = db.rawQuery("SELECT * FROM Slider", null);
        if (coursors.getCount() > 0) {
            Bitmap bpm[] = new Bitmap[coursors.getCount()];
            String link[] = new String[coursors.getCount()];
            for (int j = 0; j < coursors.getCount(); j++) {
                coursors.moveToNext();
                viewFlipper.setVisibility(View.VISIBLE);
                //slides.add();
                bpm[j] = convertToBitmap(coursors.getString(coursors.getColumnIndex("Pic")));
                link[j] = coursors.getString(coursors.getColumnIndex("Link"));
            }
            db.close();
            int i = 0;
            while (i < bpm.length) {
//                imageView = new  com.flaviofaria.kenburnsview.KenBurnsView(this);
                imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                //ImageLoader.getInstance().displayImage(slides.get(i),imageView);
                imageView.setImageBitmap(bpm[i]);
                imageView.setTag(link[i]);
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String link="";
//                        link = ((ImageView)v).getTag().toString();
//                        Toast.makeText(getBaseContext(), link, Toast.LENGTH_LONG).show();
//                    }
//                });
                viewFlipper.addView(imageView);
                i++;
            }
//            RandomTransitionGenerator randomTransitionGenerator = new RandomTransitionGenerator(3000,new FastOutLinearInInterpolator());
//            imageView.setTransitionGenerator(randomTransitionGenerator);


            Paint paint = new Paint();
            paint.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
            viewFlipper.setPaintCurrent(paint);
            paint = new Paint();

            paint.setColor(ContextCompat.getColor(this, android.R.color.white));
            viewFlipper.setPaintNormal(paint);

            viewFlipper.setRadius(10);
            viewFlipper.setMargin(5);

            CustomGestureDetector customGestureDetector = new CustomGestureDetector();
            mGestureDetector = new GestureDetector(MainMenu.this, customGestureDetector);

            viewFlipper.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    mGestureDetector.onTouchEvent(motionEvent);
                    return true;
                }
            });
        } else {
            viewFlipper.setVisibility(View.GONE);
        }


        btnCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadActivity(Credit.class, "guid", guid, "hamyarcode", hamyarcode);
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
        //****************************************************************************************
        CreateMenu(toolbar);
        //***************************************************************************************************************************
        btnDuty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goto Duty Page List
                LoadActivity(List_Dutys.class, "guid", guid, "hamyarcode", hamyarcode);
            }
        });
        btnServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goto Services Page List
                LoadActivity(List_Services.class, "guid", guid, "hamyarcode", hamyarcode);
            }
        });
    }

    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // Swipe left (next)
            if (e1.getX() > e2.getX()) {
                viewFlipper.setInAnimation(MainMenu.this, R.anim.left_in);
                viewFlipper.setOutAnimation(MainMenu.this, R.anim.left_out);

                viewFlipper.showNext();
            } else if (e1.getX() < e2.getX()) {
                viewFlipper.setInAnimation(MainMenu.this, R.anim.right_in);
                viewFlipper.setOutAnimation(MainMenu.this, R.anim.right_out);

                viewFlipper.showPrevious();
            }

            viewFlipper.setInAnimation(MainMenu.this, R.anim.left_in);
            viewFlipper.setOutAnimation(MainMenu.this, R.anim.left_out);

            return super.onFling(e1, e2, velocityX, velocityY);
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
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                    jobScheduler_SchaduleServiceGetNewJob.cancelAll();
//                    jobScheduler_SchaduleServiceGetFactorAccept.cancelAll();
//                    jobScheduler_SchaduleServiceGetLocation.cancelAll();
//                    jobScheduler_SchaduleServiceGetSliderPic.cancelAll();
//                    jobScheduler_SchaduleServiceSyncProfile.cancelAll();
//                    jobScheduler_SchaduleServiceSyncServiceSelected.cancelAll();
//                    jobScheduler_SchaduleServiceGetJobUpdate.cancelAll();
//                    jobScheduler_SchaduleServiceDeleteJob.cancelAll();
//                    jobScheduler_SchaduleServiceGetUserServiceStartDate.cancelAll();
//                } else {

                    PublicVariable.theard_DeleteJob = false;
                    PublicVariable.theard_GetFactorAccept = false;
                    PublicVariable.theard_GetJobUpdate = false;
                    PublicVariable.theard_GetLocation = false;
                    PublicVariable.theard_GetNewJob = false;
                    PublicVariable.theard_GetSliderPic = false;
                    PublicVariable.theard_GetUserServiceStartDate = false;
                    PublicVariable.theard_Profile = false;
                    PublicVariable.theard_ServiceSelected = false;
                    //**********************************************************
                    PublicVariable.createthread_DeleteJob = false;
                    PublicVariable.createthread_GetFactorAccept = false;
                    PublicVariable.createthread_GetJobUpdate = false;
                    PublicVariable.createthread_GetLocation = false;
                    PublicVariable.createthread_GetNewJob = false;
                    PublicVariable.createthread_GetSliderPic = false;
                    PublicVariable.createthread_GetUserServiceStartDate = false;
                    PublicVariable.createthread_Profile = false;
                    PublicVariable.createthread_ServiceSelected = false;
                    PublicVariable.createthread_GetNewJobNotNotifi=false;
                    //*******************************************************
                    PublicVariable.stopthread_Service_DeleteJob = true;
                    PublicVariable.stopthread_Service_GetFactorAccept = true;
                    PublicVariable.stopthread_Service_GetJobUpdate = true;
                    PublicVariable.stopthread_Service_GetLocation = true;
                    PublicVariable.stopthread_Service_GetNewJob = true;
                    PublicVariable.stopthread_Service_GetSliderPic = true;
                    PublicVariable.stopthread_Service_GetUserServiceStartDate = true;
                    PublicVariable.stopthread_Service_Profile = true;
                    PublicVariable.stopthread_Service_ServiceSelected = true;
                    PublicVariable.stopthread_GetNewJobNotNotifi = true;

                boolean checkStopService=true;
                while(checkStopService) {
                    final ActivityManager activityManager = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                    final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);
                    int CountService=0;
                    for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
                        CountService++;
                        String ClassName=runningServiceInfo.service.getClassName();
                        if (ClassName.compareTo("com.besparina.it.hamyar.ServiceGetLocation")==0) {
                            stopService(new Intent(getBaseContext(), ServiceGetLocation.class));
                        }
                        else if (ClassName.compareTo("com.besparina.it.hamyar.ServiceGetNewJob")==0) {
                              stopService(new Intent(getBaseContext(), ServiceGetNewJob.class));
                        }
                        else if (ClassName.compareTo("com.besparina.it.hamyar.ServiceGetNewJobNotNotifi") == 0 ) {
                             stopService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));
                        }
                        else if (ClassName.compareTo("com.besparina.it.hamyar.ServiceGetSliderPic") == 0 ) {
                             stopService(new Intent(getBaseContext(), ServiceGetSliderPic.class));
                        }
                        else if (ClassName.compareTo("com.besparina.it.hamyar.ServiceSyncProfile") == 0 ) {
                             stopService(new Intent(getBaseContext(), ServiceSyncProfile.class));
                        }
                        else if (ClassName.compareTo("com.besparina.it.hamyar.ServiceSyncServiceSelected") == 0 ) {
                             stopService(new Intent(getBaseContext(), ServiceSyncServiceSelected.class));
                        }
                        else if (ClassName.compareTo("com.besparina.it.hamyar.ServiceGetJobUpdate") == 0 ) {
                             stopService(new Intent(getBaseContext(), ServiceGetJobUpdate.class));
                        }
                        else if (ClassName.compareTo("com.besparina.it.hamyar.ServiceDeleteJob") == 0 ) {
                             stopService(new Intent(getBaseContext(), ServiceDeleteJob.class));
                        }
                        else if (ClassName.compareTo("com.besparina.it.hamyar.ServiceGetFactorAccept") == 0 ) {
                             stopService(new Intent(getBaseContext(), ServiceGetFactorAccept.class));
                        }
                        else if (ClassName.compareTo("com.besparina.it.hamyar.ServiceGetUserServiceStartDate") == 0 ) {
                             stopService(new Intent(getBaseContext(), ServiceGetUserServiceStartDate.class));
                        }
                        else
                        {
                            checkStopService=false;
                        }
                    }
                    if(CountService==0)
                    {
                        checkStopService=false;
                    }
                }


//                }
                try {
                    if (!db.isOpen()) {
                        db = dbh.getWritableDatabase();
                    }
                } catch (Exception ex) {
                    db = dbh.getWritableDatabase();
                }
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void CreateMenu(Toolbar toolbar) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.useravatar);
        String name = "";
        String family = "";
        String status = "";
        db = dbh.getReadableDatabase();
        Cursor coursors = db.rawQuery("SELECT * FROM Profile", null);
        if (coursors.getCount() > 0) {
            coursors.moveToNext();
            try {
                if (coursors.getString(coursors.getColumnIndex("Status")).compareTo("null") != 0) {
                    status = coursors.getString(coursors.getColumnIndex("Status"));
                    if (status.compareTo("0") == 0) {
                        status = "غیرفعال";
                    } else {
                        status = "فعال";
                    }
                } else {
                    status = "غیرفعال";
                }

            } catch (Exception ex) {
                status = "غیرفعال";
            }
            try {
                if (coursors.getString(coursors.getColumnIndex("Name")).compareTo("null") != 0) {
                    name = coursors.getString(coursors.getColumnIndex("Name"));
                } else {
                    name = "کاربر";
                }

            } catch (Exception ex) {
                name = "کاربر";
            }
            try {
                if (coursors.getString(coursors.getColumnIndex("Fam")).compareTo("null") != 0) {
                    family = coursors.getString(coursors.getColumnIndex("Fam"));
                } else {
                    family = "مهمان";
                }

            } catch (Exception ex) {
                family = "مهمان";
            }
            try {
                if (coursors.getString(coursors.getColumnIndex("Pic")).compareTo("null") != 0) {
                    bmp = convertToBitmap(coursors.getString(coursors.getColumnIndex("Pic")));
                } else {
                    bmp = BitmapFactory.decodeResource(getResources(), R.drawable.useravatar);
                }

            } catch (Exception ex) {
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.useravatar);
            }
        } else {
            name = "کاربر";
            family = "مهمان";
        }

        int drawerGravity = Gravity.END;
        Configuration config = getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            drawerGravity = Gravity.START;
        }


        String HeaderStr = name + " " + family + " - " + "وضعیت: " + status;
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
        drawer = new DrawerBuilder()
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
                        new SecondaryDrawerItem().withName(R.string.About).withIcon(R.drawable.about).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.History).withIcon(R.drawable.history).withSelectable(false),
                        //new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
                        //new SecondaryDrawerItem().withName(R.string.Exit).withIcon(R.drawable.exit).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.Logout).withIcon(R.drawable.logout).withSelectable(false)
                )//.addStickyDrawerItems(new PrimaryDrawerItem().withName(R.string.RelateUs).withSelectable(false).withEnabled(false),se),
                //new PrimaryDrawerItem().withName(R.string.telegram).withIcon(R.drawable.telegram).withSelectable(false),
                //new PrimaryDrawerItem().withName(R.string.instagram).withIcon(R.drawable.instagram).withSelectable(false))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position) {
                            case 1://Profile
                                try {
                                    if (!db.isOpen()) {
                                        db = dbh.getReadableDatabase();
                                    }
                                } catch (Exception ex) {
                                    db = dbh.getReadableDatabase();
                                }
                                Cursor coursors = db.rawQuery("SELECT * FROM Profile", null);
                                if (coursors.getCount() > 0) {
                                    coursors.moveToNext();
                                    String Status_check = coursors.getString(coursors.getColumnIndex("Status"));
                                    if (Status_check.compareTo("0") == 0) {
                                        Cursor c = db.rawQuery("SELECT * FROM login", null);
                                        if (c.getCount() > 0) {
                                            c.moveToNext();
                                            SyncProfile profile = new SyncProfile(MainMenu.this, c.getString(c.getColumnIndex("guid")), c.getString(c.getColumnIndex("hamyarcode")));
                                            profile.AsyncExecute();
                                        }
                                    } else {
                                        LoadActivity(Profile.class, "guid", guid, "hamyarcode", hamyarcode);
                                    }
                                } else {
                                    Toast.makeText(MainMenu.this, "برای استفاده از امکانات بسپارینا باید ثبت نام کنید", Toast.LENGTH_LONG).show();
                                    LoadActivity(Login.class, "guid", guid, "hamyarcode", hamyarcode);
                                }

                                db.close();
                                break;
                            case 2:
                                db = dbh.getReadableDatabase();
                                Cursor c = db.rawQuery("SELECT * FROM login", null);
                                if (c.getCount() > 0) {
                                    c.moveToNext();
                                    LoadActivity(List_Visits.class, "guid", c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
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
//                                db = dbh.getReadableDatabase();
//                                c = db.rawQuery("SELECT * FROM login",null);
//                                if(c.getCount()>0) {
//                                    c.moveToNext();
//
//                                    LoadActivity(List_Messages.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
//                                }
//                                db.close();
                                break;
                            case 6:
//                                db = dbh.getReadableDatabase();
//                                c = db.rawQuery("SELECT * FROM login",null);
//                                if(c.getCount()>0) {
//                                    c.moveToNext();
//
//                                    LoadActivity(GiftBank.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
//                                }
//                                db.close();
                                openWebPage("http://besparina.ir/?page_id=405&preview=true");
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
                                c = db.rawQuery("SELECT * FROM login", null);
                                if (c.getCount() > 0) {
                                    c.moveToNext();
                                    LoadActivity(Contact.class, "guid", c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
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
                                try {
                                    if (!db.isOpen()) {
                                        db = dbh.getReadableDatabase();
                                    }
                                } catch (Exception ex) {
                                    db = dbh.getReadableDatabase();
                                }
                                c = db.rawQuery("SELECT * FROM login", null);
                                if (c.getCount() > 0) {
                                    c.moveToNext();
                                    SyncGetHmFactorService getHmFactorService = new SyncGetHmFactorService(MainMenu.this, guid, hamyarcode);
                                    getHmFactorService.AsyncExecute();
                                    SyncGetHmFactorTools syncGetHmFactorTools = new SyncGetHmFactorTools(MainMenu.this, guid, hamyarcode);
                                    syncGetHmFactorTools.AsyncExecute();
                                    LoadActivity(Setting.class, "guid", c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
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
////                                Toast.makeText(MainMenu.this, "خروج از برنامه", Toast.LENGTH_SHORT).show();
//                                ExitApplication();
//                                break;
                            case 12:
                                LoadActivity(History.class, "guid", guid, "hamyarcode", hamyarcode);
                                break;
                            case 13:
                                Logout();
                                break;
                            case 14:
                                Toast.makeText(MainMenu.this, "تلگرام", Toast.LENGTH_SHORT).show();
                                break;
                            case 15:
                                Toast.makeText(MainMenu.this, "اینستاگرام", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                })
                .build();
//        drawer.closeDrawer();
    }
//    private void ExitApplication()
//    {
//
//        //Exit All Activity And Kill Application
//        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
//        // set the message to display
//        alertbox.setMessage("آیا می خواهید از برنامه خارج شوید ؟");
//
//        // set a negative/no button and create a listener
//        alertbox.setPositiveButton("خیر", new DialogInterface.OnClickListener() {
//            // do something when the button is clicked
//            public void onClick(DialogInterface arg0, int arg1) {
//                arg0.dismiss();
//            }
//        });
//
//        // set a positive/yes button and create a listener
//        alertbox.setNegativeButton("بله", new DialogInterface.OnClickListener() {
//            // do something when the button is clicked
//            public void onClick(DialogInterface arg0, int arg1) {
//                //Declare Object From Get Internet Connection Status For Check Internet Status
//                Intent startMain = new Intent(Intent.ACTION_MAIN);
//
//
//                startMain.addCategory(Intent.CATEGORY_HOME);
//
////                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                startActivity(startMain);
//
//                finish();
//
//                arg0.dismiss();
//
//            }
//        });
//
//        alertbox.show();
//    }

    //    @Override
//    public boolean onKeyDown( int keyCode, KeyEvent event )  {
//        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
//            //stopService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));
//
//        }
//
//        return super.onKeyDown( keyCode, event );
//    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2) {
        Intent intent = new Intent(getApplicationContext(), Cls);
        intent.putExtra(VariableName, VariableValue);
        intent.putExtra(VariableName2, VariableValue2);
        this.startActivity(intent);
    }

    public Bitmap convertToBitmap(String base) {
        Bitmap Bmp = null;
        try {
            byte[] decodedByte = Base64.decode(base, Base64.DEFAULT);
            Bmp = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
//
            return Bmp;
        } catch (Exception e) {
            e.printStackTrace();
            return Bmp;
        }
    }

    protected void onStart() {

        super.onStart();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            //*****************************************SchaduleServiceGetNewJob******************************************
//            ComponentName serviceComponent_SchaduleServiceGetNewJob = new ComponentName(getApplicationContext(), SchaduleServiceGetNewJob.class);
//            JobInfo.Builder builder_SchaduleServiceGetNewJob = null;
//            builder_SchaduleServiceGetNewJob = new JobInfo.Builder(0, serviceComponent_SchaduleServiceGetNewJob);
//            builder_SchaduleServiceGetNewJob.setMinimumLatency(5 * 1000); // wait at least
//            builder_SchaduleServiceGetNewJob.setOverrideDeadline(50 * 1000); // maximum delay
//            builder_SchaduleServiceGetNewJob.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetNewJob.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetNewJob.setRequiresCharging(false); // we don't care if the device is charging or not
//            JobScheduler jobScheduler_SchaduleServiceGetNewJob = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetNewJob = getApplicationContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetNewJob.schedule(builder_SchaduleServiceGetNewJob.build());
//            //*****************************************SchaduleServiceGetFactorAccept******************************************
//            ComponentName serviceComponent_SchaduleServiceGetFactorAccept = new ComponentName(getApplicationContext(), SchaduleServiceGetFactorAccept.class);
//            JobInfo.Builder builder_SchaduleServiceGetFactorAccept = null;
//            builder_SchaduleServiceGetFactorAccept = new JobInfo.Builder(1, serviceComponent_SchaduleServiceGetFactorAccept);
//            builder_SchaduleServiceGetFactorAccept.setMinimumLatency(5 * 1000); // wait at least
//            builder_SchaduleServiceGetFactorAccept.setOverrideDeadline(50 * 1000); // maximum delay
//            builder_SchaduleServiceGetFactorAccept.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetFactorAccept.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetFactorAccept.setRequiresCharging(false); // we don't care if the device is charging or not
//            JobScheduler jobScheduler_SchaduleServiceGetFactorAccept = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetFactorAccept = getApplicationContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetFactorAccept.schedule(builder_SchaduleServiceGetFactorAccept.build());
////*****************************************SchaduleServiceSyncProfile******************************************
//            ComponentName serviceComponent_SchaduleServiceSyncProfile = new ComponentName(getApplicationContext(), SchaduleServiceSyncProfile.class);
//            JobInfo.Builder builder_SchaduleServiceSyncProfile = null;
//            builder_SchaduleServiceSyncProfile = new JobInfo.Builder(5, serviceComponent_SchaduleServiceSyncProfile);
//            builder_SchaduleServiceSyncProfile.setMinimumLatency(5 * 1000); // wait at least
//            builder_SchaduleServiceSyncProfile.setOverrideDeadline(50 * 1000); // maximum delay
//            builder_SchaduleServiceSyncProfile.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceSyncProfile.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceSyncProfile.setRequiresCharging(false); // we don't care if the device is charging or not
//            JobScheduler jobScheduler_SchaduleServiceSyncProfile = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceSyncProfile = getApplicationContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceSyncProfile.schedule(builder_SchaduleServiceSyncProfile.build());
////*****************************************SchaduleServiceGetUserServiceStartDate******************************************
//            ComponentName serviceComponent_SchaduleServiceGetUserServiceStartDate = new ComponentName(getApplicationContext(), SchaduleServiceGetUserServiceStartDate.class);
//            JobInfo.Builder builder_SchaduleServiceGetUserServiceStartDate = null;
//            builder_SchaduleServiceGetUserServiceStartDate = new JobInfo.Builder(7, serviceComponent_SchaduleServiceGetUserServiceStartDate);
//            builder_SchaduleServiceGetUserServiceStartDate.setMinimumLatency(5 * 1000); // wait at least
//            builder_SchaduleServiceGetUserServiceStartDate.setOverrideDeadline(50 * 1000); // maximum delay
//            builder_SchaduleServiceGetUserServiceStartDate.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetUserServiceStartDate.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetUserServiceStartDate.setRequiresCharging(false); // we don't care if the device is charging or not
//            JobScheduler jobScheduler_SchaduleServiceGetUserServiceStartDate = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetUserServiceStartDate = getApplicationContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetUserServiceStartDate.schedule(builder_SchaduleServiceGetUserServiceStartDate.build());
//            if (PublicVariable.Active_Service_GetNewJob) {
//                this.startForegroundService(new Intent(this, ServiceGetNewJob.class));
//            }
//            if (PublicVariable.Active_Service_GetFactorAccept) {
//                this.startForegroundService(new Intent(this, ServiceGetFactorAccept.class));
//            }
//            if (PublicVariable.Active_Service_Profile) {
//                this.startForegroundService(new Intent(this, ServiceSyncProfile.class));
//            }
//            if (PublicVariable.Active_Service_GetUserServiceStartDate) {
//                this.startService(new Intent(this, ServiceGetUserServiceStartDate.class));
//            }
//        } else {
//            startService(new Intent(getBaseContext(), ServiceGetNewJob.class));
//            startService(new Intent(getBaseContext(), ServiceGetFactorAccept.class));
//            startService(new Intent(getBaseContext(), ServiceSyncProfile.class));
//            startService(new Intent(getBaseContext(), ServiceGetUserServiceStartDate.class));
//        }
    }

    protected void onResume() {

        super.onResume();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            //*****************************************SchaduleServiceGetNewJob******************************************
//            ComponentName serviceComponent_SchaduleServiceGetNewJob = new ComponentName(getApplicationContext(), SchaduleServiceGetNewJob.class);
//            JobInfo.Builder builder_SchaduleServiceGetNewJob = null;
//            builder_SchaduleServiceGetNewJob = new JobInfo.Builder(0, serviceComponent_SchaduleServiceGetNewJob);
//            builder_SchaduleServiceGetNewJob.setMinimumLatency(5 * 1000); // wait at least
//            builder_SchaduleServiceGetNewJob.setOverrideDeadline(50 * 1000); // maximum delay
//            builder_SchaduleServiceGetNewJob.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetNewJob.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetNewJob.setRequiresCharging(false); // we don't care if the device is charging or not
//            JobScheduler jobScheduler_SchaduleServiceGetNewJob = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetNewJob = getApplicationContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetNewJob.schedule(builder_SchaduleServiceGetNewJob.build());
//            //*****************************************SchaduleServiceGetFactorAccept******************************************
//            ComponentName serviceComponent_SchaduleServiceGetFactorAccept = new ComponentName(getApplicationContext(), SchaduleServiceGetFactorAccept.class);
//            JobInfo.Builder builder_SchaduleServiceGetFactorAccept = null;
//            builder_SchaduleServiceGetFactorAccept = new JobInfo.Builder(1, serviceComponent_SchaduleServiceGetFactorAccept);
//            builder_SchaduleServiceGetFactorAccept.setMinimumLatency(5 * 1000); // wait at least
//            builder_SchaduleServiceGetFactorAccept.setOverrideDeadline(50 * 1000); // maximum delay
//            builder_SchaduleServiceGetFactorAccept.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetFactorAccept.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetFactorAccept.setRequiresCharging(false); // we don't care if the device is charging or not
//            JobScheduler jobScheduler_SchaduleServiceGetFactorAccept = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetFactorAccept = getApplicationContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetFactorAccept.schedule(builder_SchaduleServiceGetFactorAccept.build());
////*****************************************SchaduleServiceSyncProfile******************************************
//            ComponentName serviceComponent_SchaduleServiceSyncProfile = new ComponentName(getApplicationContext(), SchaduleServiceSyncProfile.class);
//            JobInfo.Builder builder_SchaduleServiceSyncProfile = null;
//            builder_SchaduleServiceSyncProfile = new JobInfo.Builder(5, serviceComponent_SchaduleServiceSyncProfile);
//            builder_SchaduleServiceSyncProfile.setMinimumLatency(5 * 1000); // wait at least
//            builder_SchaduleServiceSyncProfile.setOverrideDeadline(50 * 1000); // maximum delay
//            builder_SchaduleServiceSyncProfile.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceSyncProfile.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceSyncProfile.setRequiresCharging(false); // we don't care if the device is charging or not
//            JobScheduler jobScheduler_SchaduleServiceSyncProfile = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceSyncProfile = getApplicationContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceSyncProfile.schedule(builder_SchaduleServiceSyncProfile.build());
////*****************************************SchaduleServiceGetUserServiceStartDate******************************************
//            ComponentName serviceComponent_SchaduleServiceGetUserServiceStartDate = new ComponentName(getApplicationContext(), SchaduleServiceGetUserServiceStartDate.class);
//            JobInfo.Builder builder_SchaduleServiceGetUserServiceStartDate = null;
//            builder_SchaduleServiceGetUserServiceStartDate = new JobInfo.Builder(7, serviceComponent_SchaduleServiceGetUserServiceStartDate);
//            builder_SchaduleServiceGetUserServiceStartDate.setMinimumLatency(5 * 1000); // wait at least
//            builder_SchaduleServiceGetUserServiceStartDate.setOverrideDeadline(50 * 1000); // maximum delay
//            builder_SchaduleServiceGetUserServiceStartDate.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetUserServiceStartDate.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetUserServiceStartDate.setRequiresCharging(false); // we don't care if the device is charging or not
//            JobScheduler jobScheduler_SchaduleServiceGetUserServiceStartDate = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetUserServiceStartDate = getApplicationContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetUserServiceStartDate.schedule(builder_SchaduleServiceGetUserServiceStartDate.build());
//            if (PublicVariable.Active_Service_GetNewJob) {
//                this.startForegroundService(new Intent(this, ServiceGetNewJob.class));
//            }
//            if (PublicVariable.Active_Service_GetFactorAccept) {
//                this.startForegroundService(new Intent(this, ServiceGetFactorAccept.class));
//            }
//            if (PublicVariable.Active_Service_Profile) {
//                this.startForegroundService(new Intent(this, ServiceSyncProfile.class));
//            }
//            if (PublicVariable.Active_Service_GetUserServiceStartDate) {
//                this.startService(new Intent(this, ServiceGetUserServiceStartDate.class));
//            }
//        } else {
//            startService(new Intent(getBaseContext(), ServiceGetNewJob.class));
//            startService(new Intent(getBaseContext(), ServiceGetFactorAccept.class));
//            startService(new Intent(getBaseContext(), ServiceSyncProfile.class));
//            startService(new Intent(getBaseContext(), ServiceGetUserServiceStartDate.class));
//        }
//        try {
//            String status = "0";
//            db = dbh.getReadableDatabase();
//            Cursor cursor = db.rawQuery("SELECT * FROM Profile", null);
//            if (cursor.getCount() > 0) {
//                cursor.moveToNext();
//                try {
//                    if (cursor.getString(cursor.getColumnIndex("Status")).compareTo("null") != 0) {
//                        status = cursor.getString(cursor.getColumnIndex("Status"));
//                        if (status.compareTo("0") == 0) {
//                            status = "غیرفعال";
//                        } else {
//                            status = "فعال";
//                        }
//                    } else {
//                        status = "غیرفعال";
//                    }
//
//                } catch (Exception ex) {
//                    status = "غیرفعال";
//                }
//            }
//            hamyarcode = getIntent().getStringExtra("hamyarcode");
//            guid = getIntent().getStringExtra("guid");
//            Check_Login(hamyarcode, guid, status);
//        } catch (Exception e) {
//            throw new Error("Error Opne Activity");
//        }
        //startService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));

    }

    protected void onStop() {

        super.onStop();
        continue_or_stop = false;
        //stopService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            //*****************************************SchaduleServiceGetNewJob******************************************
//            ComponentName serviceComponent_SchaduleServiceGetNewJob = new ComponentName(getApplicationContext(), SchaduleServiceGetNewJob.class);
//            JobInfo.Builder builder_SchaduleServiceGetNewJob = null;
//            builder_SchaduleServiceGetNewJob = new JobInfo.Builder(0, serviceComponent_SchaduleServiceGetNewJob);
//            builder_SchaduleServiceGetNewJob.setMinimumLatency(5 * 1000); // wait at least
//            builder_SchaduleServiceGetNewJob.setOverrideDeadline(50 * 1000); // maximum delay
//            builder_SchaduleServiceGetNewJob.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetNewJob.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetNewJob.setRequiresCharging(false); // we don't care if the device is charging or not
//            JobScheduler jobScheduler_SchaduleServiceGetNewJob = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetNewJob = getApplicationContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetNewJob.schedule(builder_SchaduleServiceGetNewJob.build());
//            //*****************************************SchaduleServiceGetFactorAccept******************************************
//            ComponentName serviceComponent_SchaduleServiceGetFactorAccept = new ComponentName(getApplicationContext(), SchaduleServiceGetFactorAccept.class);
//            JobInfo.Builder builder_SchaduleServiceGetFactorAccept = null;
//            builder_SchaduleServiceGetFactorAccept = new JobInfo.Builder(1, serviceComponent_SchaduleServiceGetFactorAccept);
//            builder_SchaduleServiceGetFactorAccept.setMinimumLatency(5 * 1000); // wait at least
//            builder_SchaduleServiceGetFactorAccept.setOverrideDeadline(50 * 1000); // maximum delay
//            builder_SchaduleServiceGetFactorAccept.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetFactorAccept.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetFactorAccept.setRequiresCharging(false); // we don't care if the device is charging or not
//            JobScheduler jobScheduler_SchaduleServiceGetFactorAccept = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetFactorAccept = getApplicationContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetFactorAccept.schedule(builder_SchaduleServiceGetFactorAccept.build());
////*****************************************SchaduleServiceSyncProfile******************************************
//            ComponentName serviceComponent_SchaduleServiceSyncProfile = new ComponentName(getApplicationContext(), SchaduleServiceSyncProfile.class);
//            JobInfo.Builder builder_SchaduleServiceSyncProfile = null;
//            builder_SchaduleServiceSyncProfile = new JobInfo.Builder(5, serviceComponent_SchaduleServiceSyncProfile);
//            builder_SchaduleServiceSyncProfile.setMinimumLatency(5 * 1000); // wait at least
//            builder_SchaduleServiceSyncProfile.setOverrideDeadline(50 * 1000); // maximum delay
//            builder_SchaduleServiceSyncProfile.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceSyncProfile.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceSyncProfile.setRequiresCharging(false); // we don't care if the device is charging or not
//            JobScheduler jobScheduler_SchaduleServiceSyncProfile = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceSyncProfile = getApplicationContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceSyncProfile.schedule(builder_SchaduleServiceSyncProfile.build());
////*****************************************SchaduleServiceGetUserServiceStartDate******************************************
//            ComponentName serviceComponent_SchaduleServiceGetUserServiceStartDate = new ComponentName(getApplicationContext(), SchaduleServiceGetUserServiceStartDate.class);
//            JobInfo.Builder builder_SchaduleServiceGetUserServiceStartDate = null;
//            builder_SchaduleServiceGetUserServiceStartDate = new JobInfo.Builder(7, serviceComponent_SchaduleServiceGetUserServiceStartDate);
//            builder_SchaduleServiceGetUserServiceStartDate.setMinimumLatency(5 * 1000); // wait at least
//            builder_SchaduleServiceGetUserServiceStartDate.setOverrideDeadline(50 * 1000); // maximum delay
//            builder_SchaduleServiceGetUserServiceStartDate.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetUserServiceStartDate.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetUserServiceStartDate.setRequiresCharging(false); // we don't care if the device is charging or not
//            JobScheduler jobScheduler_SchaduleServiceGetUserServiceStartDate = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetUserServiceStartDate = getApplicationContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetUserServiceStartDate.schedule(builder_SchaduleServiceGetUserServiceStartDate.build());
//            if (PublicVariable.Active_Service_GetNewJob) {
//                this.startForegroundService(new Intent(this, ServiceGetNewJob.class));
//            }
//            if (PublicVariable.Active_Service_GetFactorAccept) {
//                this.startForegroundService(new Intent(this, ServiceGetFactorAccept.class));
//            }
//            if (PublicVariable.Active_Service_Profile) {
//                this.startForegroundService(new Intent(this, ServiceSyncProfile.class));
//            }
//            if (PublicVariable.Active_Service_GetUserServiceStartDate) {
//                this.startService(new Intent(this, ServiceGetUserServiceStartDate.class));
//            }
//        } else {
//            startService(new Intent(getBaseContext(), ServiceGetNewJob.class));
//            startService(new Intent(getBaseContext(), ServiceGetFactorAccept.class));
//            startService(new Intent(getBaseContext(), ServiceSyncProfile.class));
//            startService(new Intent(getBaseContext(), ServiceGetUserServiceStartDate.class));
//            //startService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));
//        }
    }

    protected void onPause() {

        super.onPause();
        continue_or_stop = false;
        //stopService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            //*****************************************SchaduleServiceGetNewJob******************************************
//            ComponentName serviceComponent_SchaduleServiceGetNewJob = new ComponentName(getApplicationContext(), SchaduleServiceGetNewJob.class);
//            JobInfo.Builder builder_SchaduleServiceGetNewJob = null;
//            builder_SchaduleServiceGetNewJob = new JobInfo.Builder(0, serviceComponent_SchaduleServiceGetNewJob);
//            builder_SchaduleServiceGetNewJob.setMinimumLatency(5 * 1000); // wait at least
//            builder_SchaduleServiceGetNewJob.setOverrideDeadline(50 * 1000); // maximum delay
//            builder_SchaduleServiceGetNewJob.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetNewJob.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetNewJob.setRequiresCharging(false); // we don't care if the device is charging or not
//            JobScheduler jobScheduler_SchaduleServiceGetNewJob = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetNewJob = getApplicationContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetNewJob.schedule(builder_SchaduleServiceGetNewJob.build());
//            //*****************************************SchaduleServiceGetFactorAccept******************************************
//            ComponentName serviceComponent_SchaduleServiceGetFactorAccept = new ComponentName(getApplicationContext(), SchaduleServiceGetFactorAccept.class);
//            JobInfo.Builder builder_SchaduleServiceGetFactorAccept = null;
//            builder_SchaduleServiceGetFactorAccept = new JobInfo.Builder(1, serviceComponent_SchaduleServiceGetFactorAccept);
//            builder_SchaduleServiceGetFactorAccept.setMinimumLatency(5 * 1000); // wait at least
//            builder_SchaduleServiceGetFactorAccept.setOverrideDeadline(50 * 1000); // maximum delay
//            builder_SchaduleServiceGetFactorAccept.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetFactorAccept.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetFactorAccept.setRequiresCharging(false); // we don't care if the device is charging or not
//            JobScheduler jobScheduler_SchaduleServiceGetFactorAccept = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetFactorAccept = getApplicationContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetFactorAccept.schedule(builder_SchaduleServiceGetFactorAccept.build());
////*****************************************SchaduleServiceSyncProfile******************************************
//            ComponentName serviceComponent_SchaduleServiceSyncProfile = new ComponentName(getApplicationContext(), SchaduleServiceSyncProfile.class);
//            JobInfo.Builder builder_SchaduleServiceSyncProfile = null;
//            builder_SchaduleServiceSyncProfile = new JobInfo.Builder(5, serviceComponent_SchaduleServiceSyncProfile);
//            builder_SchaduleServiceSyncProfile.setMinimumLatency(5 * 1000); // wait at least
//            builder_SchaduleServiceSyncProfile.setOverrideDeadline(50 * 1000); // maximum delay
//            builder_SchaduleServiceSyncProfile.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceSyncProfile.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceSyncProfile.setRequiresCharging(false); // we don't care if the device is charging or not
//            JobScheduler jobScheduler_SchaduleServiceSyncProfile = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceSyncProfile = getApplicationContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceSyncProfile.schedule(builder_SchaduleServiceSyncProfile.build());
////*****************************************SchaduleServiceGetUserServiceStartDate******************************************
//            ComponentName serviceComponent_SchaduleServiceGetUserServiceStartDate = new ComponentName(getApplicationContext(), SchaduleServiceGetUserServiceStartDate.class);
//            JobInfo.Builder builder_SchaduleServiceGetUserServiceStartDate = null;
//            builder_SchaduleServiceGetUserServiceStartDate = new JobInfo.Builder(7, serviceComponent_SchaduleServiceGetUserServiceStartDate);
//            builder_SchaduleServiceGetUserServiceStartDate.setMinimumLatency(5 * 1000); // wait at least
//            builder_SchaduleServiceGetUserServiceStartDate.setOverrideDeadline(50 * 1000); // maximum delay
//            builder_SchaduleServiceGetUserServiceStartDate.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetUserServiceStartDate.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetUserServiceStartDate.setRequiresCharging(false); // we don't care if the device is charging or not
//            JobScheduler jobScheduler_SchaduleServiceGetUserServiceStartDate = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetUserServiceStartDate = getApplicationContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetUserServiceStartDate.schedule(builder_SchaduleServiceGetUserServiceStartDate.build());
//            if (PublicVariable.Active_Service_GetNewJob) {
//                this.startForegroundService(new Intent(this, ServiceGetNewJob.class));
//            }
//            if (PublicVariable.Active_Service_GetFactorAccept) {
//                this.startForegroundService(new Intent(this, ServiceGetFactorAccept.class));
//            }
//            if (PublicVariable.Active_Service_Profile) {
//                this.startForegroundService(new Intent(this, ServiceSyncProfile.class));
//            }
//            if (PublicVariable.Active_Service_GetUserServiceStartDate) {
//                this.startService(new Intent(this, ServiceGetUserServiceStartDate.class));
//            }
//        } else {
//            startService(new Intent(getBaseContext(), ServiceGetNewJob.class));
//            startService(new Intent(getBaseContext(), ServiceGetFactorAccept.class));
//            startService(new Intent(getBaseContext(), ServiceSyncProfile.class));
//            startService(new Intent(getBaseContext(), ServiceGetUserServiceStartDate.class));
//            //startService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));
//        }
    }

    protected void onDestroy() {

        super.onDestroy();
        continue_or_stop = false;
        //stopService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            //*****************************************SchaduleServiceGetNewJob******************************************
//            ComponentName serviceComponent_SchaduleServiceGetNewJob = new ComponentName(getApplicationContext(), SchaduleServiceGetNewJob.class);
//            JobInfo.Builder builder_SchaduleServiceGetNewJob = null;
//            builder_SchaduleServiceGetNewJob = new JobInfo.Builder(0, serviceComponent_SchaduleServiceGetNewJob);
//            builder_SchaduleServiceGetNewJob.setMinimumLatency(5 * 1000); // wait at least
//            builder_SchaduleServiceGetNewJob.setOverrideDeadline(50 * 1000); // maximum delay
//            builder_SchaduleServiceGetNewJob.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetNewJob.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetNewJob.setRequiresCharging(false); // we don't care if the device is charging or not
//            JobScheduler jobScheduler_SchaduleServiceGetNewJob = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetNewJob = getApplicationContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetNewJob.schedule(builder_SchaduleServiceGetNewJob.build());
//            //*****************************************SchaduleServiceGetFactorAccept******************************************
//            ComponentName serviceComponent_SchaduleServiceGetFactorAccept = new ComponentName(getApplicationContext(), SchaduleServiceGetFactorAccept.class);
//            JobInfo.Builder builder_SchaduleServiceGetFactorAccept = null;
//            builder_SchaduleServiceGetFactorAccept = new JobInfo.Builder(1, serviceComponent_SchaduleServiceGetFactorAccept);
//            builder_SchaduleServiceGetFactorAccept.setMinimumLatency(5 * 1000); // wait at least
//            builder_SchaduleServiceGetFactorAccept.setOverrideDeadline(50 * 1000); // maximum delay
//            builder_SchaduleServiceGetFactorAccept.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetFactorAccept.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetFactorAccept.setRequiresCharging(false); // we don't care if the device is charging or not
//            JobScheduler jobScheduler_SchaduleServiceGetFactorAccept = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetFactorAccept = getApplicationContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetFactorAccept.schedule(builder_SchaduleServiceGetFactorAccept.build());
////*****************************************SchaduleServiceSyncProfile******************************************
//            ComponentName serviceComponent_SchaduleServiceSyncProfile = new ComponentName(getApplicationContext(), SchaduleServiceSyncProfile.class);
//            JobInfo.Builder builder_SchaduleServiceSyncProfile = null;
//            builder_SchaduleServiceSyncProfile = new JobInfo.Builder(5, serviceComponent_SchaduleServiceSyncProfile);
//            builder_SchaduleServiceSyncProfile.setMinimumLatency(5 * 1000); // wait at least
//            builder_SchaduleServiceSyncProfile.setOverrideDeadline(50 * 1000); // maximum delay
//            builder_SchaduleServiceSyncProfile.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceSyncProfile.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceSyncProfile.setRequiresCharging(false); // we don't care if the device is charging or not
//            JobScheduler jobScheduler_SchaduleServiceSyncProfile = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceSyncProfile = getApplicationContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceSyncProfile.schedule(builder_SchaduleServiceSyncProfile.build());
////*****************************************SchaduleServiceGetUserServiceStartDate******************************************
//            ComponentName serviceComponent_SchaduleServiceGetUserServiceStartDate = new ComponentName(getApplicationContext(), SchaduleServiceGetUserServiceStartDate.class);
//            JobInfo.Builder builder_SchaduleServiceGetUserServiceStartDate = null;
//            builder_SchaduleServiceGetUserServiceStartDate = new JobInfo.Builder(7, serviceComponent_SchaduleServiceGetUserServiceStartDate);
//            builder_SchaduleServiceGetUserServiceStartDate.setMinimumLatency(5 * 1000); // wait at least
//            builder_SchaduleServiceGetUserServiceStartDate.setOverrideDeadline(50 * 1000); // maximum delay
//            builder_SchaduleServiceGetUserServiceStartDate.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetUserServiceStartDate.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetUserServiceStartDate.setRequiresCharging(false); // we don't care if the device is charging or not
//            JobScheduler jobScheduler_SchaduleServiceGetUserServiceStartDate = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetUserServiceStartDate = getApplicationContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetUserServiceStartDate.schedule(builder_SchaduleServiceGetUserServiceStartDate.build());
//            if (PublicVariable.Active_Service_GetNewJob) {
//                this.startForegroundService(new Intent(this, ServiceGetNewJob.class));
//            }
//            if (PublicVariable.Active_Service_GetFactorAccept) {
//                this.startForegroundService(new Intent(this, ServiceGetFactorAccept.class));
//            }
//            if (PublicVariable.Active_Service_Profile) {
//                this.startForegroundService(new Intent(this, ServiceSyncProfile.class));
//            }
//            if (PublicVariable.Active_Service_GetUserServiceStartDate) {
//                this.startService(new Intent(this, ServiceGetUserServiceStartDate.class));
//            }
//        } else {
//            startService(new Intent(getBaseContext(), ServiceGetNewJob.class));
//            startService(new Intent(getBaseContext(), ServiceGetFactorAccept.class));
//            startService(new Intent(getBaseContext(), ServiceSyncProfile.class));
//            startService(new Intent(getBaseContext(), ServiceGetUserServiceStartDate.class));
//            //startService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));
//        }
    }

    void sharecode(String shareStr) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "بسپارینا" + "\n" + "کد معرف: " + shareStr + "\n" + "آدرس سایت: " + PublicVariable.site;
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            if (doubleBackToExitPressedOnce) {
                continue_or_stop = false;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                    //*****************************************SchaduleServiceGetNewJob******************************************
//                    ComponentName serviceComponent_SchaduleServiceGetNewJob = new ComponentName(getApplicationContext(), SchaduleServiceGetNewJob.class);
//                    JobInfo.Builder builder_SchaduleServiceGetNewJob = null;
//                    builder_SchaduleServiceGetNewJob = new JobInfo.Builder(0, serviceComponent_SchaduleServiceGetNewJob);
//                    builder_SchaduleServiceGetNewJob.setMinimumLatency(5 * 1000); // wait at least
//                    builder_SchaduleServiceGetNewJob.setOverrideDeadline(50 * 1000); // maximum delay
//                    builder_SchaduleServiceGetNewJob.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//                    builder_SchaduleServiceGetNewJob.setRequiresDeviceIdle(false); // device should be idle
//                    builder_SchaduleServiceGetNewJob.setRequiresCharging(false); // we don't care if the device is charging or not
//                    JobScheduler jobScheduler_SchaduleServiceGetNewJob = null;
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                        jobScheduler_SchaduleServiceGetNewJob = getApplicationContext().getSystemService(JobScheduler.class);
//                    }
//                    jobScheduler_SchaduleServiceGetNewJob.schedule(builder_SchaduleServiceGetNewJob.build());
//
////*****************************************SchaduleServiceGetUserServiceStartDate******************************************
//                    ComponentName serviceComponent_SchaduleServiceGetUserServiceStartDate = new ComponentName(getApplicationContext(), SchaduleServiceGetUserServiceStartDate.class);
//                    JobInfo.Builder builder_SchaduleServiceGetUserServiceStartDate = null;
//                    builder_SchaduleServiceGetUserServiceStartDate = new JobInfo.Builder(7, serviceComponent_SchaduleServiceGetUserServiceStartDate);
//                    builder_SchaduleServiceGetUserServiceStartDate.setMinimumLatency(5 * 1000); // wait at least
//                    builder_SchaduleServiceGetUserServiceStartDate.setOverrideDeadline(50 * 1000); // maximum delay
//                    builder_SchaduleServiceGetUserServiceStartDate.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//                    builder_SchaduleServiceGetUserServiceStartDate.setRequiresDeviceIdle(false); // device should be idle
//                    builder_SchaduleServiceGetUserServiceStartDate.setRequiresCharging(false); // we don't care if the device is charging or not
//                    JobScheduler jobScheduler_SchaduleServiceGetUserServiceStartDate = null;
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                        jobScheduler_SchaduleServiceGetUserServiceStartDate = getApplicationContext().getSystemService(JobScheduler.class);
//                    }
//                    jobScheduler_SchaduleServiceGetUserServiceStartDate.schedule(builder_SchaduleServiceGetUserServiceStartDate.build());
//                    if (PublicVariable.Active_Service_GetNewJob) {
//                        this.startForegroundService(new Intent(this, ServiceGetNewJob.class));
//                    }
//                    if (PublicVariable.Active_Service_GetUserServiceStartDate) {
//                        this.startForegroundService(new Intent(this, ServiceGetUserServiceStartDate.class));
//                    }
//                } else {
//                    startService(new Intent(getBaseContext(), ServiceGetNewJob.class));
//                    startService(new Intent(getBaseContext(), ServiceGetUserServiceStartDate.class));
//                }
                Intent startMain = new Intent(Intent.ACTION_MAIN);

                startMain.addCategory(Intent.CATEGORY_HOME);

                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(startMain);

                finish();
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "جهت خروج از برنامه مجددا دکمه برگشت را لمس کنید", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    public void Check_Login(String hamyarcode, String guid, String status) {
        if (hamyarcode == null || guid == null) {

            Cursor cursor;
            try {
                if (!db.isOpen()) {
                    db = dbh.getReadableDatabase();
                }
            } catch (Exception ex) {
                db = dbh.getReadableDatabase();
            }
            cursor = db.rawQuery("SELECT * FROM login", null);
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                String Result = cursor.getString(cursor.getColumnIndex("islogin"));
                if (Result.compareTo("0") == 0) {
                    LoadActivity(Login.class, "hamyarcode", "0", "guid", "0");
                }
            } else {
                LoadActivity(Login.class, "hamyarcode", "0", "guid", "0");
            }
        } else if (hamyarcode.compareTo("0") == 0 || guid.compareTo("0") == 0 || status.compareTo("غیرفعال") == 0) {
            Toast.makeText(MainMenu.this, "شما فعال نشده اید", Toast.LENGTH_LONG).show();
            PublicVariable.IsActive = false;
            IsActive = false;
        } else {
            PublicVariable.IsActive = true;
            IsActive = true;
        }

        db.close();
    }

    public static String faToEn(String num) {
        return num
                .replace("۰", "0")
                .replace("۱", "1")
                .replace("۲", "2")
                .replace("۳", "3")
                .replace("۴", "4")
                .replace("۵", "5")
                .replace("۶", "6")
                .replace("۷", "7")
                .replace("۸", "8")
                .replace("۹", "9");
    }

    public static String EnToFa(String num) {
        return num
                .replace("0", "۰")
                .replace("1", "۱")
                .replace("2", "۲")
                .replace("3", "۳")
                .replace("4", "۴")
                .replace("5", "۵")
                .replace("6", "۶")
                .replace("7", "۷")
                .replace("8", "۸")
                .replace("9", "۹");
    }

    public void count_service() {
        int CountDuty = 0, CountService = 0;
        try {
            if (!db.isOpen()) {
                db = dbh.getReadableDatabase();
            }
        } catch (Exception ex) {
            db = dbh.getReadableDatabase();
        }
        String query = "SELECT BsUserServices.*,Servicesdetails.name FROM BsUserServices " +
                "LEFT JOIN " +
                "Servicesdetails ON " +
                "Servicesdetails.code=BsUserServices.ServiceDetaileCode ORDER BY StartDate DESC";
        Cursor coursors = db.rawQuery(query, null);
        Cursor Ctime;
        for (int i = 0; i < coursors.getCount(); i++) {
            coursors.moveToNext();
            if ((coursors.getString(coursors.getColumnIndex("IsEmergency")).compareTo("1") == 1)) {
                CountDuty++;
            } else {
                String DateTimeStr = ChangeDate.changeFarsiToMiladi(coursors.getString(coursors.getColumnIndex("StartDate"))) + " " + coursors.getString(coursors.getColumnIndex("StartTime")) + ":00";
                String GetDateTime = "Select Cast ((JulianDay('" + faToEn(DateTimeStr.replace("/", "-")) + "') - JulianDay('now'))" +
                        " * 24 As Integer) time";
                Ctime = db.rawQuery(GetDateTime, null);
                if (Ctime.getCount() > 0) {
                    Ctime.moveToNext();
                    int STime = Integer.parseInt(Ctime.getString(Ctime.getColumnIndex("time")));
                    if (STime <= 24) {
                        CountDuty++;
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }
        }
        db.close();
        btnDuty.setText(String.valueOf(CountDuty));

        //************************************************************************************
        try {
            if (!db.isOpen()) {
                db = dbh.getReadableDatabase();
            }
        } catch (Exception ex) {
            db = dbh.getReadableDatabase();
        }
        query = "SELECT BsUserServices.*,Servicesdetails.name FROM BsUserServices " +
                "LEFT JOIN " +
                "Servicesdetails ON " +
                "Servicesdetails.code=BsUserServices.ServiceDetaileCode  ORDER BY StartDate DESC";
        coursors = db.rawQuery(query, null);
        for (int i = 0; i < coursors.getCount(); i++) {
            coursors.moveToNext();
            if ((coursors.getString(coursors.getColumnIndex("IsEmergency")).compareTo("0") == 1)) {
                CountService++;
            } else {
                String DateTimeStr = ChangeDate.changeFarsiToMiladi(coursors.getString(coursors.getColumnIndex("StartDate"))) + " " + coursors.getString(coursors.getColumnIndex("StartTime")) + ":00";
                String GetDateTime = "Select Cast ((JulianDay('" + faToEn(DateTimeStr.replace("/", "-")) + "') - JulianDay('now'))" +
                        " * 24 As Integer) time";
                Ctime = db.rawQuery(GetDateTime, null);
                if (Ctime.getCount() > 0) {
                    Ctime.moveToNext();
                    int STime = Integer.parseInt(Ctime.getString(Ctime.getColumnIndex("time")));
                    if (STime > 24) {
                        CountService++;
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }
        }
        db.close();
        btnServices.setText(String.valueOf(CountService));
    }
}
