package com.project.it.hamyar;

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
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryToggleDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainMenu extends AppCompatActivity {
    private String hamyarcode;
    private String guid;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private Drawer drawer=null;
    private String countMessage;
    private String countVisit;
    private Button btnDuty;
    private Button btnServices;
    private Button btnCredit;
    private Button btnOrders;
    private Button btnHome;
    private boolean IsActive=true;
    ArrayList<String> slides;
    ImageView imageView;
    Custom_ViewFlipper viewFlipper;
    GestureDetector mGestureDetector;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");//set font for page
        btnDuty=(Button)findViewById(R.id.btnDuty);
        btnServices=(Button)findViewById(R.id.btnServices);
        //****************************************************************
        btnDuty.setTypeface(FontMitra);
        btnServices.setTypeface(FontMitra);
        //****************************************************************
        btnDuty.setTextSize(18);
        btnServices.setTextSize(18);
        //****************************************************************
        btnCredit=(Button)findViewById(R.id.btnCredit);
        btnOrders=(Button)findViewById(R.id.btnOrders);
        btnHome=(Button)findViewById(R.id.btnHome);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(MainMenu.this));
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
                db=dbh.getReadableDatabase();
                cursors = db.rawQuery("SELECT * FROM login", null);
                if(cursors.getCount()>0)
                {
                    cursors.moveToNext();
                    String Result=cursors.getString(cursors.getColumnIndex("islogin"));
                    if(Result.compareTo("0")==0)
                    {
                        LoadActivity(Login.class,"hamyarcode","0","guid","0");
                    }
                    else
                    {
                        hamyarcode = cursors.getString(cursors.getColumnIndex("hamyarcode"));
                        guid = cursors.getString(cursors.getColumnIndex("guid"));
                        db=dbh.getReadableDatabase();
                        coursors = db.rawQuery("SELECT * FROM UpdateApp",null);
                        if(coursors.getCount()>0)
                        {
                            coursors.moveToNext();
                            if(coursors.getString(coursors.getColumnIndex("Status")).compareTo("1")==0){
                                String Query="UPDATE UpdateApp SET Status='0'";
                                db=dbh.getWritableDatabase();
                                db.execSQL(Query);
                                //update();
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

            db.close();
        }
        catch(Exception e)
        {
            throw new Error("Error Opne Activity");
        }
        stopService(new Intent(getBaseContext(), ServiceGetNewJob.class));
        startService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));
        startService(new Intent(getBaseContext(), ServiceGetLocation.class));
        startService(new Intent(getBaseContext(), ServiceGetSliderPic.class));
        //**************************************************************************
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        viewFlipper = (Custom_ViewFlipper) findViewById(R.id.vf);
        db=dbh.getReadableDatabase();
        coursors = db.rawQuery("SELECT * FROM Slider",null);
        if(coursors.getCount()>0) {
            Bitmap bpm[]=new Bitmap[coursors.getCount()];
            String link[]=new String[coursors.getCount()];
            for (int j=0;j<coursors.getCount();j++) {
                coursors.moveToNext();
                viewFlipper.setVisibility(View.VISIBLE);
                //slides.add();
                bpm[j]=convertToBitmap(coursors.getString(coursors.getColumnIndex("Pic")));
                link[j]=coursors.getString(coursors.getColumnIndex("Link"));
            }
            db.close();
            int i = 0;
            while(i<bpm.length)
            {
//                imageView = new  com.flaviofaria.kenburnsview.KenBurnsView(this);
                imageView=new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                //ImageLoader.getInstance().displayImage(slides.get(i),imageView);
                imageView.setImageBitmap(bpm[i]);
                imageView.setTag(link[i]);
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String link="";
//                        link = ((ImageView)v).getTag().toString();
//                        Toast.makeText(getBaseContext(), link, Toast.LENGTH_LONG).show();
//                    }
//                });
                viewFlipper.addView(imageView);
                i++;
            }
//            RandomTransitionGenerator randomTransitionGenerator = new RandomTransitionGenerator(3000,new FastOutLinearInInterpolator());
//            imageView.setTransitionGenerator(randomTransitionGenerator);



            Paint paint = new Paint();
            paint.setColor(ContextCompat.getColor(this,R.color.colorPrimary));
            viewFlipper.setPaintCurrent(paint);
            paint = new Paint();

            paint.setColor(ContextCompat.getColor(this,android.R.color.white));
            viewFlipper.setPaintNormal(paint);

            viewFlipper.setRadius(10);
            viewFlipper.setMargin(5);

            CustomGestureDetector customGestureDetector = new CustomGestureDetector();
            mGestureDetector = new GestureDetector(MainMenu.this, customGestureDetector);

            viewFlipper.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    mGestureDetector.onTouchEvent(motionEvent);
                    return true;
                }
            });
        }
        else
        {
            viewFlipper.setVisibility(View.GONE);
        }


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
        //****************************************************************************************
        CreateMenu(toolbar);
        //***************************************************************************************************************************
        btnDuty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goto Duty Page List
                LoadActivity(List_Dutys.class, "guid",  guid, "hamyarcode", hamyarcode);
            }
        });
        btnServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goto Services Page List
                LoadActivity(List_Services.class, "guid",  guid, "hamyarcode", hamyarcode);
            }
        });
	}
    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // Swipe left (next)
            if (e1.getX() > e2.getX()) {
                viewFlipper.setInAnimation(MainMenu.this, R.anim.left_in);
                viewFlipper.setOutAnimation(MainMenu.this, R.anim.left_out);

                viewFlipper.showNext();
            }else if (e1.getX() < e2.getX()) {
                viewFlipper.setInAnimation(MainMenu.this, R.anim.right_in);
                viewFlipper.setOutAnimation(MainMenu.this, R.anim.right_out);

                viewFlipper.showPrevious();
            }

            viewFlipper.setInAnimation(MainMenu.this, R.anim.left_in);
            viewFlipper.setOutAnimation(MainMenu.this, R.anim.left_out);

            return super.onFling(e1, e2, velocityX, velocityY);
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
                System.exit(0);
                arg0.dismiss();

            }
        });

        alertbox.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void CreateMenu(Toolbar toolbar){
        Bitmap bmp=BitmapFactory.decodeResource(getResources(),R.drawable.useravatar);
        String name="";
        String family="";
        db=dbh.getReadableDatabase();
        Cursor coursors = db.rawQuery("SELECT * FROM Profile",null);
        if(coursors.getCount()>0) {
            coursors.moveToNext();
            name=coursors.getString(coursors.getColumnIndex("Name"));
            family= coursors.getString(coursors.getColumnIndex("Fam"));
            bmp=convertToBitmap(coursors.getString(coursors.getColumnIndex("Pic")));
            db.close();
        }
        else
        {
            name="کاربر";
            family="مهمان";
        }

        int drawerGravity= Gravity.END;
            Configuration config = getResources().getConfiguration();
            if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                drawerGravity= Gravity.START;
            }



        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.menu_header)
                .addProfiles(
                        new ProfileDrawerItem().withName(name+" "+family).withIcon(bmp)// withIcon(getResources().getDrawable(R.drawable.personpic))
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
                        new SecondaryDrawerItem().withName(R.string.Profile).withIcon(R.drawable.profile).withSelectable(false),
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
                                            SyncProfile profile = new SyncProfile(MainMenu.this, c.getString(c.getColumnIndex("guid")), c.getString(c.getColumnIndex("hamyarcode")));
                                            profile.AsyncExecute();
                                        }
                                    }
                                    else
                                    {
                                        LoadActivity(Profile.class, "guid", guid,"hamyarcode",hamyarcode);
                                    }
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
                                db = dbh.getReadableDatabase();
                                c = db.rawQuery("SELECT * FROM login",null);
                                if(c.getCount()>0) {
                                    c.moveToNext();

                                    LoadActivity(YourCommitment.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
                                }
                                db.close();
                                break;
                            case 4:db = dbh.getReadableDatabase();
                                c = db.rawQuery("SELECT * FROM login",null);
                                if(c.getCount()>0) {
                                    c.moveToNext();

                                    LoadActivity(OurCommitment.class, "guid",  c.getString(c.getColumnIndex("guid")), "hamyarcode", c.getString(c.getColumnIndex("hamyarcode")));
                                }
                                db.close();
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
//                                Toast.makeText(MainMenu.this, "تنظیمات", Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder alertbox = new AlertDialog.Builder(MainMenu.this);
                                // set the message to display
                                alertbox.setMessage("تنظیمات پیش فاکتور");

                                // set a negative/no button and create a listener
                                alertbox.setPositiveButton("مراحل کاری", new DialogInterface.OnClickListener() {
                                    // do something when the button is clicked
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        db=dbh.getReadableDatabase();
                                        Cursor  c = db.rawQuery("SELECT * FROM login",null);
                                        if(c.getCount()>0) {
                                            c.moveToNext();

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
////                                Toast.makeText(MainMenu.this, "خروج از برنامه", Toast.LENGTH_SHORT).show();
//                                ExitApplication();
//                                break;
                            case 12:
//                                Toast.makeText(MainMenu.this, "خروج از کاربری", Toast.LENGTH_SHORT).show();
                                Logout();
                                break;
                            case 14:
                                Toast.makeText(MainMenu.this, "تلگرام", Toast.LENGTH_SHORT).show();
                                break;
                            case 15:
                                Toast.makeText(MainMenu.this, "اینستاگرام", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                })
                .build();
    }
    private void ExitApplication()
    {
        //Exit All Activity And Kill Application
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
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
            stopService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));
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
        this.startActivity(intent);
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
    protected void onStart() {

        super.onStart();
        stopService(new Intent(getBaseContext(), ServiceGetNewJob.class));
        startService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));

    }
    protected void onStop() {

        super.onStop();
        stopService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));
        startService(new Intent(getBaseContext(), ServiceGetNewJob.class));
    }
    protected void onPause() {

        super.onPause();
        stopService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));
        startService(new Intent(getBaseContext(), ServiceGetNewJob.class));
    }
    protected void onDestroy() {

        super.onDestroy();
        stopService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));
        startService(new Intent(getBaseContext(), ServiceGetNewJob.class));
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
}
