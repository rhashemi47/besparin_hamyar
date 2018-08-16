package com.besparina.it.hamyar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.io.IOException;
import java.util.Calendar;

/**
 * Created by hashemi on 01/23/2018.
 */

public class ViewJob extends Activity{
    private String hamyarcode;
    private String guid;
    private String BsUserServicesID;
    private String tab;
    private String status;
    private TextView ContentShowJob;
    private	DatabaseHelper dbh;
    private SQLiteDatabase db;
    private Button btnCansel;
    private Button btnPause;
    private Button btnPerFactor;
    private Button btnVisit;
    private Button btnSelect;
    private Button btnResume;
    private Button btnFinal;
    private Cursor coursors;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewjob);
        ContentShowJob=(TextView)findViewById(R.id.ContentShowJob);
        btnSelect=(Button)findViewById(R.id.btnSelect);
        btnCansel=(Button)findViewById(R.id.btnCansel);
        btnPause=(Button)findViewById(R.id.btnPause);
        btnPerFactor=(Button)findViewById(R.id.btnPerFactor);
        btnVisit=(Button)findViewById(R.id.btnVisit);
        btnResume=(Button)findViewById(R.id.btnResume);
        btnFinal=(Button)findViewById(R.id.btnFinal);

        try
        {
            hamyarcode = getIntent().getStringExtra("hamyarcode").toString();
            guid = getIntent().getStringExtra("guid").toString();
            BsUserServicesID = getIntent().getStringExtra("BsUserServicesID").toString();
            tab = getIntent().getStringExtra("tab").toString();
        }
        catch (Exception e)
        {
            //todo
        }
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
        if(tab.compareTo("1")==0)
        {
            String query="SELECT BsUserServices.*,Servicesdetails.name FROM BsUserServices " +
                    "LEFT JOIN " +
                    "Servicesdetails ON " +
                    "Servicesdetails.code=BsUserServices.ServiceDetaileCode AND BsUserServices.id="+BsUserServicesID;
            coursors = db.rawQuery(query,null);
            for(int i=0;i<coursors.getCount();i++){
                coursors.moveToNext();
                ContentShowJob.setText("موضوع: "+coursors.getString(coursors.getColumnIndex("name"))+"\n"
                        +"نام صاحبکار: "+coursors.getString(coursors.getColumnIndex("UserName"))+
                        " "+coursors.getString(coursors.getColumnIndex("UserFamily"))+"\n"+
                        "تاریخ حضور: "+coursors.getString(coursors.getColumnIndex("StartDate"))+"\n"+
                        "تاریخ ثبت بازدید: "+coursors.getString(coursors.getColumnIndex("VisitDate"))+"\n"+
                        "آدرس: "+coursors.getString(coursors.getColumnIndex("AddressText"))+"\n"+
                        "وضعیت: "+((coursors.getString(coursors.getColumnIndex("IsEmergency")).compareTo("0")==1? "عادی":"فوری")));
            }
        }
        else {
            String query = "SELECT BsHamyarSelectServices.*,Servicesdetails.name FROM BsHamyarSelectServices " +
                    "LEFT JOIN " +
                    "Servicesdetails ON " +
                    "Servicesdetails.code=BsHamyarSelectServices.ServiceDetaileCode AND BsHamyarSelectServices.id=" + BsUserServicesID;
            coursors = db.rawQuery(query, null);
            for (int i = 0; i < coursors.getCount(); i++) {
                coursors.moveToNext();
                ContentShowJob.setText("موضوع: "+coursors.getString(coursors.getColumnIndex("name"))+"\n"
                        +"نام صاحبکار: "+coursors.getString(coursors.getColumnIndex("UserName"))+
                        " "+coursors.getString(coursors.getColumnIndex("UserFamily"))+"\n"+
                        "تاریخ حضور: "+coursors.getString(coursors.getColumnIndex("StartDate"))+"\n"+
                        "تاریخ ثبت بازدید: "+coursors.getString(coursors.getColumnIndex("VisitDate"))+"\n"+
                        "آدرس: "+coursors.getString(coursors.getColumnIndex("AddressText"))+"\n"+
                        "وضعیت: "+((coursors.getString(coursors.getColumnIndex("IsEmergency")).compareTo("0")==1? "عادی":"فوری")));
                status=coursors.getString(coursors.getColumnIndex("Status"));
            }
        }
        if(tab.compareTo("0")==0)//status 0 is check-status 1 is select- 2 is pause - 3 is resume - 4 is cansel - 5 is visit - 6 is perfactor-7 is Final
        {
            if(status.compareTo("0")==0)//check
            {
                btnSelect.setEnabled(true);
                btnPerFactor.setEnabled(true);
                btnVisit.setEnabled(true);
                btnCansel.setEnabled(false);
                btnPause.setEnabled(false);
                btnResume.setEnabled(false);
                btnFinal.setEnabled(false);
                //**********************************************************************
                btnSelect.setBackgroundResource(R.drawable.rounded_button_select);
                btnPerFactor.setBackgroundResource(R.drawable.rounded_button_visit);
                btnPause.setBackgroundResource(R.drawable.rounded_disable_button);
                btnResume.setBackgroundResource(R.drawable.rounded_disable_button);
                btnFinal.setBackgroundResource(R.drawable.rounded_disable_button);
                btnCansel.setBackgroundResource(R.drawable.rounded_disable_button);
            }
            else if(status.compareTo("1")==0)//select
            {
                btnCansel.setEnabled(true);
                btnPause.setEnabled(true);
                btnPerFactor.setEnabled(true);
                btnVisit.setEnabled(true);
                btnFinal.setEnabled(true);
                btnSelect.setEnabled(false);
                btnResume.setEnabled(false);
                //**********************************************************************
                btnCansel.setBackgroundResource(R.drawable.rounded_button_cansel);
                btnPause.setBackgroundResource(R.drawable.rounded_button_pause);
                btnPerFactor.setBackgroundResource(R.drawable.rounded_button_visit);
                btnVisit.setBackgroundResource(R.drawable.rounded_button_visit);
                btnFinal.setBackgroundResource(R.drawable.rounded_button_final);
                //***********************************************************************
                btnSelect.setBackgroundResource(R.drawable.rounded_disable_button);
                btnResume.setBackgroundResource(R.drawable.rounded_disable_button);
            }
            else if(status.compareTo("2")==0)//Pause
            {
                btnResume.setEnabled(true);
                btnCansel.setEnabled(true);
                btnPause.setEnabled(false);
                btnSelect.setEnabled(false);
                btnPerFactor.setEnabled(false);
                btnVisit.setEnabled(false);
                btnFinal.setEnabled(false);
                //**********************************************************************
                btnResume.setBackgroundResource(R.drawable.rounded_button_pause);
                btnCansel.setBackgroundResource(R.drawable.rounded_button_cansel);
                //**********************************************************************
                btnSelect.setBackgroundResource(R.drawable.rounded_disable_button);
                btnPause.setBackgroundResource(R.drawable.rounded_disable_button);
                btnPerFactor.setBackgroundResource(R.drawable.rounded_disable_button);
                btnVisit.setBackgroundResource(R.drawable.rounded_disable_button);
                btnFinal.setBackgroundResource(R.drawable.rounded_disable_button);
            }
            else if(status.compareTo("3")==0)//resume
            {
                btnVisit.setEnabled(true);
                btnFinal.setEnabled(true);
                btnCansel.setEnabled(true);
                btnPause.setEnabled(true);
                btnSelect.setEnabled(false);
                btnResume.setEnabled(false);
                btnPerFactor.setEnabled(false);
                //**********************************************************************
                btnCansel.setBackgroundResource(R.drawable.rounded_button_cansel);
                btnPause.setBackgroundResource(R.drawable.rounded_button_pause);
                btnVisit.setBackgroundResource(R.drawable.rounded_button_visit);
                btnFinal.setBackgroundResource(R.drawable.rounded_button_final);
                //**********************************************************************
                btnSelect.setBackgroundResource(R.drawable.rounded_disable_button);
                btnResume.setBackgroundResource(R.drawable.rounded_disable_button);
                btnPerFactor.setBackgroundResource(R.drawable.rounded_disable_button);
            }
            else if(status.compareTo("4")==0)//cansel
            {
                btnResume.setEnabled(false);
                btnCansel.setEnabled(false);
                btnPause.setEnabled(false);
                btnSelect.setEnabled(false);
                btnPerFactor.setEnabled(false);
                btnVisit.setEnabled(false);
                btnFinal.setEnabled(false);
                //**********************************************************************

                btnResume.setBackgroundResource(R.drawable.rounded_disable_button);
                btnCansel.setBackgroundResource(R.drawable.rounded_disable_button);
                btnPause.setBackgroundResource(R.drawable.rounded_disable_button);
                btnSelect.setBackgroundResource(R.drawable.rounded_disable_button);
                btnPerFactor.setBackgroundResource(R.drawable.rounded_disable_button);
                btnVisit.setBackgroundResource(R.drawable.rounded_disable_button);
                btnFinal.setBackgroundResource(R.drawable.rounded_disable_button);
            }
            else if(status.compareTo("5")==0)//visit
            {
                btnPerFactor.setEnabled(true);
                btnCansel.setEnabled(true);
                btnFinal.setEnabled(true);
                btnResume.setEnabled(false);
                btnPause.setEnabled(false);
                btnSelect.setEnabled(false);
                btnVisit.setEnabled(true);
                //**********************************************************************
                btnCansel.setBackgroundResource(R.drawable.rounded_button_cansel);
                btnPerFactor.setBackgroundResource(R.drawable.rounded_button_visit);
                btnFinal.setBackgroundResource(R.drawable.rounded_button_final);
                //**********************************************************************
                btnResume.setBackgroundResource(R.drawable.rounded_disable_button);
                btnPause.setBackgroundResource(R.drawable.rounded_disable_button);
                btnSelect.setBackgroundResource(R.drawable.rounded_disable_button);
                btnVisit.setBackgroundResource(R.drawable.rounded_button_visit);
            }
            else if(status.compareTo("6")==0)//perfactor
            {
                btnSelect.setEnabled(true);
                btnVisit.setEnabled(true);
                btnCansel.setEnabled(true);
                btnResume.setEnabled(false);
                btnPause.setEnabled(false);
                btnPerFactor.setEnabled(false);
                btnFinal.setEnabled(false);
                //**********************************************************************
                btnSelect.setBackgroundResource(R.drawable.rounded_button_select);
                btnVisit.setBackgroundResource(R.drawable.rounded_button_pause);
                btnCansel.setBackgroundResource(R.drawable.rounded_button_cansel);
                //**********************************************************************
                btnFinal.setBackgroundResource(R.drawable.rounded_disable_button);
                btnSelect.setBackgroundResource(R.drawable.rounded_disable_button);
                btnVisit.setBackgroundResource(R.drawable.rounded_disable_button);
                btnPerFactor.setBackgroundResource(R.drawable.rounded_disable_button);
            }
            else if(status.compareTo("7")==0)//final
            {
                btnResume.setEnabled(false);
                btnCansel.setEnabled(false);
                btnPause.setEnabled(false);
                btnSelect.setEnabled(false);
                btnPerFactor.setEnabled(false);
                btnVisit.setEnabled(false);
                btnFinal.setEnabled(false);
                //**********************************************************************
                btnResume.setBackgroundResource(R.drawable.rounded_disable_button);
                btnCansel.setBackgroundResource(R.drawable.rounded_disable_button);
                btnPause.setBackgroundResource(R.drawable.rounded_disable_button);
                btnSelect.setBackgroundResource(R.drawable.rounded_disable_button);
                btnPerFactor.setBackgroundResource(R.drawable.rounded_disable_button);
                btnVisit.setBackgroundResource(R.drawable.rounded_disable_button);
                btnFinal.setBackgroundResource(R.drawable.rounded_disable_button);
            }
        }
        else
        {
            btnSelect.setEnabled(true);
            btnPerFactor.setEnabled(false);
            btnVisit.setEnabled(false);
            btnCansel.setEnabled(false);
            btnPause.setEnabled(false);
            btnResume.setEnabled(false);
            btnFinal.setEnabled(false);
            //**********************************************************************
            btnSelect.setBackgroundResource(R.drawable.rounded_button_select);
            //**********************************************************************
            btnResume.setBackgroundResource(R.drawable.rounded_disable_button);
            btnCansel.setBackgroundResource(R.drawable.rounded_disable_button);
            btnPause.setBackgroundResource(R.drawable.rounded_disable_button);
            btnPerFactor.setBackgroundResource(R.drawable.rounded_disable_button);
            btnVisit.setBackgroundResource(R.drawable.rounded_disable_button);
            btnFinal.setBackgroundResource(R.drawable.rounded_disable_button);
        }
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SyncSelecteJob syncSelecteJob=new SyncSelecteJob(ViewJob.this,guid,hamyarcode,coursors.getString(coursors.getColumnIndex("Code")));
                syncSelecteJob.AsyncExecute();
            }
        });
        btnCansel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SyncCanselJob syncCanselJob=new SyncCanselJob(ViewJob.this,guid,hamyarcode,coursors.getString(coursors.getColumnIndex("Code")),
                        coursors.getString(coursors.getColumnIndex("id")));
                syncCanselJob.AsyncExecute();
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SyncPauseJob syncPauseJob=new SyncPauseJob(ViewJob.this,guid,hamyarcode,coursors.getString(coursors.getColumnIndex("Code")),
                        coursors.getString(coursors.getColumnIndex("id")));
                syncPauseJob.AsyncExecute();
            }
        });
        btnVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                PersianCalendar now = new PersianCalendar();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                             @Override
                             public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                        db=dbh.getWritableDatabase();
                                String query="UPDATE  DateTB SET Date = '" +String.valueOf(year)+"/"+String.valueOf(monthOfYear)+"/"+String.valueOf(dayOfMonth)+"'";
                                 db.execSQL(query);
                                GetTime();
                             }
                         }, now.getPersianYear(),
                        now.getPersianMonth(),
                        now.getPersianDay());
                datePickerDialog.setThemeDark(true);
                datePickerDialog.show(getFragmentManager(), "tpd");

            }
        });
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SyncResumeJob syncResumeJob=new SyncResumeJob(ViewJob.this,guid,hamyarcode,coursors.getString(coursors.getColumnIndex("Code")),
                        coursors.getString(coursors.getColumnIndex("id")));
                syncResumeJob.AsyncExecute();
            }
        });
        btnFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SyncFinalJob syncFinalJob=new SyncFinalJob(ViewJob.this,guid,hamyarcode,coursors.getString(coursors.getColumnIndex("Code")),
                        coursors.getString(coursors.getColumnIndex("id")));
                syncFinalJob.AsyncExecute();
            }
        });
        btnPerFactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String query = "SELECT BsHamyarSelectServices.*,Servicesdetails.name FROM BsHamyarSelectServices " +
                        "LEFT JOIN " +
                        "Servicesdetails ON " +
                        "Servicesdetails.code=BsHamyarSelectServices.ServiceDetaileCode AND BsHamyarSelectServices.id=" + BsUserServicesID;
                coursors = db.rawQuery(query, null);
                if (coursors.getCount()>0) {
                    coursors.moveToNext();
                    LoadActivity_PerFactor(Save_Per_Factor.class,"tab",tab,"BsUserServicesID",coursors.getString(coursors.getColumnIndex("Code")),"ServiceDetaileCode",coursors.getString(coursors.getColumnIndex("ServiceDetaileCode")));
                }
            }
        });
    }
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            ViewJob.this.LoadActivity(MainActivity.class, "guid", guid, "hamyarcode", hamyarcode);
        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
    {
        Intent intent = new Intent(getApplicationContext(),Cls);
        intent.putExtra(VariableName, VariableValue);
        intent.putExtra(VariableName2, VariableValue2);
        ViewJob.this.startActivity(intent);
    }
    public void LoadActivity_PerFactor(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2, String VariableName3, String VariableValue3)
    {
        Intent intent = new Intent(getApplicationContext(),Cls);
        intent.putExtra(VariableName, VariableValue);
        intent.putExtra(VariableName2, VariableValue2);
        intent.putExtra(VariableName3, VariableValue3);
        ViewJob.this.startActivity(intent);
    }
    public void GetTime()
    {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ViewJob.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String AM_PM;
                if (selectedHour >=0 && selectedHour < 12){
                    AM_PM = "AM";
                } else {
                    AM_PM = "PM";
                }
                 db=dbh.getWritableDatabase();
                String query="UPDATE  DateTB SET Time = '" +String.valueOf(selectedHour)+":"+String.valueOf(selectedMinute)+"'";
                db.execSQL(query);
                db=dbh.getReadableDatabase();
                query="SELECT * FROM DateTB";
                Cursor c=db.rawQuery(query,null);
                if(c.getCount()>0)
                {
                    c.moveToNext();
                    String[] DateTB = c.getString(c.getColumnIndex("Date")).split("/");
                    String[] TimeTB = c.getString(c.getColumnIndex("Time")).split(":");
                    SyncVisitJob syncVisitJob = new SyncVisitJob(ViewJob.this, guid, hamyarcode, coursors.getString(coursors.getColumnIndex("Code")), DateTB[0], DateTB[1], DateTB[2], TimeTB[0], TimeTB[1]);
                    syncVisitJob.AsyncExecute();
                }
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }
}
