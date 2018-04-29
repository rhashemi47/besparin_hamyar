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

    import java.io.IOException;

    import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

    public class Messages extends Activity {
        private String hamyarcode;
        private String guid;
        private DatabaseHelper dbh;
        private SQLiteDatabase db;
        private Button btnCredit;
        private Button btnOrders;
        private Button btnHome;
        @Override
        protected void attachBaseContext(Context newBase) {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages);
            btnCredit=(Button)findViewById(R.id.btnCredit);
            btnOrders=(Button)findViewById(R.id.btnOrders);
            btnHome=(Button)findViewById(R.id.btnHome);
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
            Cursor coursors = db.rawQuery("SELECT * FROM login",null);
            for(int i=0;i<coursors.getCount();i++){
                coursors.moveToNext();
                guid=coursors.getString(coursors.getColumnIndex("guid"));
                hamyarcode=coursors.getString(coursors.getColumnIndex("hamyarcode"));
            }

            db.close();
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
    }
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            Messages.this.LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
        {
            Intent intent = new Intent(getApplicationContext(),Cls);
            intent.putExtra(VariableName, VariableValue);
            intent.putExtra(VariableName2, VariableValue2);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Messages.this.startActivity(intent);
        }
    }
