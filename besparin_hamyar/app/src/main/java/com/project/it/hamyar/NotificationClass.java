package com.project.it.hamyar;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import java.io.IOException;

public class NotificationClass {
        private String hamyarcode;
        private String guid;
        private DatabaseHelper dbh;
        private SQLiteDatabase db;
	public void Notificationm(Context context, String Title, String Detils,int notificationID,Class<?> Cls){
            dbh=new DatabaseHelper(context);
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
                    guid=coursors.getString(coursors.getColumnIndex("guid"));
                    hamyarcode=coursors.getString(coursors.getColumnIndex("hamyarcode"));
            }
            long[] v = {500,1000};
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setSmallIcon(R.drawable.jobs);
            mBuilder.setContentTitle(Title);
            mBuilder.setContentText(Detils);
            mBuilder.setVibrate(v);
            mBuilder.setSound(uri);
            mBuilder.setAutoCancel(true);
            Intent intent = new Intent(context, Cls);
            intent.putExtra("guid", guid);
            intent.putExtra("hamyarcode", hamyarcode);
            PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);
            mBuilder.setContentIntent(pIntent);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // notificationID allows you to update the notification later on.
            mNotificationManager.notify(notificationID, mBuilder.build());
    }
}
