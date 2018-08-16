	package com.besparina.it.hamyar;

    import android.app.Activity;
    import android.content.Intent;
    import android.database.Cursor;
    import android.database.SQLException;
    import android.database.sqlite.SQLiteDatabase;
    import android.os.Bundle;
    import android.support.annotation.NonNull;
    import android.support.design.widget.BottomNavigationView;
    import android.view.KeyEvent;
    import android.view.MenuItem;
    import android.widget.ListView;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.HashMap;

    public class List_Messages extends Activity {
        private String hamyarcode;
        private String guid;
        private ListView lvMessage;
        private DatabaseHelper dbh;
        private SQLiteDatabase db;
        private String[] title;
//        private String[] content;
        private Integer[] imgID;
        private Integer[] rowID;
        private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_messages);
        lvMessage=(ListView)findViewById(R.id.listViewMessages);
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
        db=dbh.getReadableDatabase();
        Cursor coursors = db.rawQuery("SELECT * FROM messages WHERE IsDelete='0'",null);
        if(coursors.getCount()>0)
        {
            title=new String[coursors.getCount()];
//            content=new String[coursors.getCount()];
            rowID=new Integer[coursors.getCount()];
            imgID=new Integer[coursors.getCount()];
            for(int i=0;i<coursors.getCount();i++){
                coursors.moveToNext();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Title",coursors.getString(coursors.getColumnIndex("Title")));
//                map.put("Content",coursors.getString(coursors.getColumnIndex("Content")));
                map.put("Code",coursors.getString(coursors.getColumnIndex("Code")));
                map.put("IsReade",coursors.getString(coursors.getColumnIndex("IsReade")));
                valuse.add(map);
            }
            AdapterMessage dataAdapter=new AdapterMessage(List_Messages.this,valuse);
            lvMessage.setAdapter(dataAdapter);
        }
            String Query="UPDATE UpdateApp SET Status='1'";
            db=dbh.getWritableDatabase();
            db.execSQL(Query);
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
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            List_Messages.this.LoadActivity(MainActivity.class, "guid", guid, "hamyarcode", hamyarcode);
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
    }
