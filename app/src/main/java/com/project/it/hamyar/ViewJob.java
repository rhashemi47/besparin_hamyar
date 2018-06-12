package com.project.it.hamyar;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.io.IOException;
import java.util.Calendar;

import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by hashemi on 01/23/2018.
 */

public class ViewJob extends AppCompatActivity{
    private String hamyarcode;
    private String guid;
    private Drawer drawer=null;
    private String countMessage;
    private String countVisit;
    private boolean IsActive;
    private String BsUserServicesID;
    private String tab;
    private String status;
    private String latStr="0";
    private String lonStr="0";
    private String DateStr="";
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
    private Button btnCallToCustomer;
    private Cursor coursors;
    private Button btnCredit;
    private Button btnOrders;
    private Button btnHome;
    GoogleMap map;
    private ir.hamsaa.persiandatepicker.util.PersianCalendar initDate;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewjob);
        final Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");//set font for page
        btnCredit=(Button)findViewById(R.id.btnCredit);
        btnOrders=(Button)findViewById(R.id.btnOrders);
        btnHome=(Button)findViewById(R.id.btnHome);
        ContentShowJob=(TextView)findViewById(R.id.ContentShowJob);
        btnSelect=(Button)findViewById(R.id.btnSelect);
        btnCansel=(Button)findViewById(R.id.btnCansel);
        btnPause=(Button)findViewById(R.id.btnPause);
        btnPerFactor=(Button)findViewById(R.id.btnPerFactor);
        btnVisit=(Button)findViewById(R.id.btnVisit);
        btnResume=(Button)findViewById(R.id.btnResume);
        btnFinal=(Button)findViewById(R.id.btnFinal);
        btnCallToCustomer=(Button)findViewById(R.id.btnCallToCustomer);
        //********************************
        btnCredit.setTypeface(FontMitra);
        btnOrders.setTypeface(FontMitra);
        btnHome.setTypeface(FontMitra);
        ContentShowJob.setTypeface(FontMitra);
        btnSelect.setTypeface(FontMitra);
        btnCansel.setTypeface(FontMitra);
        btnPause.setTypeface(FontMitra);
        btnPerFactor.setTypeface(FontMitra);
        btnVisit.setTypeface(FontMitra);
        btnResume.setTypeface(FontMitra);
        btnFinal.setTypeface(FontMitra);
        btnCallToCustomer.setTypeface(FontMitra);

        try
        {
            hamyarcode = getIntent().getStringExtra("hamyarcode").toString();
            guid = getIntent().getStringExtra("guid").toString();
            BsUserServicesID = getIntent().getStringExtra("BsUserServicesID").toString();
            tab = getIntent().getStringExtra("tab").toString();
        }
        catch (Exception e)
        {
            tab ="1";
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
        //****************************************************************************************
        db=dbh.getReadableDatabase();
        Cursor coursor = db.rawQuery("SELECT * FROM messages WHERE IsReade='0' AND IsDelete='0'",null);
        if(coursor.getCount()>0)
        {
            countMessage=String.valueOf(coursor.getCount());
        }
        coursor = db.rawQuery("SELECT * FROM BsHamyarSelectServices WHERE Status='5' AND IsDelete='0'",null);
        if(coursor.getCount()>0)
        {
            countVisit=String.valueOf(coursor.getCount());
        }
        IsActive=PublicVariable.IsActive;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CreateMenu(toolbar);
        //***************************************************************************************************************************

        db=dbh.getReadableDatabase();
        if(tab.compareTo("1")==0)
        {
            String query="SELECT BsUserServices.*,Servicesdetails.name FROM BsUserServices " +
                    "LEFT JOIN " +
                    "Servicesdetails ON " +
                    "Servicesdetails.code=BsUserServices.ServiceDetaileCode WHERE BsUserServices.Code="+BsUserServicesID;
            coursors = db.rawQuery(query,null);
            for(int i=0;i<coursors.getCount();i++){
                coursors.moveToNext();
                latStr=coursors.getString(coursors.getColumnIndex("Lat"));
                lonStr=coursors.getString(coursors.getColumnIndex("Lng"));
                String Content="";
                try
                {
                    Content+="شماره درخواست: "+coursors.getString(coursors.getColumnIndex("Code"))+"\n";
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    Content+="سرویس درخواستی: "+coursors.getString(coursors.getColumnIndex("name"))+"\n";
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    Content+="نام متقاضی: "+coursors.getString(coursors.getColumnIndex("UserName"))+" "+coursors.getString(coursors.getColumnIndex("UserFamily"))+"\n";
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    Content+="تاریخ شروع: "+coursors.getString(coursors.getColumnIndex("StartDate"))+"\n";
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    Content+="تاریخ پایان: "+coursors.getString(coursors.getColumnIndex("EndDate"))+"\n";
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    Content+="از ساعت: "+coursors.getString(coursors.getColumnIndex("StartTime"))+"\n";
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    Content+="تا ساعت: "+coursors.getString(coursors.getColumnIndex("EndTime"))+"\n";
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    if(coursors.getString(coursors.getColumnIndex("PeriodicServices")).toString().compareTo("1")==0)
                    {
                        Content+="خدمت دوره ای: "+"روزانه"+"\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("PeriodicServices")).toString().compareTo("2")==0)
                    {
                        Content+="خدمت دوره ای: "+"هفتگی"+"\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("PeriodicServices")).toString().compareTo("3")==0)
                    {
                        Content+="خدمت دوره ای: "+"هفته در میان"+"\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("PeriodicServices")).toString().compareTo("4")==0)
                    {
                        Content+="خدمت دوره ای: "+"ماهانه"+"\n";
                    }

                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    if(coursors.getString(coursors.getColumnIndex("MaleCount")).toString().compareTo("0")!=0) {
                        Content += "تعداد همیار مرد: " + coursors.getString(coursors.getColumnIndex("MaleCount")) + "\n";
                    }
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    if(coursors.getString(coursors.getColumnIndex("FemaleCount")).toString().compareTo("0")!=0) {
                        Content += "تعداد همیار زن: " + coursors.getString(coursors.getColumnIndex("FemaleCount")) + "\n";
                    }
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    if(coursors.getString(coursors.getColumnIndex("HamyarCount")).toString().compareTo("0")!=0) {
                        Content += "تعداد همیار: " + coursors.getString(coursors.getColumnIndex("HamyarCount")) + "\n";
                    }
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    String EducationTitle=coursors.getString(coursors.getColumnIndex("EducationTitle"));
                    if(EducationTitle.compareTo("0")!=0) {
                        Content += "عنوان آموزش: " + coursors.getString(coursors.getColumnIndex("EducationTitle")) + "\n";
                    }
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    if(coursors.getString(coursors.getColumnIndex("EducationGrade")).toString().compareTo("0")!=0) {
                        Content += "پایه تحصیلی: " + coursors.getString(coursors.getColumnIndex("EducationGrade")) + "\n";
                    }
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    if(coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("1")==0) {
                        Content += "رشته تحصیلی: " + "ابتدایی" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("2")==0) {
                        Content += "رشته تحصیلی: " + "متوسطه اول" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("3")==0) {
                        Content += "رشته تحصیلی: " + "علوم تجربی" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("4")==0) {
                        Content += "رشته تحصیلی: " + "ریاضی و فیزیک" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("5")==0) {
                        Content += "رشته تحصیلی: " + "انسانی" + "\n";
                    }
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    if(coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("0")!=0)
                    {
                        if(coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("2")==0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        }
                        else if(coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("3")==0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        }
                        else if(coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("4")==0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        }
                        else if(coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("5")==0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        }
                        else if(coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("6")==0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        }
                        else if(coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("7")==0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        }
                        else if(coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("7")==0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        }
                        else
                        {
                            Content += "رشته هنری: " + coursors.getString(coursors.getColumnIndex("ArtField")) + "\n";
                        }
                    }
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    if(coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("1")==0) {
                        Content += "زبان: " + "انگلیسی" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("2")==0) {
                        Content += "زبان: " + "روسی" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("3")==0) {
                        Content += "زبان: " + "آلمانی" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("4")==0) {
                        Content += "زبان: " + "فرانسه" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("5")==0) {
                        Content += "زبان: " + "ترکی" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("6")==0) {
                        Content += "زبان: " + "عربی" + "\n";
                    }
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    if(coursors.getString(coursors.getColumnIndex("StudentGender")).toString().compareTo("1")==0) {
                        Content += "جنسیت دانش آموز: " + "زن" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("StudentGender")).toString().compareTo("2")==0) {
                        Content += "جنسیت دانش آموز: " + "مرد" + "\n";
                    }
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    if(coursors.getString(coursors.getColumnIndex("CarWashType")).toString().compareTo("1")==0) {
                        Content += "نوع سرویس: " + "روشویی" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("CarWashType")).toString().compareTo("2")==0) {
                        Content += "نوع سرویس: " + "روشویی و توشویی" + "\n";
                    }
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    if(coursors.getString(coursors.getColumnIndex("CarType")).toString().compareTo("1")==0) {
                        Content+="نوع خودرو: "+"سواری"+"\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("CarType")).toString().compareTo("2")==0) {
                        Content += "نوع سرویس: " + "شاسی و نیم شاسی" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("CarType")).toString().compareTo("3")==0) {
                        Content += "نوع سرویس: " + "ون" + "\n";
                    }

                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    Content+="توضیحات: "+coursors.getString(coursors.getColumnIndex("Description"))+"\n";
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    Content+="آدرس: "+coursors.getString(coursors.getColumnIndex("AddressText"))+"\n";
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    Content+="وضعیت: "+((coursors.getString(coursors.getColumnIndex("IsEmergency")).compareTo("0")==0? "عادی":"فوری"));
                }
                catch (Exception ex)
                {
                    //todo
                }
                ContentShowJob.setText(Content);
                ContentShowJob.setTypeface(FontMitra);
                ContentShowJob.setTextSize(18);
            }
        }
        else
            {
            String query = "SELECT BsHamyarSelectServices.*,Servicesdetails.name FROM BsHamyarSelectServices " +
                    "LEFT JOIN " +
                    "Servicesdetails ON " +
                    "Servicesdetails.code=BsHamyarSelectServices.ServiceDetaileCode WHERE BsHamyarSelectServices.Code=" + BsUserServicesID;
            coursors = db.rawQuery(query, null);
            for (int i = 0; i < coursors.getCount(); i++) {
                coursors.moveToNext();
                latStr=coursors.getString(coursors.getColumnIndex("Lat"));
                lonStr=coursors.getString(coursors.getColumnIndex("Lng"));
                String Content="";
                try
                {
                    Content+="شماره درخواست: "+coursors.getString(coursors.getColumnIndex("Code"))+"\n";
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    Content+="سرویس درخواستی: "+coursors.getString(coursors.getColumnIndex("name"))+"\n";
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    Content+="نام متقاضی: "+coursors.getString(coursors.getColumnIndex("UserName"))+" "+coursors.getString(coursors.getColumnIndex("UserFamily"))+"\n";
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    Content+="تاریخ شروع: "+coursors.getString(coursors.getColumnIndex("StartDate"))+"\n";
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    Content+="تاریخ پایان: "+coursors.getString(coursors.getColumnIndex("EndDate"))+"\n";
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    Content+="از ساعت: "+coursors.getString(coursors.getColumnIndex("StartTime"))+"\n";
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    Content+="تا ساعت: "+coursors.getString(coursors.getColumnIndex("EndTime"))+"\n";
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    if(coursors.getString(coursors.getColumnIndex("PeriodicServices")).toString().compareTo("1")==0)
                    {
                        Content+="خدمت دوره ای: "+"روزانه"+"\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("PeriodicServices")).toString().compareTo("2")==0)
                    {
                        Content+="خدمت دوره ای: "+"هفتگی"+"\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("PeriodicServices")).toString().compareTo("3")==0)
                    {
                        Content+="خدمت دوره ای: "+"هفته در میان"+"\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("PeriodicServices")).toString().compareTo("4")==0)
                    {
                        Content+="خدمت دوره ای: "+"ماهانه"+"\n";
                    }

                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    if(coursors.getString(coursors.getColumnIndex("MaleCount")).toString().compareTo("0")!=0) {
                        Content += "تعداد همیار مرد: " + coursors.getString(coursors.getColumnIndex("MaleCount")) + "\n";
                    }
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    if(coursors.getString(coursors.getColumnIndex("FemaleCount")).toString().compareTo("0")!=0) {
                        Content += "تعداد همیار زن: " + coursors.getString(coursors.getColumnIndex("FemaleCount")) + "\n";
                    }
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    if(coursors.getString(coursors.getColumnIndex("HamyarCount")).toString().compareTo("0")!=0) {
                        Content += "تعداد همیار: " + coursors.getString(coursors.getColumnIndex("HamyarCount")) + "\n";
                    }
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    String EducationTitle=coursors.getString(coursors.getColumnIndex("EducationTitle"));
                    if(EducationTitle.compareTo("0")!=0) {
                        Content += "عنوان آموزش: " + coursors.getString(coursors.getColumnIndex("EducationTitle")) + "\n";
                    }
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    if(coursors.getString(coursors.getColumnIndex("EducationGrade")).toString().compareTo("0")!=0) {
                        Content += "پایه تحصیلی: " + coursors.getString(coursors.getColumnIndex("EducationGrade")) + "\n";
                    }
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    if(coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("1")==0) {
                        Content += "رشته تحصیلی: " + "ابتدایی" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("2")==0) {
                        Content += "رشته تحصیلی: " + "متوسطه اول" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("3")==0) {
                        Content += "رشته تحصیلی: " + "علوم تجربی" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("4")==0) {
                        Content += "رشته تحصیلی: " + "ریاضی و فیزیک" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("5")==0) {
                        Content += "رشته تحصیلی: " + "انسانی" + "\n";
                    }
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    if(coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("0")!=0)
                    {
                        if(coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("2")==0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        }
                        else if(coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("3")==0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        }
                        else if(coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("4")==0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        }
                        else if(coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("5")==0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        }
                        else if(coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("6")==0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        }
                        else if(coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("7")==0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        }
                        else if(coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("7")==0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        }
                       else
                        {
                            Content += "رشته هنری: " + coursors.getString(coursors.getColumnIndex("ArtField")) + "\n";
                        }
                    }
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    if(coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("1")==0) {
                        Content += "زبان: " + "انگلیسی" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("2")==0) {
                        Content += "زبان: " + "روسی" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("3")==0) {
                        Content += "زبان: " + "آلمانی" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("4")==0) {
                        Content += "زبان: " + "فرانسه" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("5")==0) {
                        Content += "زبان: " + "ترکی" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("6")==0) {
                        Content += "زبان: " + "عربی" + "\n";
                    }
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    if(coursors.getString(coursors.getColumnIndex("StudentGender")).toString().compareTo("1")==0) {
                        Content += "جنسیت دانش آموز: " + "زن" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("StudentGender")).toString().compareTo("2")==0) {
                        Content += "جنسیت دانش آموز: " + "مرد" + "\n";
                    }
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    if(coursors.getString(coursors.getColumnIndex("CarWashType")).toString().compareTo("1")==0) {
                        Content += "نوع سرویس: " + "روشویی" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("CarWashType")).toString().compareTo("2")==0) {
                        Content += "نوع سرویس: " + "روشویی و توشویی" + "\n";
                    }
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    if(coursors.getString(coursors.getColumnIndex("CarType")).toString().compareTo("1")==0) {
                        Content+="نوع خودرو: "+"سواری"+"\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("CarType")).toString().compareTo("2")==0) {
                        Content += "نوع سرویس: " + "شاسی و نیم شاسی" + "\n";
                    }
                    else if(coursors.getString(coursors.getColumnIndex("CarType")).toString().compareTo("3")==0) {
                        Content += "نوع سرویس: " + "ون" + "\n";
                    }

                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    Content+="توضیحات: "+coursors.getString(coursors.getColumnIndex("Description"))+"\n";
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    Content+="آدرس: "+coursors.getString(coursors.getColumnIndex("AddressText"))+"\n";
                }
                catch (Exception ex)
                {
                    //todo
                }
                try
                {
                    Content+="وضعیت: "+((coursors.getString(coursors.getColumnIndex("IsEmergency")).compareTo("0")==0? "عادی":"فوری"));
                }
                catch (Exception ex)
                {
                    //todo
                }
                ContentShowJob.setText(Content);
                ContentShowJob.setTypeface(FontMitra);
                ContentShowJob.setTextSize(18);
                status=coursors.getString(coursors.getColumnIndex("Status"));
            }
            db.close();
        }
        //**************************************************************************************
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                LatLng point;
                if(latStr.length()>0 && lonStr.length()>0)
                {
                    double lat=Double.parseDouble(latStr);
                    double lon=Double.parseDouble(lonStr);
                    point = new LatLng(lat,lon);
                }
                else
                {
                    point = new LatLng(0, 0);
                }

                map.addMarker(new MarkerOptions().position(point).title("سرویس").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(point,17));


                map.getUiSettings().setZoomControlsEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            }
        });

