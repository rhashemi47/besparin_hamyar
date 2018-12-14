package com.besparina.it.hamyar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

public class HmLogin {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Activity activity;
	private String phonenumber;
	private String check_load;
	private String acceptcode;
	private String WsResponse;
	private String LastMessageCode;
	private boolean CuShowDialog=true;
	private boolean CuLoadActivityAfterExecute;
	private String[] res;
	//Contractor
	public HmLogin(Activity activity, String phonenumber, String acceptcode, String check_load) {
		this.activity = activity;
		this.phonenumber = phonenumber;		
		this.acceptcode=acceptcode;
		this.check_load=check_load;
		IC = new InternetConnection(this.activity.getApplicationContext());
		PV = new PublicVariable();
		
		dbh=new DatabaseHelper(this.activity.getApplicationContext());
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
   		
	}
	
	public void AsyncExecute()
	{
		if(IC.isConnectingToInternet()==true)
		{
			try
			{
				AsyncCallWS task = new AsyncCallWS(this.activity);
				task.execute();
			}	
			 catch (Exception e) {
				//Toast.makeText(this.activity.getApplicationContext(), PersianReshape.reshape("ط¹ط¯ظ… ط¯ط³طھط±ط³غŒ ط¨ظ‡ ط³ط±ظˆط±"), Toast.LENGTH_SHORT).show();
	            e.printStackTrace();
			 }
		}
		else
		{
			Toast.makeText(this.activity.getApplicationContext(), "لطفا ارتباط شبکه خود را چک کنید", Toast.LENGTH_SHORT).show();
		}
	}
	
	//Async Method
	private class AsyncCallWS extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Activity activity;
		
		public AsyncCallWS(Activity activity) {
		    this.activity = activity;
		    this.dialog = new ProgressDialog(activity);
		    this.dialog.setCanceledOnTouchOutside(false);
		}
		
        @Override
        protected String doInBackground(String... params) {
        	String result = null;
        	try
        	{
        		CallWsMethod("HmLogin");
        	}
	    	catch (Exception e) {
	    		result = e.getMessage().toString();
			}
	        return result;
        }
 
