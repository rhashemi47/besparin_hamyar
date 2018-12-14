	package com.besparina.it.hamyar;

    import android.app.Activity;
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
    import android.widget.LinearLayout;
    import android.widget.ListView;
    import android.widget.Spinner;
    import android.widget.TextView;
    import android.widget.Toast;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;

    public class Setting extends Activity {
        private String hamyarcode;
        private String guid;
        private String Show_linearLayoutSave_working_levels="0";
        private String Show_linearLayoutSave_working_Details="0";
        //**************************************
        private TextView btnCredit;
        private Button btnDutyToday;
        private Button btnServices_at_the_turn;
        private Button btnHome;
        private Button btnSave_working_levels;
        private Button btnSave_working_Details;
        private Button btnSaveStepJob;
        private DatabaseHelper dbh;
        private SQLiteDatabase db;
        //****************************
        private TextView tvtitleServiDetailName;
        private TextView tvtitleStepServise;
        private TextView tvtitleStep;
        private TextView tvUnit;
        private TextView tvUnitPrice;
        private EditText EttitleStep;
        private String EttitleToolsStepStr;
        private String EtPriceToolStr;
        private String EtBrandToolStr;
        private TextView tvtitleServiDetailNameTool;
        private TextView tvtitleStepServiseTool;
        private TextView tvtitleToolsStep;
        private TextView tvBrand;
        private TextView tvPriceTools;
        private EditText EttitleToolsStep;
        private EditText EtPriceTool;
        private ListView ListViewSaveSetpTool;
        private EditText EtBrandTool;
        private Spinner ServiceNameTool;
        private Spinner SpDitalNameServiceTools;
        private Button btnSaveStepJobTool;
        //        private Button btnSend;
        private List<String> labelsServiceNameDetailTools = new ArrayList<String>();
        private List<String> labelsServiceDetailNameTools;
        private List<String> listItemsTools;
        private ArrayAdapter<String> adapterListTools;
        private EditText EtUnitPrice;
        private ListView lvSetting;
        private Spinner SpUnit;
        private Spinner SpNameService;
        private Spinner SpDitalNameServiceWork;
        private Spinner SpDitalNameServiceDetail;
        private List<String> labels = new ArrayList<String>();
        private List<String> labelsServiceName = new ArrayList<String>();
        private List<String> labelsServiceDetailName;
        private List<String> listItems;
        private ArrayAdapter<String> adapterList;
        private HashMap<String,String> Unit_value=new HashMap<String, String>();
        private HashMap<String,String> Unit_key=new HashMap<String, String>();
        private String EttitleStepStr;
        private String UnitStr;
        private String EtUnitPriceStr;
        private LinearLayout linearLayoutSave_working_levels;
        private LinearLayout linearLayoutSave_working_Details;
        //****************************
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        btnCredit=(TextView)findViewById(R.id.btnCredit);
        btnServices_at_the_turn=(Button)findViewById(R.id.btnServices_at_the_turn);
        btnDutyToday=(Button)findViewById(R.id.btnDutyToday);
        btnHome=(Button)findViewById(R.id.btnHome);
        btnSave_working_levels=(Button)findViewById(R.id.btnSave_working_levels);
        btnSave_working_Details=(Button)findViewById(R.id.btnSave_working_Details);
        //**********************************
        EttitleStep = (EditText) findViewById(R.id.EttitleStep);
        EtUnitPrice = (EditText) findViewById(R.id.EtUnitPrice);
        lvSetting = (ListView) findViewById(R.id.ListViewSaveSetp);
        SpUnit = (Spinner) findViewById(R.id.SpUnit);
        SpNameService = (Spinner) findViewById(R.id.ServiceName);
        SpDitalNameServiceWork = (Spinner) findViewById(R.id.ServiDetailName);
        btnSaveStepJob = (Button) findViewById(R.id.btnSaveStepJob);
        //            btnSend = (Button) findViewById(R.id.btnSendSetting);
        tvtitleServiDetailName= (TextView) findViewById(R.id.tvtitleServiDetailName);
        tvtitleStepServise= (TextView) findViewById(R.id.tvtitleStepServise);
        tvtitleStep= (TextView) findViewById(R.id.tvtitleStep);
        tvUnit= (TextView) findViewById(R.id.tvUnit);
        tvUnitPrice= (TextView) findViewById(R.id.tvUnitPrice);
        btnSave_working_Details=(Button)findViewById(R.id.btnSave_working_Details);
        linearLayoutSave_working_levels= (LinearLayout) findViewById(R.id.linearLayoutSave_working_levels);
        linearLayoutSave_working_Details= (LinearLayout) findViewById(R.id.linearLayoutSave_working_Details);
        //**********************************
            EttitleToolsStep = (EditText) findViewById(R.id.EttitleToolsStep);
            EtPriceTool = (EditText) findViewById(R.id.EtPriceTool);
            ListViewSaveSetpTool = (ListView) findViewById(R.id.ListViewSaveSetpTool);
            EtBrandTool = (EditText) findViewById(R.id.EtBrandTool);
            ServiceNameTool = (Spinner) findViewById(R.id.ServiceNameTool);
            SpDitalNameServiceDetail = (Spinner) findViewById(R.id.ServiDetailNameTool);
            btnSaveStepJobTool = (Button) findViewById(R.id.btnSaveStepJobTool);
//            btnSend = (Button) findViewById(R.id.btnSendStepJobTool);
            tvtitleServiDetailNameTool = (TextView) findViewById(R.id.tvtitleServiDetailNameTool);
            tvtitleStepServiseTool = (TextView) findViewById(R.id.tvtitleStepServiseTool);
            tvtitleToolsStep = (TextView) findViewById(R.id.tvtitleToolsStep);
            tvBrand = (TextView) findViewById(R.id.tvBrand);
            tvPriceTools = (TextView) findViewById(R.id.tvPriceTools);
        //**********************************

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
            }

            //****************************************************************************************
            /*TextView tvAmountCredit=(TextView) findViewById(R.id.tvAmountCredit);
            try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
            Cursor cursor = db.rawQuery("SELECT * FROM AmountCredit", null);
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                String splitStr[] = (cursor.getString(cursor.getColumnIndex("Amount")).toString()).split("\\.");
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
            btnSave_working_levels.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Show_linearLayoutSave_working_levels.compareTo("0")==0)
                    {
                        Show_linearLayoutSave_working_levels="1";
                        Show_linearLayoutSave_working_Details="0";
                        linearLayoutSave_working_levels.setVisibility(View.VISIBLE);
                        linearLayoutSave_working_Details.setVisibility(View.GONE);
                    }
                    else 
                    {
                        Show_linearLayoutSave_working_levels="0";
                        linearLayoutSave_working_levels.setVisibility(View.GONE);
                    }
                }
            });
            btnSave_working_Details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Show_linearLayoutSave_working_Details.compareTo("0")==0)
                    {
                        Show_linearLayoutSave_working_Details="1";
                        Show_linearLayoutSave_working_levels="0";
                        linearLayoutSave_working_Details.setVisibility(View.VISIBLE);
                        linearLayoutSave_working_levels.setVisibility(View.GONE);
                    }
                    else 
                    {
                        Show_linearLayoutSave_working_Details="0";
                        linearLayoutSave_working_Details.setVisibility(View.GONE);
                    }
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
            //*****************************************
            db = dbh.getReadableDatabase();
            Cursor coursors = db.rawQuery("SELECT * FROM Unit", null);
            if (coursors.getCount() > 0) {
                for (int i = 0; i < coursors.getCount(); i++) {
                    coursors.moveToNext();
                    Unit_value.put(coursors.getString(coursors.getColumnIndex("Name")),coursors.getString(coursors.getColumnIndex("Code")));
                    Unit_key.put(coursors.getString(coursors.getColumnIndex("Code")),coursors.getString(coursors.getColumnIndex("Name")));
                    labels.add(coursors.getString(coursors.getColumnIndex("Name")));
                }

                addItemFromListWork(true);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels){
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
                SpUnit.setAdapter(dataAdapter);
            } else {
                SyncUnit unit = new SyncUnit(this,guid,hamyarcode);
                unit.AsyncExecute();
            }
            try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
            coursors = db.rawQuery("SELECT * FROM services", null);
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

                db.close();
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpNameService.setAdapter(dataAdapter);
            }
            SpNameService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    db = dbh.getReadableDatabase();
                    Cursor coursors = db.rawQuery("SELECT * FROM services WHERE servicename='"+adapterView.getItemAtPosition(i).toString()+"'", null);
                    if (coursors.getCount() > 0) {
                        coursors.moveToNext();
                        FillSpinnerChildWork(coursors.getString(coursors.getColumnIndex("code")));
                    }

                    db.close();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            EtUnitPrice.addTextChangedListener(new NumberTextWatcherForThousand(EtUnitPrice));
//            btnSave.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    addItemFromList(false);
//                }
//            });
//            btnSend.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                   SendFarctor();
//                }
//            });
            lvSetting.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                //وقتی برروی لیست چند ثانیه لمس شود
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int position, long arg3) {
                    removeItemFromList_work(position);
                    return true;
                }
            });
//****************************************
            addItemFromListDetail(true);
            try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
            coursors = db.rawQuery("SELECT * FROM services", null);
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
                        FillSpinnerChildDetail(coursors.getString(coursors.getColumnIndex("code")));
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
                    addItemFromListDetail(false);
                }
            });
            btnSaveStepJob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addItemFromListWork(false);
//                    SendFarctorWork();
                }
            });
            EtPriceTool.addTextChangedListener(new NumberTextWatcherForThousand(EtPriceTool));
            ListViewSaveSetpTool.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                //وقتی برروی لیست چند ثانیه لمس شود
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int position, long arg3) {
                    removeItemFromList_Detail(position);
                    return true;
                }
            });

        }
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            Setting.this.LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
        {
            Intent intent = new Intent(getApplicationContext(),Cls);
            intent.putExtra(VariableName, VariableValue);
            intent.putExtra(VariableName2, VariableValue2);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Setting.this.startActivity(intent);
        }
        private void addItemFromListWork(boolean FirestFill) {
            InternetConnection IC;
            IC = new InternetConnection(Setting.this);
            if(IC.isConnectingToInternet()==true)
            {
                if (!FirestFill) {
                    String temp;
                    EttitleStepStr = EttitleStep.getText().toString();
                    EtUnitPriceStr = EtUnitPrice.getText().toString().replace(",","");
                    UnitStr = SpUnit.getSelectedItem().toString();
                    if (EttitleStepStr.compareTo("") == 0 || EtUnitPriceStr.compareTo("") == 0)
                    {
                        Toast.makeText(Setting.this, "لطفا تمام فیلد ها را پر فرمایید", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String[] StrDital=SpDitalNameServiceWork.getSelectedItem().toString().split(":");
                        temp=SpDitalNameServiceWork.getSelectedItem().toString()+ "-" +EttitleStepStr+ "-" +UnitStr+ "-" +EtUnitPriceStr;
                        db = dbh.getReadableDatabase();
                        Cursor coursors = db.rawQuery("SELECT * FROM HmFactorService WHERE ServiceName='" + EttitleStepStr + "' AND PricePerUnit='" + EtUnitPriceStr + "'AND " +
                                "Unit='" +Unit_value.get(UnitStr) + "' AND ServiceDetaileCode='"+StrDital[0]+"'", null);
                        if (coursors.getCount() > 0)
                        {
                            Toast.makeText(Setting.this, "این مقدار تکراریست", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String[] StrDetail=SpDitalNameServiceWork.getSelectedItem().toString().split(":");
                            String query="INSERT INTO HmFactorService (ServiceName,PricePerUnit,Unit,ServiceDetaileCode) VALUES('" + EttitleStepStr + "','" + EtUnitPriceStr
                                    + "','" + Unit_value.get(UnitStr) + "','" +StrDetail[0] +"')";
                            try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
                            db.execSQL(query);
                            if (lvSetting.getCount() > 0) {
                                adapterList.add(temp);
                                lvSetting.setAdapter(adapterList);
                                SendFarctorWork();
                            } else {
                                listItems = new ArrayList<String>();
                                listItems.add(temp);
                                adapterList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems){
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

                                lvSetting.setAdapter(adapterList);
                                SendFarctorWork();
                            }
                        }
                    }
                }
                else
                {
                    db = dbh.getReadableDatabase();
                    String query="SELECT HmFactorService.*,Servicesdetails.name FROM HmFactorService " +
                            "LEFT JOIN " +
                            "Servicesdetails ON " +
                            "Servicesdetails.code=HmFactorService.ServiceDetaileCode WHERE Status='1'";
                    Cursor coursors = db.rawQuery(query, null);
                    for (int i = 0; i < coursors.getCount(); i++) {
                        coursors.moveToNext();
                        String temp;
                        //query="SELECT * FROM servicesdetails WHERE code='"+coursors.getString(coursors.getColumnIndex("ServiceDetaileCode"))+"'";
                        temp =coursors.getString(coursors.getColumnIndex("ServiceDetaileCode")) + ":" +
                                coursors.getString(coursors.getColumnIndex("name"))+ "-" +
                                coursors.getString(coursors.getColumnIndex("ServiceName")) + "-" +
                                Unit_key.get(coursors.getString(coursors.getColumnIndex("Unit")))+ "-" +
                                coursors.getString(coursors.getColumnIndex("PricePerUnit"));
                        if (lvSetting.getCount() > 0) {
                            adapterList.add(temp);
                            lvSetting.setAdapter(adapterList);
                        } else {
                            listItems = new ArrayList<String>();
                            listItems.add(temp);
                            adapterList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems){
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
                            lvSetting.setAdapter(adapterList);
                        }
                    }
                }
                db.close();
            }
            else
            {
                Toast.makeText(Setting.this, "لطفا ارتباط شبکه خود را چک کنید", Toast.LENGTH_SHORT).show();
            }

        }
        private void addItemFromListDetail(boolean FirestFill) {
            InternetConnection IC;
            IC = new InternetConnection(Setting.this);
            if (IC.isConnectingToInternet() == true) {
                if (!FirestFill) {
                    String temp;
                    EttitleToolsStepStr = EttitleToolsStep.getText().toString();
                    EtPriceToolStr = EtPriceTool.getText().toString().replace(",","");
                    EtBrandToolStr = EtBrandTool.getText().toString();
                    if (EttitleToolsStepStr.compareTo("") == 0 || EtPriceToolStr.compareTo("") == 0) {
                        Toast.makeText(this, "لطفا تمام فیلد ها را پر فرمایید", Toast.LENGTH_SHORT).show();
                    } else {
                        String[] StrDital = SpDitalNameServiceDetail.getSelectedItem().toString().split(":");
                        temp = SpDitalNameServiceDetail.getSelectedItem().toString() + "-" + EttitleToolsStepStr + "-" + EtBrandToolStr + "-" + EtPriceToolStr;
                        db = dbh.getReadableDatabase();
                        String query = "SELECT * FROM HmFactorTools WHERE ToolName='" + EttitleToolsStepStr
                                + "' AND Price='" + EtPriceToolStr
                                + "' AND ServiceDetaileCode='" + StrDital[0] +
                                "' AND BrandName='" + EtBrandToolStr + "'";
                        Cursor coursors = db.rawQuery(query, null);
                        if (coursors.getCount() > 0) {
                            Toast.makeText(this, "این مقدار تکراریست", Toast.LENGTH_SHORT).show();
                        } else {
                            String[] StrDetail = SpDitalNameServiceDetail.getSelectedItem().toString().split(":");
                            query = "INSERT INTO HmFactorTools (ToolName,Price,ServiceDetaileCode,BrandName) VALUES('" + EttitleToolsStepStr + "','" + EtPriceToolStr
                                    + "','" + StrDetail[0] + "','" + EtBrandToolStr + "')";
                            try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
                            db.execSQL(query);
                            if (ListViewSaveSetpTool.getCount() > 0) {
                                adapterList.add(temp);
                                ListViewSaveSetpTool.setAdapter(adapterList);
                                SendFarctorDetaile();
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
                                SendFarctorDetaile();
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
                Toast.makeText(Setting.this, "لطفا ارتباط شبکه خود را چک کنید", Toast.LENGTH_SHORT).show();
            }
        }
        void removeItemFromList_work(final int position) {
            final int deletePosition = position;
            android.app.AlertDialog.Builder alertbox = new android.app.AlertDialog.Builder(Setting.this);
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
                    String query="SELECT * FROM HmFactorService WHERE ServiceName='"+STR[1]+"' AND PricePerUnit='"+STR[3]+"' AND Unit='"+Unit_value.get(STR[2])+"'" +
                            " AND ServiceDetaileCode='"+STRDetails[0]+"' AND Status='1'";
                    Cursor c=db.rawQuery(query,null);
                    if(c.getCount()>0)
                    {
                        c.moveToNext();
                        SyncUpdateStepJobs syncUpdateStepJobs=new SyncUpdateStepJobs(Setting.this,guid,hamyarcode,c.getString(c.getColumnIndex("Code")),STRDetails[0],STR[1],STR[3],Unit_value.get(STR[2]),"0");
                        syncUpdateStepJobs.AsyncExecute();

                    }
                    listItems.remove(deletePosition);
                    adapterList.notifyDataSetChanged();


                    arg0.dismiss();

                }
            });

            alertbox.show();
        }
        void removeItemFromList_Detail(final int position) {
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
                        SyncUpdateStepDetailJobs syncUpdateStepDetailJobs=new SyncUpdateStepDetailJobs(Setting.this,guid,hamyarcode,c.getString(c.getColumnIndex("Code")),STRDetails[0],STR[1],STR[2],STR[3],"0");
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
        public void FillSpinnerChildWork(String ServiceCode){
            db = dbh.getReadableDatabase();
            Cursor coursors = db.rawQuery("SELECT * FROM servicesdetails WHERE servicename='"+ServiceCode+"'", null);
            if (coursors.getCount() > 0) {
                labelsServiceDetailName  = new ArrayList<String>();
                for (int i = 0; i < coursors.getCount(); i++) {
                    coursors.moveToNext();
                    labelsServiceDetailName.add(coursors.getString(coursors.getColumnIndex("code"))+":"+coursors.getString(coursors.getColumnIndex("name")));
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsServiceDetailName){
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
                SpDitalNameServiceWork.setAdapter(dataAdapter);
            }
        }
        public void FillSpinnerChildDetail(String ServiceCode){
            db = dbh.getReadableDatabase();
            Cursor coursors = db.rawQuery("SELECT * FROM servicesdetails WHERE servicename='"+ServiceCode+"'", null);
            if (coursors.getCount() > 0) {
                labelsServiceDetailName  = new ArrayList<String>();
                for (int i = 0; i < coursors.getCount(); i++) {
                    coursors.moveToNext();
                    labelsServiceDetailName.add(coursors.getString(coursors.getColumnIndex("code"))+":"+coursors.getString(coursors.getColumnIndex("name")));
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsServiceDetailName){
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
                SpDitalNameServiceDetail.setAdapter(dataAdapter);
            }
        }
        public void SendFarctorWork() {
            db = dbh.getReadableDatabase();
            Cursor coursors = db.rawQuery("SELECT * FROM HmFactorService WHERE Status='0' AND IsSend='0'", null);
            if (coursors.getCount() > 0)
            {
                for (int i = 0; i < coursors.getCount(); i++)
                {
                    coursors.moveToNext();
                    SyncSendStepJobs sendStepJobs=new SyncSendStepJobs(Setting.this,
                            guid,
                            hamyarcode,
                            coursors.getString(coursors.getColumnIndex("ServiceDetaileCode")),
                            coursors.getString(coursors.getColumnIndex("ServiceName")),
                            coursors.getString(coursors.getColumnIndex("PricePerUnit")),
                            coursors.getString(coursors.getColumnIndex("Unit"))
                    );
                    sendStepJobs.AsyncExecute();
                }
            }
            else
            {
                Toast.makeText(Setting.this, "لطفا آیتم جدیدی ایجاد کنید", Toast.LENGTH_LONG).show();
            }
        }
        public void SendFarctorDetaile() {
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
                Toast.makeText(Setting.this, "لطفا آیتم جدیدی ایجاد کنید", Toast.LENGTH_LONG).show();
            }

            db.close();
        }
    }
