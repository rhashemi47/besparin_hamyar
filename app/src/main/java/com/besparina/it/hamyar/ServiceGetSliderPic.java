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

public class ServiceGetSliderPic extends Service {
    Handler mHandler;
    boolean continue_or_stop = true;
    boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db,db_Write;
    private String hamyarcode="0";
    private String guid="0";
//    private static final String ACTION_STOP = "com.besparina.it.hamyar.ServiceGetSliderPic.ACTION_STOP";

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
//        PublicVariable.Active_Service_GetSliderPic=true;
        continue_or_stop=false;
    }
//
//    public static void stop(Context context) {
//        context.sendStickyBroadcast(new Intent(ACTION_STOP));
//    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
//        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
//        PublicVariable.Active_Service_GetSliderPic=false;

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
//            db_Write.execSQL("UPDATE ActiceBackgroundService SET Service_GetSliderPic='0'");
//        }
//        catch (Exception ex)
//        {
//            db_Write=dbh.getWritableDatabase();
//            db_Write.execSQL("UPDATE ActiceBackgroundService SET Service_GetSliderPic='0'");
//        }
//        PublicVariable.Active_Service_GetSliderPic=false;
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
                                Log.d("Service Slider", "Run");
                                mHandler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        if (PublicVariable.theard_GetSliderPic) {
                                            if (db != null) {
                                                if (db.isOpen()) {
                                                    db.close();
                                                }
                                            }
                                            db = dbh.getReadableDatabase();
                                            Cursor coursors = db.rawQuery("SELECT * FROM login", null);
                                            for (int i = 0; i < coursors.getCount(); i++) {
                                                coursors.moveToNext();
                                                guid = coursors.getString(coursors.getColumnIndex("guid"));
                                                hamyarcode = coursors.getString(coursors.getColumnIndex("hamyarcode"));
                                            }
                                            coursors.close();
                                            if (guid.compareTo("0") != 0 && hamyarcode.compareTo("0") != 0) {
                                                SyncSliderPic syncSliderPic = new SyncSliderPic(getApplicationContext(), guid, hamyarcode,dbh,db);
                                                syncSliderPic.AsyncExecute();
                                            }

                                            if (db != null) {
                                                if (db.isOpen()) {
                                                    db.close();
                                                }
                                            }


                                        }
                                    }
                                });
                                if (db != null) {
                                    if (db.isOpen()) {
                                        db.close();
                                    }
                                }
                                db = dbh.getReadableDatabase();
                                Cursor cursor = db.rawQuery("SELECT * FROM Slider", null);
                                if (cursor.getCount() > 0) {
                                    Thread.sleep(43200000); // every 12 hour
                                } else {
                                    Thread.sleep(6000); // every 12 hour
                                }


                                if (db != null) {
                                    if (db.isOpen()) {
                                        db.close();
                                    }
                                }
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(),"Error Stop Service Slider",Toast.LENGTH_LONG).show();
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