//**************************************************************************************
        if(tab.compareTo("0")==0)//status 0 is check-status 1 is select- 2 is pause - 3 is pause - 4 is cansel - 5 is visit - 6 is perfactor-7 is Final
        {
            if(status.compareTo("0")==0 )//check
            {
                btnSelect.setEnabled(true);
                btnPerFactor.setEnabled(true);
                btnVisit.setEnabled(true);
                btnCansel.setEnabled(false);
                btnPause.setEnabled(false);
                btnResume.setEnabled(false);
                btnFinal.setEnabled(false);

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
            }
            else if(status.compareTo("3")==0)//pause
            {
                btnVisit.setEnabled(true);
                btnFinal.setEnabled(true);
                btnCansel.setEnabled(true);
                btnPause.setEnabled(true);
                btnSelect.setEnabled(false);
                btnResume.setEnabled(false);
                btnPerFactor.setEnabled(false);
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
            }
            else if(status.compareTo("7")==0)//final_job
            {
                btnResume.setEnabled(false);
                btnCansel.setEnabled(false);
                btnPause.setEnabled(false);
                btnSelect.setEnabled(false);
                btnPerFactor.setEnabled(false);
                btnVisit.setEnabled(false);
                btnFinal.setEnabled(false);
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
                LayoutInflater li = LayoutInflater.from(ViewJob.this);
                View promptsView = li.inflate(R.layout.cansel, null);
                AlertDialog.Builder alertbox = new AlertDialog.Builder(ViewJob.this);
                //set view
                alertbox.setView(promptsView);
                final EditText descriptionCansel = (EditText) promptsView.findViewById(R.id.etCansel);

                alertbox
                        .setCancelable(true)
                        .setPositiveButton("بله",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        SyncCanselJob syncCanselJob=new SyncCanselJob(ViewJob.this,guid,hamyarcode,coursors.getString(coursors.getColumnIndex("Code")),
                                        coursors.getString(coursors.getColumnIndex("id")),descriptionCansel.getText().toString());
                                        syncCanselJob.AsyncExecute();
                                    }
                                })
                        .setNegativeButton("خیر",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertbox.create();

                // show it
                alertDialog.show();

            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LayoutInflater li = LayoutInflater.from(ViewJob.this);
                View promptsView = li.inflate(R.layout.cansel, null);
                AlertDialog.Builder alertbox = new AlertDialog.Builder(ViewJob.this);
                //set view
                alertbox.setView(promptsView);
                final EditText descriptionCansel = (EditText) promptsView.findViewById(R.id.etCansel);

                alertbox
                        .setCancelable(true)
                        .setPositiveButton("بله",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        SyncPauseJob syncPauseJob=new SyncPauseJob(ViewJob.this,guid,hamyarcode,coursors.getString(coursors.getColumnIndex("Code")),
                                                coursors.getString(coursors.getColumnIndex("id")));
                                        syncPauseJob.AsyncExecute();
                                    }
                                })
                        .setNegativeButton("خیر",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertbox.create();

                // show it
                alertDialog.show();
            }
        });
        btnVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                PersianCalendar now = new PersianCalendar();
