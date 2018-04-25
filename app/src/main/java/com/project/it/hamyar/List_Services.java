	package com.project.it.hamyar;

    import android.annotation.SuppressLint;
    import android.app.Activity;
    import android.app.AlertDialog;
    import android.app.TimePickerDialog;
    import android.content.Context;
    import android.content.Intent;
    import android.database.Cursor;
    import android.database.SQLException;
    import android.database.sqlite.SQLiteDatabase;
    import android.os.Bundle;
    import android.support.annotation.NonNull;
    import android.support.design.widget.BottomNavigationView;
    import android.text.Editable;
    import android.text.TextWatcher;
    import android.view.KeyEvent;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ListView;
    import android.widget.Spinner;
    import android.widget.TimePicker;

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
        private EditText etArea;
        private Spinner spExpert;
        private List<String> labelssp;
        //*****************************************************
        private ArrayList<HashMap<String, String>> valuse ;

        @Override
        protected void attachBaseContext(Context newBase) {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.list_services);
            btnCredit = (Button) findViewById(R.id.btnCredit);
            btnOrders = (Button) findViewById(R.id.btnOrders);
            btnHome = (Button) findViewById(R.id.btnHome);
            //************************************************************
            etFromDate = (EditText) findViewById(R.id.etFromDate);
            etToDate = (EditText) findViewById(R.id.etToDate);
            etFromTime = (EditText) findViewById(R.id.etFromTime);
            etToTime = (EditText) findViewById(R.id.etToTime);
            etArea = (EditText) findViewById(R.id.etArea);
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
        if(etArea.getText().toString().length()>0) {
            query = query + " AND AddressText LIKE '%" + etArea.getText().toString()+"%'";
        }
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
                    "وضعیت: "+((coursors.getString(coursors.getColumnIndex("IsEmergency")).compareTo("0")==1? "عادی":"فوری")));
            map.put("Code",coursors.getString(coursors.getColumnIndex("Code")));
            map.put("UserPhone",coursors.getString(coursors.getColumnIndex("UserPhone")));
            valuse.add(map);
        }
        db.close();
        AdapterServices dataAdapter=new AdapterServices(this,valuse,guid,hamyarcode);
        lvServices.setAdapter(dataAdapter);
    }
}
