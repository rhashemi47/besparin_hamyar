package com.project.it.hamyar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TabHost;
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

public class MainMenu extends Activity {
    private String hamyarcode;
    private String guid;
    private String updateflag;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private Drawer drawer=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        //************************************************************************************
        TabHost tabs = (TabHost) findViewById(R.id.tabHost);

        tabs.setup();

        /*** تب اول ***/
        TabHost.TabSpec tab1 = tabs.newTabSpec("tag 1");
        tab1.setIndicator("فرصت شغلی",getResources().getDrawable(R.drawable.profile));
        tab1.setContent(R.id.tabJob);
        tabs.addTab(tab1);

        /*** تب دوم ***/
        TabHost.TabSpec tab2 = tabs.newTabSpec("tag 2");
        tab2.setIndicator("وظایف",getResources().getDrawable(R.drawable.profile));
        tab2.setContent(R.id.tabDuty);
        tabs.addTab(tab2);
        //************************************************************************************
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
        try
        {
            hamyarcode = getIntent().getStringExtra("hamyarcode");
            guid = getIntent().getStringExtra("guid");
            // updateflag = getIntent().getStringExtra("updateflag");
        }
        catch(Exception e)
        {
            Cursor cursors=null;
            db=dbh.getReadableDatabase();
            cursors = db.rawQuery("SELECT * FROM login", null);
            if(cursors.getCount()>0)
            {
                cursors.moveToNext();
                hamyarcode=cursors.getString(cursors.getColumnIndex("hamyarcode"));
                guid=cursors.getString(cursors.getColumnIndex("guid"));
            }
        }
        try
        {
            updateflag = getIntent().getStringExtra("updateflag");
        }
        catch(Exception e)
        {
            updateflag ="1";
        }
        //****************************************************************************************
        int drawerGravity= Gravity.END;
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
                .withAccountHeader(headerResult)
                .withDrawerGravity(drawerGravity)
                .withShowDrawerOnFirstLaunch(true)
                .addDrawerItems(
                        new SectionDrawerItem().withName("").withDivider(false).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
                        new SecondaryDrawerItem().withName(R.string.Profile).withIcon(R.drawable.profile).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.Job).withIcon(R.drawable.jobs).withSelectable(false),
                        new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
                        new SecondaryDrawerItem().withName(R.string.Yourcommitment).withIcon(R.drawable.yourcommitment).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.Ourcommitment).withIcon(R.drawable.ourcommitment).withSelectable(false),
                        new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
                        new SecondaryDrawerItem().withName(R.string.Messages).withIcon(R.drawable.messages).withBadge("19").withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.GiftBank).withIcon(R.drawable.gift).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.Contact).withIcon(R.drawable.contact).withSelectable(false),
                        new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
                        new SecondaryDrawerItem().withName(R.string.action_settings).withIcon(R.drawable.setting).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.Help).withIcon(R.drawable.help).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.About).withIcon(R.drawable.about).withSelectable(false),
        new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
                new SecondaryDrawerItem().withName(R.string.Exit).withIcon(R.drawable.profile).withSelectable(false),
                new SecondaryDrawerItem().withName(R.string.Logout).withIcon(R.drawable.profile).withSelectable(false)
                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position){
                            case 2:
//                                Toast.makeText(MainMenu.this, "پروفایل", Toast.LENGTH_SHORT).show();
                                LoadActivity(Profile.class, "guid", guid,"hamyarcode",hamyarcode);
                                break;
                            case 3:
                                Toast.makeText(MainMenu.this, "فرصت شغلی", Toast.LENGTH_SHORT).show();
                                break;
                            case 5:
//                                Toast.makeText(MainMenu.this, "تعهدات شما", Toast.LENGTH_SHORT).show();
                                LoadActivity(YourCommitment.class, "guid", guid,"hamyarcode",hamyarcode);
                                break;
                            case 6:
//                                Toast.makeText(MainMenu.this, "تعهدات ما", Toast.LENGTH_SHORT).show();
                                LoadActivity(OurCommitment.class, "guid", guid,"hamyarcode",hamyarcode);
                                break;
                            case 8:
//                                Toast.makeText(MainMenu.this, "پیام ها", Toast.LENGTH_SHORT).show();
                                LoadActivity(List_Messages.class, "guid", guid,"hamyarcode",hamyarcode);
                                break;
                            case 9:
//                                Toast.makeText(MainMenu.this, "بانک هدیه", Toast.LENGTH_SHORT).show();
                                LoadActivity(GiftBank.class, "guid", guid,"hamyarcode",hamyarcode);
                                break;
                            case 10:
//                                Toast.makeText(MainMenu.this, "تماس با پشتیبانی", Toast.LENGTH_SHORT).show();
                                LoadActivity(Contact.class, "guid", guid,"hamyarcode",hamyarcode);
                                break;
                            case 12:
//                                Toast.makeText(MainMenu.this, "تنظیمات", Toast.LENGTH_SHORT).show();
                                //LoadActivity(StepJob.class, "guid", guid,"hamyarcode",hamyarcode);
                                Builder alertbox = new AlertDialog.Builder(MainMenu.this);
                                // set the message to display
                                alertbox.setMessage("تنظیمات پیش فاکتور");

                                // set a negative/no button and create a listener
                                alertbox.setPositiveButton("مراحل کاری", new DialogInterface.OnClickListener() {
                                    // do something when the button is clicked
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        LoadActivity(StepJob.class, "guid", guid,"hamyarcode",hamyarcode);
                                        arg0.dismiss();
                                    }
                                });

                                // set a positive/yes button and create a listener
                                alertbox.setNegativeButton("ملزومات کاری", new DialogInterface.OnClickListener() {
                                    // do something when the button is clicked
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        //Declare Object From Get Internet Connection Status For Check Internet Status
                                        LoadActivity(StepJob.class, "guid", guid,"hamyarcode",hamyarcode);
                                        arg0.dismiss();

                                    }
                                });

                                alertbox.show();
                                break;
                            case 13:
