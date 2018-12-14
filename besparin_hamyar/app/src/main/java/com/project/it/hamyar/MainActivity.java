package com.besparina.it.hamyar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String hamyarcode;
    private String guid;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private Drawer drawer=null;
    private String countMessage;
    private String countVisit;
    private boolean IsActive=true;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stopService(new Intent(getBaseContext(), ServiceGetNewJob.class));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //*******************************************************************
        Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");//set font for page
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
        try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
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
        try
        {
            hamyarcode = getIntent().getStringExtra("hamyarcode");
            guid = getIntent().getStringExtra("guid");
            if(hamyarcode==null || guid==null)
            {

                Cursor cursors=null;
                try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
                cursors = db.rawQuery("SELECT * FROM login", null);
                if(cursors.getCount()>0)
                {
                    cursors.moveToNext();
                    String Result=cursors.getString(cursors.getColumnIndex("islogin"));
                    if(Result.compareTo("0")==0)
                    {
                        //todo*******************************************************
                        LoadActivity(MainActivity.class,"hamyarcode","0","guid","0");
                    }
                    else
                    {
                        hamyarcode = cursors.getString(cursors.getColumnIndex("hamyarcode"));
                        guid = cursors.getString(cursors.getColumnIndex("guid"));
                        try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
                        coursors = db.rawQuery("SELECT * FROM UpdateApp",null);
                        if(coursors.getCount()>0)
                        {
                            coursors.moveToNext();
                            if(coursors.getString(coursors.getColumnIndex("Status")).compareTo("1")==0){
                                String Query="UPDATE UpdateApp SET Status='0'";
                                db=dbh.getWritableDatabase();
                                db.execSQL(Query);
                                update();
                            }
                        }
                    }
                }
                else
                {
                    LoadActivity(Login.class,"hamyarcode","0","guid","0");
                }
            }
            else if(hamyarcode.compareTo("0")==0 || guid.compareTo("0")==0)
            {
                IsActive=false;
            }
            else
            {
                try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
                coursors = db.rawQuery("SELECT * FROM UpdateApp",null);
                if(coursors.getCount()>0)
                {
                    coursors.moveToNext();
                    if(coursors.getString(coursors.getColumnIndex("Status")).compareTo("1")==0){
                        String Query="UPDATE UpdateApp SET Status='0'";
                        db=dbh.getWritableDatabase();
                        db.execSQL(Query);
                        update();
                    }
                }
            }


        }
        catch(Exception e)
        {
            throw new Error("Error Opne Activity");
        }

        //**************************************************************************
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LoadActivity(List_Messages.class, "guid", guid,"hamyarcode",hamyarcode);
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//            }
//        });
        //**************************************************************************
        int drawerGravity= Gravity.START;
        if(Build.VERSION.SDK_INT<= Build.VERSION_CODES.JELLY_BEAN){
            drawerGravity= Gravity.START;
        }
        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.menu_header)
                .addProfiles(
                        new ProfileDrawerItem().withName("سید عبد العزیز هاشمی").withIcon(getResources().getDrawable(R.drawable.personpic))
                )
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
                        new SecondaryDrawerItem().withName(R.string.Profile).withIcon(R.drawable.profile).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.ListVisit).withIcon(R.drawable.jobs).withBadge(countVisit).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withSelectable(false).withEnabled(IsActive),
                        new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
                        new SecondaryDrawerItem().withName(R.string.Yourcommitment).withIcon(R.drawable.yourcommitment).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.Ourcommitment).withIcon(R.drawable.ourcommitment).withSelectable(false),
                        new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
                        new SecondaryDrawerItem().withName(R.string.Messages).withIcon(R.drawable.messages).withBadge(countMessage).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withSelectable(false).withEnabled(IsActive),
                        new SecondaryDrawerItem().withName(R.string.GiftBank).withIcon(R.drawable.gift).withSelectable(false).withEnabled(IsActive),
                        new SecondaryDrawerItem().withName(R.string.Contact).withIcon(R.drawable.contact).withSelectable(false),
                        new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
                        new SecondaryDrawerItem().withName(R.string.action_settings).withIcon(R.drawable.setting).withSelectable(false).withEnabled(IsActive),
                        new SecondaryDrawerItem().withName(R.string.Help).withIcon(R.drawable.help).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.About).withIcon(R.drawable.about).withSelectable(false),                        new SecondaryDrawerItem().withName(R.string.History).withIcon(R.drawable.history).withSelectable(false),
                        new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
                        new SecondaryDrawerItem().withName(R.string.Exit).withIcon(R.drawable.profile).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.Logout).withIcon(R.drawable.profile).withSelectable(false)
                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position){
                            case 1://Profile
                                try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
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
                                            SyncProfile profile = new SyncProfile(MainActivity.this, c.getString(c.getColumnIndex("guid")), c.getString(c.getColumnIndex("hamyarcode")));
                                            profile.AsyncExecute();
                                        }
                                    }
                                    else
                                    {
                                        LoadActivity(Profile.class, "guid", guid,"hamyarcode",hamyarcode);
                                    }
                                }
                                break;
                            case 2:
                                Cursor c = db.rawQuery("SELECT * FROM login",null);
                                if(c.getCount()>0) {
                                    c.moveToNext();
                                    LoadActivity(List_Visits.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
                                }
                                break;
                            case 4:
                                c = db.rawQuery("SELECT * FROM login",null);
                                if(c.getCount()>0) {
                                    c.moveToNext();

                                    LoadActivity(YourCommitment.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
                                }

                                break;
                            case 5:
                                c = db.rawQuery("SELECT * FROM login",null);
                                if(c.getCount()>0) {
                                    c.moveToNext();

                                    LoadActivity(OurCommitment.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
                                }

                                break;
                            case 7:
                                c = db.rawQuery("SELECT * FROM login",null);
                                if(c.getCount()>0) {
                                    c.moveToNext();

                                    LoadActivity(List_Messages.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
                                }
                                break;
                            case 8:
                                c = db.rawQuery("SELECT * FROM login",null);
                                if(c.getCount()>0) {
                                    c.moveToNext();

                                    LoadActivity(GiftBank.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
                                }
                                break;
                            case 9:
                                c = db.rawQuery("SELECT * FROM login",null);
                                if(c.getCount()>0) {
                                    c.moveToNext();

                                    LoadActivity(Contact.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
                                }
                                break;
                            case 11:
//                                Toast.makeText(MainMenu.this, "تنظیمات", Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder alertbox = new AlertDialog.Builder(MainActivity.this);
                                // set the message to display
                                alertbox.setMessage("تنظیمات پیش فاکتور");

                                // set a negative/no button and create a listener
                                alertbox.setPositiveButton("مراحل کاری", new DialogInterface.OnClickListener() {
                                    // do something when the button is clicked
                                    public void onClick(DialogInterface arg0, int arg1) {
                                      Cursor  c = db.rawQuery("SELECT * FROM login",null);
                                        if(c.getCount()>0) {
                                            c.moveToNext();

                                            LoadActivity(StepJob.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
                                        }
                                        arg0.dismiss();
                                    }
                                });

                                // set a positive/yes button and create a listener
                                alertbox.setNegativeButton("ملزومات کاری", new DialogInterface.OnClickListener() {
                                    // do something when the button is clicked
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        //Declare Object From Get Internet Connection Status For Check Internet Status
                                        Cursor  c = db.rawQuery("SELECT * FROM login",null);
                                        if(c.getCount()>0) {
                                            c.moveToNext();

                                            LoadActivity(StepJobDetaile.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
                                        }
                                        arg0.dismiss();

                                    }
                                });

                                alertbox.show();
                                break;
                            case 12:
                                c = db.rawQuery("SELECT * FROM login",null);
                                if(c.getCount()>0) {
                                    c.moveToNext();

                                    LoadActivity(Help.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
                                }
                                break;
                            case 13:
                                c = db.rawQuery("SELECT * FROM login",null);
                                if(c.getCount()>0) {
                                    c.moveToNext();

                                    LoadActivity(About.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
                                }
                                break;
                            case 15:
//                                Toast.makeText(MainMenu.this, "خروج از برنامه", Toast.LENGTH_SHORT).show();
                                ExitApplication();
                                break;
                            case 16:
//                                Toast.makeText(MainMenu.this, "خروج از کاربری", Toast.LENGTH_SHORT).show();
                                Logout();
                                break;
                        }
                        return true;
                    }
                })
                .build();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.credite) {
//                    Toast.makeText(getBaseContext(), "اعتبارات", Toast.LENGTH_LONG).show();
                    LoadActivity(Credit.class, "guid",  guid, "hamyarcode", hamyarcode);
                    return true;
                } else if (item.getItemId() == R.id.itemDuty) {
//                    Toast.makeText(getBaseContext(), "وظایف", Toast.LENGTH_LONG).show();
                    return true;
                } else if (item.getItemId() == R.id.home) {
//                    Toast.makeText(getBaseContext(), "صفحه اصلی", Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
        private String guid;
        private String hamyarcode;
        private static int check_tap_number=0;
        private	DatabaseHelper dbh;
        private SQLiteDatabase db;
        private List<String> labels = new ArrayList<String>();
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {

            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            dbh=new DatabaseHelper(getContext());
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
            if(check_tap_number==0)
            {
                check_tap_number=1;
                try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
                Cursor coursors = db.rawQuery("SELECT BsUserServices.*,Servicesdetails.name FROM BsUserServices " +
                        "LEFT JOIN " +
                        "Servicesdetails ON " +
                        "Servicesdetails.code=BsUserServices.ServiceDetaileCode",null);
                for(int i=0;i<coursors.getCount();i++){
                    coursors.moveToNext();
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("name","موضوع: "+coursors.getString(coursors.getColumnIndex("name"))+"\n"
                            +"نام صاحبکار"+coursors.getString(coursors.getColumnIndex("UserName"))+" "+coursors.getString(coursors.getColumnIndex("UserFamily"))+"\n"+
                            "تاریخ حضور"+coursors.getString(coursors.getColumnIndex("StartDate"))+"\n"+"ساعت حضور"+coursors.getString(coursors.getColumnIndex("StartTime"))+"\n"+
                            "وضعیت: "+((coursors.getString(coursors.getColumnIndex("IsEmergency")).compareTo("0")==1? "عادی":"فوری")));
                    map.put("id",coursors.getString(coursors.getColumnIndex("id")));
                    valuse.add(map);
                }
                coursors = db.rawQuery("SELECT * FROM login",null);
                for(int i=0;i<coursors.getCount();i++){
                    coursors.moveToNext();
                    guid=coursors.getString(coursors.getColumnIndex("guid"));
                    hamyarcode=coursors.getString(coursors.getColumnIndex("hamyarcode"));
                }
                CustomAdapter dataAdapter=new CustomAdapter(this.getActivity(),valuse,guid,hamyarcode,check_tap_number);
                View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                ListView lvJob=(ListView) rootView.findViewById(R.id.lvJob);
                lvJob.setAdapter(dataAdapter);

                return rootView;
            }
            else
            {

                check_tap_number=0;
                try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
                Cursor coursors = db.rawQuery("SELECT BsHamyarSelectServices.*,Servicesdetails.name FROM BsHamyarSelectServices " +
                        "LEFT JOIN " +
                        "Servicesdetails ON " +
                        "Servicesdetails.code=BsHamyarSelectServices.ServiceDetaileCode WHERE IsDelete='0'",null);
                for(int i=0;i<coursors.getCount();i++){
                    coursors.moveToNext();
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("name","موضوع: "+coursors.getString(coursors.getColumnIndex("name"))+"\n"
                            +"نام صاحبکار"+coursors.getString(coursors.getColumnIndex("UserName"))+" "+coursors.getString(coursors.getColumnIndex("UserFamily"))+"\n"+
                            "تاریخ حضور"+coursors.getString(coursors.getColumnIndex("StartDate"))+"\n"+"ساعت حضور"+coursors.getString(coursors.getColumnIndex("StartTime"))+"\n"+
                            "وضعیت: "+((coursors.getString(coursors.getColumnIndex("IsEmergency")).compareTo("0")==1? "عادی":"فوری")));
                    map.put("id",coursors.getString(coursors.getColumnIndex("id")));
                    valuse.add(map);
                }
                coursors = db.rawQuery("SELECT * FROM login",null);
                for(int i=0;i<coursors.getCount();i++){
                    coursors.moveToNext();
                    guid=coursors.getString(coursors.getColumnIndex("guid"));
                    hamyarcode=coursors.getString(coursors.getColumnIndex("hamyarcode"));
                }
                CustomAdapter dataAdapter=new CustomAdapter(this.getActivity(),valuse,guid,hamyarcode,check_tap_number);
                View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                ListView lvJob=(ListView) rootView.findViewById(R.id.lvJob);
                lvJob.setAdapter(dataAdapter);
                return rootView;
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
    public void Logout() {
        //Exit All Activity And Kill Application
        AlertDialog.Builder alertbox = new AlertDialog.Builder(MainActivity.this);
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
                try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
                db.execSQL("DELETE FROM login");
                db.execSQL("DELETE FROM Profile");
                db.execSQL("DELETE FROM BsHamyarSelectServices");
                db.execSQL("DELETE FROM BsUserServices");
                db.execSQL("DELETE FROM education");
                db.execSQL("DELETE FROM exprtise");
                db.execSQL("DELETE FROM messages");
                db.execSQL("DELETE FROM services");
                db.execSQL("DELETE FROM servicesdetails");
                db.execSQL("DELETE FROM DateTB");
                db.execSQL("DELETE FROM FaktorUserDetailes");
                db.execSQL("DELETE FROM HeadFactor");
                db.execSQL("DELETE FROM HmFactorService");
                db.execSQL("DELETE FROM HmFactorTools");
                db.execSQL("DELETE FROM HmFactorTools_List");
                db.execSQL("DELETE FROM InsertFaktorUserDetailes");
                db.execSQL("DELETE FROM Unit");
                System.exit(0);
                arg0.dismiss();

            }
        });

        alertbox.show();
    }
    private void disable_menu(){
        //todo
    }
    private void ExitApplication()
    {
        //Exit All Activity And Kill Application
        AlertDialog.Builder alertbox = new AlertDialog.Builder(MainActivity.this);
        // set the message to display
        alertbox.setMessage("آیا می خواهید از برنامه خارج شوید ؟");

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
                System.exit(0);
                arg0.dismiss();

            }
        });

        alertbox.show();
    }

    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            startService(new Intent(getBaseContext(), ServiceGetNewJob.class));
            ExitApplication();
        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
    {
        Intent intent = new Intent(getApplicationContext(),Cls);
        intent.putExtra(VariableName, VariableValue);
        intent.putExtra(VariableName2, VariableValue2);
        MainActivity.this.startActivity(intent);
    }
    public void update(){
        String LastHamyarSelectUserServiceCode=null;
        String LastMessageCode=null;
        Cursor cursors = db.rawQuery("SELECT ifnull(MAX(code),0)as code FROM BsHamyarSelectServices", null);
        if(cursors.getCount()>0)
        {
            cursors.moveToNext();
            LastHamyarSelectUserServiceCode=cursors.getString(cursors.getColumnIndex("code"));
        }
        cursors = db.rawQuery("SELECT ifnull(MAX(code),0)as code FROM messages", null);
        if(cursors.getCount()>0)
        {
            cursors.moveToNext();
            LastMessageCode=cursors.getString(cursors.getColumnIndex("code"));
        }
        SyncMessage syncMessage=new SyncMessage(MainActivity.this,guid, hamyarcode,LastMessageCode,LastHamyarSelectUserServiceCode);
        syncMessage.AsyncExecute();

    }
    protected void onStart() {

        super.onStart();
        stopService(new Intent(getBaseContext(), ServiceGetNewJob.class));

    }
    protected void onStop() {

        super.onStop();
        startService(new Intent(getBaseContext(), ServiceGetNewJob.class));
    }
    protected void onPause() {

        super.onPause();
        startService(new Intent(getBaseContext(), ServiceGetNewJob.class));
    }
    protected void onDestroy() {

        super.onDestroy();
        startService(new Intent(getBaseContext(), ServiceGetNewJob.class));
    }
}
