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

import com.besparina.it.hamyar.Date.ChangeDate;

import java.io.IOException;

/**
 * Created by hashemi on 02/18/2018.
 */

public class ServiceGetLocation extends Service {
    Handler mHandler;
    private Thread thread;
    private Runnable runnable;
    boolean continue_or_stop = true;
    //boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db,db_Write;
    private String hamyarcode;
    private String guid;
    private double latitude;
    private double longitude;
    private Cursor c;
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
public boolean stopService(Intent name) {
    if(PublicVariable.stopthread_Service_GetLocation)
    {
        thread.interrupt();
    }
    return super.stopService(name);
}
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(PublicVariable.stopthread_Service_GetLocation)
        {
            thread.interrupt();
        }
    }

//    public static void stop(Context context) {
//        context.sendStickyBroadcast(new Intent(ACTION_STOP));
//    }
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
//        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
//        PublicVariable.Active_Service_GetLocation=false;
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
//            db_Write.execSQL("UPDATE ActiceBackgroundService SET Service_GetLocation='0'");
//        }
//        catch (Exception ex)
//        {
//            db_Write=dbh.getWritableDatabase();
//            db_Write.execSQL("UPDATE ActiceBackgroundService SET Service_GetLocation='0'");
//        }
//        PublicVariable.Active_Service_GetLocation=false;
        if(Check_Login()) {
            continue_or_stop = true;
            if (PublicVariable.createthread_GetLocation) {
                mHandler = new Handler();
                runnable=new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        while (continue_or_stop) {
                            try {
                                Log.d("Service Location", "Run");
                                Thread.sleep(180000); // every 30 Minute
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (PublicVariable.theard_GetLocation) {
                                            if (db != null) {
                                                if (db.isOpen()) {
                                                    db.close();
                                                }
                                            }
                                            GPSTracker gps = new GPSTracker(getApplicationContext());

                                            // check if GPS enabled
                                            if (gps.canGetLocation()) {
                                                db = dbh.getReadableDatabase();
                                                Cursor coursors = db.rawQuery("SELECT * FROM Profile", null);
                                                if (coursors.getCount() > 0) {
                                                    c = db.rawQuery("SELECT * FROM login", null);
                                                    if (c.getCount() > 0) {
                                                        c.moveToNext();
                                                        guid = c.getString(c.getColumnIndex("guid"));
                                                        hamyarcode = c.getString(c.getColumnIndex("hamyarcode"));
                                                        latitude = gps.getLatitude();
                                                        longitude = gps.getLongitude();
                                                        String query = "UPDATE Profile SET Lat='" + Double.toString(latitude) + "',Lon='" + Double.toString(longitude) + "'";
                                                        try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
                                                        db.execSQL(query);
                                                        String[] DateSp = ChangeDate.getCurrentDate().split("/");
                                                        String Yearobj = DateSp[0];
                                                        String Monthobj = DateSp[1];
                                                        String Dayobj = DateSp[2];
                                                        String[] TimeSp = ChangeDate.getCurrentTime().split(":");
                                                        String Hourobj = TimeSp[0];
                                                        String Minuteobj = TimeSp[1];
                                                        SyncInsertHamyarLocation syncInsertHamyarLocation = new SyncInsertHamyarLocation(getApplicationContext(),
                                                                guid, hamyarcode, Yearobj, Monthobj, Dayobj, Hourobj, Minuteobj, Double.toString(latitude), Double.toString(longitude),dbh,db);
                                                        syncInsertHamyarLocation.AsyncExecute();
                                                    }
                                                }


                                                if (db != null) {
                                                    if (db.isOpen()) {
                                                        db.close();
                                                    }
                                                }
                                            }

                                        }
                                    }
                                });
                            } catch (Exception e) {
                                //Toast.makeText(getApplicationContext(),"Error Stop Service Location",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                };
                thread=new Thread(runnable);
                if(PublicVariable.stopthread_Service_GetLocation)
                {
                    thread.interrupt();
                }
                else {
                    thread.start();
                }
                PublicVariable.createthread_GetLocation = false;
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
