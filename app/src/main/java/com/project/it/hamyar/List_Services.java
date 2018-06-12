	package com.project.it.hamyar;

    import android.annotation.SuppressLint;
    import android.app.Activity;
    import android.app.AlertDialog;
    import android.app.TimePickerDialog;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.content.res.Configuration;
    import android.database.Cursor;
    import android.database.SQLException;
    import android.database.sqlite.SQLiteDatabase;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.graphics.Color;
    import android.net.Uri;
    import android.os.Build;
    import android.os.Bundle;
    import android.support.annotation.NonNull;
    import android.support.annotation.RequiresApi;
    import android.support.design.widget.BottomNavigationView;
    import android.support.v7.widget.Toolbar;
    import android.text.Editable;
    import android.text.TextWatcher;
    import android.util.Base64;
    import android.view.Gravity;
    import android.view.KeyEvent;
    import android.view.LayoutInflater;
    import android.view.MenuItem;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ListView;
    import android.widget.Spinner;
    import android.widget.TimePicker;
    import android.widget.Toast;

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
    import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.HashMap;
    import java.util.List;

    import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

    public class List_Services extends Activity {
        private String hamyarcode;
        private String guid;
        private Drawer drawer=null;
        private String countMessage;
        private String countVisit;
        private boolean IsActive;
        private ListView lvServices;
        private DatabaseHelper dbh;
        private SQLiteDatabase db;
        private Button btnCredit;
        private Button btnOrders;
        private Button btnHome;
        //*****************************************************
        private EditText etFromDate;
        private EditText etToDate;
        private EditText etFromTime;
        private EditText etToTime;
//        private EditText etArea;
        private Spinner spExpert;
        private List<String> labelssp;
        //*****************************************************
        private ArrayList<HashMap<String, String>> valuse ;

        @Override
        protected void attachBaseContext(Context newBase) {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.list_services);
            //************************************************************************
            LayoutInflater layoutInflater=getLayoutInflater();
            getWindow().addContentView(layoutInflater.inflate(R.layout.sliding_filter,null),new
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT));
            //************************************************************************
            btnCredit = (Button) findViewById(R.id.btnCredit);
            btnOrders = (Button) findViewById(R.id.btnOrders);
            btnHome = (Button) findViewById(R.id.btnHome);
            //************************************************************
            etFromDate = (EditText) findViewById(R.id.etFromDate);
            etToDate = (EditText) findViewById(R.id.etToDate);
            etFromTime = (EditText) findViewById(R.id.etFromTime);
            etToTime = (EditText) findViewById(R.id.etToTime);
//            etArea = (EditText) findViewById(R.id.etArea);
            spExpert = (Spinner) findViewById(R.id.spExpert);
            //************************************************************
            lvServices = (ListView) findViewById(R.id.listViewServices);
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
            try {
                hamyarcode = getIntent().getStringExtra("hamyarcode").toString();
                guid = getIntent().getStringExtra("guid").toString();
            } catch (Exception e) {
                db = dbh.getReadableDatabase();
                Cursor coursors = db.rawQuery("SELECT * FROM login", null);
                for (int i = 0; i < coursors.getCount(); i++) {
                    coursors.moveToNext();
                    guid = coursors.getString(coursors.getColumnIndex("guid"));
                    hamyarcode = coursors.getString(coursors.getColumnIndex("hamyarcode"));
                }
                db.close();
            }
            //****************************************************************************************
            db=dbh.getReadableDatabase();
            Cursor coursors = db.rawQuery("SELECT * FROM messages WHERE IsReade='0' AND IsDelete='0'",null);
            if(coursors.getCount()>0)
            {
                countMessage=String.valueOf(coursors.getCount());
            }
            coursors = db.rawQuery("SELECT * FROM BsHamyarSelectServices WHERE Status='5' AND IsDelete='0'",null);
            if(coursors.getCount()>0)
            {
                countVisit=String.valueOf(coursors.getCount());
            }
            IsActive=PublicVariable.IsActive;
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            CreateMenu(toolbar);
            //***************************************************************************************************************************
