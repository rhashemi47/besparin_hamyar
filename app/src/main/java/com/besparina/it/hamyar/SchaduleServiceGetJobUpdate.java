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
public class SchaduleServiceGetJobUpdate extends JobService {
    Handler mHandler;
    boolean continue_or_stop = true;
    boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db,db_Write;
    private String hamyarcode;
    private String guid;
    private  Cursor coursors;
    private  Cursor cursors;


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
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
        try
        {
            if(!db_Write.isOpen())
            {
                db_Write=dbh.getWritableDatabase();
            }
            db_Write.execSQL("UPDATE ActiceBackgroundService SET Service_GetJobUpdate='0'");
        }
        catch (Exception ex)
        {
            db_Write=dbh.getWritableDatabase();
            db_Write.execSQL("UPDATE ActiceBackgroundService SET Service_GetJobUpdate='0'");
        }
        PublicVariable.Active_Service_GetJobUpdate=false;
        if(Check_Login()) {
            continue_or_stop = true;
            if (createthread) {
                mHandler = new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (continue_or_stop) {
                            try {
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
