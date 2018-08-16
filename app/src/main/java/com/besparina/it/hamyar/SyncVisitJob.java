package com.besparina.it.hamyar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

public class SyncVisitJob {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Activity activity;
	private String guid;
	private String hamyarcode;
	private String WsResponse;
	private String UserServiceCode;
	private String Year;
	private String Mon;
	private String Day;
	private String Hour;
	private String Min;
	//private String acceptcode;
	private boolean CuShowDialog=true;
	//Contractor
	public SyncVisitJob(Activity activity, String guid, String hamyarcode, String UserServiceCode, String Year, String Mon, String Day,String Hour,String Min) {
		this.activity = activity;
		this.guid = guid;
		this.UserServiceCode=UserServiceCode;
		this.hamyarcode=hamyarcode;
		this.Year=Year;
		this.Mon=Mon;
		this.Day=Day;
		this.Hour=Hour;
		this.Min=Min;
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
		    this.dialog = new ProgressDialog(activity);		    		    this.dialog.setCanceledOnTouchOutside(false);
		}
		
        @Override
        protected String doInBackground(String... params) {
        	String result = null;
        	try
        	{
        		CallWsMethod("InsertServiceVisit");
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
	            	Toast.makeText(this.activity.getApplicationContext(), "خطایی رخداده است", Toast.LENGTH_LONG).show();
					//LoadActivity(MainMenu.class, "guid", guid,"hamyarcode",hamyarcode,"updateflag","1");
	            }
				else if(WsResponse.toString().compareTo("1") == 0)
				{
					Toast.makeText(this.activity.getApplicationContext(), "ثبت بازدید", Toast.LENGTH_LONG).show();
					InsertDataFromWsToDb();
				}
				else if(WsResponse.toString().compareTo("2") == 0)
				{
					Toast.makeText(this.activity.getApplicationContext(), "همیار شناسایی نشد!", Toast.LENGTH_LONG).show();
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
		PropertyInfo UserServiceCodePI = new PropertyInfo();
		//Set Name
		UserServiceCodePI.setName("UserServiceCode");
		//Set Value
		UserServiceCodePI.setValue(this.UserServiceCode);
		//Set dataType
		UserServiceCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(UserServiceCodePI);
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
		MonthPI.setValue(this.Mon);
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
		PropertyInfo MinPI = new PropertyInfo();
		//Set Name
		MinPI.setName("Min");
		//Set Value
		MinPI.setValue(this.Min);
		//Set dataType
		MinPI.setType(String.class);
		//Add the property to request object
		request.addProperty(MinPI);
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
		String query=null;
		db=dbh.getWritableDatabase();
		query="UPDATE  BsHamyarSelectServices" +
				" SET  Status='5' ,VisitDate='"+this.Year+"/" +this.Mon+"/"+this.Day+"' ,VisitTime='"+this.Hour+":"+this.Min+
				"' WHERE Code='"+UserServiceCode+"'";
		db.execSQL(query);

		db.close();
		LoadActivity(ViewJob.class,"guid", guid,"hamyarcode",hamyarcode,"tab","0","BsUserServicesID",UserServiceCode);
	}
	public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2, String VariableName3, String VariableValue3, String VariableName4, String VariableValue4)
	{
		Intent intent = new Intent(activity,Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);
		intent.putExtra(VariableName3, VariableValue3);
		intent.putExtra(VariableName4, VariableValue4);
		activity.startActivity(intent);
	}
}
