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

public class ServiceGetNewJobNotNotifi extends Service {
    Handler mHandler;
    private Thread thread;
    private Runnable runnable;
    boolean continue_or_stop = true;
    //boolean createthread=true;
    private DatabaseHelper dbh;
    SQLiteDatabase dbRW,dbR,db_Write;
    private String hamyarcode;
    private String guid;
//    private static final String ACTION_STOP = "com.besparina.it.hamyar.ServiceGetLocation.ACTION_STOP";

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
        if(PublicVariable.stopthread_GetNewJobNotNotifi)
        {
            thread.interrupt();
        }
    }
    @Override
    public boolean stopService(Intent name) {
        if(PublicVariable.stopthread_GetNewJobNotNotifi)
        {
            thread.interrupt();
        }
        return super.stopService(name);
    }
//    public static void stop(Context context) {
//        context.sendStickyBroadcast(new Intent(ACTION_STOP));
//    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
//        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
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
//            db_Write.execSQL("UPDATE ActiceBackgroundService SET Service_GetNewJob='0'");
//        }
//        catch (Exception ex)
//        {
//            db_Write=dbh.getWritableDatabase();
//            db_Write.execSQL("UPDATE ActiceBackgroundService SET Service_GetNewJob='0'");
//        }
//        PublicVariable.Active_Service_GetNewJob=false;
        if(Check_Login()) {
            continue_or_stop = true;
            if (PublicVariable.createthread_GetNewJobNotNotifi) {
                mHandler = new Handler();
                runnable=new Runnable() {
                    @Override
                    public void run() {
                        while (continue_or_stop) {
                            try {
                                Log.d("ServiceNew Not Notifi", "Run");
                                Thread.sleep(6000); // every 60 seconds
                                mHandler.post(new Runnable() {

                                    public String LastHamyarUserServiceCode;

                                    @Override
                                    public void run() {
                                        if (PublicVariable.theard_GetNewJob) {
                                            if (dbR != null) {
                                                if (dbR.isOpen()) {
                                                    try {	if (dbR.isOpen()) {	dbR.close();	}}	catch (Exception ex){	}
                                                }
                                            }
                                            dbR = dbh.getReadableDatabase();
                                            dbRW = dbh.getWritableDatabase();
                                            Cursor coursors = dbR.rawQuery("SELECT * FROM login", null);
                                            for (int i = 0; i < coursors.getCount(); i++) {

                                                coursors.moveToNext();
                                                guid = coursors.getString(coursors.getColumnIndex("guid"));
                                                hamyarcode = coursors.getString(coursors.getColumnIndex("hamyarcode"));
                                            }

                                            Cursor cursors = dbR.rawQuery("SELECT ifnull(MAX(CAST (code AS INT)),0)as code FROM BsUserServices", null);
                                            if (cursors.getCount() > 0) {
                                                cursors.moveToNext();
                                                LastHamyarUserServiceCode = cursors.getString(cursors.getColumnIndex("code"));
                                            }

                                            if (dbR != null) {
                                                if (dbR.isOpen()) {
                                                    try {	if (dbR.isOpen()) {	dbR.close();	}}	catch (Exception ex){	}
                                                }
                                            }
                                            SyncNewJob syncNewJob = new SyncNewJob(getApplicationContext(), guid, hamyarcode, LastHamyarUserServiceCode, false,dbh,dbR,dbRW);
                                            syncNewJob.AsyncExecute();
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                //Toast.makeText(getApplicationContext(),"Error Stop Service NewService Not Notifi",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                };
                thread=new Thread(runnable);
                if(PublicVariable.stopthread_GetNewJobNotNotifi)
                {
                    thread.interrupt();
                }
                else {
                    thread.start();
                }
                PublicVariable.createthread_GetNewJobNotNotifi = false;
            }
        }
        return START_STICKY;
    }

    public boolean Check_Login()
    {
        Cursor cursor;
        if(dbR==null)
        {
            dbR = dbh.getReadableDatabase();
        }
        if(!dbR.isOpen()) {
            dbR = dbh.getReadableDatabase();
        }
        cursor = dbR.rawQuery("SELECT * FROM login", null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            String Result = cursor.getString(cursor.getColumnIndex("islogin"));
            if (Result.compareTo("0") == 0)
            {
                if(dbR.isOpen())
                    try {	if (dbR.isOpen()) {	dbR.close();	}}	catch (Exception ex){	}
                return false;
            }
            else
            {
                if(dbR.isOpen())
                    try {	if (dbR.isOpen()) {	dbR.close();	}}	catch (Exception ex){	}
                return true;
            }
        }
        else
        {
            if(dbR.isOpen())
                try {	if (dbR.isOpen()) {	dbR.close();	}}	catch (Exception ex){	}
            return false;
        }
    }
}