//*********************************************************
            labelssp=new ArrayList<>();
            labelssp.add("");
            db = dbh.getReadableDatabase();
            Cursor cursors = db.rawQuery("SELECT * FROM servicesdetails ", null);
            String str;
            for (int i = 0; i < cursors.getCount(); i++) {
                cursors.moveToNext();
                str = cursors.getString(cursors.getColumnIndex("name"));
                labelssp.add(str);
            }
            ArrayAdapter<String> dataAdaptersp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelssp);
            dataAdaptersp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spExpert.setAdapter(dataAdaptersp);
            db.close();
//*********************************************************
            setListServices();
//*********************************************************
            etFromDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PersianCalendar now = new PersianCalendar();
                    DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                    String StrMon,StrDay;
                                    if((monthOfYear+1)<10)
                                    {
                                        StrMon="0"+String.valueOf(monthOfYear + 1);
                                    }
                                    else {
                                        StrMon=String.valueOf(monthOfYear + 1);
                                    }
                                    if((dayOfMonth)<10)
                                    {
                                        StrDay="0"+String.valueOf(dayOfMonth);
                                    }
                                    else {
                                        StrDay=String.valueOf(dayOfMonth);
                                    }
                                    etFromDate.setText(String.valueOf(year) + "/" + StrMon + "/" + StrDay);
                                }
                            }, now.getPersianYear(),
                            now.getPersianMonth(),
                            now.getPersianDay());
                    datePickerDialog.setThemeDark(true);
                    datePickerDialog.show(getFragmentManager(), "tpd");

                }

            });
//            etFromDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (hasFocus) {
//                        PersianCalendar now = new PersianCalendar();
//                        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
//                                new DatePickerDialog.OnDateSetListener() {
//                                    @Override
//                                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//                                        etFromDate.setText(String.valueOf(year) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(dayOfMonth));
//                                    }
//                                }, now.getPersianYear(),
//                                now.getPersianMonth(),
//                                now.getPersianDay());
//                        datePickerDialog.setThemeDark(true);
//                        datePickerDialog.show(getFragmentManager(), "tpd");
//                    }
//                }
//            });

            etFromDate.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    setListServices();
                }
            });
//*********************************************************
            etToDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PersianCalendar now = new PersianCalendar();
                    DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                    String StrMon,StrDay;
                                    if((monthOfYear+1)<10)
                                    {
                                        StrMon="0"+String.valueOf(monthOfYear + 1);
                                    }
                                    else {
                                        StrMon=String.valueOf(monthOfYear + 1);
                                    }
                                    if((dayOfMonth)<10)
                                    {
                                        StrDay="0"+String.valueOf(dayOfMonth);
                                    }
                                    else {
                                        StrDay=String.valueOf(dayOfMonth);
                                    }
                                    etToDate.setText(String.valueOf(year) + "/" + StrMon + "/" + StrDay);
                                }
                            }, now.getPersianYear(),
                            now.getPersianMonth(),
                            now.getPersianDay());
                    datePickerDialog.setThemeDark(true);
                    datePickerDialog.show(getFragmentManager(), "tpd");

                }

            });
//            etToDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (hasFocus) {
//                        PersianCalendar now = new PersianCalendar();
//                        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
//                                new DatePickerDialog.OnDateSetListener() {
//                                    @Override
//                                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//                                        etToDate.setText(String.valueOf(year) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(dayOfMonth));
//                                    }
//                                }, now.getPersianYear(),
//                                now.getPersianMonth(),
//                                now.getPersianDay());
//                        datePickerDialog.setThemeDark(true);
//                        datePickerDialog.show(getFragmentManager(), "tpd");
//                    }
//                }
//            });

            etToDate.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    setListServices();
                }
            });
//*********************************************************
            etFromTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);

                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(List_Services.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,new TimePickerDialog.OnTimeSetListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            String AM_PM;
                            if (selectedHour >= 0 && selectedHour < 12) {
                                AM_PM = "AM";
                            } else {
                                AM_PM = "PM";
                            }
                            String StrHour,StrMin;
                            if(selectedHour<10)
                            {
                                StrHour="0"+String.valueOf(selectedHour);
                            }
                            else {
                                StrHour=String.valueOf(selectedHour);
                            }
                            if(selectedMinute<10)
                            {
                                StrMin="0"+String.valueOf(selectedMinute);
                            }
                            else {
                                StrMin=String.valueOf(selectedMinute);
                            }
                            etFromTime.setText(StrHour + ":" + StrMin);
                        }
                    }, hour, minute, false);
                    mTimePicker.setTitle("از ساعت");
                    mTimePicker.show();

                }

            });
