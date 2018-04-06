	package com.project.it.hamyar;

    import android.app.Activity;
    import android.content.Intent;
    import android.database.sqlite.SQLiteDatabase;
    import android.graphics.Typeface;
    import android.os.Bundle;
    import android.view.KeyEvent;
    import android.widget.TextView;

    public class YourCommitment extends Activity {
        private String hamyarcode;
        private String guid;
        private TextView txtContent;
        private DatabaseHelper dbh;
        private SQLiteDatabase db;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yourcommitment);
        hamyarcode = getIntent().getStringExtra("hamyarcode").toString();
        guid = getIntent().getStringExtra("guid").toString();
        Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");//set font for page
        txtContent=(TextView)findViewById(R.id.tvTextOurcommitment);
        txtContent.setTypeface(FontMitra);
    }
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            YourCommitment.this.LoadActivity(MainActivity.class, "guid", guid, "hamyarcode", hamyarcode);
        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
        {
            Intent intent = new Intent(getApplicationContext(),Cls);
            intent.putExtra(VariableName, VariableValue);
            intent.putExtra(VariableName2, VariableValue2);
            YourCommitment.this.startActivity(intent);
        }
    }
