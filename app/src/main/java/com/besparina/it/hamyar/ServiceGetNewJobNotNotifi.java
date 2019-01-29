package com.besparina.it.hamyar;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;

import java.io.IOException;

/**
 * Created by hashemi on 02/18/2018.
 */

public class ServiceGetNewJobNotNotifi extends Service {
    Handler mHandler;
    boolean continue_or_stop = true;
    boolean createthread=true;
    private DatabaseHelper dbh;
    SQLiteDatabase dbRW,dbR;
    private String hamyarcode;
    private String guid;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
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
                                Thread.sleep(60000); // every 60 seconds
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
                                // TODO: handle exception
                            }
                        }
                    }
                }).start();
                createthread = false;
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        continue_or_stop=false;
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
