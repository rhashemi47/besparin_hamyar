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

import java.io.IOException;

/**
 * Created by hashemi on 02/18/2018.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SchaduleServiceGetSliderPic extends JobService {
    Handler mHandler;
    boolean continue_or_stop = true;
    boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db,db_Write;
    private String hamyarcode="0";
    private String guid="0";

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
            db_Write.execSQL("UPDATE ActiceBackgroundService SET Service_GetSliderPic='0'");
        }
        catch (Exception ex)
        {
            db_Write=dbh.getWritableDatabase();
            db_Write.execSQL("UPDATE ActiceBackgroundService SET Service_GetSliderPic='0'");
        }
        PublicVariable.Active_Service_GetSliderPic=false;
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
                                String error = "";
                                error = e.getMessage().toString();
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
