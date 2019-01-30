package com.besparina.it.hamyar;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;

import com.besparina.it.hamyar.Date.ChangeDate;

import java.io.IOException;

/**
 * Created by hashemi on 02/18/2018.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SchaduleServiceGetLocation extends JobService {
    Handler mHandler;
    boolean continue_or_stop = true;
    boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db,db_Write;
    private String hamyarcode;
    private String guid;
    private double latitude;
    private double longitude;
    private Cursor c;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {    // Let it continue running until it is stopped.
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
        try
        {
            if(!db_Write.isOpen())
            {
                db_Write=dbh.getWritableDatabase();
            }
            db_Write.execSQL("UPDATE ActiceBackgroundService SET Service_GetLocation='0'");
        }
        catch (Exception ex)
        {
            db_Write=dbh.getWritableDatabase();
            db_Write.execSQL("UPDATE ActiceBackgroundService SET Service_GetLocation='0'");
        }
        PublicVariable.Active_Service_GetLocation=false;
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
                                // TODO: handle exception
                            }
                        }
                    }
                }).start();
                createthread = false;
            }
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        continue_or_stop=false;
        return true;
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
                return true;
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
            return true;
        }
    }
}
