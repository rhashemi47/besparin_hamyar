package com.project.it.hamyar;

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

public class SyncInsertHamyarCredit {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Activity activity;
	private String Price;
	private String pHamyarCode;
	private String WsResponse;
	private String Method;
	private String DocNumber;
	private String Description;
	//private String acceptcode;
	private boolean CuShowDialog=true;
	//Contractor
	public SyncInsertHamyarCredit(Activity activity, String Price, String pHamyarCode, String Method,String DocNumber, String Description) {
		this.activity = activity;
		this.Price = Price;
		this.Method=Method;
		this.pHamyarCode=pHamyarCode;
		this.Description=Description;
		this.DocNumber=DocNumber;
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
        		CallWsMethod("InsertHamyarCredit");
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
	    PropertyInfo pHamyarCodePI = new PropertyInfo();
	    //Set Name
		pHamyarCodePI.setName("pHamyarCode");
	    //Set Value
		pHamyarCodePI.setValue(this.pHamyarCode);
	    //Set dataType
		pHamyarCodePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(pHamyarCodePI);
	    //*****************************************************
		PropertyInfo PricePI = new PropertyInfo();
		//Set Name
		PricePI.setName("Price");
		//Set Value
		PricePI.setValue(this.Price);
		//Set dataType
		PricePI.setType(String.class);
		//Add the property to request object
		request.addProperty(PricePI);
		//*****************************************************
		PropertyInfo MethodPI = new PropertyInfo();
		//Set Name
		MethodPI.setName("Method");
		//Set Value
		MethodPI.setValue(this.Method);
		//Set dataType
		MethodPI.setType(String.class);
		//Add the property to request object
		request.addProperty(MethodPI);
		//*****************************************************
		PropertyInfo DocNumberPI = new PropertyInfo();
		//Set Name
		DocNumberPI.setName("DocNumber");
		//Set Value
		DocNumberPI.setValue(this.DocNumber);
		//Set dataType
		DocNumberPI.setType(String.class);
		//Add the property to request object
		request.addProperty(DocNumberPI);
		//*****************************************************
		PropertyInfo DescriptionPI = new PropertyInfo();
		//Set Name
		DescriptionPI.setName("Description");
		//Set Value
		DescriptionPI.setValue(this.Description);
		//Set dataType
		DescriptionPI.setType(String.class);
		//Add the property to request object
		request.addProperty(DescriptionPI);
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


		SyncGettHamyarCreditHistory syncGettHamyarCreditHistory=new SyncGettHamyarCreditHistory(this.activity,this.pHamyarCode,"1");
		syncGettHamyarCreditHistory.AsyncExecute();

	}
}
