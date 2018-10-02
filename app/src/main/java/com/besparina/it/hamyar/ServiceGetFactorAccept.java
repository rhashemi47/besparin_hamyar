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

public class ServiceGetFactorAccept extends Service {
    Handler mHandler;
    boolean continue_or_stop = true;
    boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private String hamyarcode;
    private String guid;
    private Cursor cursors;
    private Cursor coursors;
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
                            Thread.sleep(6000); // every 6 seconds
                            mHandler.post(new Runnable() {

                                @Override
                                public void run() {
                                    if (PublicVariable.theard_GetFactorAccept) {
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
                                        db = dbh.getReadableDatabase();
                                        coursors = db.rawQuery("SELECT * FROM login", null);
                                        for (int i = 0; i < coursors.getCount(); i++) {

                                            coursors.moveToNext();
                                            guid = coursors.getString(coursors.getColumnIndex("guid"));
                                            hamyarcode = coursors.getString(coursors.getColumnIndex("hamyarcode"));
                                        }
                                        if(!coursors.isClosed())
                                        {
                                            coursors.close();
                                        }
                                        if (db != null) {
                                            if (db.isOpen()) {
                                                db.close();
                                            }
                                        }
                                        db = dbh.getReadableDatabase();
                                        cursors = db.rawQuery("SELECT Code FROM BsHamyarSelectServices WHERE IsDelete='0'", null);
                                        for (int i = 0; i < cursors.getCount(); i++) {
                                            cursors.moveToNext();
                                            if (!db.isOpen()) {
                                                db = dbh.getReadableDatabase();
                                            }
                                            c = db.rawQuery("SELECT * FROM HeadFactor WHERE UserServiceCode='" + cursors.getString(cursors.getColumnIndex("Code")) + "' ORDER BY CAST(Code AS INTEGER) DESC", null);
                                            if (c.getCount() > 0) {
                                                c.moveToNext();
                                                String Code=c.getString(c.getColumnIndex("Code"));
                                                String TypeSTR=c.getString(c.getColumnIndex("Type"));
                                                if (TypeSTR.compareTo("1") == 0) {
                                                    SyncGetPreInvoiceAccept syncGetPreInvoiceAccept = new SyncGetPreInvoiceAccept(getApplicationContext(), guid, hamyarcode, c.getString(c.getColumnIndex("Code")), cursors.getString(cursors.getColumnIndex("Code")));
                                                    syncGetPreInvoiceAccept.AsyncExecute();
                                                    c.close();
                                                } else {
                                                    SyncGetInvoiceAccept syncGetInvoiceAccept = new SyncGetInvoiceAccept(getApplicationContext(), guid, hamyarcode, c.getString(c.getColumnIndex("Code")), cursors.getString(cursors.getColumnIndex("Code")));
                                                    syncGetInvoiceAccept.AsyncExecute();
                                                    c.close();
                                                }
                                            }
                                            if(!c.isClosed())
                                            {
                                                c.close();
                                                c=null;
                                            }
                                        }

                                        if (db != null) {
                                            if (db.isOpen()) {
                                                db.close();
                                            }
                                        }
                                        if (!cursors.isClosed()) {
                                            cursors.close();
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
