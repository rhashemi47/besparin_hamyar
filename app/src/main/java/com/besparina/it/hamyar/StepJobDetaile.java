	package com.besparina.it.hamyar;

    import android.app.Activity;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.database.Cursor;
    import android.database.SQLException;
    import android.database.sqlite.SQLiteDatabase;
    import android.graphics.Typeface;
    import android.os.Bundle;
    import android.view.KeyEvent;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ListView;
    import android.widget.Spinner;
    import android.widget.TextView;
    import android.widget.Toast;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.List;

    import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

    public class StepJobDetaile extends Activity {
        private String hamyarcode;
        private String guid;
        private String EttitleToolsStepStr;
        private String EtPriceToolStr;
        private String EtBrandToolStr;
        private TextView tvtitleServiDetailNameTool;
        private TextView tvtitleStepServiseTool;
        private TextView tvtitleToolsStep;
        private TextView tvBrand;
        private TextView tvPriceTools;
        private DatabaseHelper dbh;
        private SQLiteDatabase db;
        private EditText EttitleToolsStep;
        private EditText EtPriceTool;
        private ListView ListViewSaveSetpTool;
        private EditText EtBrandTool;
        private Spinner ServiceNameTool;
        private Spinner SpDitalNameService;
        private Button btnSaveStepJobTool;
//        private Button btnSend;
        private List<String> labelsServiceName = new ArrayList<String>();
        private List<String> labelsServiceDetailName;
        private List<String> listItems;
        private ArrayAdapter<String> adapterList;
        private TextView btnCredit;
        private Button btnDutyToday;
        private Button btnServices_at_the_turn;
        private Button btnHome;

        @Override
        protected void attachBaseContext(Context newBase) {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.stepjobdetaile);
            btnCredit=(TextView)findViewById(R.id.btnCredit);
            btnServices_at_the_turn=(Button)findViewById(R.id.btnServices_at_the_turn);
            btnDutyToday=(Button)findViewById(R.id.btnDutyToday);
            btnHome=(Button)findViewById(R.id.btnHome);
            EttitleToolsStep = (EditText) findViewById(R.id.EttitleToolsStep);
            EtPriceTool = (EditText) findViewById(R.id.EtPriceTool);
            ListViewSaveSetpTool = (ListView) findViewById(R.id.ListViewSaveSetpTool);
            EtBrandTool = (EditText) findViewById(R.id.EtBrandTool);
            ServiceNameTool = (Spinner) findViewById(R.id.ServiceNameTool);
            SpDitalNameService = (Spinner) findViewById(R.id.ServiDetailNameTool);
            btnSaveStepJobTool = (Button) findViewById(R.id.btnSaveStepJobTool);
//            btnSend = (Button) findViewById(R.id.btnSendStepJobTool);
            tvtitleServiDetailNameTool = (TextView) findViewById(R.id.tvtitleServiDetailNameTool);
            tvtitleStepServiseTool = (TextView) findViewById(R.id.tvtitleStepServiseTool);
            tvtitleToolsStep = (TextView) findViewById(R.id.tvtitleToolsStep);
            tvBrand = (TextView) findViewById(R.id.tvBrand);
            tvPriceTools = (TextView) findViewById(R.id.tvPriceTools);
            //*******************************************************************

            Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/IRANSans.ttf");
            //**********************************************************************************
            tvtitleServiDetailNameTool.setTypeface(FontMitra);
            tvtitleStepServiseTool.setTypeface(FontMitra);
            tvtitleToolsStep.setTypeface(FontMitra);
            tvBrand.setTypeface(FontMitra);
            tvPriceTools.setTypeface(FontMitra);
            //*************************************************************************
            tvtitleServiDetailNameTool.setTextSize(18);
            tvtitleStepServiseTool.setTextSize(18);
            tvtitleToolsStep.setTextSize(18);
            tvBrand.setTextSize(18);
            tvPriceTools.setTextSize(18);
            //*************************************************************************
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
            }

            //****************************************************************************************
            /*TextView tvAmountCredit=(TextView) findViewById(R.id.tvAmountCredit);
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
            }*/
            //****************************************************************************************
            addItemFromList(true);
            db=dbh.getReadableDatabase();
            Cursor coursors = db.rawQuery("SELECT * FROM services", null);
            if (coursors.getCount() > 0) {
                for (int i = 0; i < coursors.getCount(); i++) {
                    coursors.moveToNext();
                    labelsServiceName.add(coursors.getString(coursors.getColumnIndex("servicename")));
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsServiceName){
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);

                        Typeface typeface=Typeface.createFromAsset(getAssets(), "font/IRANSans.ttf");
                        ((TextView) v).setTypeface(typeface);

                        return v;
                    }

                    public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                        View v =super.getDropDownView(position, convertView, parent);


                        Typeface typeface=Typeface.createFromAsset(getAssets(), "font/IRANSans.ttf");
                        ((TextView) v).setTypeface(typeface);

                        return v;
                    }
                };
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ServiceNameTool.setAdapter(dataAdapter);
            }

            db.close();
            ServiceNameTool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    db = dbh.getReadableDatabase();
                    Cursor coursors = db.rawQuery("SELECT * FROM services WHERE servicename='"+adapterView.getItemAtPosition(i).toString()+"'", null);
                    if (coursors.getCount() > 0) {
                        coursors.moveToNext();
                        FillSpinnerChild(coursors.getString(coursors.getColumnIndex("code")));
                    }

                    db.close();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            btnSaveStepJobTool.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addItemFromList(false);
                }
            });
