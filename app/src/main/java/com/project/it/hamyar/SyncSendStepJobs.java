package com.project.it.hamyar;

import android.app.Activity;
import android.app.ProgressDialog;
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

public class SyncSendStepJobs {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Activity activity;
	private String guid;
	private String hamyarcode;
	private String WsResponse;
	private String ServiceDetaileCode;
	private String ServiceName;
	private String PricePerUnit;
	private String UnitCode;
	private boolean CuShowDialog=true;
	//Contractor
	public SyncSendStepJobs(Activity activity, String guid, String hamyarcode, String ServiceDetaileCode, String ServiceName, String PricePerUnit, String UnitCode) {
		this.activity = activity;
		this.guid = guid;
		this.ServiceDetaileCode=ServiceDetaileCode;
		this.ServiceName=ServiceName;
		this.PricePerUnit=PricePerUnit;
		this.UnitCode=UnitCode;
		this.hamyarcode=hamyarcode;
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
		}
		
        @Override
        protected String doInBackground(String... params) {
        	String result = null;
        	try
        	{
        		CallWsMethod("InsertHmFactorService");
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
	            if(WsResponse.toString().compareTo("ER") == 0)
	            {
	            	Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else if(WsResponse.toString().compareTo("0") == 0)
	            {
	            	Toast.makeText(this.activity.getApplicationContext(), "خطایی رخ داده است", Toast.LENGTH_LONG).show();
	            }
				else if(WsResponse.toString().compareTo("2") == 0)
				{
					Toast.makeText(this.activity.getApplicationContext(), "همیار شناسایی نشد!", Toast.LENGTH_LONG).show();
				}
				else if(WsResponse.toString().compareTo("1") == 0)
	            {
	            	InsertDataFromWsToDb(WsResponse);
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
	
	String LastNewsId;
	public void LoadMaxNewId()
	{
		db = dbh.getReadableDatabase();
		Cursor cursors = db.rawQuery("select IFNULL(max(id),0)MID from news", null);
		if(cursors.getCount() > 0)
		{
			cursors.moveToNext();
			LastNewsId = cursors.getString(cursors.getColumnIndex("MID"));
		}
	}
	
	public void CallWsMethod(String METHOD_NAME) {
	    //Create request
	    SoapObject request = new SoapObject(PV.NAMESPACE, METHOD_NAME);
	    PropertyInfo GuidPI = new PropertyInfo();
	    //Set Name
	    GuidPI.setName("GUID");
	    //Set Value
		GuidPI.setValue(this.guid);
	    //Set dataType
		GuidPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(GuidPI);
	    //*****************************************************
		PropertyInfo HamyarCodePI = new PropertyInfo();
		//Set Name
		HamyarCodePI.setName("HamyarCode");
		//Set Value
		HamyarCodePI.setValue(this.hamyarcode);
		//Set dataType
		HamyarCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(HamyarCodePI);
		//*****************************************************
		PropertyInfo ServiceDetaileCodePI = new PropertyInfo();
		//Set Name
		ServiceDetaileCodePI.setName("ServiceDetaileCode");
		//Set Value
		ServiceDetaileCodePI.setValue(this.ServiceDetaileCode);
		//Set dataType
		ServiceDetaileCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(ServiceDetaileCodePI);
		//*****************************************************
		PropertyInfo ServiceNamePI = new PropertyInfo();
		//Set Name
		ServiceNamePI.setName("ServiceName");
		//Set Value
		ServiceNamePI.setValue(this.ServiceName);
		//Set dataType
		ServiceNamePI.setType(String.class);
		//Add the property to request object
		request.addProperty(ServiceNamePI);
		//*****************************************************
		PropertyInfo PricePerUnitPI = new PropertyInfo();
		//Set Name
		PricePerUnitPI.setName("PricePerUnit");
		//Set Value
		PricePerUnitPI.setValue(this.PricePerUnit);
		//Set dataType
		PricePerUnitPI.setType(String.class);
		//Add the property to request object
		request.addProperty(PricePerUnitPI);
		//*****************************************************
		PropertyInfo UnitCodePI = new PropertyInfo();
		//Set Name
		UnitCodePI.setName("UnitCode");
		//Set Value
		UnitCodePI.setValue(this.UnitCode);
		//Set dataType
		UnitCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(UnitCodePI);
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
	
	
	public void InsertDataFromWsToDb(String AllRecord)
    {	
//		String query;
//		db=dbh.getWritableDatabase();
//		query="UPDATE HmFactorService SET IsSend='1'";
//		db.execSQL(query);
		Toast.makeText(activity, "ثبت نهایی انجام شد", Toast.LENGTH_LONG).show();
		SyncGetHmFactorService getHmFactorService=new SyncGetHmFactorService(activity,guid,hamyarcode);
		getHmFactorService.AsyncExecute();
    }
}