        @Override
        protected void onPostExecute(String result) {
        	if(result == null)
        	{
        		res=WsResponse.split("##");
	            if(res[0].toString().compareTo("ER") == 0)
	            {
	            	Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else if(res[0].toString().compareTo("0") == 0)
	            {
	            	Toast.makeText(this.activity.getApplicationContext(), "کد اشتباه است", Toast.LENGTH_LONG).show();
	            }
	            else if(res[0].toString().compareTo("1") == 0)//همیار شناسایی شده و باید به روز رسانی اطلاعات انجام شود
	            {
	            	setlogin();
	            }
	            else if(res[0].toString().compareTo("2") == 0)//نیروی جدید می باشد و باید اطلاعات اولیه دریافت شود.
	            {

					if(check_load.compareTo("0")==0)
					{
						SyncProfile profile = new SyncProfile(this.activity, res[2], res[1]);
						profile.AsyncExecute();
						setloginDeactive();
					}
					else
					{
						InsertDataFromWsToDb(res);
					}
	            }
	            else if(res[0].toString().compareTo("3") == 0)
	            {
	            	//به صفحه منو برده شود اما امکانات غیرفعال گردد.
					SyncProfile profile = new SyncProfile(this.activity, res[2], res[1]);
					profile.AsyncExecute();
	            	setloginDeactive();
	            }
        	}
        	else
        	{
        		//Toast.makeText(this.activity, "ط®ط·ط§ ط¯ط± ط§طھطµط§ظ„ ط¨ظ‡ ط³ط±ظˆط±", Toast.LENGTH_SHORT).show();
        	}
            try
            {
            	if (this.dialog.isShowing()) {
            		this.dialog.dismiss();
            	}
            }
            catch (Exception e) {}
        }
 
        @Override
        protected void onPreExecute() {
        	if(CuShowDialog)
        	{
        		this.dialog.setMessage("در حال پردازش");
        		this.dialog.show();
        	}
        }
 
        @Override
        protected void onProgressUpdate(Void... values) {
        }
        
    }

	
	public void CallWsMethod(String METHOD_NAME) {
	    //Create request
	    SoapObject request = new SoapObject(PV.NAMESPACE, METHOD_NAME);
	    PropertyInfo phoneNuberPI = new PropertyInfo();
	    //Set Name
	    phoneNuberPI.setName("PhoneNumber");
	    //Set Value
	    phoneNuberPI.setValue(this.phonenumber);
	    //Set dataType
	    phoneNuberPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(phoneNuberPI);
	    PropertyInfo acceptcodePI = new PropertyInfo();
	    //****************************************************************
	    //Set Name
	    acceptcodePI.setName("AcceptCode");
	    //Set Value
	    acceptcodePI.setValue(this.acceptcode);
	    //Set dataType
	    acceptcodePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(acceptcodePI);	
	    //Create envelope
	    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
	            SoapEnvelope.VER11);
	    envelope.dotNet = true;
	    //Set output SOAP object
	    envelope.setOutputSoapObject(request);
	    //Create HTTP call object
	    HttpTransportSE androidHttpTransport = new HttpTransportSE(PV.URL);
	    try {
	        //Invoke web service
	        androidHttpTransport.call("http://tempuri.org/"+METHOD_NAME, envelope);
	        //Get the response
	        SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
	        //Assign it to FinalResultForCheck static variable
	        WsResponse = response.toString();	
	        if(WsResponse == null) WsResponse="ER";
	    } catch (Exception e) {
	    	WsResponse = "ER";
	    	e.printStackTrace();
	    }
	}
	
	
	public void InsertDataFromWsToDb(String[] AllRecord)
    {
		Toast.makeText(this.activity.getApplicationContext(), "شما فعال نشده اید", Toast.LENGTH_LONG).show();
		SyncEducation syncEducation=new SyncEducation(this.activity,this.phonenumber,this.acceptcode);
		syncEducation.AsyncExecute();
    }
	public void setlogin() 
	{
	    String LastHamyarUserServiceCode=null;
		db = dbh.getReadableDatabase();
		Cursor cursors = db.rawQuery("SELECT * FROM login", null);
		if(cursors.getCount()>0)
		{
			cursors.moveToNext();
			String Result=cursors.getString(cursors.getColumnIndex("islogin"));
			if(Result.compareTo("0")==0)
			{
				try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
				db.execSQL("UPDATE login SET hamyarcode='"+res[1].toString()+"' , guid='"+res[2].toString()+"' , islogin = '1'");
			}
		}
		else
		{
			try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
			db.execSQL("DELETE FROM login");
			String query="INSERT INTO login (hamyarcode,guid,islogin) VALUES('"+res[1].toString()+"','"+res[2].toString()+"','1')";
			db.execSQL(query);
		}
        cursors = db.rawQuery("SELECT ifnull(MAX(CAST (code AS INT)),0)as code FROM BsHamyarSelectServices", null);
        if(cursors.getCount()>0)
        {
            cursors.moveToNext();
            LastHamyarUserServiceCode=cursors.getString(cursors.getColumnIndex("code"));
        }
        cursors = db.rawQuery("SELECT ifnull(MAX(CAST (code AS INT)),0)as code FROM messages", null);
        if(cursors.getCount()>0)
        {
            cursors.moveToNext();
			LastMessageCode=cursors.getString(cursors.getColumnIndex("code"));
        }

		SyncMessage syncMessage=new SyncMessage(this.activity, res[2].toString(), res[1].toString(),LastMessageCode,LastHamyarUserServiceCode);
		syncMessage.AsyncExecute();
		SyncProfile syncProfile=new SyncProfile(this.activity,res[2].toString(), res[1].toString());
		syncProfile.AsyncExecute();
		db.close();

	}
	public void setloginDeactive() 
	{
		if(check_load.compareTo("0")!=0)
		{
			Toast.makeText(this.activity.getApplicationContext(), "شما فعال نشده اید", Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(this.activity.getApplicationContext(), "برای استفاده از امکانات بسپارینا باید ثبت نام کنید", Toast.LENGTH_LONG).show();
		}
		LoadActivity(MainMenu.class, "guid", "0","hamyarcode","0");
	}
	public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
	{
		Intent intent = new Intent(activity,Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		activity.startActivity(intent);
	}
	
}
