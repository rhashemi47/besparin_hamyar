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
public class SchaduleServiceSyncServiceSelected extends JobService {
    Handler mHandler;
    boolean continue_or_stop = true;
    boolean createthread = true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db, db_Write;
    private String guid;
    private String hamyarcode;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        // Let it continue running until it is stopped.
//        keText(this, "Service Started", Toast.LENGTH_LONG).show();
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
        try {
            if (!db_Write.isOpen()) {
                db_Write = dbh.getWritableDatabase();
            }
            db_Write.execSQL("UPDATE ActiceBackgroundService SET Service_ServiceSelected='0'");
        } catch (Exception ex) {
            db_Write = dbh.getWritableDatabase();
            db_Write.execSQL("UPDATE ActiceBackgroundService SET Service_ServiceSelected='0'");
        }
        PublicVariable.Active_Service_ServiceSelected = false;
        if (Check_Login()) {
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
                                            SyncGetSelectJobsForService syncGetSelectJobsForService = new SyncGetSelectJobsForService(getApplicationContext(), guid, hamyarcode, "0", dbh, db);
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
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        continue_or_stop = false;
        return false;
    }

    public boolean Check_Login() {
        Cursor cursor;
        if (db == null) {
            db = dbh.getReadableDatabase();
        }
        if (!db.isOpen()) {
            db = dbh.getReadableDatabase();
        }
        cursor = db.rawQuery("SELECT * FROM login", null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            String Result = cursor.getString(cursor.getColumnIndex("islogin"));
            if (Result.compareTo("0") == 0) {
                if (db.isOpen())
                    db.close();
                return false;
            } else {
                if (db.isOpen())
                    db.close();
                return true;
            }
        } else {
            if (db.isOpen())
                db.close();
            return false;
        }
    }
}
