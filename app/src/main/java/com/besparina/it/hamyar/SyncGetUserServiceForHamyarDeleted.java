package com.besparina.it.hamyar;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

public class SyncGetUserServiceForHamyarDeleted {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Context activity;
	private String GUID;
	private String HamyarCode;
	private String UserServiceCode;
	private String WsResponse;
	//private String acceptcode;
	private boolean CuShowDialog=false;
	//Contractor
	public SyncGetUserServiceForHamyarDeleted(Context activity, String GUID, String HamyarCode, String UserServiceCode,
											  DatabaseHelper dbh,
											  SQLiteDatabase db) {
		this.activity = activity;
		this.GUID = GUID;
		this.HamyarCode = HamyarCode;
		this.UserServiceCode = UserServiceCode;
		this.dbh = dbh;
		this.db = db;
		IC = new InternetConnection(this.activity.getApplicationContext());
		PV = new PublicVariable();
		PublicVariable.theard_DeleteJob=false;
//		dbh=new DatabaseHelper(this.activity.getApplicationContext());
//		try {
//
//			dbh.createDataBase();
//
//   		} catch (IOException ioe) {
//			PublicVariable.theard_DeleteJob=true;
//   			throw new Error("Unable to create database");
//
//   		}
//
//   		try {
//
//   			dbh.openDataBase();
//
//   		} catch (SQLException sqle) {
//			PublicVariable.theard_DeleteJob=true;
//   			throw sqle;
//   		}
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
				 PublicVariable.theard_DeleteJob=true;
	            e.printStackTrace();
			 }
		}
		else
		{
			PublicVariable.theard_DeleteJob=true;
		}
	}
	
	//Async Method
	private class AsyncCallWS extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context activity;
		
		public AsyncCallWS(Context activity) {
		    this.activity = activity;
		    this.dialog = new ProgressDialog(activity);
			this.dialog.setCanceledOnTouchOutside(false);
		}
		
        @Override
        protected String doInBackground(String... params) {
        	String result = null;
        	try
        	{
        		CallWsMethod("GetUserServiceForHamyarDeleted");
        	}
	    	catch (Exception e) {
				PublicVariable.theard_DeleteJob=true;
	    		result = e.getMessage().toString();
			}
	        return result;
        }
 
        @Override
        protected void onPostExecute(String result) {
        	if(result == null)
        	{
				PublicVariable.theard_DeleteJob=true;
	            if(WsResponse.toString().compareTo("ER") == 0)
	            {
	            	//Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else if(WsResponse.toString().compareTo("0") == 0)
	            {
	            	//Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else
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
            catch (Exception e) {
				PublicVariable.theard_DeleteJob=true;
			}
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
	    PropertyInfo GUIDPI = new PropertyInfo();
	    //Set Name
		GUIDPI.setName("GUID");
	    //Set Value
		GUIDPI.setValue(GUID);
	    //Set dataType
		GUIDPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(GUIDPI);
	    //Create envelope
	    PropertyInfo HamyarCodePI = new PropertyInfo();
	    //Set Name
		HamyarCodePI.setName("HamyarCode");
	    //Set Value
		HamyarCodePI.setValue(HamyarCode);
	    //Set dataType
		HamyarCodePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(HamyarCodePI);
	    PropertyInfo UserServiceCodePI = new PropertyInfo();
	    //Set Name
		UserServiceCodePI.setName("UserServiceCode");
	    //Set Value
		UserServiceCodePI.setValue(UserServiceCode);
	    //Set dataType
		UserServiceCodePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(UserServiceCodePI);
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

		db=dbh.getWritableDatabase();			
		db.execSQL("DELETE FROM BsUserServices WHERE Code in ("+WsResponse+")");
		try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
    }
	
}
