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

public class SyncInsertHamyarLocation {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Context activity;
	private String guid;
	private String hamyarcode;
	private String WsResponse;
	private String Year;
	private String Month;
	private String Day;
	private String Hour;
	private String Minute;
	private String Lat;
	private String Lng;
	private boolean CuShowDialog=false;
	//Contractor
	public SyncInsertHamyarLocation(Context activity,
									String guid,
									String hamyarcode,
									String Year,
									String Month,
									String Day,
									String Hour,
									String Minute,
									String Lat,
									String Lng,
									DatabaseHelper dbh,
									SQLiteDatabase db) {
		this.activity = activity;
		this.guid = guid;
		this.Hour = Hour;
		this.hamyarcode=hamyarcode;
		this.Year=Year;
		this.Month=Month;
		this.Day=Day;
		this.Minute=Minute;
		this.Lat=Lat;
		this.Lng=Lng;
		this.dbh = dbh;
		this.db = db;
		IC = new InternetConnection(this.activity.getApplicationContext());
		PV = new PublicVariable();
		PublicVariable.theard_GetLocation=false;
//		dbh=new DatabaseHelper(this.activity.getApplicationContext());
//		try {
//
//			dbh.createDataBase();
//
//   		} catch (IOException ioe) {
//			PublicVariable.theard_GetLocation=true;
//   			throw new Error("Unable to create database");
//
//   		}
//
//   		try {
//
//   			dbh.openDataBase();
//
//   		} catch (SQLException sqle) {
//			PublicVariable.theard_GetLocation=true;
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
				 PublicVariable.theard_GetLocation=true;
	            e.printStackTrace();
			 }
		}
		else
		{
			PublicVariable.theard_GetLocation=true;
		}
	}
	
	//Async Method
	private class AsyncCallWS extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context activity;
		
		public AsyncCallWS(Context activity) {
		    this.activity = activity;
		    this.dialog = new ProgressDialog(activity);		    		    this.dialog.setCanceledOnTouchOutside(false);
		}
		
        @Override
        protected String doInBackground(String... params) {
        	String result = null;
        	try
        	{
        		CallWsMethod("InsertHamyarLocation");
        	}
	    	catch (Exception e) {
				PublicVariable.theard_GetLocation=true;
	    		result = e.getMessage().toString();
			}
	        return result;
        }
 
        @Override
        protected void onPostExecute(String result) {
        	if(result == null)
        	{
				PublicVariable.theard_GetLocation=true;
	            if(WsResponse.toString().compareTo("ER") == 0)
	            {
	            	//Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else if(WsResponse.toString().compareTo("0") == 0)
	            {
	            	//Toast.makeText(this.activity.getApplicationContext(), "خطایی رخداده است", Toast.LENGTH_LONG).show();
					//LoadActivity(MainMenu.class, "guid", guid,"hamyarcode",hamyarcode,"updateflag","1");
	            }
				else if(WsResponse.toString().compareTo("2") == 0)
				{
					//Toast.makeText(this.activity.getApplicationContext(), "همیار شناسایی نشد!", Toast.LENGTH_LONG).show();

				}
				else
				{
					InsertDataFromWsToDb();
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
				PublicVariable.theard_GetLocation=true;
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
		PropertyInfo YearPI = new PropertyInfo();
		//Set Name
		YearPI.setName("Year");
		//Set Value
		YearPI.setValue(this.Year);
		//Set dataType
		YearPI.setType(String.class);
		//Add the property to request object
		request.addProperty(YearPI);
		//*****************************************************
		PropertyInfo MonthPI = new PropertyInfo();
		//Set Name
		MonthPI.setName("Month");
		//Set Value
		MonthPI.setValue(this.Month);
		//Set dataType
		MonthPI.setType(String.class);
		//Add the property to request object
		request.addProperty(MonthPI);
		//*****************************************************
		PropertyInfo DayPI = new PropertyInfo();
		//Set Name
		DayPI.setName("Day");
		//Set Value
		DayPI.setValue(this.Day);
		//Set dataType
		DayPI.setType(String.class);
		//Add the property to request object
		request.addProperty(DayPI);
		//*****************************************************
		PropertyInfo HourPI = new PropertyInfo();
		//Set Name
		HourPI.setName("Hour");
		//Set Value
		HourPI.setValue(this.Hour);
		//Set dataType
		HourPI.setType(String.class);
		//Add the property to request object
		request.addProperty(HourPI);
		//*****************************************************
		PropertyInfo MinutePI = new PropertyInfo();
		//Set Name
		MinutePI.setName("Minute");
		//Set Value
		MinutePI.setValue(this.Minute);
		//Set dataType
		MinutePI.setType(String.class);
		//Add the property to request object
		request.addProperty(MinutePI);
		//*****************************************************
		PropertyInfo LatPI = new PropertyInfo();
		//Set Name
		LatPI.setName("Lat");
		//Set Value
		LatPI.setValue(this.Lat);
		//Set dataType
		LatPI.setType(String.class);
		//Add the property to request object
		request.addProperty(LatPI);
		//*****************************************************
		PropertyInfo LngPI = new PropertyInfo();
		//Set Name
		LngPI.setName("Lng");
		//Set Value
		LngPI.setValue(this.Lng);
		//Set dataType
		LngPI.setType(String.class);
		//Add the property to request object
		request.addProperty(LngPI);
		//*****************************************************
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


	public void InsertDataFromWsToDb()
	{
		//nothing
	}
}
