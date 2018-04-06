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

public class SyncSelecteJob {

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
	//private String acceptcode;
	private boolean CuShowDialog=true;
	//Contractor
	public SyncSelecteJob(Activity activity, String guid, String hamyarcode, String UserServiceCode) {
		this.activity = activity;
		this.guid = guid;
		this.UserServiceCode=UserServiceCode;
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
        		CallWsMethod("SendUserServiceHamyarAccept");
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
					Toast.makeText(this.activity.getApplicationContext(), "این سرویس برای شما رزور شد", Toast.LENGTH_LONG).show();
					InsertDataFromWsToDb();
				}
				else if(WsResponse.toString().compareTo("2") == 0)
				{
					Toast.makeText(this.activity.getApplicationContext(), "همیار شناسایی نشد!", Toast.LENGTH_LONG).show();
				}
				else if(WsResponse.toString().compareTo("3") == 0)
				{
					Toast.makeText(this.activity.getApplicationContext(), "این سرویس توسط شخص دیگری رزرو شده است!", Toast.LENGTH_LONG).show();

				}
				else if(WsResponse.toString().compareTo("4") == 0)
				{
					Toast.makeText(this.activity.getApplicationContext(), "این سرویس توسط شما رزرو شده است!", Toast.LENGTH_LONG).show();

				}
				else if(WsResponse.toString().compareTo("5") == 0)
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
		PropertyInfo UserServiceCodePI = new PropertyInfo();
		//Set Name
		UserServiceCodePI.setName("UserServiceCode");
		//Set Value
		UserServiceCodePI.setValue(this.UserServiceCode);
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
	
	
	public void InsertDataFromWsToDb()
    {
		String query=null;
		Cursor coursors;
		db=dbh.getReadableDatabase();
		query = "SELECT * FROM BsUserServices WHERE code='" + UserServiceCode+"'";
		coursors = db.rawQuery(query,null);
		if(coursors.getCount()>0)
		{
			coursors.moveToNext();
			query="INSERT INTO BsHamyarSelectServices (" +
					"Code," +
					"UserCode" +
					",UserName" +
					",UserFamily" +
					",UserPhone" +
					",ServiceDetaileCode" +
					",MaleCount" +
					",FemaleCount" +
					",StartDate" +
					",EndDate" +
					",AddressCode" +
					",AddressText" +
					",Lat" +
					",Lng" +
					",City" +
					",State" +
					",Description" +
					",IsEmergency," +
					"InsertUser" +
					",StartTime" +
					",EndTime" +
					",HamyarCount" +
					",PeriodicServices" +
					",EducationGrade" +
					",FieldOfStudy" +
					",StudentGender" +
					",TeacherGender" +
					",EducationTitle" +
					",ArtField" +
					",CarWashType" +
					",CarType" +
					",Language" +
					",InsertDate" +
					",ArtFieldOther" +
					",IsDelete" +
					",Status" +
					") VALUES('"+
						  coursors.getString(coursors.getColumnIndex("Code"))+
					"','"+coursors.getString(coursors.getColumnIndex("UserCode"))+
					"','"+coursors.getString(coursors.getColumnIndex("UserName"))+
					"','"+coursors.getString(coursors.getColumnIndex("UserFamily"))+
					"','"+coursors.getString(coursors.getColumnIndex("UserPhone"))+
					"','"+coursors.getString(coursors.getColumnIndex("ServiceDetaileCode"))+
					"','"+coursors.getString(coursors.getColumnIndex("MaleCount"))+
					"','"+coursors.getString(coursors.getColumnIndex("FemaleCount"))+
					"','"+coursors.getString(coursors.getColumnIndex("StartDate"))+
					"','"+coursors.getString(coursors.getColumnIndex("EndDate"))+
					"','"+coursors.getString(coursors.getColumnIndex("AddressCode"))+
					"','"+coursors.getString(coursors.getColumnIndex("AddressText"))+
					"','"+coursors.getString(coursors.getColumnIndex("Lat"))+
					"','"+coursors.getString(coursors.getColumnIndex("Lng"))+
					"','"+coursors.getString(coursors.getColumnIndex("City"))+
					"','"+coursors.getString(coursors.getColumnIndex("State"))+
					"','"+coursors.getString(coursors.getColumnIndex("Description"))+
					"','"+coursors.getString(coursors.getColumnIndex("IsEmergency"))+
					"','"+coursors.getString(coursors.getColumnIndex("InsertUser"))+
					"','"+coursors.getString(coursors.getColumnIndex("StartTime"))+
					"','"+coursors.getString(coursors.getColumnIndex("EndTime"))+
					"','"+coursors.getString(coursors.getColumnIndex("HamyarCount"))+
					"','"+coursors.getString(coursors.getColumnIndex("PeriodicServices"))+
					"','"+coursors.getString(coursors.getColumnIndex("EducationGrade"))+
					"','"+coursors.getString(coursors.getColumnIndex("FieldOfStudy"))+
					"','"+coursors.getString(coursors.getColumnIndex("StudentGender"))+
					"','"+coursors.getString(coursors.getColumnIndex("TeacherGender"))+
					"','"+coursors.getString(coursors.getColumnIndex("EducationTitle"))+
					"','"+coursors.getString(coursors.getColumnIndex("ArtField"))+
					"','"+coursors.getString(coursors.getColumnIndex("CarWashType"))+
					"','"+coursors.getString(coursors.getColumnIndex("CarType"))+
					"','"+coursors.getString(coursors.getColumnIndex("Language"))+
					"','"+coursors.getString(coursors.getColumnIndex("InsertDate"))+
					"','"+coursors.getString(coursors.getColumnIndex("ArtFieldOther"))+
					"','" + "0" +
					"','" + "1')";//status 1 is select- 2 is pause - 3 is pause - 4 is cansel - 5 is visit - 6 is perfactor
			db=dbh.getWritableDatabase();
			db.execSQL(query);
			query = "DELETE  FROM BsUserServices WHERE Code=" + coursors.getString(coursors.getColumnIndex("Code"));
			db.execSQL(query);
			Toast.makeText(this.activity.getApplicationContext(), "این سرویس توسط شما رزرو و بسته شد", Toast.LENGTH_LONG).show();
			db.close();
		}
		LoadActivity(MainMenu.class,"guid", guid,"hamyarcode",hamyarcode,"tab","1","BsUserServicesID",UserServiceCode);
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