//            etFromTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (hasFocus) {
//                        Calendar mcurrentTime = Calendar.getInstance();
//                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                        int minute = mcurrentTime.get(Calendar.MINUTE);
//
//                        TimePickerDialog mTimePicker;
//                        mTimePicker = new TimePickerDialog(List_Services.this, new TimePickerDialog.OnTimeSetListener() {
//                            @Override
//                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                                String AM_PM;
//                                if (selectedHour >= 0 && selectedHour < 12) {
//                                    AM_PM = "AM";
//                                } else {
//                                    AM_PM = "PM";
//                                }
//                                etFromTime.setText(String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
//                            }
//                        }, hour, minute, false);
//                        mTimePicker.setTitle("Select Time");
//                        mTimePicker.show();
//                    }
//                }
//            });
            etFromTime.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    setListServices();
                }
            });
//*********************************************************
            etToTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);

                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(List_Services.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            String AM_PM;
                            if (selectedHour >= 0 && selectedHour < 12) {
                                AM_PM = "AM";
                            } else {
                                AM_PM = "PM";
                            }
                            String StrHour,StrMin;
                            if(selectedHour<10)
                            {
                                StrHour="0"+String.valueOf(selectedHour);
                            }
                            else {
                                StrHour=String.valueOf(selectedHour);
                            }
                            if(selectedMinute<10)
                            {
                                StrMin="0"+String.valueOf(selectedMinute);
                            }
                            else {
                                StrMin=String.valueOf(selectedMinute);
                            }
                            etToTime.setText(StrHour + ":" + StrMin);
                        }

                    }, hour, minute, false);
                    mTimePicker.setTitle("تا ساعت");
                    mTimePicker.show();

                }

            });
//            etToTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (hasFocus) {
//                        Calendar mcurrentTime = Calendar.getInstance();
//                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                        int minute = mcurrentTime.get(Calendar.MINUTE);
//
//                        TimePickerDialog mTimePicker;
//                        mTimePicker = new TimePickerDialog(List_Services.this, new TimePickerDialog.OnTimeSetListener() {
//                            @Override
//                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                                String AM_PM;
//                                if (selectedHour >= 0 && selectedHour < 12) {
//                                    AM_PM = "AM";
//                                } else {
//                                    AM_PM = "PM";
//                                }
//                                etToTime.setText(String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
//                            }
//                        }, hour, minute, false);
//                        mTimePicker.setTitle("Select Time");
//                        mTimePicker.show();
//
//                    }
//                }
//            });
            etToTime.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    setListServices();
                }
            });
//*********************************************************
            spExpert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setListServices();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
//*********************************************************
            btnCredit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadActivity(Credit.class, "guid", guid, "hamyarcode", hamyarcode);
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
                            new SecondaryDrawerItem().withName(R.string.Invite_friends).withIcon(R.drawable.contact).withSelectable(false).withEnabled(IsActive),
                            new SecondaryDrawerItem().withName(R.string.Contact).withIcon(R.drawable.contact).withSelectable(false),
                            //new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
                            new SecondaryDrawerItem().withName(R.string.action_settings).withIcon(R.drawable.setting).withSelectable(false).withEnabled(IsActive),
                            new SecondaryDrawerItem().withName(R.string.Help).withIcon(R.drawable.help).withSelectable(false),
                            new SecondaryDrawerItem().withName(R.string.Contact).withIcon(R.drawable.contact).withSelectable(false),
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
                                                SyncProfile profile = new SyncProfile(List_Services.this, c.getString(c.getColumnIndex("guid")), c.getString(c.getColumnIndex("hamyarcode")));
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
                                        Toast.makeText(List_Services.this, "برای استفاده از امکانات بسپارینا باید ثبت نام کنید", Toast.LENGTH_LONG).show();
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
//                                Toast.makeText(Contact.this, "تنظیمات", Toast.LENGTH_SHORT).show();
                                    AlertDialog.Builder alertbox = new AlertDialog.Builder(List_Services.this);
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
                                                SyncGetHmFactorService getHmFactorService=new SyncGetHmFactorService(List_Services.this,guid,hamyarcode);
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
                                                SyncGetHmFactorTools syncGetHmFactorTools=new SyncGetHmFactorTools(List_Services.this,guid,hamyarcode);
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

                                        LoadActivity(Contact.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
                                    }
                                    db.close();
                                    break;
