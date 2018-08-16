	package com.besparina.it.hamyar;

    import android.app.Activity;
    import android.app.AlertDialog;
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
    import android.graphics.Typeface;
    import android.net.Uri;
    import android.os.Build;
    import android.os.Bundle;
    import android.support.annotation.RequiresApi;
    import android.support.v7.widget.Toolbar;
    import android.util.Base64;
    import android.view.Gravity;
    import android.view.KeyEvent;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ListView;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.mikepenz.materialdrawer.AccountHeader;
    import com.mikepenz.materialdrawer.AccountHeaderBuilder;
    import com.mikepenz.materialdrawer.Drawer;
    import com.mikepenz.materialdrawer.DrawerBuilder;
    import com.mikepenz.materialdrawer.holder.BadgeStyle;
    import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
    import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
    import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
    import com.mikepenz.materialdrawer.model.interfaces.IProfile;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.HashMap;

    import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

    public class ListServiceAtTheTurn extends Activity {
        private String hamyarcode;
        private String guid;
        private Drawer drawer=null;
        private String countMessage;
        private String countVisit;
        private boolean IsActive;
        private TextView tvHistory;
        private ListView lstHistory;
        private DatabaseHelper dbh;
        private SQLiteDatabase db;
        private TextView btnCredit;
        private Button btnDutyToday;
        private Button btnServices_at_the_turn;
        private Button btnHome;
        private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
        @Override
        protected void attachBaseContext(Context newBase) {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        }
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.service_at_turn);
            btnCredit=(TextView)findViewById(R.id.btnCredit);
            btnServices_at_the_turn=(Button)findViewById(R.id.btnServices_at_the_turn);
            btnDutyToday=(Button)findViewById(R.id.btnDutyToday);
            btnHome=(Button)findViewById(R.id.btnHome);
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
                db=dbh.getReadableDatabase();
                Cursor coursors = db.rawQuery("SELECT * FROM login", null);
                for (int i = 0; i < coursors.getCount(); i++) {
                    coursors.moveToNext();
                    guid = coursors.getString(coursors.getColumnIndex("guid"));
                    hamyarcode = coursors.getString(coursors.getColumnIndex("hamyarcode"));
                }
                db.close();
            }
            //****************************************************************************************
            TextView tvAmountCredit=(TextView) findViewById(R.id.tvAmountCredit);
            db=dbh.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM AmountCredit", null);
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                String splitStr[] = cursor.getString(cursor.getColumnIndex("Amount")).toString().split("\\.");
                if(splitStr.length>=2)
                {
                    if (splitStr[1].compareTo("00") == 0) {
                        tvAmountCredit.setText(PersianDigitConverter.PerisanNumber(splitStr[0]));
                    } else
                    {
                        tvAmountCredit.setText(PersianDigitConverter.PerisanNumber(cursor.getString(cursor.getColumnIndex("Amount"))));
                    }
                }
                else
                {
                    tvAmountCredit.setText(PersianDigitConverter.PerisanNumber(cursor.getString(cursor.getColumnIndex("Amount"))));
                }
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
            Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");//set font for page
            tvHistory=(TextView)findViewById(R.id.tvHistory);
            tvHistory.setTypeface(FontMitra);
            tvHistory.setTextSize(18);
            lstHistory=(ListView)findViewById(R.id.lstHistory);
            db=dbh.getReadableDatabase();
            String query = "SELECT BsHamyarSelectServices.*,Servicesdetails.name FROM BsHamyarSelectServices " +
                    "LEFT JOIN " +
                    "Servicesdetails ON " +
                    "Servicesdetails.code=BsHamyarSelectServices.ServiceDetaileCode WHERE IsDelete='0'";
            coursors = db.rawQuery(query, null);
            for (int i = 0; i < coursors.getCount(); i++) {
                coursors.moveToNext();
                HashMap<String, String> map = new HashMap<String, String>();
                try
                {
                    map.put("NumberService",coursors.getString(coursors.getColumnIndex("Code")));
                }
                catch (Exception ex) {
                    //todo
                }
                try
                {
                    map.put("TitleService",coursors.getString(coursors.getColumnIndex("name")));
                }
                catch (Exception ex) {
                    //todo
                }
                try
                {
                    map.put("NameCustomer",coursors.getString(coursors.getColumnIndex("UserName")) + " " + coursors.getString(coursors.getColumnIndex("UserFamily")));
                } catch (Exception ex) {
                    //todo
                }
                try
                {
                    map.put("Date",coursors.getString(coursors.getColumnIndex("StartDate"))+ " - " + coursors.getString(coursors.getColumnIndex("EndDate")));
                } catch (Exception ex) {
                    //todo
                }
                try
                {
                    map.put("Time",coursors.getString(coursors.getColumnIndex("StartTime"))+ " - " + coursors.getString(coursors.getColumnIndex("EndTime")));
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("PeriodicServices")).toString().compareTo("1") == 0) {
                        map.put("Period","روزانه");
                    } else if (coursors.getString(coursors.getColumnIndex("PeriodicServices")).toString().compareTo("2") == 0) {
                        map.put("Period","هفتگی");
                    } else if (coursors.getString(coursors.getColumnIndex("PeriodicServices")).toString().compareTo("3") == 0) {
                        map.put("Period","هفته در میان");
                    } else if (coursors.getString(coursors.getColumnIndex("PeriodicServices")).toString().compareTo("4") == 0) {
                        map.put("Period","ماهانه");
                    }

                } catch (Exception ex) {
                    //todo
                }
                String CountStr = "";
                try {
                    if (coursors.getString(coursors.getColumnIndex("MaleCount")).toString().compareTo("0") != 0) {
                        if (CountStr.length() == 0) {
                            CountStr = coursors.getString(coursors.getColumnIndex("MaleCount")) + "مرد ";
                        }
                        else
                        {
                            CountStr += " - " + coursors.getString(coursors.getColumnIndex("MaleCount")) + "مرد ";
                        }
                    }
                    if (coursors.getString(coursors.getColumnIndex("FemaleCount")).toString().compareTo("0") != 0) {
                        if (CountStr.length() == 0) {
                            CountStr = coursors.getString(coursors.getColumnIndex("FemaleCount")) + "زن ";
                        }
                        else
                        {
                            CountStr += " - " + coursors.getString(coursors.getColumnIndex("FemaleCount")) + "زن ";
                        }
                    }
                    if (coursors.getString(coursors.getColumnIndex("HamyarCount")).toString().compareTo("0") != 0) {
                        if (CountStr.length() == 0) {
                            CountStr = coursors.getString(coursors.getColumnIndex("HamyarCount"));
                        }
                        else
                        {
                            CountStr += " - " + coursors.getString(coursors.getColumnIndex("HamyarCount"));
                        }
                    }
                    map.put("CountHamyar",CountStr);

                } catch (Exception ex) {
                    //todo
                }
