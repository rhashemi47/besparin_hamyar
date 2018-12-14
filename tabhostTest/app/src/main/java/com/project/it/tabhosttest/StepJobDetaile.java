	package com.besparina.it.hamyar;

    import android.app.Activity;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.database.Cursor;
    import android.database.SQLException;
    import android.database.sqlite.SQLiteDatabase;
    import android.os.Bundle;
    import android.view.KeyEvent;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ListView;
    import android.widget.Spinner;
    import android.widget.Toast;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;

    public class StepJobDetaile extends Activity {
        private String hamyarcode;
        private String guid;
        private String EttitleStepStr;
        private String EtUnitPriceStr;
        private String EtBrandStr;
        private DatabaseHelper dbh;
        private SQLiteDatabase db;
        private EditText EttitleStep;
        private EditText EtUnitPrice;
        private ListView lvStepJob;
        private EditText EtBrand;
        private Spinner SpNameService;
        private Spinner SpDitalNameService;
        private Button btnSave;
        private Button btnSend;
        private List<String> labelsServiceName = new ArrayList<String>();
        private List<String> labelsServiceDetailName;
        private List<String> listItems;
        private ArrayAdapter<String> adapterList;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.stepjobdetaile);
            EttitleStep = (EditText) findViewById(R.id.EttitleToolsStep);
            EtUnitPrice = (EditText) findViewById(R.id.EtPriceTool);
            lvStepJob = (ListView) findViewById(R.id.ListViewSaveSetpTool);
            EtBrand = (EditText) findViewById(R.id.EtBrandTool);
            SpNameService = (Spinner) findViewById(R.id.ServiceNameTool);
            SpDitalNameService = (Spinner) findViewById(R.id.ServiDetailNameTool);
            btnSave = (Button) findViewById(R.id.btnSaveStepJobTool);
            btnSend = (Button) findViewById(R.id.btnSendStepJobTool);
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
            addItemFromList(true);
            try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
            Cursor coursors = db.rawQuery("SELECT * FROM services", null);
            if (coursors.getCount() > 0) {
                for (int i = 0; i < coursors.getCount(); i++) {
                    coursors.moveToNext();
                    labelsServiceName.add(coursors.getString(coursors.getColumnIndex("servicename")));
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsServiceName);
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
                        FillSpinnerChild(coursors.getString(coursors.getColumnIndex("code")));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addItemFromList(false);
                }
            });
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendFarctor();
                }
            });
            lvStepJob.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                //وقتی برروی لیست چند ثانیه لمس شود
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int position, long arg3) {
                    removeItemFromList(position);
                    return true;
                }
            });
        }

        private void addItemFromList(boolean FirestFill) {
            if (!FirestFill) {
                String temp;
                EttitleStepStr = EttitleStep.getText().toString();
                EtUnitPriceStr = EtUnitPrice.getText().toString();
                EtBrandStr = EtBrand.getText().toString();
                if (EttitleStepStr.compareTo("") == 0 || EtUnitPriceStr.compareTo("") == 0) {
                    Toast.makeText(this, "لطفا تمام فیلد ها را پر فرمایید", Toast.LENGTH_SHORT).show();
                } else {
                    String[] StrDital=SpDitalNameService.getSelectedItem().toString().split(":");
                    temp=SpDitalNameService.getSelectedItem().toString()+ "-" +EttitleStepStr+ "-" +EtBrandStr+ "-" +EtUnitPriceStr;
                    db = dbh.getReadableDatabase();
                    String query="SELECT * FROM HmFactorTools WHERE ToolName='" + EttitleStepStr
                            + "' AND Price='" + EtUnitPriceStr
                            +"' AND ServiceDetaileCode='" +StrDital[0]+
                            "' AND BrandName='"+EtBrandStr+"'";
                    Cursor coursors = db.rawQuery(query, null);
                    if (coursors.getCount() > 0) {
                        Toast.makeText(this, "این مقدار تکراریست", Toast.LENGTH_SHORT).show();
                    } else {
                        String[] StrDetail=SpDitalNameService.getSelectedItem().toString().split(":");
                        query="INSERT INTO HmFactorTools (ToolName,Price,ServiceDetaileCode,BrandName) VALUES('" + EttitleStepStr + "','" + EtUnitPriceStr
                                + "','" +StrDetail[0] +"','" +EtBrandStr+"')";
                        try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
                        db.execSQL(query);
                        if (lvStepJob.getCount() > 0) {
                            adapterList.add(temp);
                            lvStepJob.setAdapter(adapterList);
                        } else {
                            listItems = new ArrayList<String>();
                            listItems.add(temp);
                            adapterList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
                            lvStepJob.setAdapter(adapterList);
                        }
                    }
                }
            } else {
                db = dbh.getReadableDatabase();
                String query="SELECT HmFactorTools.*,Servicesdetails.name FROM HmFactorTools " +
                        "LEFT JOIN " +
                        "Servicesdetails ON " +
                        "Servicesdetails.code=HmFactorTools.ServiceDetaileCode WHERE Status='1'";
                Cursor coursors = db.rawQuery(query, null);
                for (int i = 0; i < coursors.getCount(); i++) {
                    coursors.moveToNext();
                    String temp;
                    //query="SELECT * FROM servicesdetails WHERE code='"+coursors.getString(coursors.getColumnIndex("ServiceDetaileCode"))+"'";
                    temp =coursors.getString(coursors.getColumnIndex("ServiceDetaileCode")) + ":" +
                            coursors.getString(coursors.getColumnIndex("name"))+ "-" +
                            coursors.getString(coursors.getColumnIndex("ToolName")) + "-" +
                            coursors.getString(coursors.getColumnIndex("BrandName")) + "-" +
                            coursors.getString(coursors.getColumnIndex("Price"));
                    if (lvStepJob.getCount() > 0) {
                        adapterList.add(temp);
                        lvStepJob.setAdapter(adapterList);
                    } else {
                        listItems = new ArrayList<String>();
                        listItems.add(temp);
                        adapterList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
                        lvStepJob.setAdapter(adapterList);
                    }
                }
            }
        }


        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                String query="DELETE FROM HmFactorTools WHERE IsSend='0' AND Status='0'";
                db=dbh.getWritableDatabase();
                db.execSQL(query);
                this.LoadActivity(MainActivity.class, "guid", guid, "hamyarcode", hamyarcode);
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
                    Toast.makeText(StepJobDetaile.this, "آیتم حذف شد", Toast.LENGTH_LONG).show();
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
        }
    }