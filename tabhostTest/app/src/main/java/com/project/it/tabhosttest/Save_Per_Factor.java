package com.besparina.it.hamyar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rm on 02/02/2018.
 */

public class Save_Per_Factor extends Activity {
    private String hamyarcode;
    private String guid;
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
    private CheckBox CheckTitleTools;
    private Button btnSendPerFactor;
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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_per_factor);
        SpTitleStepJob=(Spinner)findViewById(R.id.SpTitleStepJob);
        SPTitleTools=(Spinner)findViewById(R.id.SPTitleTools);
        EtUnitValuePrice=(EditText) findViewById(R.id.EtUnitValuePrice);
        EtToolValuePrice=(EditText) findViewById(R.id.EtToolValuePrice);
        EtDescription=(EditText) findViewById(R.id.EtDescription);
        tvUnit=(TextView) findViewById(R.id.tvUnit);
        tvUnitPrice=(TextView) findViewById(R.id.EtDescription);
        tvBrand=(TextView) findViewById(R.id.tvBrand);
        tvPriceTools=(TextView) findViewById(R.id.tvPriceTools);
        tvTotalSumStep=(TextView) findViewById(R.id.tvTotalSumStep);
        tvTotalSumTool=(TextView) findViewById(R.id.tvTotalSumTool);
        tvPriceTools=(TextView) findViewById(R.id.tvPriceTools);
        CheckTitleTools=(CheckBox) findViewById(R.id.CheckTitleTools);
        ListTools=(ListView) findViewById(R.id.ListTools);
        btnSendPerFactor=(Button) findViewById(R.id.btnSendPerFactor);
        LinerTitleTools=(LinearLayout) findViewById(R.id.LinerTitleTools);
        LinerBrand=(LinearLayout) findViewById(R.id.LinerBrand);
        LinerPriceTool=(LinearLayout) findViewById(R.id.LinerPriceTool);
        LinerValueTools=(LinearLayout) findViewById(R.id.LinerValueTools);
        LinerListTools=(LinearLayout) findViewById(R.id.LinerListTools);
        LinerTotalSumStep=(LinearLayout) findViewById(R.id.LinerTotalSumStep);
        LinerTotalSumTool=(LinearLayout) findViewById(R.id.LinerTotalSumTool);
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
            try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
            Cursor coursors = db.rawQuery("SELECT * FROM login",null);
            for(int i=0;i<coursors.getCount();i++){
                coursors.moveToNext();
                guid=coursors.getString(coursors.getColumnIndex("guid"));
                hamyarcode=coursors.getString(coursors.getColumnIndex("hamyarcode"));
            }
        }
        try
        {
            BsUserServicesID = getIntent().getStringExtra("BsUserServicesID").toString();
            tab = getIntent().getStringExtra("tab").toString();
            ServiceDetaileCode = getIntent().getStringExtra("ServiceDetaileCode").toString();
        }
        catch (Exception e)
        {
            //todo
        }
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
                        EtToolValuePrice.setText("0");
                    }
                    else
                    {
                        String PriceStep;
                        String Amount;
                        float Result;
                        Amount= EtUnitValuePrice.getText().toString();
                        PriceStep=tvUnitPrice.getText().toString();
                        Result=Float.parseFloat(Amount)*Float.parseFloat(PriceStep);
                        tvTotalSumStep.setText(tvTotalSumStep.getText().toString()+": "+Float.toString(Result));
                    }
            }
        });
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
                        EtUnitValuePrice.setText("0");
                    }
                    else
                    {
                        String PriceStep;
                        String Amount;
                        float Result;
                        Amount= EtToolValuePrice.getText().toString();
                        PriceStep=tvPriceTools.getText().toString();
                        Result=Float.parseFloat(Amount)*Float.parseFloat(PriceStep);
                        tvTotalSumTool.setText(tvTotalSumTool.getText().toString()+": "+Float.toString(Result));
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            Save_Per_Factor.this.LoadActivity_Select(ViewJob.class, "guid", guid, "hamyarcode", hamyarcode,"BsUserServicesID", BsUserServicesID, "tab", tab);
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
    public void LoadActivity_Select(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2, String VariableName3, String VariableValue3, String VariableName4, String VariableValue4)
    {
        Intent intent = new Intent(getApplicationContext(),Cls);
        intent.putExtra(VariableName, VariableValue);
        intent.putExtra(VariableName2, VariableValue2);
        intent.putExtra(VariableName3, VariableValue3);
        intent.putExtra(VariableName4, VariableValue4);
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
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsServiceDetailName);
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
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsServiceDetailName);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SPTitleTools.setAdapter(dataAdapter);
        }
    }
    public void SendFarctor() {
        try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
//        String query = "INSERT INTO SendFinalFactor (UserServiceCode,Description,Type,ObjectCode,PricePerUnit,Amount) " +
//                "VALUES('"
//                +BsUserServicesID + "'"
//                +EtDescription.getText() + "'"
//                +"'1'"
//                + "'"+StepCode + "'"
//                +"'"+txtPriceStep+"'"
//                +"'"+EtUnitValuePrice.getText()+"'"
//                +"')";//todo
//        db.execSQL(query);
        if (CheckTitleTools.isChecked())
        {
//            query = "INSERT INTO SendFinalFactor (UserServiceCode,Description,Type,ObjectCode,PricePerUnit,Amount) " +
//                    "VALUES('"
//                    +BsUserServicesID + "'"
//                    +EtDescription.getText() + "'"
//                    +"'2'"
//                    + "'"+StepDetailCode + "'"
//                    +"'"+txtPriceStep+"'"
//                    +"'"+EtToolValuePrice.getText()+"'"
//                    +"')";//todo
//            db.execSQL(query);
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
        }
        else
        {
            LinerTitleTools.setVisibility(View.GONE);
            LinerBrand.setVisibility(View.GONE);
            LinerPriceTool.setVisibility(View.GONE);
            LinerValueTools.setVisibility(View.GONE);
            LinerTotalSumTool.setVisibility(View.GONE);
        }
    }