//                try {
//                    if (coursors.getString(coursors.getColumnIndex("EducationTitle")).toString().compareTo("0") != 0) {
//                        Content += "عنوان آموزش: " + coursors.getString(coursors.getColumnIndex("EducationTitle")) + "\n";
//                    }
//                } catch (Exception ex) {
//                    //todo
//                }
//                try {
//                    if (coursors.getString(coursors.getColumnIndex("EducationGrade")).toString().compareTo("0") != 0) {
//                        Content += "پایه تحصیلی: " + coursors.getString(coursors.getColumnIndex("EducationGrade")) + "\n";
//                    }
//                } catch (Exception ex) {
//                    //todo
//                }
//                try {
//                    if (coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("1") == 0) {
//                        Content += "رشته تحصیلی: " + "ابتدایی" + "\n";
//                    } else if (coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("2") == 0) {
//                        Content += "رشته تحصیلی: " + "متوسطه اول" + "\n";
//                    } else if (coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("3") == 0) {
//                        Content += "رشته تحصیلی: " + "علوم تجربی" + "\n";
//                    } else if (coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("4") == 0) {
//                        Content += "رشته تحصیلی: " + "ریاضی و فیزیک" + "\n";
//                    } else if (coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("5") == 0) {
//                        Content += "رشته تحصیلی: " + "انسانی" + "\n";
//                    }
//                } catch (Exception ex) {
//                    //todo
//                }
//                try {
//                    if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("0") != 0) {
//                        if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("2") == 0) {
//                            Content += "رشته هنری: " + "موسیقی" + "\n";
//                        } else if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("3") == 0) {
//                            Content += "رشته هنری: " + "موسیقی" + "\n";
//                        } else if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("4") == 0) {
//                            Content += "رشته هنری: " + "موسیقی" + "\n";
//                        } else if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("5") == 0) {
//                            Content += "رشته هنری: " + "موسیقی" + "\n";
//                        } else if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("6") == 0) {
//                            Content += "رشته هنری: " + "موسیقی" + "\n";
//                        } else if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("7") == 0) {
//                            Content += "رشته هنری: " + "موسیقی" + "\n";
//                        } else if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("7") == 0) {
//                            Content += "رشته هنری: " + "موسیقی" + "\n";
//                        } else {
//                            Content += "رشته هنری: " + coursors.getString(coursors.getColumnIndex("ArtField")) + "\n";
//                        }
//                    }
//                } catch (Exception ex) {
//                    //todo
//                }
//                try {
//                    if (coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("1") == 0) {
//                        Content += "زبان: " + "انگلیسی" + "\n";
//                    } else if (coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("2") == 0) {
//                        Content += "زبان: " + "روسی" + "\n";
//                    } else if (coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("3") == 0) {
//                        Content += "زبان: " + "آلمانی" + "\n";
//                    } else if (coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("4") == 0) {
//                        Content += "زبان: " + "فرانسه" + "\n";
//                    } else if (coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("5") == 0) {
//                        Content += "زبان: " + "ترکی" + "\n";
//                    } else if (coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("6") == 0) {
//                        Content += "زبان: " + "عربی" + "\n";
//                    }
//                } catch (Exception ex) {
//                    //todo
//                }
//                try {
//                    if (coursors.getString(coursors.getColumnIndex("StudentGender")).toString().compareTo("1") == 0) {
//                        Content += "جنسیت دانش آموز: " + "زن" + "\n";
//                    } else if (coursors.getString(coursors.getColumnIndex("StudentGender")).toString().compareTo("2") == 0) {
//                        Content += "جنسیت دانش آموز: " + "مرد" + "\n";
//                    }
//                } catch (Exception ex) {
//                    //todo
//                }
//                try {
//                    if (coursors.getString(coursors.getColumnIndex("CarWashType")).toString().compareTo("1") == 0) {
//                        Content += "نوع سرویس: " + "روشویی" + "\n";
//                    } else if (coursors.getString(coursors.getColumnIndex("CarWashType")).toString().compareTo("2") == 0) {
//                        Content += "نوع سرویس: " + "روشویی و توشویی" + "\n";
//                    }
//                } catch (Exception ex) {
//                    //todo
//                }
//                try {
//                    if (coursors.getString(coursors.getColumnIndex("CarType")).toString().compareTo("1") == 0) {
//                        Content += "نوع خودرو: " + "سواری" + "\n";
//                    } else if (coursors.getString(coursors.getColumnIndex("CarType")).toString().compareTo("2") == 0) {
//                        Content += "نوع سرویس: " + "شاسی و نیم شاسی" + "\n";
//                    } else if (coursors.getString(coursors.getColumnIndex("CarType")).toString().compareTo("3") == 0) {
//                        Content += "نوع سرویس: " + "ون" + "\n";
//                    }
//
//                } catch (Exception ex) {
//                    //todo
//                }
                try
                {
                    map.put("Description",coursors.getString(coursors.getColumnIndex("Description")));
                } catch (Exception ex) {
                    //todo
                }
                try
                {
                    map.put("Addres",coursors.getString(coursors.getColumnIndex("AddressText")));
                } catch (Exception ex) {
                    //todo
                }
                try
                {
                    map.put("Emergency",((coursors.getString(coursors.getColumnIndex("IsEmergency")).compareTo("0") == 1 ? "عادی" : "فوری")));
                } catch (Exception ex) {
                    //todo
                }
                valuse.add(map);
            }
            if(valuse.size()==0)
            {
                lstHistory.setVisibility(View.GONE);
                tvHistory.setVisibility(View.VISIBLE);
                tvHistory.setText("موردی جهت نمایش وجود ندارد");
            }
            else
            {
                lstHistory.setVisibility(View.VISIBLE);
                tvHistory.setVisibility(View.GONE);
                AdapterServiceAtTurn dataAdapter=new AdapterServiceAtTurn(this,valuse,guid,hamyarcode);
                lstHistory.setAdapter(dataAdapter);
            }
            db.close();

            btnCredit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadActivity(Credit.class, "guid",  guid, "hamyarcode", hamyarcode);
                }
            });
            btnDutyToday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LoadActivity(List_Dutys.class, "guid", guid, "hamyarcode", hamyarcode);
                }
            });
            btnServices_at_the_turn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadActivity(ListServiceAtTheTurn.class, "guid", guid, "hamyarcode", hamyarcode);
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
                    )//.addStickyDrawerItems(new PrimaryDrawerItem().withName(R.string.RelateUs).withSelectable(false).withEnabled(false),se),
                            //new PrimaryDrawerItem().withName(R.string.telegram).withIcon(R.drawable.telegram).withSelectable(false),
                            //new PrimaryDrawerItem().withName(R.string.instagram).withIcon(R.drawable.instagram).withSelectable(false))
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
                                                SyncProfile profile = new SyncProfile(ListServiceAtTheTurn.this, c.getString(c.getColumnIndex("guid")), c.getString(c.getColumnIndex("hamyarcode")));
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
                                        Toast.makeText(ListServiceAtTheTurn.this, "برای استفاده از امکانات بسپارینا باید ثبت نام کنید", Toast.LENGTH_LONG).show();
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
                                    AlertDialog.Builder alertbox = new AlertDialog.Builder(ListServiceAtTheTurn.this);
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
                                                SyncGetHmFactorService getHmFactorService=new SyncGetHmFactorService(ListServiceAtTheTurn.this,guid,hamyarcode);
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
                                                SyncGetHmFactorTools syncGetHmFactorTools=new SyncGetHmFactorTools(ListServiceAtTheTurn.this,guid,hamyarcode);
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
                                    Toast.makeText(ListServiceAtTheTurn.this, "تلگرام", Toast.LENGTH_SHORT).show();
                                    break;
                                case 15:
                                    Toast.makeText(ListServiceAtTheTurn.this, "اینستاگرام", Toast.LENGTH_SHORT).show();
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
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "بسپارینا" + "\n"+"کد معرف: "+shareStr+"\n"+"آدرس سایت: " + PublicVariable.site;
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "عنوان");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
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
            ListServiceAtTheTurn.this.LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
        {
            Intent intent = new Intent(getApplicationContext(),Cls);
            intent.putExtra(VariableName, VariableValue);
            intent.putExtra(VariableName2, VariableValue2);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            ListServiceAtTheTurn.this.startActivity(intent);
        }
    }
