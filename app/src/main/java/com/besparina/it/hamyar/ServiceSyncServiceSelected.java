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

import java.io.IOException;

/**
 * Created by hashemi on 02/18/2018.
 */

public class ServiceSyncServiceSelected extends Service {
    Handler mHandler;
    boolean continue_or_stop = true;
    boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db,db_Write;
    private String guid;
    private String hamyarcode;

    private static final String ACTION_STOP = "com.besparina.it.hamyar.ServiceSyncServiceSelected.ACTION_STOP";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    private final BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            context.removeStickyBroadcast(intent);
            stopForeground(true);
            stopSelf();
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
//        startForeground(1,new Intent(this, ServiceDeleteJob.class));
        registerReceiver(stopReceiver, new IntentFilter(ACTION_STOP));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(stopReceiver);
        PublicVariable.Active_Service_ServiceSelected=true;
        continue_or_stop=false;
    }

    public static void stop(Context context) {
        context.sendStickyBroadcast(new Intent(ACTION_STOP));
    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
//        keText(this, "Service Started", Toast.LENGTH_LONG).show();
        PublicVariable.Active_Service_ServiceSelected=false;
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
//        try {
//            if (!db_Write.isOpen()) {
//                db_Write = dbh.getWritableDatabase();
//            }
//            db_Write.execSQL("UPDATE ActiceBackgroundService SET Service_ServiceSelected='0'");
//        } catch (Exception ex) {
//            db_Write = dbh.getWritableDatabase();
//            db_Write.execSQL("UPDATE ActiceBackgroundService SET Service_ServiceSelected='0'");
//        }
        PublicVariable.Active_Service_ServiceSelected = false;
        if(Check_Login()) {
            continue_or_stop = true;
            if (createthread) {
                mHandler = new Handler();
                new Thread(new Runnable() {
                    //                public String LastHamyarUserServiceCode;
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        while (continue_or_stop) {
                            try {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (PublicVariable.theard_ServiceSelected) {
                                            if (db != null) {
                                                if (db.isOpen()) {
                                                    db.close();
                                                }
                                            }
                                            db = dbh.getReadableDatabase();
                                            Cursor coursors = db.rawQuery("SELECT * FROM login", null);
                                            for (int i = 0; i < coursors.getCount(); i++) {
                                                coursors.moveToNext();

                                                hamyarcode = coursors.getString(coursors.getColumnIndex("hamyarcode"));
                                                guid = coursors.getString(coursors.getColumnIndex("guid"));
                                            }
                                            db.close();
//                                    try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
//                                    Cursor cursors = db.rawQuery("SELECT * FROM BsHamyarSelectServices WHERE IsDelete='0'", null);
//                                    if(cursors.getCount()>0)
//                                    {
//                                        cursors.moveToNext();
//                                        LastHamyarUserServiceCode=cursors.getString(cursors.getColumnIndex("code"));
//                                    }
//
//                                    if(db!=null) {
//                                        if (db.isOpen()) {
//                                            db.close();
//                                        }
//                                    }
                                            SyncGetSelectJobsForService syncGetSelectJobsForService = new SyncGetSelectJobsForService(getApplicationContext(), guid, hamyarcode, "0",dbh,db);
                                            syncGetSelectJobsForService.AsyncExecute();
                                        }
                                    }
                                });
                                Thread.sleep(6000); // every 60 seconds
                            } catch (Exception e) {
                                // TODO: handle exception
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
