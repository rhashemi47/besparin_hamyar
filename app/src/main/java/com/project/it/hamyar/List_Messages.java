	package com.project.it.hamyar;

    import android.app.Activity;
    import android.content.Context;
    import android.content.Intent;
    import android.database.Cursor;
    import android.database.SQLException;
    import android.database.sqlite.SQLiteDatabase;
    import android.os.Bundle;
    import android.support.annotation.NonNull;
    import android.support.design.widget.BottomNavigationView;
    import android.view.KeyEvent;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ListView;
    import android.widget.TextView;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.HashMap;

    import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

    public class List_Messages extends Activity {
        private String hamyarcode;
        private String guid;
        private TextView tvNotMessage;
        private ListView lvMessage;
        private DatabaseHelper dbh;
        private SQLiteDatabase db;
        private Button btnCredit;
        private Button btnOrders;
        private Button btnHome;
        private String[] title;
//        private String[] content;
        private Integer[] imgID;
        private Integer[] rowID;
        private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
        @Override
        protected void attachBaseContext(Context newBase) {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_messages);
            btnCredit=(Button)findViewById(R.id.btnCredit);
            btnOrders=(Button)findViewById(R.id.btnOrders);
            btnHome=(Button)findViewById(R.id.btnHome);
        lvMessage=(ListView)findViewById(R.id.listViewMessages);
        tvNotMessage=(TextView) findViewById(R.id.tvNotMessage);
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
            hamyarcode = getIntent().getStringExtra("hamyarcode").toString();
            guid = getIntent().getStringExtra("guid").toString();
        }
        catch (Exception e)
        {
            db=dbh.getReadableDatabase();
            Cursor coursors = db.rawQuery("SELECT * FROM login",null);
            for(int i=0;i<coursors.getCount();i++){
                coursors.moveToNext();
                guid=coursors.getString(coursors.getColumnIndex("guid"));
                hamyarcode=coursors.getString(coursors.getColumnIndex("hamyarcode"));
            }
        }
            //******************************************************************************
            Preparedata();
            //*****************************************************************************
//            BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
//
//            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                    if (item.getItemId() == R.id.credite) {
////                    Toast.makeText(getBaseContext(), "اعتبارات", Toast.LENGTH_LONG).show();
//                        LoadActivity(Credit.class, "guid",  guid, "hamyarcode", hamyarcode);
//                        return true;
//                    } else if (item.getItemId() == R.id.History	) {
////                     History.this.LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
//                        return true;
//                    } else if (item.getItemId() == R.id.home) {
////                    Toast.makeText(getBaseContext(), "صفحه اصلی", Toast.LENGTH_LONG).show();
//                        return true;
//                    }
//                    return false;
//                }
//            });
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
            List_Messages.this.LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
        {
            Intent intent = new Intent(getApplicationContext(),Cls);
            intent.putExtra(VariableName, VariableValue);
            intent.putExtra(VariableName2, VariableValue2);
            List_Messages.this.startActivity(intent);
        }
        public void Preparedata()
        {
            tvNotMessage.setVisibility(View.VISIBLE);
            lvMessage.setVisibility(View.GONE);
            db=dbh.getReadableDatabase();
            Cursor coursors = db.rawQuery("SELECT * FROM messages WHERE IsDelete='0'",null);
            if(coursors.getCount()>0)
            {
                tvNotMessage.setVisibility(View.GONE);
                lvMessage.setVisibility(View.VISIBLE);
                title=new String[coursors.getCount()];
                rowID=new Integer[coursors.getCount()];
                imgID=new Integer[coursors.getCount()];
                for(int i=0;i<coursors.getCount();i++){
                    coursors.moveToNext();
                    String subStr="";
                    int len=0,charselectlen=50;
                    HashMap<String, String> map = new HashMap<String, String>();
                    len=coursors.getString(coursors.getColumnIndex("Content")).length();
                    if(len<=charselectlen){
                        subStr=coursors.getString(coursors.getColumnIndex("Content"));
                    }
                    else
                    {
                        subStr=coursors.getString(coursors.getColumnIndex("Content")).substring(0,charselectlen);
                    }
                    map.put("Title",coursors.getString(coursors.getColumnIndex("Title")));
                    map.put("Content",subStr+"\n"+coursors.getString(coursors.getColumnIndex("InsertDate")));
                    map.put("Code",coursors.getString(coursors.getColumnIndex("Code")));
                    map.put("IsReade",coursors.getString(coursors.getColumnIndex("IsReade")));
                    valuse.add(map);
                }
                AdapterMessage dataAdapter=new AdapterMessage(List_Messages.this,valuse);
                lvMessage.setAdapter(dataAdapter);
            }
        }
    }
