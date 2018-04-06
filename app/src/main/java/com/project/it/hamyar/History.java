	package com.project.it.hamyar;

    import android.app.Activity;
    import android.content.Context;
    import android.content.Intent;
    import android.database.Cursor;
    import android.database.SQLException;
    import android.database.sqlite.SQLiteDatabase;
    import android.graphics.Typeface;
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

    public class History extends Activity {
        private String hamyarcode;
        private String guid;
        private TextView tvHistory;
        private ListView lstHistory;
        private DatabaseHelper dbh;
        private SQLiteDatabase db;
        private Button btnCredit;
        private Button btnOrders;
        private Button btnHome;
        private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
        @Override
        protected void attachBaseContext(Context newBase) {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.history);
            btnCredit=(Button)findViewById(R.id.btnCredit);
            btnOrders=(Button)findViewById(R.id.btnOrders);
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
                Cursor coursors = db.rawQuery("SELECT * FROM login", null);
                for (int i = 0; i < coursors.getCount(); i++) {
                    coursors.moveToNext();
                    guid = coursors.getString(coursors.getColumnIndex("guid"));
                    hamyarcode = coursors.getString(coursors.getColumnIndex("hamyarcode"));
                }
            }
            Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");//set font for page
            tvHistory=(TextView)findViewById(R.id.tvHistory);
            tvHistory.setTypeface(FontMitra);
            tvHistory.setTextSize(18);
            lstHistory=(ListView)findViewById(R.id.lstHistory);
            String Query = "UPDATE UpdateApp SET Status='1'";
            db = dbh.getWritableDatabase();
            db.execSQL(Query);
            String query = "SELECT BsHamyarSelectServices.*,Servicesdetails.name FROM BsHamyarSelectServices " +
                    "LEFT JOIN " +
                    "Servicesdetails ON " +
                    "Servicesdetails.code=BsHamyarSelectServices.ServiceDetaileCode";
            Cursor coursors = db.rawQuery(query, null);
            for (int i = 0; i < coursors.getCount(); i++) {
                coursors.moveToNext();
                String Content = "";
                HashMap<String, String> map = new HashMap<String, String>();
                try {
                    Content +="شماره درخواست: " + coursors.getString(coursors.getColumnIndex("Code")) + "\n";
                }
                catch (Exception ex) {
                    //todo
                }
                try {
                    Content +="موضوع: "+ coursors.getString(coursors.getColumnIndex("name")) + "\n";
                }
                catch (Exception ex) {
                    //todo
                }
                try {
                    Content += "نام متقاضی: " + coursors.getString(coursors.getColumnIndex("UserName")) + " " + coursors.getString(coursors.getColumnIndex("UserFamily")) + "\n";
                } catch (Exception ex) {
                    //todo
                }
                try {
                    Content += "تاریخ شروع: " + coursors.getString(coursors.getColumnIndex("StartDate")) + "\n";
                } catch (Exception ex) {
                    //todo
                }
                try {
                    Content += "تاریخ پایان: " + coursors.getString(coursors.getColumnIndex("EndDate")) + "\n";
                } catch (Exception ex) {
                    //todo
                }
                try {
                    Content += "از ساعت: " + coursors.getString(coursors.getColumnIndex("StartTime")) + "\n";
                } catch (Exception ex) {
                    //todo
                }
                try {
                    Content += "تا ساعت: " + coursors.getString(coursors.getColumnIndex("EndTime")) + "\n";
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("PeriodicServices")).toString().compareTo("1") == 0) {
                        Content += "خدمت دوره ای: " + "روزانه" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("PeriodicServices")).toString().compareTo("2") == 0) {
                        Content += "خدمت دوره ای: " + "هفتگی" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("PeriodicServices")).toString().compareTo("3") == 0) {
                        Content += "خدمت دوره ای: " + "هفته در میان" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("PeriodicServices")).toString().compareTo("4") == 0) {
                        Content += "خدمت دوره ای: " + "ماهانه" + "\n";
                    }

                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("MaleCount")).toString().compareTo("0") != 0) {
                        Content += "تعداد همیار مرد: " + coursors.getString(coursors.getColumnIndex("MaleCount")) + "\n";
                    }
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("FemaleCount")).toString().compareTo("0") != 0) {
                        Content += "تعداد همیار زن: " + coursors.getString(coursors.getColumnIndex("FemaleCount")) + "\n";
                    }
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("HamyarCount")).toString().compareTo("0") != 0) {
                        Content += "تعداد همیار: " + coursors.getString(coursors.getColumnIndex("HamyarCount")) + "\n";
                    }
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("EducationTitle")).toString().compareTo("0") != 0) {
                        Content += "عنوان آموزش: " + coursors.getString(coursors.getColumnIndex("EducationTitle")) + "\n";
                    }
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("EducationGrade")).toString().compareTo("0") != 0) {
                        Content += "پایه تحصیلی: " + coursors.getString(coursors.getColumnIndex("EducationGrade")) + "\n";
                    }
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("1") == 0) {
                        Content += "رشته تحصیلی: " + "ابتدایی" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("2") == 0) {
                        Content += "رشته تحصیلی: " + "متوسطه اول" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("3") == 0) {
                        Content += "رشته تحصیلی: " + "علوم تجربی" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("4") == 0) {
                        Content += "رشته تحصیلی: " + "ریاضی و فیزیک" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("5") == 0) {
                        Content += "رشته تحصیلی: " + "انسانی" + "\n";
                    }
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("0") != 0) {
                        if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("2") == 0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        } else if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("3") == 0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        } else if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("4") == 0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        } else if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("5") == 0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        } else if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("6") == 0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        } else if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("7") == 0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        } else if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("7") == 0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        } else {
                            Content += "رشته هنری: " + coursors.getString(coursors.getColumnIndex("ArtField")) + "\n";
                        }
                    }
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("1") == 0) {
                        Content += "زبان: " + "انگلیسی" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("2") == 0) {
                        Content += "زبان: " + "روسی" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("3") == 0) {
                        Content += "زبان: " + "آلمانی" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("4") == 0) {
                        Content += "زبان: " + "فرانسه" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("5") == 0) {
                        Content += "زبان: " + "ترکی" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("6") == 0) {
                        Content += "زبان: " + "عربی" + "\n";
                    }
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("StudentGender")).toString().compareTo("1") == 0) {
                        Content += "جنسیت دانش آموز: " + "زن" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("StudentGender")).toString().compareTo("2") == 0) {
                        Content += "جنسیت دانش آموز: " + "مرد" + "\n";
                    }
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("CarWashType")).toString().compareTo("1") == 0) {
                        Content += "نوع سرویس: " + "روشویی" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("CarWashType")).toString().compareTo("2") == 0) {
                        Content += "نوع سرویس: " + "روشویی و توشویی" + "\n";
                    }
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("CarType")).toString().compareTo("1") == 0) {
                        Content += "نوع خودرو: " + "سواری" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("CarType")).toString().compareTo("2") == 0) {
                        Content += "نوع سرویس: " + "شاسی و نیم شاسی" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("CarType")).toString().compareTo("3") == 0) {
                        Content += "نوع سرویس: " + "ون" + "\n";
                    }

                } catch (Exception ex) {
                    //todo
                }
                try {
                    Content += "توضیحات: " + coursors.getString(coursors.getColumnIndex("Description")) + "\n";
                } catch (Exception ex) {
                    //todo
                }
                try {
                    Content += "آدرس: " + coursors.getString(coursors.getColumnIndex("AddressText")) + "\n";
                } catch (Exception ex) {
                    //todo
                }
                try {
                    Content += "وضعیت: " + ((coursors.getString(coursors.getColumnIndex("IsEmergency")).compareTo("0") == 1 ? "عادی" : "فوری"));
                } catch (Exception ex) {
                    //todo
                }
                map.put("name",Content);
                map.put("Code",coursors.getString(coursors.getColumnIndex("Code")));
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
                AdapterHistory dataAdapter=new AdapterHistory(this,valuse,guid,hamyarcode);
                lstHistory.setAdapter(dataAdapter);
            }

//                BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
//
//                bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                        if (item.getItemId() == R.id.credite) {
////                    Toast.makeText(getBaseContext(), "اعتبارات", Toast.LENGTH_LONG).show();
//                            LoadActivity(Credit.class, "guid",  guid, "hamyarcode", hamyarcode);
//                            return true;
//                        } else if (item.getItemId() == R.id.History	) {
//                            //LoadActivity(History.class, "guid", guid, "hamyarcode", hamyarcode);
//                            return true;
//                        } else if (item.getItemId() == R.id.home) {
//                            LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
//                            return true;
//                        }
//                        return false;
//                    }
//                });
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
            History.this.LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
        {
            Intent intent = new Intent(getApplicationContext(),Cls);
            intent.putExtra(VariableName, VariableValue);
            intent.putExtra(VariableName2, VariableValue2);
            History.this.startActivity(intent);
        }
    }