//            btnSend.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    SendFarctor();
//                }
//            });
            EtPriceTool.addTextChangedListener(new NumberTextWatcherForThousand(EtPriceTool));
            ListViewSaveSetpTool.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                //وقتی برروی لیست چند ثانیه لمس شود
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int position, long arg3) {
                    removeItemFromList(position);
                    return true;
                }
            });

            btnCredit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadActivity(Credit.class, "guid",  guid, "hamyarcode", hamyarcode);
                }
            });
            btnDutyToday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadActivity(ListServiceAtTheTurn.class, "guid", guid, "hamyarcode", hamyarcode);
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

        private void addItemFromList(boolean FirestFill) {
            InternetConnection IC;
            IC = new InternetConnection(StepJobDetaile.this);
            if (IC.isConnectingToInternet() == true) {
                if (!FirestFill) {
                    String temp;
                    EttitleToolsStepStr = EttitleToolsStep.getText().toString();
                    EtPriceToolStr = EtPriceTool.getText().toString().replace(",","");
                    EtBrandToolStr = EtBrandTool.getText().toString();
                    if (EttitleToolsStepStr.compareTo("") == 0 || EtPriceToolStr.compareTo("") == 0) {
                        Toast.makeText(this, "لطفا تمام فیلد ها را پر فرمایید", Toast.LENGTH_SHORT).show();
                    } else {
                        String[] StrDital = SpDitalNameService.getSelectedItem().toString().split(":");
                        temp = SpDitalNameService.getSelectedItem().toString() + "-" + EttitleToolsStepStr + "-" + EtBrandToolStr + "-" + EtPriceToolStr;
                        db = dbh.getReadableDatabase();
                        String query = "SELECT * FROM HmFactorTools WHERE ToolName='" + EttitleToolsStepStr
                                + "' AND Price='" + EtPriceToolStr
                                + "' AND ServiceDetaileCode='" + StrDital[0] +
                                "' AND BrandName='" + EtBrandToolStr + "'";
                        Cursor coursors = db.rawQuery(query, null);
                        if (coursors.getCount() > 0) {
                            Toast.makeText(this, "این مقدار تکراریست", Toast.LENGTH_SHORT).show();
                        } else {
                            String[] StrDetail = SpDitalNameService.getSelectedItem().toString().split(":");
                            query = "INSERT INTO HmFactorTools (ToolName,Price,ServiceDetaileCode,BrandName) VALUES('" + EttitleToolsStepStr + "','" + EtPriceToolStr
                                    + "','" + StrDetail[0] + "','" + EtBrandToolStr + "')";
                            db = dbh.getWritableDatabase();
                            db.execSQL(query);
                            if (ListViewSaveSetpTool.getCount() > 0) {
                                adapterList.add(temp);
                                ListViewSaveSetpTool.setAdapter(adapterList);
                                SendFarctor();
                            } else {
                                listItems = new ArrayList<String>();
                                listItems.add(temp);
                                adapterList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems) {
                                    public View getView(int position, View convertView, ViewGroup parent) {
                                        View v = super.getView(position, convertView, parent);

                                        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/IRANSans.ttf");
                                        ((TextView) v).setTypeface(typeface);

                                        return v;
                                    }

                                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                        View v = super.getDropDownView(position, convertView, parent);


                                        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/IRANSans.ttf");
                                        ((TextView) v).setTypeface(typeface);

                                        return v;
                                    }
                                };
                                ListViewSaveSetpTool.setAdapter(adapterList);
                                SendFarctor();
                            }
                        }
                    }
                } else {
                    db = dbh.getReadableDatabase();
                    String query = "SELECT HmFactorTools.*,Servicesdetails.name FROM HmFactorTools " +
                            "LEFT JOIN " +
                            "Servicesdetails ON " +
                            "Servicesdetails.code=HmFactorTools.ServiceDetaileCode WHERE Status='1'";
                    Cursor coursors = db.rawQuery(query, null);
                    for (int i = 0; i < coursors.getCount(); i++) {
                        coursors.moveToNext();
                        String temp;
                        //query="SELECT * FROM servicesdetails WHERE code='"+coursors.getString(coursors.getColumnIndex("ServiceDetaileCode"))+"'";
                        temp = coursors.getString(coursors.getColumnIndex("ServiceDetaileCode")) + ":" +
                                coursors.getString(coursors.getColumnIndex("name")) + "-" +
                                coursors.getString(coursors.getColumnIndex("ToolName")) + "-" +
                                coursors.getString(coursors.getColumnIndex("BrandName")) + "-" +
                                coursors.getString(coursors.getColumnIndex("Price"));
                        if (ListViewSaveSetpTool.getCount() > 0) {
                            adapterList.add(temp);
                            ListViewSaveSetpTool.setAdapter(adapterList);
                        } else {
                            listItems = new ArrayList<String>();
                            listItems.add(temp);
                            adapterList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems) {
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    View v = super.getView(position, convertView, parent);

                                    Typeface typeface = Typeface.createFromAsset(getAssets(), "font/IRANSans.ttf");
                                    ((TextView) v).setTypeface(typeface);

                                    return v;
                                }

                                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                    View v = super.getDropDownView(position, convertView, parent);


                                    Typeface typeface = Typeface.createFromAsset(getAssets(), "font/IRANSans.ttf");
                                    ((TextView) v).setTypeface(typeface);

                                    return v;
                                }
                            };

                            db.close();
                            ListViewSaveSetpTool.setAdapter(adapterList);
                        }
                    }
                }
            }
            else
            {
                Toast.makeText(StepJobDetaile.this, "لطفا ارتباط شبکه خود را چک کنید", Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                String query="DELETE FROM HmFactorTools WHERE IsSend='0' AND Status='0'";
                db=dbh.getWritableDatabase();
                db.execSQL(query);
                this.LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
            }
            return super.onKeyDown(keyCode, event);
        }

        public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2) {
            Intent intent = new Intent(getApplicationContext(), Cls);
            intent.putExtra(VariableName, VariableValue);
            intent.putExtra(VariableName2, VariableValue2);
            this.startActivity(intent);
        }

        void removeItemFromList(final int position) {
            final int deletePosition = position;
            android.app.AlertDialog.Builder alertbox = new android.app.AlertDialog.Builder(this);
            // set the message to display
            alertbox.setMessage("آیا می خواهید این مورد را حذف کنید؟");

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
                    String[] STR=listItems.get(position).toString().split("-");
                    String[] STRDetails=STR[0].split(":");
                    db = dbh.getReadableDatabase();
                    String query="SELECT * FROM HmFactorTools WHERE ToolName='"+STR[1]+"' AND Price='"+STR[3]+"'" +
                            " AND ServiceDetaileCode='"+STRDetails[0]+"' AND BrandName='"+STR[2]+"' AND Status='1'";
                    Cursor c=db.rawQuery(query,null);
                    if(c.getCount()>0)
                    {
                        c.moveToNext();
                        SyncUpdateStepDetailJobs syncUpdateStepDetailJobs=new SyncUpdateStepDetailJobs(StepJobDetaile.this,guid,hamyarcode,c.getString(c.getColumnIndex("Code")),STRDetails[0],STR[1],STR[2],STR[3],"0");
                        syncUpdateStepDetailJobs.AsyncExecute();

                    }

                    db.close();
                    listItems.remove(deletePosition);
                    adapterList.notifyDataSetChanged();


                    arg0.dismiss();

                }
            });

            alertbox.show();
        }
        public void FillSpinnerChild(String ServiceCode){
            db = dbh.getReadableDatabase();
            Cursor coursors = db.rawQuery("SELECT * FROM servicesdetails WHERE servicename='"+ServiceCode+"'", null);
            if (coursors.getCount() > 0) {
                labelsServiceDetailName  = new ArrayList<String>();
                for (int i = 0; i < coursors.getCount(); i++) {
                    coursors.moveToNext();
                    labelsServiceDetailName.add(coursors.getString(coursors.getColumnIndex("code"))+":"+coursors.getString(coursors.getColumnIndex("name")));
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsServiceDetailName);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpDitalNameService.setAdapter(dataAdapter);
            }

            db.close();
        }
        public void SendFarctor() {
            db = dbh.getReadableDatabase();
            Cursor coursors = db.rawQuery("SELECT * FROM HmFactorTools WHERE Status='0' AND IsSend='0'", null);
            if (coursors.getCount() > 0)
            {
                for (int i = 0; i < coursors.getCount(); i++)
                {
                    coursors.moveToNext();
                    SyncSendStepDetailJobs syncSendStepDetailJobs=new SyncSendStepDetailJobs(this,
                            guid,
                            hamyarcode,
                            coursors.getString(coursors.getColumnIndex("ServiceDetaileCode")),
                            coursors.getString(coursors.getColumnIndex("ToolName")),
                            coursors.getString(coursors.getColumnIndex("BrandName")),
                            coursors.getString(coursors.getColumnIndex("Price"))
                    );

                    syncSendStepDetailJobs.AsyncExecute();
                }
            }
            else
            {
                Toast.makeText(StepJobDetaile.this, "لطفا آیتم جدیدی ایجاد کنید", Toast.LENGTH_LONG).show();
            }

            db.close();
        }
    }