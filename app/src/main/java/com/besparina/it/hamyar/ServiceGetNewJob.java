package com.besparina.it.hamyar;

import android.app.Service;
import android.content.Intent;
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

public class ServiceGetNewJob extends Service {
    Handler mHandler;
    boolean continue_or_stop = true;
    boolean createthread=true;
    private DatabaseHelper dbh;
    SQLiteDatabase dbRW,dbR,db_Write;
    private String hamyarcode;
    private String guid;
//    private static final String ACTION_STOP = "com.besparina.it.hamyar.ServiceGetNewJob.ACTION_STOP";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //continue_or_stop=false;
    }

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
        if(Check_Login()) {
            continue_or_stop = true;
            if (createthread) {
                mHandler = new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (continue_or_stop) {
                            try {
                                Log.d("ServiceNew", "Run");
                                Thread.sleep(6000); // every 60 seconds
                                mHandler.post(new Runnable() {

//                                public String LastHamyarUserServiceCode;

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
//                                        Cursor cursors = dbR.rawQuery("SELECT ifnull(MAX(CAST (code AS INT)),0)as code FROM BsUserServices", null);
//                                        if (cursors.getCount() > 0) {
//                                            cursors.moveToNext();
//                                            LastHamyarUserServiceCode = cursors.getString(cursors.getColumnIndex("code"));
//                                        }
                                            SyncNewJob syncNewJob = new SyncNewJob(getApplicationContext(), guid, hamyarcode, "0", true,dbh,dbR,dbRW);
                                            syncNewJob.AsyncExecute();//Get Allways All Free Services
                                            if (dbR.isOpen()) {
                                                try {	if (dbR.isOpen()) {	dbR.close();	}}	catch (Exception ex){	}
                                            }
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(),"Error Stop Service New Service",Toast.LENGTH_LONG).show();
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