//    private void addItemFromList(boolean FirestFill) {
//        if (!FirestFill) {
//            String temp;
//            EttitleStepStr = EttitleStep.getText().toString();
//            EtUnitPriceStr = EtUnitPrice.getText().toString();
//            UnitStr = SpUnit.getSelectedItem().toString();
//            if (EttitleStepStr.compareTo("") == 0 || EtUnitPriceStr.compareTo("") == 0) {
//                Toast.makeText(StepJob.this, "لطفا تمام فیلد ها را پر فرمایید", Toast.LENGTH_SHORT).show();
//            } else {
//                String[] StrDital=SpDitalNameService.getSelectedItem().toString().split(":");
//                temp=SpDitalNameService.getSelectedItem().toString()+ "-" +EttitleStepStr+ "-" +UnitStr+ "-" +EtUnitPriceStr;
//                db = dbh.getReadableDatabase();
//                Cursor coursors = db.rawQuery("SELECT * FROM HmFactorService WHERE ServiceName='" + EttitleStepStr + "' AND PricePerUnit='" + EtUnitPriceStr + "'AND " +
//                        "Unit='" +Unit_value.get(UnitStr) + "' AND ServiceDetaileCode='"+StrDital[0]+"'", null);
//                if (coursors.getCount() > 0) {
//                    Toast.makeText(StepJob.this, "این مقدار تکراریست", Toast.LENGTH_SHORT).show();
//                } else {
//                    String[] StrDetail=SpDitalNameService.getSelectedItem().toString().split(":");
//                    String query="INSERT INTO HmFactorService (ServiceName,PricePerUnit,Unit,ServiceDetaileCode) VALUES('" + EttitleStepStr + "','" + EtUnitPriceStr
//                            + "','" + Unit_value.get(UnitStr) + "','" +StrDetail[0] +"')";
//                    try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
//                    db.execSQL(query);
//                    if (lvStepJob.getCount() > 0) {
//                        adapterList.add(temp);
//                        lvStepJob.setAdapter(adapterList);
//                    } else {
//                        listItems = new ArrayList<String>();
//                        listItems.add(temp);
//                        adapterList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
//                        lvStepJob.setAdapter(adapterList);
//                    }
//                }
//            }
//        } else {
//            db = dbh.getReadableDatabase();
//            String query="SELECT HmFactorService.*,Servicesdetails.name FROM HmFactorService " +
//                    "LEFT JOIN " +
//                    "Servicesdetails ON " +
//                    "Servicesdetails.code=HmFactorService.ServiceDetaileCode WHERE Status='1'";
//            Cursor coursors = db.rawQuery(query, null);
//            for (int i = 0; i < coursors.getCount(); i++) {
//                coursors.moveToNext();
//                String temp;
//                //query="SELECT * FROM servicesdetails WHERE code='"+coursors.getString(coursors.getColumnIndex("ServiceDetaileCode"))+"'";
//                temp =coursors.getString(coursors.getColumnIndex("ServiceDetaileCode")) + ":" +
//                        coursors.getString(coursors.getColumnIndex("name"))+ "-" +
//                        coursors.getString(coursors.getColumnIndex("ServiceName")) + "-" +
//                        Unit_key.get(coursors.getString(coursors.getColumnIndex("Unit")))+ "-" +
//                        coursors.getString(coursors.getColumnIndex("PricePerUnit"));
//                if (lvStepJob.getCount() > 0) {
//                    adapterList.add(temp);
//                    lvStepJob.setAdapter(adapterList);
//                } else {
//                    listItems = new ArrayList<String>();
//                    listItems.add(temp);
//                    adapterList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
//                    lvStepJob.setAdapter(adapterList);
//                }
//            }
//        }
//    }
}
