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
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.besparina.it.hamyar.Date.ChangeDate;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Rm on 02/02/2018.
 */

public class Save_Per_Factor extends Activity {
    private String hamyarcode;
    private String guid;
    private String TitleTool;
    private String BrandName;
    private String Price;
    private String Amont;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private String BsUserServicesID;
    private String tab;
    private String ServiceDetaileCode;
    private Spinner SPTitleTools;
    private Spinner SpTitleStepJob;
    private EditText EtUnitValuePrice;
    private EditText EtToolValuePrice;
    private EditText EtDescription;
    private TextView tvUnit;
    private TextView tvUnitPrice;
    private TextView tvBrand;
    private TextView tvPriceTools;
    private TextView tvTotalSumStep;
    private TextView tvTotalSumTool;
    private TextView tvTitleStepJob;
    private TextView tvUnitPriceTitle;
    private TextView tvUnitValuePrice;
    private TextView tvTotalSumStepTitle;
    private TextView tvCheckTitleTool;
    private TextView tvTitleTools;
    private TextView tvPriceToolsTitle;
    private TextView tvToolValuePrice;
    private TextView tvTotalSumToolTitle;
    private TextView tvDescription;
    private CheckBox CheckTitleTools;
    private Button btnSendPerFactor;
    private Button btnSaveTool;
    private ListView ListTools;
    private ArrayAdapter<String> adapterList;
    private List<String> listItems;
    private List<String> labelsServiceDetailName;
    private LinearLayout LinerTitleTools;
    private LinearLayout LinerBrand;
    private LinearLayout LinerPriceTool;
    private LinearLayout LinerValueTools;
    private LinearLayout LinerListTools;
    private LinearLayout LinerTotalSumStep;
    private LinearLayout LinerTotalSumTool;
    private HashMap<String,String > mapStep=new HashMap<String, String>();
    private HashMap<String,String > mapTool=new HashMap<String, String>();
    private TextView btnCredit;
    private Button btnDutyToday;
    private Button btnServices_at_the_turn;
    private Button btnHome;
    private String back_activity;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_per_factor);
        Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/IRANSans.ttf");//set font for page
        //********************************************************************

        btnCredit=(TextView)findViewById(R.id.btnCredit);
        btnServices_at_the_turn=(Button)findViewById(R.id.btnServices_at_the_turn);
        btnDutyToday=(Button)findViewById(R.id.btnDutyToday);
        btnHome=(Button)findViewById(R.id.btnHome);
        SpTitleStepJob=(Spinner)findViewById(R.id.SpTitleStepJob);
        SPTitleTools=(Spinner)findViewById(R.id.SPTitleTools);
        EtUnitValuePrice=(EditText) findViewById(R.id.EtUnitValuePrice);
        EtToolValuePrice=(EditText) findViewById(R.id.EtToolValuePrice);
        EtDescription=(EditText) findViewById(R.id.EtDescription);
        tvUnit=(TextView) findViewById(R.id.tvUnit);
        tvUnitPrice=(TextView) findViewById(R.id.tvUnitPrice);
        tvBrand=(TextView) findViewById(R.id.tvBrand);
        tvPriceTools=(TextView) findViewById(R.id.tvPriceTools);
        tvTotalSumStep=(TextView) findViewById(R.id.tvTotalSumStep);
        tvTotalSumTool=(TextView) findViewById(R.id.tvTotalSumTool);
        tvPriceTools=(TextView) findViewById(R.id.tvPriceTools);
        tvTitleStepJob=(TextView) findViewById(R.id.tvTitleStepJob);
        tvUnitPriceTitle=(TextView) findViewById(R.id.tvUnitPriceTitle);
        tvUnitValuePrice=(TextView) findViewById(R.id.tvUnitValuePrice);
        tvTotalSumStepTitle=(TextView) findViewById(R.id.tvTotalSumStepTitle);
        tvCheckTitleTool=(TextView) findViewById(R.id.tvCheckTitleTool);
        tvTitleTools=(TextView) findViewById(R.id.tvTitleTools);
        tvPriceToolsTitle=(TextView) findViewById(R.id.tvPriceToolsTitle);
        tvToolValuePrice=(TextView) findViewById(R.id.tvToolValuePrice);
        tvTotalSumToolTitle=(TextView) findViewById(R.id.tvTotalSumToolTitle);
        tvDescription=(TextView) findViewById(R.id.tvDescription);
        CheckTitleTools=(CheckBox) findViewById(R.id.CheckTitleTools);
        btnSendPerFactor=(Button) findViewById(R.id.btnSendPerFactor);
        btnSaveTool=(Button) findViewById(R.id.btnSaveTool);
        ListTools=(ListView) findViewById(R.id.ListTools);
        //**********************************************************
        LinerTitleTools=(LinearLayout) findViewById(R.id.LinerTitleTools);
        LinerBrand=(LinearLayout) findViewById(R.id.LinerBrand);
        LinerPriceTool=(LinearLayout) findViewById(R.id.LinerPriceTool);
        LinerValueTools=(LinearLayout) findViewById(R.id.LinerValueTools);
        LinerListTools=(LinearLayout) findViewById(R.id.LinerListTools);
        LinerTotalSumStep=(LinearLayout) findViewById(R.id.LinerTotalSumStep);
        LinerTotalSumTool=(LinearLayout) findViewById(R.id.LinerTotalSumTool);
        //******************************************************************************
        EtUnitValuePrice.setTypeface(FontMitra);
        EtToolValuePrice.setTypeface(FontMitra);
        EtDescription.setTypeface(FontMitra);
        tvUnit.setTypeface(FontMitra);
        tvUnitPrice.setTypeface(FontMitra);
        tvBrand.setTypeface(FontMitra);
        tvPriceTools.setTypeface(FontMitra);
        tvTotalSumStep.setTypeface(FontMitra);
        tvTotalSumTool.setTypeface(FontMitra);
        tvPriceTools.setTypeface(FontMitra);
        tvTitleStepJob.setTypeface(FontMitra);
        tvUnitPriceTitle.setTypeface(FontMitra);
        tvUnitValuePrice.setTypeface(FontMitra);
        tvTotalSumStepTitle.setTypeface(FontMitra);
        tvCheckTitleTool.setTypeface(FontMitra);
        tvTitleTools.setTypeface(FontMitra);
        tvPriceToolsTitle.setTypeface(FontMitra);
        tvToolValuePrice.setTypeface(FontMitra);
        tvTotalSumToolTitle.setTypeface(FontMitra);
        tvDescription.setTypeface(FontMitra);
        CheckTitleTools.setTypeface(FontMitra);
        btnSendPerFactor.setTypeface(FontMitra);
        btnSaveTool.setTypeface(FontMitra);
        //******************************************************************************
        EtUnitValuePrice.setTextSize(18);
        EtToolValuePrice.setTextSize(18);
        EtDescription.setTextSize(18);
        tvUnit.setTextSize(18);
        tvUnitPrice.setTextSize(18);
        tvBrand.setTextSize(18);
        tvPriceTools.setTextSize(18);
        tvTotalSumStep.setTextSize(18);
        tvTotalSumTool.setTextSize(18);
        tvPriceTools.setTextSize(18);
        tvTitleStepJob.setTextSize(18);
        tvUnitPriceTitle.setTextSize(18);
        tvUnitValuePrice.setTextSize(18);
        tvTotalSumStepTitle.setTextSize(18);
        tvCheckTitleTool.setTextSize(18);
        tvTitleTools.setTextSize(18);
        tvPriceToolsTitle.setTextSize(18);
        tvToolValuePrice.setTextSize(18);
        tvTotalSumToolTitle.setTextSize(18);
        tvDescription.setTextSize(18);
        CheckTitleTools.setTextSize(18);
        btnSendPerFactor.setTextSize(18);
        btnSaveTool.setTextSize(18);
        //******************************************************************************
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

            db.close();
        }
        try
        {
            BsUserServicesID = getIntent().getStringExtra("BsUserServicesID").toString();
            tab = getIntent().getStringExtra("tab").toString();
            ServiceDetaileCode = getIntent().getStringExtra("ServiceDetaileCode").toString();
            back_activity = getIntent().getStringExtra("back_activity").toString();
        }
        catch (Exception e)
        {
            //todo
        }
        db = dbh.getWritableDatabase();
        String query="DELETE FROM HmFactorTools_List";
        db.execSQL(query);
        db.close();

        //****************************************************************************************
        TextView tvAmountCredit=(TextView) findViewById(R.id.tvAmountCredit);
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
        }
        //****************************************************************************************
        FillSpinnerStep();
        FillSpinnerTools();
        ShowOrHidde();
        CheckTitleTools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowOrHidde();
            }
        });
        btnSendPerFactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendFarctor();
            }
        });
        btnSaveTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemFromList();
            }
        });
        EtToolValuePrice.addTextChangedListener(new NumberTextWatcherForThousand(EtToolValuePrice));
        EtToolValuePrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                    if(EtToolValuePrice.length()<=0 )
                    {
                        String PriceStep;
                        Integer Result;
                        PriceStep=tvPriceTools.getText().toString().replace("/",".");
                        Result=0*Integer.parseInt(PriceStep);
                        tvTotalSumTool.setText(Float.toString(Result));
                    }
                    else
                    {
                        DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###,###");
                        String PriceStep;
                        String Amount;
                        double Result;
                        try {
                            Amount= EtToolValuePrice.getText().toString().replace(",","");
                            Result =  df.parse(Amount).longValue();
                            PriceStep=tvPriceTools.getText().toString();
                            Result=Double.parseDouble(Amount)*Double.parseDouble(PriceStep);
                            tvTotalSumTool.setText(df.format(Result));
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
            }
        });
        EtUnitValuePrice.addTextChangedListener(new NumberTextWatcherForThousand(EtUnitValuePrice));
        EtUnitValuePrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                    if(EtUnitValuePrice.length()<=0 )
                    {
                        String PriceStep;
//                        String Amount;
                        Integer Result;
//                        Amount= EtUnitValuePrice.getText().toString();
                        PriceStep=tvUnitPrice.getText().toString();
                        Result=0*Integer.parseInt(PriceStep);
                        tvTotalSumStep.setText(Integer.toString(Result));
                    }
                    else
                    {
                        DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###,###");
                        String PriceStep;
                        String Amount;
                        double Result;
                        try {
                            Amount= EtUnitValuePrice.getText().toString().replace(",","");
                            Result =  df.parse(Amount).longValue();
                            PriceStep=tvUnitPrice.getText().toString();
                            Result=Double.parseDouble(Amount)*Double.parseDouble(PriceStep);
                            tvTotalSumStep.setText(df.format(Result));
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
            }
        });
        SpTitleStepJob.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                db = dbh.getReadableDatabase();
                String temp=SpTitleStepJob.getSelectedItem().toString();
                String codeStep=mapStep.get(temp);
                Cursor coursors = db.rawQuery("SELECT HmFactorService.*,Unit.Name FROM HmFactorService" +
                        " LEFT JOIN " +
                        "Unit ON HmFactorService.Unit=Unit.Code " +
                        "WHERE HmFactorService.Code='"+codeStep+"'", null);
                if (coursors.getCount() > 0) {
                    coursors.moveToNext();
                    tvUnit.setText(coursors.getString(coursors.getColumnIndex("Name")));
                    tvUnitPrice.setText(coursors.getString(coursors.getColumnIndex("PricePerUnit")));
                    EtUnitValuePrice.setText("0");
                }

                db.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        SPTitleTools.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                db = dbh.getReadableDatabase();
                String codeStep=mapTool.get(SPTitleTools.getSelectedItem().toString());
                Cursor coursors = db.rawQuery("SELECT * FROM HmFactorTools WHERE Code='"+codeStep+"'", null);
                if (coursors.getCount() > 0) {
                    coursors.moveToNext();
                        tvBrand.setText(coursors.getString(coursors.getColumnIndex("BrandName")));
                        tvPriceTools.setText(coursors.getString(coursors.getColumnIndex("Price")));
                        EtToolValuePrice.setText("0");
                    }

                db.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ListTools.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

                LoadActivity(List_Dutys.class, "guid", guid, "hamyarcode", hamyarcode);
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
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            String query="DELETE FROM HmFactorTools_List";
            db=dbh.getWritableDatabase();
            db.execSQL(query);

            db.close();
            Save_Per_Factor.this.LoadActivity_Select(ViewJob.class, "guid", guid, "hamyarcode", hamyarcode,"BsUserServicesID", BsUserServicesID, "tab", tab,"back_activity",back_activity);
        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
    {
        Intent intent = new Intent(getApplicationContext(),Cls);
        intent.putExtra(VariableName, VariableValue);
        intent.putExtra(VariableName2, VariableValue2);
        Save_Per_Factor.this.startActivity(intent);
    }
    public void LoadActivity_Select(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2, String VariableName3, String VariableValue3, String VariableName4, String VariableValue4, String VariableName5, String VariableValue5)
    {
        Intent intent = new Intent(getApplicationContext(),Cls);
        intent.putExtra(VariableName, VariableValue);
        intent.putExtra(VariableName2, VariableValue2);
        intent.putExtra(VariableName3, VariableValue3);
        intent.putExtra(VariableName4, VariableValue4);
        intent.putExtra(VariableName5, VariableValue5);
        Save_Per_Factor.this.startActivity(intent);
    }
    public void FillSpinnerStep(){
        db = dbh.getReadableDatabase();
        Cursor coursors = db.rawQuery("SELECT * FROM HmFactorService WHERE ServiceDetaileCode='"+ServiceDetaileCode+"' AND Status='1'", null);
        if (coursors.getCount() > 0) {
            labelsServiceDetailName  = new ArrayList<String>();
            for (int i = 0; i < coursors.getCount(); i++) {
                coursors.moveToNext();
                mapStep.put(coursors.getString(coursors.getColumnIndex("ServiceName")),coursors.getString(coursors.getColumnIndex("Code")));
                labelsServiceDetailName.add(coursors.getString(coursors.getColumnIndex("ServiceName")));
            }

            db.close();
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
            SpTitleStepJob.setAdapter(dataAdapter);
        }
    }
    public void FillSpinnerTools(){

        db = dbh.getReadableDatabase();
        Cursor coursors = db.rawQuery("SELECT * FROM HmFactorTools WHERE ServiceDetaileCode='"+ServiceDetaileCode+"'", null);
        if (coursors.getCount() > 0) {
            labelsServiceDetailName  = new ArrayList<String>();
            for (int i = 0; i < coursors.getCount(); i++) {
                coursors.moveToNext();
                mapTool.put(coursors.getString(coursors.getColumnIndex("ToolName")),coursors.getString(coursors.getColumnIndex("Code")));
                labelsServiceDetailName.add(coursors.getString(coursors.getColumnIndex("ToolName")));
            }

            db.close();
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
            SPTitleTools.setAdapter(dataAdapter);
        }
    }
    public void SendFarctor() {
        String[] DateSp= ChangeDate.getCurrentDate().split("/");
        String Year=DateSp[0];
        String Mounth=DateSp[1];
        String Day=DateSp[2];
        try {
                    SyncGetFactorUsersHeadCode syncGetFactorUsersHeadCode = new SyncGetFactorUsersHeadCode(Save_Per_Factor.this,
                            guid,
                            hamyarcode,
                            BsUserServicesID,
                            Year,
                            Mounth,
                            Day,
                            EtDescription.getText().toString(),
                            CheckTitleTools.isChecked(),
                            mapStep.get(SpTitleStepJob.getSelectedItem().toString())
                            , EtUnitValuePrice.getText().toString());
                    syncGetFactorUsersHeadCode.AsyncExecute();
            }
        catch(Exception ex)
            {
                Toast.makeText(getApplicationContext(), "مقادیر به درستی وارد نشده است", Toast.LENGTH_LONG).show();
            }
    }
    public void ShowOrHidde()
    {
        if(CheckTitleTools.isChecked()){
            LinerTitleTools.setVisibility(View.VISIBLE);
            LinerBrand.setVisibility(View.VISIBLE);
            LinerPriceTool.setVisibility(View.VISIBLE);
            LinerValueTools.setVisibility(View.VISIBLE);
            LinerTotalSumTool.setVisibility(View.VISIBLE);
            btnSaveTool.setVisibility(View.VISIBLE);
        }
        else
        {
            LinerTitleTools.setVisibility(View.GONE);
            LinerBrand.setVisibility(View.GONE);
            LinerPriceTool.setVisibility(View.GONE);
            LinerValueTools.setVisibility(View.GONE);
            LinerTotalSumTool.setVisibility(View.GONE);
            btnSaveTool.setVisibility(View.GONE);
        }
    }
    private void addItemFromList() {
            String temp;
            TitleTool = SPTitleTools.getSelectedItem().toString();
            BrandName = tvBrand.getText().toString();
            Price=tvPriceTools.getText().toString();
            Amont=EtToolValuePrice.getText().toString().replace(",","");
            float FAmount=Float.parseFloat(Amont);
            Amont=Float.toString(FAmount);
            if (Amont.compareTo("0.0")==0 || Amont.length()<=0) {
                Toast.makeText(Save_Per_Factor.this, "لطفا  فیلد مقدار را پر فرمایید", Toast.LENGTH_SHORT).show();
            } else {
                temp=TitleTool+ "-" +BrandName+ "-" +Price+ "-" +Amont;
                db = dbh.getReadableDatabase();
                Cursor coursors = db.rawQuery("SELECT * FROM HmFactorTools_List WHERE ToolName='" + TitleTool + "' AND BrandName='" + BrandName + "'AND " +
                        "Price='" +Price+"' AND " +
                        "Amount='" +Amont+ "' AND ServiceDetaileCode='"+ServiceDetaileCode+"'", null);
                if (coursors.getCount() > 0) {
                    Toast.makeText(Save_Per_Factor.this, "این مقدار تکراریست", Toast.LENGTH_SHORT).show();
                } else {
                    String query="INSERT INTO HmFactorTools_List (Code,ToolName,BrandName,Price,Amount,ServiceDetaileCode) VALUES('" +mapTool.get(SPTitleTools.getSelectedItem().toString())+"','"+ TitleTool + "','" + BrandName
                            + "','" + Price + "','" + Amont + "','" +ServiceDetaileCode+"')";
                    db = dbh.getWritableDatabase();
                    db.execSQL(query);
                    if (ListTools.getCount() > 0) {
                        adapterList.add(temp);
                        ListTools.setAdapter(adapterList);
                    } else {
                        listItems = new ArrayList<String>();
                        listItems.add(temp);
                        adapterList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
                        ListTools.setAdapter(adapterList);
                    }
                }
            }

        db.close();
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
            db = dbh.getWritableDatabase();
            String query="DELETE FROM HmFactorTools WHERE ToolName='"+STR[0]+"' AND Price='"+STR[2]+"'" +
                    " AND ServiceDetaileCode='"+ServiceDetaileCode+"' AND BrandName='"+STR[1]+"' AND Amount='"+STR[3]+"'";
           db.execSQL(query);

            db.close();
            listItems.remove(deletePosition);
            adapterList.notifyDataSetChanged();
            Toast.makeText(Save_Per_Factor.this, "آیتم حذف شد", Toast.LENGTH_LONG).show();
            arg0.dismiss();

        }
    });

    alertbox.show();
}
}
