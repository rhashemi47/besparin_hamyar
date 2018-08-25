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

public class ServiceSyncServiceSelected extends Service {
    Handler mHandler;
    boolean continue_or_stop = true;
    boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private String guid;
    private String hamyarcode;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
//        keText(this, "Service Started", Toast.LENGTH_LONG).show();
        continue_or_stop=true;
        if(createthread) {
            mHandler = new Handler();
            new Thread(new Runnable() {
                public String LastHamyarUserServiceCode;
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    while (continue_or_stop) {
                        try {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    dbh=new DatabaseHelper(getApplicationContext());
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
                                    db=dbh.getReadableDatabase();
                                    Cursor coursors = db.rawQuery("SELECT * FROM login",null);
                                    for(int i=0;i<coursors.getCount();i++){
                                        coursors.moveToNext();

                                        hamyarcode=coursors.getString(coursors.getColumnIndex("hamyarcode"));
                                        guid=coursors.getString(coursors.getColumnIndex("guid"));
                                    }
                                    db.close();
                                    db=dbh.getReadableDatabase();
                                    Cursor cursors = db.rawQuery("SELECT ifnull(MAX(CAST (code AS INT)),0)as code FROM BsHamyarSelectServices", null);
                                    if(cursors.getCount()>0)
                                    {
                                        cursors.moveToNext();
                                        LastHamyarUserServiceCode=cursors.getString(cursors.getColumnIndex("code"));
                                    }

                                    db.close();
                                    SyncGetSelectJobsForService syncGetSelectJobsForService =new SyncGetSelectJobsForService(getApplicationContext(),guid,hamyarcode,"0");
                                    syncGetSelectJobsForService.AsyncExecute();
                                }
                            });
                            Thread.sleep(60000); // every 60 seconds
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
       // keText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        continue_or_stop=false;
    }
}
