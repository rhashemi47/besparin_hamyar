package com.project.it.hamyar;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.io.IOException;
import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by hashemi on 01/23/2018.
 */

public class ViewJob extends AppCompatActivity{
    private String hamyarcode;
    private String guid;
    private String BsUserServicesID;
    private String tab;
    private String status;
    private String latStr="0";
    private String lonStr="0";
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewjob);
        Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");//set font for page
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
                    Content+="وضعیت: "+((coursors.getString(coursors.getColumnIndex("IsEmergency")).compareTo("0")==1? "عادی":"فوری"));
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
                    Content+="وضعیت: "+((coursors.getString(coursors.getColumnIndex("IsEmergency")).compareTo("0")==1? "عادی":"فوری"));
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
            if(status.compareTo("0")==0)//check
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
                PersianCalendar now = new PersianCalendar();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                             @Override
                             public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                        db=dbh.getWritableDatabase();
                                String query="UPDATE  DateTB SET Date = '" +String.valueOf(year)+"/"+String.valueOf(monthOfYear+1)+"/"+String.valueOf(dayOfMonth)+"'";
                                 db.execSQL(query);

                                 db.close();
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
}
