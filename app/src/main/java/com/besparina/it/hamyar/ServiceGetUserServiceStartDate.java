package com.besparina.it.hamyar;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by hashemi on 02/18/2018.
 */

public class ServiceGetUserServiceStartDate extends Service {
    Handler mHandler;
    boolean continue_or_stop = true;
    boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db,db_Write;
    private String pUserServiceCode;
//    private static final String ACTION_STOP = "com.besparina.it.hamyar.ServiceGetUserServiceStartDate.ACTION_STOP";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
//    private final BroadcastReceiver stopReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            context.removeStickyBroadcast(intent);
//            stopForeground(true);
//            stopSelf();
//        }
//    };
//    @Override
//    public void onCreate() {
//        super.onCreate();
////        startForeground(1,new Intent(this, ServiceDeleteJob.class));
//        registerReceiver(stopReceiver, new IntentFilter(ACTION_STOP));
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(stopReceiver);
//        PublicVariable.Active_Service_GetUserServiceStartDate=true;
        continue_or_stop=false;
    }
//
//    public static void stop(Context context) {
//        context.sendStickyBroadcast(new Intent(ACTION_STOP));
//    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
//        akeText(this, "Service Started", Toast.LENGTH_LONG).show();

//        PublicVariable.Active_Service_GetUserServiceStartDate=false;
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
//        try
//        {
//            if(!db_Write.isOpen())
//            {
//                db_Write=dbh.getWritableDatabase();
//            }
//            db_Write.execSQL("UPDATE ActiceBackgroundService SET Service_GetUserServiceStartDate='0'");
//        }
//        catch (Exception ex)
//        {
//            db_Write=dbh.getWritableDatabase();
//            db_Write.execSQL("UPDATE ActiceBackgroundService SET Service_GetUserServiceStartDate='0'");
//        }
//        PublicVariable.Active_Service_GetUserServiceStartDate=false;
        if(Check_Login()) {
            continue_or_stop = true;
            if (createthread) {
                mHandler = new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        while (continue_or_stop) {
                            try {
                                Log.d("Service Start Date", "Run");
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (PublicVariable.theard_GetUserServiceStartDate) {
                                            if(!db.isOpen()) {
                                                db = dbh.getReadableDatabase();
                                            }
                                            Cursor coursors = db.rawQuery("SELECT * FROM BsHamyarSelectServices A WHERE A.Status='1' AND " +
                                                    "A.Code NOT IN (SELECT BsUserServiceCode FROM StartDateService)", null);
                                            for (int i = 0; i < coursors.getCount(); i++) {
                                                coursors.moveToNext();

                                                pUserServiceCode = coursors.getString(coursors.getColumnIndex("Code"));
                                                SyncGetUserServiceStartDate syncGetUserServiceStartDate = new SyncGetUserServiceStartDate(getApplicationContext(), pUserServiceCode,dbh,db);
                                                syncGetUserServiceStartDate.AsyncExecute();
                                            }
                                            if(db.isOpen()) {
                                                db.close();
                                            }
                                        }
                                    }
                                });
                                Thread.sleep(6000); // every 6 seconds
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(),"Error Stop Service Start Service",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }).start();
                createthread = false;
            }
        }
        return START_STICKY;
    }

    public boolean Check_Login()
    {
        Cursor cursor;
        if(db==null)
        {
            db = dbh.getReadableDatabase();
        }
        if(!db.isOpen()) {
            db = dbh.getReadableDatabase();
        }
        cursor = db.rawQuery("SELECT * FROM login", null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            String Result = cursor.getString(cursor.getColumnIndex("islogin"));
            if (Result.compareTo("0") == 0)
            {
                if(db.isOpen())
                    db.close();
                return false;
            }
            else
            {
                if(db.isOpen())
                    db.close();
                return true;
            }
        }
        else
        {
            if(db.isOpen())
                db.close();
            return false;
        }
    }
}
