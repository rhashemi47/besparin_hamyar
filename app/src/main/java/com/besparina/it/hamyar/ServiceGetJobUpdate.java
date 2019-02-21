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

public class ServiceGetJobUpdate extends Service {
    Handler mHandler;
    private Thread thread;
    private Runnable runnable;
    boolean continue_or_stop = true;
    //boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db,db_Write;
    private String hamyarcode;
    private String guid;
    private  Cursor coursors;
    private  Cursor cursors;
//    private static final String ACTION_STOP = "com.besparina.it.hamyar.ServiceGetJobUpdate.ACTION_STOP";

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
        if(PublicVariable.stopthread_Service_GetJobUpdate)
        {
            thread.interrupt();
        }
    }
    @Override
    public boolean stopService(Intent name) {
        if(PublicVariable.stopthread_Service_GetJobUpdate)
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

//        PublicVariable.Active_Service_GetJobUpdate=false;
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
//            db_Write.execSQL("UPDATE ActiceBackgroundService SET Service_GetJobUpdate='0'");
//        }
//        catch (Exception ex)
//        {
//            db_Write=dbh.getWritableDatabase();
//            db_Write.execSQL("UPDATE ActiceBackgroundService SET Service_GetJobUpdate='0'");
//        }
//        PublicVariable.Active_Service_GetJobUpdate=false;
        if(Check_Login()) {
            continue_or_stop = true;
            if (PublicVariable.createthread_GetJobUpdate) {
                mHandler = new Handler();
                runnable=new Runnable() {
                    @Override
                    public void run() {
                        while (continue_or_stop) {
                            try {
                                Log.d("ServiceUpdate", "Run");
                                Thread.sleep(6000); // every 6 seconds
                                mHandler.post(new Runnable() {

                                    public String ListServiceCode = "";

                                    @Override
                                    public void run() {
                                        if (PublicVariable.theard_GetJobUpdate) {
                                            if (db != null) {
                                                if (db.isOpen()) {
                                                    db.close();
                                                }
                                            }
                                            db = dbh.getReadableDatabase();
                                            coursors = db.rawQuery("SELECT * FROM login", null);
                                            for (int i = 0; i < coursors.getCount(); i++) {

                                                coursors.moveToNext();
                                                guid = coursors.getString(coursors.getColumnIndex("guid"));
                                                hamyarcode = coursors.getString(coursors.getColumnIndex("hamyarcode"));
                                            }
                                            if (!coursors.isClosed())
                                                coursors.close();
                                            cursors = db.rawQuery("SELECT Code FROM BsUserServices", null);
                                            for (int i = 0; i < cursors.getCount(); i++) {
                                                cursors.moveToNext();
                                                if (i == 0) {
                                                    ListServiceCode = cursors.getString(cursors.getColumnIndex("Code"));
                                                } else {
                                                    ListServiceCode = ListServiceCode + "," + cursors.getString(cursors.getColumnIndex("Code"));
                                                }

                                            }
                                            if (!cursors.isClosed())
                                                cursors.close();

                                            if (db != null) {
                                                if (db.isOpen()) {
                                                    db.close();
                                                }
                                            }
                                            if (!ListServiceCode.isEmpty()) {
                                                SyncGetUserServiceForHamyarUpdated syncGetUserServiceForHamyarUpdated = new SyncGetUserServiceForHamyarUpdated(getApplicationContext(), guid, hamyarcode, ListServiceCode,dbh,db);
                                                syncGetUserServiceForHamyarUpdated.AsyncExecute();
                                            }
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                //Toast.makeText(getApplicationContext(),"Error Stop Service Update",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                };
                thread=new Thread(runnable);
                if(PublicVariable.stopthread_Service_GetJobUpdate)
                {
                    thread.interrupt();
                }
                else {
                    thread.start();
                }
                PublicVariable.createthread_GetJobUpdate = false;
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
