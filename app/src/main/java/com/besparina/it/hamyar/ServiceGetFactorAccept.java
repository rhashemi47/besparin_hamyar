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

import java.io.IOException;

/**
 * Created by hashemi on 02/18/2018.
 */

public class ServiceGetFactorAccept extends Service {
    Handler mHandler;
    boolean continue_or_stop = true;
    boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db,db_Write;
    private String hamyarcode;
    private String guid;
    private Cursor cursors;
    private Cursor coursors;
    private Cursor c;
    private static final String ACTION_STOP = "com.besparina.it.hamyar.ServiceGetFactorAccept.ACTION_STOP";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    private final BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            context.removeStickyBroadcast(intent);
            stopForeground(true);
            stopSelf();
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
//        startForeground(1,new Intent(this, ServiceDeleteJob.class));
        registerReceiver(stopReceiver, new IntentFilter(ACTION_STOP));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(stopReceiver);
        PublicVariable.Active_Service_GetFactorAccept=true;
        continue_or_stop=false;
    }

    public static void stop(Context context) {
        context.sendStickyBroadcast(new Intent(ACTION_STOP));
    }
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
//        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        PublicVariable.Active_Service_GetFactorAccept=false;
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
//            db_Write.execSQL("UPDATE ActiceBackgroundService SET Service_GetFactorAccept='0'");
//        }
//        catch (Exception ex)
//        {
//            db_Write=dbh.getWritableDatabase();
//            db_Write.execSQL("UPDATE ActiceBackgroundService SET Service_GetFactorAccept='0'");
//        }
        PublicVariable.Active_Service_GetFactorAccept=false;
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
                                Thread.sleep(6000); // every 6 seconds
                                mHandler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        if (PublicVariable.theard_GetFactorAccept) {

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
                                            if (!coursors.isClosed()) {
                                                coursors.close();
                                            }
                                            if (db != null) {
                                                if (db.isOpen()) {
                                                    db.close();
                                                }
                                            }
                                            if(!db.isOpen()){
                                                try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
                                            }
                                            cursors = db.rawQuery("SELECT Code FROM BsHamyarSelectServices WHERE IsDelete='0'", null);
                                            for (int i = 0; i < cursors.getCount(); i++) {
                                                cursors.moveToNext();
                                                if (!db.isOpen()) {
                                                    db = dbh.getReadableDatabase();
                                                }
                                                c = db.rawQuery("SELECT * FROM HeadFactor WHERE UserServiceCode='" + cursors.getString(cursors.getColumnIndex("Code")) + "' ORDER BY CAST(Code AS INTEGER) DESC", null);
                                                if (c.getCount() > 0) {
                                                    c.moveToNext();
                                                    String Code = c.getString(c.getColumnIndex("Code"));
                                                    String TypeSTR = c.getString(c.getColumnIndex("Type"));
                                                    if (TypeSTR.compareTo("1") == 0) {
                                                        SyncGetPreInvoiceAccept syncGetPreInvoiceAccept = new SyncGetPreInvoiceAccept(getApplicationContext(), guid, hamyarcode, c.getString(c.getColumnIndex("Code")), cursors.getString(cursors.getColumnIndex("Code")),dbh,db);
                                                        syncGetPreInvoiceAccept.AsyncExecute();
                                                        c.close();
                                                    } else {
                                                        SyncGetInvoiceAccept syncGetInvoiceAccept = new SyncGetInvoiceAccept(getApplicationContext(), guid, hamyarcode, c.getString(c.getColumnIndex("Code")), cursors.getString(cursors.getColumnIndex("Code")),dbh,db);
                                                        syncGetInvoiceAccept.AsyncExecute();
                                                        c.close();
                                                    }
                                                }
                                                if (!c.isClosed()) {
                                                    c.close();
                                                    c = null;
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
                createthread = false;
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