//                                Toast.makeText(MainMenu.this, "راهنما", Toast.LENGTH_SHORT).show();
                                LoadActivity(Help.class, "guid", guid,"hamyarcode",hamyarcode);
                                break;
                            case 14:
//                                Toast.makeText(MainMenu.this, "درباره ما", Toast.LENGTH_SHORT).show();
                                LoadActivity(About.class, "guid", guid,"hamyarcode",hamyarcode);
                                break;
                            case 16:
//                                Toast.makeText(MainMenu.this, "خروج از برنامه", Toast.LENGTH_SHORT).show();
                                ExitApplication();
                                break;
                            case 17:
//                                Toast.makeText(MainMenu.this, "خروج از کاربری", Toast.LENGTH_SHORT).show();
                                Logout();
                                break;
                        }
                        return true;
                    }
                })
                .build();


//        if(hamyarcode.compareTo("0")== 0 || guid.compareTo("0")== 0){
//        	disable_menu();
//        }
//        if(updateflag.compareTo("0")==0){
//            update();
//        }
//        btnUpdate.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//
//            }
//        });
	}


    public void Logout() {
        //Exit All Activity And Kill Application
        Builder alertbox = new AlertDialog.Builder(MainMenu.this);
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
                System.exit(0);
                arg0.dismiss();

            }
        });

        alertbox.show();
    }
    private void disable_menu(){
	}
	private void ExitApplication()
	{
		//Exit All Activity And Kill Application
		 Builder alertbox = new AlertDialog.Builder(MainMenu.this);
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
	    	ExitApplication();
	    }

	    return super.onKeyDown( keyCode, event );
	}
	  public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
		{
			Intent intent = new Intent(getApplicationContext(),Cls);
			intent.putExtra(VariableName, VariableValue);
			intent.putExtra(VariableName2, VariableValue2);
			MainMenu.this.startActivity(intent);
		}
	  public void update(){
	  }
}
