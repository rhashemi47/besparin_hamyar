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

public class SyncUpdateStepDetailJobs {

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
	private String HmFactorToolsCode;
	private String Status;
	private String ToolName;
	private String BrandName;
	private String Price;
	private boolean CuShowDialog=true;
	//Contractor
	public SyncUpdateStepDetailJobs(Activity activity, String GUID, String HamyarCode, String HmFactorToolsCode, String ServiceDetaileCode, String ToolName, String BrandName, String Price, String Status) {
		this.activity = activity;
		this.guid = GUID;
		this.ServiceDetaileCode=ServiceDetaileCode;
		this.HmFactorToolsCode=HmFactorToolsCode;
		this.Status=Status;
		this.ToolName=ToolName;
		this.BrandName=BrandName;
		this.Price=Price;
		this.hamyarcode=HamyarCode;
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
        		CallWsMethod("UpdateHmFactorTools");
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
		PropertyInfo ToolNamePI = new PropertyInfo();
		//Set Name
		ToolNamePI.setName("ToolName");
		//Set Value
		ToolNamePI.setValue(this.ToolName);
		//Set dataType
		ToolNamePI.setType(String.class);
		//Add the property to request object
		request.addProperty(ToolNamePI);
		//*****************************************************
		PropertyInfo BrandNamePI = new PropertyInfo();
		//Set Name
		BrandNamePI.setName("BrandName");
		//Set Value
		BrandNamePI.setValue(this.BrandName);
		//Set dataType
		BrandNamePI.setType(String.class);
		//Add the property to request object
		request.addProperty(BrandNamePI);
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
		PropertyInfo HmFactorToolsCodePI = new PropertyInfo();
		//Set Name
		HmFactorToolsCodePI.setName("HmFactorToolsCode");
		//Set Value
		HmFactorToolsCodePI.setValue(this.HmFactorToolsCode);
		//Set dataType
		HmFactorToolsCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(HmFactorToolsCodePI);
		//*****************************************************
		PropertyInfo StatusPI = new PropertyInfo();
		//Set Name
		StatusPI.setName("Status");
		//Set Value
		StatusPI.setValue(this.Status);
		//Set dataType
		StatusPI.setType(String.class);
		//Add the property to request object
		request.addProperty(StatusPI);
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
//		SyncGetHmFactorService getHmFactorService=new SyncGetHmFactorService(activity,guid,hamyarcode);
//		getHmFactorService.AsyncExecute();
		db = dbh.getWritableDatabase();
		String query="UPDATE HmFactorTools SET Status='0',IsSend='1' WHERE ToolName='"+ToolName+"' AND Price='"+Price+"' AND BrandName='"+BrandName+"'" +
				" AND ServiceDetaileCode='"+ServiceDetaileCode+"'";
		db.execSQL(query);

		db.close();

	}
}