//                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
//                        new DatePickerDialog.OnDateSetListener() {
//                             @Override
//                             public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//                                        db=dbh.getWritableDatabase();
//                                String query="UPDATE  DateTB SET Date = '" +String.valueOf(year)+"/"+String.valueOf(monthOfYear+1)+"/"+String.valueOf(dayOfMonth)+"'";
//                                 db.execSQL(query);
//
//                                 db.close();
//                                GetTime();
//                             }
//                         }, now.getPersianYear(),
//                        now.getPersianMonth(),
//                        now.getPersianDay());
//                datePickerDialog.setThemeDark(false);
//                datePickerDialog.show(getFragmentManager(), "tpd");
                date_picker();
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
                        "Servicesdetails.code=BsHamyarSelectServices.ServiceDetaileCode AND BsHamyarSelectServices.Code=" + BsUserServicesID;
                db=dbh.getReadableDatabase();
                coursors = db.rawQuery(query, null);
                if (coursors.getCount()>0) {
                    coursors.moveToNext();
                    LoadActivity_PerFactor(Save_Per_Factor.class,"tab",tab,"BsUserServicesID",BsUserServicesID,"ServiceDetaileCode",coursors.getString(coursors.getColumnIndex("ServiceDetaileCode")));
                }

                db.close();
            }
        });
        btnCallToCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(ViewJob.this,
                        android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(ViewJob.this, android.Manifest.permission.CALL_PHONE))
                    {
                        //do nothing
                    }
                    else{

                        ActivityCompat.requestPermissions(ViewJob.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
                    }

                }
                db = dbh.getReadableDatabase();
                Cursor cursorPhone;
                if(tab.compareTo("0")==0){
                     cursorPhone = db.rawQuery("SELECT * FROM BsHamyarSelectServices WHERE Code='"+BsUserServicesID+"'", null);
                }
                else {
                     cursorPhone = db.rawQuery("SELECT * FROM BsUserServices WHERE Code='"+BsUserServicesID+"'", null);
                }

                if (cursorPhone.getCount() > 0) {
                    cursorPhone.moveToNext();
                    dialContactPhone(cursorPhone.getString(cursorPhone.getColumnIndex("UserPhone")));
                }
                db.close();
            }
        });

        btnCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadActivity(Credit.class, "guid",  guid, "hamyarcode", hamyarcode);
            }
        });
        btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadActivity(History.class, "guid", guid, "hamyarcode", hamyarcode);
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void CreateMenu(Toolbar toolbar){
        Bitmap bmp= BitmapFactory.decodeResource(getResources(),R.drawable.useravatar);
        String name="";
        String family="";
        String status="";
        db = dbh.getReadableDatabase();
        Cursor coursors = db.rawQuery("SELECT * FROM Profile", null);
        if (coursors.getCount() > 0) {
            coursors.moveToNext();
            try
            {
                if(coursors.getString(coursors.getColumnIndex("Status")).compareTo("null")!=0){
                    status = coursors.getString(coursors.getColumnIndex("Status"));
                    if(status.compareTo("0")==0)
                    {
                        status="غیرفعال";
                    }
                    else
                    {
                        status="فعال";
                    }
                }
                else
                {
                    status = "غیرفعال";
                }

            }
            catch (Exception ex){
                status = "غیرفعال";
            }
            try
            {
                if(coursors.getString(coursors.getColumnIndex("Name")).compareTo("null")!=0){
                    name = coursors.getString(coursors.getColumnIndex("Name"));
                }
                else
                {
                    name = "کاربر";
                }

            }
            catch (Exception ex){
                name = "کاربر";
            }
            try
            {
                if(coursors.getString(coursors.getColumnIndex("Fam")).compareTo("null")!=0){
                    family = coursors.getString(coursors.getColumnIndex("Fam"));
                }
                else
                {
                    family = "مهمان";
                }

            }
            catch (Exception ex){
                family = "مهمان";
            }
            try
            {
                if(coursors.getString(coursors.getColumnIndex("Pic")).compareTo("null")!=0){
                    bmp = convertToBitmap(coursors.getString(coursors.getColumnIndex("Pic")));
                }
                else
                {
                    bmp = BitmapFactory.decodeResource(getResources(), R.drawable.useravatar);
                }

            }
            catch (Exception ex){
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.useravatar);
            }
        }
        else
        {
            name = "کاربر";
            family = "مهمان";
        }

        int drawerGravity= Gravity.END;
        Configuration config = getResources().getConfiguration();
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            drawerGravity= Gravity.START;
        }


        String HeaderStr=name+" "+family+" - "+"وضعیت: "+status;
        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.menu_header)
                .addProfiles(
                        new ProfileDrawerItem().withName(HeaderStr).withIcon(bmp)// withIcon(getResources().getDrawable(R.drawable.personpic))
                ).withSelectionListEnabledForSingleProfile(false)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();
        drawer= new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withDrawerGravity(drawerGravity)
                .withShowDrawerOnFirstLaunch(true)
                .addDrawerItems(
                        new SecondaryDrawerItem().withName(R.string.Profile).withIcon(R.drawable.profile).withSelectable(true),
                        new SecondaryDrawerItem().withName(R.string.ListVisit).withIcon(R.drawable.job).withBadge(countVisit).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withSelectable(false).withEnabled(IsActive),
                        // new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
                        new SecondaryDrawerItem().withName(R.string.Yourcommitment).withIcon(R.drawable.yourcommitment).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.Ourcommitment).withIcon(R.drawable.ourcommitment).withSelectable(false),
                        //new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
                        new SecondaryDrawerItem().withName(R.string.Messages).withIcon(R.drawable.messages).withBadge(countMessage).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withSelectable(false).withEnabled(IsActive),
                        new SecondaryDrawerItem().withName(R.string.GiftBank).withIcon(R.drawable.gift).withSelectable(false).withEnabled(IsActive),
                        new SecondaryDrawerItem().withName(R.string.Invite_friends).withIcon(R.drawable.about).withSelectable(false).withEnabled(IsActive),
                        new SecondaryDrawerItem().withName(R.string.Contact).withIcon(R.drawable.contact).withSelectable(false),
                        //new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
                        new SecondaryDrawerItem().withName(R.string.action_settings).withIcon(R.drawable.setting).withSelectable(false).withEnabled(IsActive),
                        new SecondaryDrawerItem().withName(R.string.Help).withIcon(R.drawable.help).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.About).withIcon(R.drawable.about).withSelectable(false),
                        //new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
                        //new SecondaryDrawerItem().withName(R.string.Exit).withIcon(R.drawable.exit).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.Logout).withIcon(R.drawable.logout).withSelectable(false)
                ).addStickyDrawerItems(new PrimaryDrawerItem().withName(R.string.RelateUs).withSelectable(false).withEnabled(false),
                        new PrimaryDrawerItem().withName(R.string.telegram).withIcon(R.drawable.telegram).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.instagram).withIcon(R.drawable.instagram).withSelectable(false))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position){
                            case 1://Profile
                                db=dbh.getReadableDatabase();
                                Cursor coursors = db.rawQuery("SELECT * FROM Profile",null);
                                if(coursors.getCount()>0)
                                {
                                    coursors.moveToNext();
                                    String Status_check=coursors.getString(coursors.getColumnIndex("Status"));
                                    if(Status_check.compareTo("0")==0)
                                    {
                                        Cursor c = db.rawQuery("SELECT * FROM login",null);
                                        if(c.getCount()>0)
                                        {
                                            c.moveToNext();
                                            SyncProfile profile = new SyncProfile(ViewJob.this, c.getString(c.getColumnIndex("guid")), c.getString(c.getColumnIndex("hamyarcode")));
                                            profile.AsyncExecute();
                                        }
                                    }
                                    else
                                    {
                                        LoadActivity(Profile.class, "guid", guid,"hamyarcode",hamyarcode);
                                    }
                                }
                                else
                                {
                                    Toast.makeText(ViewJob.this, "برای استفاده از امکانات بسپارینا باید ثبت نام کنید", Toast.LENGTH_LONG).show();
                                    LoadActivity(Login.class,"guid",guid,"hamyarcode",hamyarcode);
                                }

                                db.close();
                                break;
                            case 2:
                                db = dbh.getReadableDatabase();
                                Cursor c = db.rawQuery("SELECT * FROM login",null);
                                if(c.getCount()>0) {
                                    c.moveToNext();
                                    LoadActivity(List_Visits.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
                                }
                                db.close();
                                break;
                            case 3:
//                                db = dbh.getReadableDatabase();
//                                c = db.rawQuery("SELECT * FROM login",null);
//                                if(c.getCount()>0) {
//                                    c.moveToNext();
//
//                                    LoadActivity(YourCommitment.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
//                                }
//                                db.close();
                                openWebPage("http://besparina.ir");
                                break;
                            case 4:
//                                db = dbh.getReadableDatabase();
//                                c = db.rawQuery("SELECT * FROM login",null);
//                                if(c.getCount()>0) {
//                                    c.moveToNext();
//
//                                    LoadActivity(OurCommitment.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
//                                }
//                                db.close();
                                openWebPage("http://besparina.ir");
                                break;
                            case 5:
                                db = dbh.getReadableDatabase();
                                c = db.rawQuery("SELECT * FROM login",null);
                                if(c.getCount()>0) {
                                    c.moveToNext();

                                    LoadActivity(List_Messages.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
                                }
                                db.close();
                                break;
                            case 6:
                                db = dbh.getReadableDatabase();
                                c = db.rawQuery("SELECT * FROM login",null);
                                if(c.getCount()>0) {
                                    c.moveToNext();

                                    LoadActivity(GiftBank.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
                                }
                                db.close();
                                break;
                            case 7:
                                db = dbh.getReadableDatabase();
                                c = db.rawQuery("SELECT * FROM Profile", null);
                                if (c.getCount() > 0) {
                                    c.moveToNext();
                                    sharecode(c.getString(c.getColumnIndex("HamyarCodeForReagent")));
                                    // LoadActivity(GiftBank.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
                                }
                                db.close();
                                break;
                            case 8:
                                db = dbh.getReadableDatabase();
                                c = db.rawQuery("SELECT * FROM login",null);
                                if(c.getCount()>0) {
                                    c.moveToNext();
                                    LoadActivity(Contact.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
                                }
                                break;
                            case 9:
//                                Toast.makeText(About.this, "تنظیمات", Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder alertbox = new AlertDialog.Builder(ViewJob.this);
                                // set the message to display
                                alertbox.setMessage("تنظیمات پیش فاکتور");

                                // set a negative/no button and create a listener
                                alertbox.setPositiveButton("مراحل کاری", new DialogInterface.OnClickListener() {
                                    // do something when the button is clicked
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        db=dbh.getReadableDatabase();
                                        Cursor  c = db.rawQuery("SELECT * FROM login",null);
                                        if(c.getCount()>0)
                                        {
                                            c.moveToNext();
                                            SyncGetHmFactorService getHmFactorService=new SyncGetHmFactorService(ViewJob.this,guid,hamyarcode);
                                            getHmFactorService.AsyncExecute();
                                            LoadActivity(StepJob.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
                                        }
                                        db.close();
                                        arg0.dismiss();
                                    }
                                });

                                // set a positive/yes button and create a listener
                                alertbox.setNegativeButton("ملزومات کاری", new DialogInterface.OnClickListener() {
                                    // do something when the button is clicked
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        //Declare Object From Get Internet Connection Status For Check Internet Status
                                        db=dbh.getReadableDatabase();
                                        Cursor  c = db.rawQuery("SELECT * FROM login",null);
                                        if(c.getCount()>0) {
                                            c.moveToNext();
                                            SyncGetHmFactorTools syncGetHmFactorTools=new SyncGetHmFactorTools(ViewJob.this,guid,hamyarcode);
                                            syncGetHmFactorTools.AsyncExecute();
                                            LoadActivity(StepJobDetaile.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
                                        }

                                        db.close();
                                        arg0.dismiss();

                                    }
                                });

                                alertbox.show();
                                break;
                            case 10:
                                db = dbh.getReadableDatabase();
                                c = db.rawQuery("SELECT * FROM login",null);
                                if(c.getCount()>0) {
                                    c.moveToNext();

                                    LoadActivity(Help.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
                                }
                                db.close();
                                break;
                            case 11:
                                db = dbh.getReadableDatabase();
                                c = db.rawQuery("SELECT * FROM login",null);
                                if(c.getCount()>0) {
                                    c.moveToNext();

                                    LoadActivity(About.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
                                }
                                db.close();
                                break;
//                            case 12:
////                                Toast.makeText(About.this, "خروج از برنامه", Toast.LENGTH_SHORT).show();
//                                ExitApplication();
//                                break;
                            case 12:
//                                Toast.makeText(About.this, "خروج از کاربری", Toast.LENGTH_SHORT).show();
                                Logout();
                                break;
                            case 14:
                                Toast.makeText(ViewJob.this, "تلگرام", Toast.LENGTH_SHORT).show();
                                break;
                            case 15:
                                Toast.makeText(ViewJob.this, "اینستاگرام", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                })
                .build();
//        drawer.closeDrawer();
    }
    public Bitmap convertToBitmap(String base){
        Bitmap Bmp=null;
        try
        {
            byte[] decodedByte = Base64.decode(base, Base64.DEFAULT);
            Bmp = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
//
            return Bmp;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Bmp;
        }
    }
    void sharecode(String shareStr)
    {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "بسپارینا" + "\n"+"کد معرف: "+shareStr+"\n"+"آدرس سایت: " + PublicVariable.site;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "عنوان");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "اشتراک گذاری با"));
    }
    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    public void Logout() {
        //Exit All Activity And Kill Application
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        // set the message to display
        alertbox.setMessage("آیا می خواهید از کاربری خارج شوید ؟");

        // set a negative/no button and create a listener
        alertbox.setPositiveButton("خیر", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });

        // set a positive/yes button and create a listener
        alertbox.setNegativeButton("بله", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                //Declare Object From Get Internet Connection Status For Check Internet Status
                stopService(new Intent(getBaseContext(), ServiceGetLocation.class));
                stopService(new Intent(getBaseContext(), ServiceGetNewJob.class));
                stopService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));
                stopService(new Intent(getBaseContext(), ServiceGetSliderPic.class));
                stopService(new Intent(getBaseContext(), ServiceSyncProfile.class));
                stopService(new Intent(getBaseContext(), ServiceSyncServiceSelected.class));
                db = dbh.getWritableDatabase();
                db.execSQL("DELETE FROM AmountCredit");
                db.execSQL("DELETE FROM android_metadata");
                db.execSQL("DELETE FROM BsHamyarSelectServices");
                db.execSQL("DELETE FROM BsUserServices");
                db.execSQL("DELETE FROM credits");
                db.execSQL("DELETE FROM DateTB");
                db.execSQL("DELETE FROM education");
                db.execSQL("DELETE FROM exprtise");
                db.execSQL("DELETE FROM FaktorUserDetailes");
                db.execSQL("DELETE FROM HeadFactor");
                db.execSQL("DELETE FROM HmFactorService");
                db.execSQL("DELETE FROM HmFactorTools");
                db.execSQL("DELETE FROM HmFactorTools_List");
                db.execSQL("DELETE FROM InsertFaktorUserDetailes");
                db.execSQL("DELETE FROM login");
                db.execSQL("DELETE FROM messages");
                db.execSQL("DELETE FROM Profile");
                db.execSQL("DELETE FROM services");
                db.execSQL("DELETE FROM servicesdetails");
                db.execSQL("DELETE FROM Slider");
                db.execSQL("DELETE FROM sqlite_sequence");
                db.execSQL("DELETE FROM Supportphone");
                db.execSQL("DELETE FROM Unit");
                db.execSQL("DELETE FROM UpdateApp");
                db.close();
                Intent startMain = new Intent(Intent.ACTION_MAIN);


                startMain.addCategory(Intent.CATEGORY_HOME);

//                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(startMain);

                finish();
                arg0.dismiss();

            }
        });

        alertbox.show();
    }
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            if(tab.compareTo("0")==0)
            {
                LoadActivity(List_Dutys.class, "guid", guid, "hamyarcode", hamyarcode);
            }
            else
            {
                LoadActivity(List_Services.class, "guid", guid, "hamyarcode", hamyarcode);
            }

        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
    {
        Intent intent = new Intent(getApplicationContext(),Cls);
        intent.putExtra(VariableName, VariableValue);
        intent.putExtra(VariableName2, VariableValue2);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

         //****************************
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ViewJob.this,  new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String AM_PM;
                if (selectedHour >=0 && selectedHour < 12){
                    AM_PM = "AM";
                } else
                {
                    AM_PM = "PM";
                }
//                 db=dbh.getWritableDatabase();
//                String query="UPDATE  DateTB SET Time = '" +String.valueOf(selectedHour)+":"+String.valueOf(selectedMinute)+"'";
//                db.execSQL(query);
//                db=dbh.getReadableDatabase();
//                query="SELECT * FROM DateTB";
//                Cursor c=db.rawQuery(query,null);
//                if(c.getCount()>0)
//                {
//                    c.moveToNext();
//                    String[] DateTB = c.getString(c.getColumnIndex("Date")).split("/");
//                    String[] TimeTB = c.getString(c.getColumnIndex("Time")).split(":");
                    String[] DateTB = DateStr.split("/");
                    SyncVisitJob syncVisitJob = new SyncVisitJob(ViewJob.this, guid, hamyarcode, coursors.getString(coursors.getColumnIndex("Code")), DateTB[0], DateTB[1], DateTB[2], String.valueOf(selectedHour), String.valueOf(selectedMinute));
                    syncVisitJob.AsyncExecute();
//                }
            }
        }, hour, minute, true);
        mTimePicker.setTitle("");

        mTimePicker.show();

    }
    public void dialContactPhone(String phoneNumber) {
        //startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        startActivity(callIntent);
    }
    private void date_picker()
    {
        ir.hamsaa.persiandatepicker.util.PersianCalendar calNow=new ir.hamsaa.persiandatepicker.util.PersianCalendar();
        calNow.setPersianDate(calNow.getPersianYear(),calNow.getPersianMonth()+1,calNow.getPersianDay());
          //  initDate.setPersianDate(1370, 3, 13);
        PersianDatePickerDialog picker = new PersianDatePickerDialog(this);
        picker.setPositiveButtonString("تایید");
        picker.setNegativeButton("انصراف");
        picker.setTodayButton("امروز");
        picker.setTodayButtonVisible(true);
        //  picker.setInitDate(initDate);
        picker.setMaxYear(PersianDatePickerDialog.THIS_YEAR);
        picker.setMinYear(calNow.getPersianYear());
        picker.setActionTextColor(Color.GRAY);
        //picker.setTypeFace(FontMitra);
        picker.setListener(new Listener() {

            @Override
            public void onDateSelected(ir.hamsaa.persiandatepicker.util.PersianCalendar persianCalendar) {
                //Toast.makeText(getApplicationContext(), persianCalendar.getPersianYear() + "/" + persianCalendar.getPersianMonth() + "/" + persianCalendar.getPersianDay(), Toast.LENGTH_SHORT).show();
                 DateStr= persianCalendar.getPersianYear() + "/" + persianCalendar.getPersianMonth() + "/" + persianCalendar.getPersianDay();

                GetTime();
            }

            @Override
            public void onDismissed() {

            }
        });
        picker.show();
    }
}
