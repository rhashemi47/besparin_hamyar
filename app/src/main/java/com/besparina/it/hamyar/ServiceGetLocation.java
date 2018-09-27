package com.besparina.it.hamyar;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;

import com.besparina.it.hamyar.Date.ChangeDate;

import java.io.IOException;

/**
 * Created by hashemi on 02/18/2018.
 */

public class ServiceGetLocation extends Service {
    Handler mHandler;
    boolean continue_or_stop = true;
    boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private String hamyarcode;
    private String guid;
    private double latitude;
    private double longitude;
    private Cursor c;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
//        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        continue_or_stop=true;
        if(createthread) {
            mHandler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    while (continue_or_stop) {
                        try {
                            Thread.sleep(180000); // every 30 Minute
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (PublicVariable.theard_GetLocation) {
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
                                                    db = dbh.getWritableDatabase();
                                                    db.execSQL(query);
                                                    String[] DateSp = ChangeDate.getCurrentDate().split("/");
                                                    String Yearobj = DateSp[0];
                                                    String Monthobj = DateSp[1];
                                                    String Dayobj = DateSp[2];
                                                    String[] TimeSp = ChangeDate.getCurrentTime().split(":");
                                                    String Hourobj = TimeSp[0];
                                                    String Minuteobj = TimeSp[1];
                                                    SyncInsertHamyarLocation syncInsertHamyarLocation = new SyncInsertHamyarLocation(getApplicationContext(),
                                                            guid, hamyarcode, Yearobj, Monthobj, Dayobj, Hourobj, Minuteobj, Double.toString(latitude), Double.toString(longitude));
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
                            // TODO: handle exception
                        }
                    }
                }
            }).start();
            createthread=false;
        }
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
       // Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        continue_or_stop=false;
    }
}
