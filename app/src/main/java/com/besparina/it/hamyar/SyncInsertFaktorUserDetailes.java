package com.besparina.it.hamyar;

import android.app.Activity;
import android.app.ProgressDialog;
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

public class SyncInsertFaktorUserDetailes {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Activity activity;
	private String guid;
	private String hamyarcode;
	private String WsResponse;
	private String FaktorUsersHeadCode;
	private String Type;
	private String ObjectCode;
	private String PricePerUnit;
	private String Amount;
	private boolean CuShowDialog=true;
	//Contractor
	public SyncInsertFaktorUserDetailes(Activity activity, String guid, String hamyarcode,String FaktorUsersHeadCode, String Type, String ObjectCode, String PricePerUnit, String Amount) {
		this.activity = activity;
		this.guid = guid;
		this.hamyarcode=hamyarcode;
		this.FaktorUsersHeadCode=FaktorUsersHeadCode;
		this.Type=Type;
		this.ObjectCode=ObjectCode;
		this.PricePerUnit=PricePerUnit;
		this.Amount=Amount;
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
        		CallWsMethod("InsertFaktorUserDetailes");
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
				else if(WsResponse.toString().compareTo("2") == 0)
				{
					Toast.makeText(this.activity.getApplicationContext(), "همیار شناسایی نشد!", Toast.LENGTH_LONG).show();

				}
				else
				{
					InsertDataFromWsToDb();
				}

        	}
        	else
        	{
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
		PropertyInfo FaktorUsersHeadCodePI = new PropertyInfo();
		//Set Name
		FaktorUsersHeadCodePI.setName("FaktorUsersHeadCode");
		//Set Value
		FaktorUsersHeadCodePI.setValue(this.FaktorUsersHeadCode);
		//Set dataType
		FaktorUsersHeadCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(FaktorUsersHeadCodePI);
		//*****************************************************
		PropertyInfo TypePI = new PropertyInfo();
		//Set Name
		TypePI.setName("Type");
		//Set Value
		TypePI.setValue(this.Type);
		//Set dataType
		TypePI.setType(String.class);
		//Add the property to request object
		request.addProperty(TypePI);
		//*****************************************************
		PropertyInfo ObjectCodePI = new PropertyInfo();
		//Set Name
		ObjectCodePI.setName("ObjectCode");
		//Set Value
		ObjectCodePI.setValue(this.ObjectCode);
		//Set dataType
		ObjectCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(ObjectCodePI);
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
		PropertyInfo AmountPI = new PropertyInfo();
		//Set Name
		AmountPI.setName("Amount");
		//Set Value
		AmountPI.setValue(this.Amount);
		//Set dataType
		AmountPI.setType(String.class);
		//Add the property to request object
		request.addProperty(AmountPI);
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
		query="INSERT INTO  InsertFaktorUserDetailes (FaktorUsersHeadCode,Type,ObjectCode,PricePerUnit,Amount) VALUES ('"
				+FaktorUsersHeadCode+"','"
				+Type+"','"
				+ObjectCode+"','"
				+PricePerUnit+"','"
				+Amount+"')";
		db.execSQL(query);

		db.close();
		Toast.makeText(activity," ثبت شد", Toast.LENGTH_SHORT).show();
	}
}