//                            case 12:
////                                Toast.makeText(Contact.this, "خروج از برنامه", Toast.LENGTH_SHORT).show();
//                                ExitApplication();
//                                break;
                                case 12:
//                                Toast.makeText(Contact.this, "خروج از کاربری", Toast.LENGTH_SHORT).show();
                                    Logout();
                                    break;
                                case 14:
                                    Toast.makeText(List_Services.this, "تلگرام", Toast.LENGTH_SHORT).show();
                                    break;
                                case 15:
                                    Toast.makeText(List_Services.this, "اینستاگرام", Toast.LENGTH_SHORT).show();
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
            List_Services.this.LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
        {
            Intent intent = new Intent(getApplicationContext(),Cls);
            intent.putExtra(VariableName, VariableValue);
            intent.putExtra(VariableName2, VariableValue2);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            List_Services.this.startActivity(intent);
        }
    public void setListServices()
    {
        valuse = new ArrayList<HashMap<String, String>>();
        String query="SELECT BsUserServices.*,Servicesdetails.name FROM BsUserServices " +
                "LEFT JOIN " +
                "Servicesdetails ON " +
                "Servicesdetails.code=BsUserServices.ServiceDetaileCode WHERE 1=1 ";
        if(etFromDate.getText().toString().length()>0) {
            query = query + " AND StartDate>='" + etFromDate.getText().toString()+"'";
        }
        if(etToDate.getText().toString().length()>0) {
            query = query + " AND EndDate<='" + etToDate.getText().toString()+"'";
        }
        if(etFromTime.getText().toString().length()>0) {
            query = query + " AND StartTime>='" + etFromTime.getText().toString()+"'";
        }
        if(etToTime.getText().toString().length()>0) {
            query = query + " AND EndTime<='" + etToTime.getText().toString()+"'";
        }
//        if(etArea.getText().toString().length()>0) {
//            query = query + " AND AddressText LIKE '%" + etArea.getText().toString()+"%'";
//        }
//        if(spExpert.getSelectedItem().toString().compareTo("")!=0) {
//            query = query + " AND " + spExpert.getSelectedItem().toString();
//        }
        db=dbh.getReadableDatabase();
        Cursor coursors = db.rawQuery(query,null);
        for(int i=0;i<coursors.getCount();i++){
            coursors.moveToNext();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name","شماره درخواست: "+coursors.getString(coursors.getColumnIndex("Code"))+"\n"+
                    "موضوع: "+coursors.getString(coursors.getColumnIndex("name"))+"\n"
                    +"نام متقاضی: "+coursors.getString(coursors.getColumnIndex("UserName"))+" "+coursors.getString(coursors.getColumnIndex("UserFamily"))+"\n"+
                    "تاریخ حضور: "+coursors.getString(coursors.getColumnIndex("StartDate"))+"\n"+"ساعت حضور: "+coursors.getString(coursors.getColumnIndex("StartTime"))+"\n"+
                    "وضعیت: "+((coursors.getString(coursors.getColumnIndex("IsEmergency")).compareTo("0")==0? "عادی":"فوری")));
            map.put("Code",coursors.getString(coursors.getColumnIndex("Code")));
            map.put("UserPhone",coursors.getString(coursors.getColumnIndex("UserPhone")));
            valuse.add(map);
        }
        db.close();
        AdapterServices dataAdapter=new AdapterServices(this,valuse,guid,hamyarcode);
        lvServices.setAdapter(dataAdapter);
    }
}
